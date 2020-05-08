package smt666.mall.service;

import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.codehaus.jackson.type.TypeReference;
import smt666.common.beans.CacheKeyConstants;
import smt666.common.util.JsonMapper;
import smt666.common.util.RequestHolder;
import smt666.mall.dao.SysAclMapper;
import smt666.mall.dao.SysRoleAclMapper;
import smt666.mall.dao.SysRoleUserMapper;
import smt666.mall.model.SysAcl;
import smt666.mall.model.SysUser;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 专门用来获取角色或者用户的修改权限点信息的类。
 * @author 卢2714065385
 * @date 2020-05-06 23:05
 */
@Service
public class SysCoreService {

    @Resource
    private SysAclMapper sysAclMapper;
    @Resource
    private SysRoleUserMapper sysRoleUserMapper;
    @Resource
    private SysRoleAclMapper sysRoleAclMapper;
    @Resource
    private SysCacheService sysCacheService;

    /**
     * 获取当前用户的权限信息列表。
     * @return
     */
    public List<SysAcl> getCurrentUserAclList() {
        int userId = RequestHolder.getCurrentUser().getId();
        return getUserAclList(userId);
    }

    /**
     * 获取到指定角色ID的权限点列表信息。
     * @param roleId
     * @return
     */
    public List<SysAcl> getRoleAclList(int roleId) {
        List<Integer> roleIdAclList =  sysRoleAclMapper.getAclIdListByRoleIdList(Lists.<Integer>newArrayList(roleId));
        if (CollectionUtils.isEmpty(roleIdAclList)) {
            return Lists.newArrayList();
        }
        return sysAclMapper.getByIdList(roleIdAclList);
    }

    /**
     * 获取用户的权限点的方法。
     * @param userId
     * @return
     */
    public List<SysAcl> getUserAclList(int userId) {
        if (isSuperAdmin()) {
            /*
            如果当前用户为超级管理员角色的话，
            那么我们直接就获取出所有的权限点列表信息进行返回。
             */
            return sysAclMapper.getAll();
        }
        List<Integer> userRoleIdList = sysRoleUserMapper.getRoleIdListByUserId(userId);
        if (CollectionUtils.isEmpty(userRoleIdList)) {
            // 如果当前用户并没有分配过任何的权限的话，那么我们就直接返回一个空的权限列表信息即可。
            return Lists.newArrayList();
        }
        // 如果当前用户是有分配权限了的话，那么我们要分配这些角色所对应的权限等级。
        /*
        我们传入多个角色ID，来分别查询多个角色合在一起的权限点信息的总和的信息。
        这样一来，我们就获取到了这个用户的所有分配过的权限点的列表信息。
         */
        List<Integer> userAclIdList = sysRoleAclMapper.getAclIdListByRoleIdList(userRoleIdList);
        if (CollectionUtils.isEmpty(userAclIdList)) {
            // 如果查询不到任何权限点信息，说明当前这个用户的权限都已经被取消掉了，那么我们也直接返回一个空的权限列表信息。
            return Lists.newArrayList();
        }
        // 接着我们查询权限点对于的用户对象的列表数据（一个权限点信息是可以被对个用户拥有的）。
        return sysAclMapper.getByIdList(userAclIdList);
    }


    public boolean isSuperAdmin() {
        // 这里是我自己定义了一个假的超级管理员规则，实际中要根据项目进行修改
        // 可以是配置文件获取，可以指定某个用户，也可以指定某个角色
        SysUser sysUser = RequestHolder.getCurrentUser();
        if (sysUser.getMail().contains("abc")) {
//            return true;
            return true;
        }
//        return false;
        return false;
    }

    /**
     * 判断当前用户，是否拥有访问指定url请求的权限。
     * @param url
     * @return
     */
    public boolean hasUrlAcl(String url) {
        if (isSuperAdmin()) {
            return true;
        }
        List<SysAcl> aclList = sysAclMapper.getByUrl(url);
        if (CollectionUtils.isEmpty(aclList)) {
            return true;
        }

        List<SysAcl> userAclList = getCurrentUserAclListFromCache();
        Set<Integer> userAclIdSet = userAclList.stream().map(acl -> acl.getId()).collect(Collectors.toSet());

        boolean hasValidAcl = false;
        // 规则：只要有一个权限点有权限，那么我们就认为有访问权限
        for (SysAcl acl : aclList) {
            // 判断一个用户是否具有某个权限点的访问权限
            // 权限点无效
            if (acl == null || acl.getStatus() != 1) {
                continue;
            }
            hasValidAcl = true;
            if (((Set) userAclIdSet).contains(acl.getId())) {
                return true;
            }
        }
        if (!hasValidAcl) {
            return true;
        }
        return false;
    }

    public List<SysAcl> getCurrentUserAclListFromCache() {
        int userId = RequestHolder.getCurrentUser().getId();
        String cacheValue = sysCacheService.getFromCache(CacheKeyConstants.USER_ACLS, String.valueOf(userId));
        if (StringUtils.isBlank(cacheValue)) {
            //去正常的用户列表中获取权限点信息的列表
            List<SysAcl> aclList = getCurrentUserAclList();
            if (CollectionUtils.isNotEmpty(aclList)) {
                // 如果aclList权限列表有数据需要缓存，那么我们先转成JSON数据，数据有效时间为600秒(10分钟)，第3个参数是key ，第4个为用户的ID值。
                sysCacheService.saveCache(JsonMapper.objectToString(aclList), 600, CacheKeyConstants.USER_ACLS, String.valueOf(userId));
            }
            // 从缓存中没有回去到权限列表的情况，我们就直接去正常的用户列表中获取权限点信息的列表。
            return aclList;
        }
        // 如果一开始就可以从redis缓存中获取到权限列表信息，那么：我们就把从redis中获取的Value数据（通常是JSON字符串）转成Object对象数据的权限List列表。
        return JsonMapper.stringToObject(cacheValue, new TypeReference<List<SysAcl>>() {
        });
    }

}
