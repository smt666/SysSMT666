package smt666.mall.service;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import smt666.common.exception.ParamException;
import smt666.common.util.BeanValidator;
import smt666.common.util.IpUtil;
import smt666.common.util.RequestHolder;
import smt666.mall.dao.SysRoleAclMapper;
import smt666.mall.dao.SysRoleMapper;
import smt666.mall.dao.SysRoleUserMapper;
import smt666.mall.dao.SysUserMapper;
import smt666.mall.model.SysRole;
import smt666.mall.model.SysUser;
import smt666.param.RoleParam;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 卢2714065385
 * @date 2020-05-06 20:42
 */
@Service
public class SysRoleService {

    @Resource
    private SysRoleMapper sysRoleMapper;
    @Resource
    private SysRoleUserMapper sysRoleUserMapper;
    @Resource
    private SysRoleAclMapper sysRoleAclMapper;
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysLogService sysLogService;
    /**
     * 注册或新增一个角色。
     * @param param
     */
    public void save(RoleParam param){
        BeanValidator.checkParams(param);
        if (checkRoleNameIsExists(param.getName(), param.getId())) {
            // 如果参数中的角色名称已经存在，那么说明传入的param参数是有问题的，我们直接抛出异常，中止继续执行。
            throw new ParamException("角色的名称：重复啦！");
        }
        SysRole sysRole = SysRole.builder()
                .name(param.getName())
                .status(param.getStatus())
                .type(param.getType())
                .remark(param.getRemark()).build();
        sysRole.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysRole.setOperateTime(new Date());
        sysRole.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysRoleMapper.insertSelective(sysRole);
        sysLogService.saveRoleLog(null, sysRole);
    }

    /**
     * 修改并且更新角色的信息。
     * @param param
     */
    public void update(RoleParam param){
        BeanValidator.checkParams(param);
        if (checkRoleNameIsExists(param.getName(), param.getId())) {
            // 如果参数中的角色名称已经存在，那么说明传入的param参数是有问题的，我们直接抛出异常，中止继续执行。
            throw new ParamException("角色名称：重复啦！");
        }
        // 再次校验原有的参数是否存在。
        SysRole before = sysRoleMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before, "待更新的角色不存在（或已被删除），请先添加一个角色后再更新。");
        // 准备需要更新的数据。
        SysRole after = SysRole.builder()
                .id(param.getId())
                .name(param.getName())
                .status(param.getStatus())
                .type(param.getType())
                .remark(param.getRemark()).build();
        after.setOperator(RequestHolder.getCurrentUser().getUsername());
        after.setOperateTime(new Date());
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        // 实现更新的操作。
        sysRoleMapper.updateByPrimaryKeySelective(after);
        sysLogService.saveRoleLog(before, after);
    }

    public List<SysRole> getAll() {
        return sysRoleMapper.getAll();
    }

    /**
     * 内部检查传入的角色名称的参数是否已经存在的方法。
     * @param roleName
     * @param roleId
     * @return
     */
    private boolean checkRoleNameIsExists(String roleName, Integer roleId){
        return  sysRoleMapper.countByName(roleName, roleId) > 0 ;
    }

    public List<SysRole> getRoleListByUserId(int userId) {
        List<Integer> roleIdList = sysRoleUserMapper.getRoleIdListByUserId(userId);
        if (CollectionUtils.isEmpty(roleIdList)) {
            return Lists.newArrayList();
        }
        return sysRoleMapper.getByIdList(roleIdList);
    }

    /**
     * 获取角色的列表。
     * @param aclId
     * @return
     */
    public List<SysRole> getRoleListByAclId(int aclId) {
        List<Integer> roleIdList = sysRoleAclMapper.getRoleIdListByAclId(aclId);
        if (CollectionUtils.isEmpty(roleIdList)) {
            return Lists.newArrayList();
        }
        return sysRoleMapper.getByIdList(roleIdList);
    }

    public List<SysUser> getUserListByRoleList(List<SysRole> roleList) {
        if (CollectionUtils.isEmpty(roleList)) {
            return Lists.newArrayList();
        }
        List<Integer> roleIdList = roleList.stream().map(role -> role.getId()).collect(Collectors.toList());
        List<Integer> userIdList = sysRoleUserMapper.getUserIdListByRoleIdList(roleIdList);
        if (CollectionUtils.isEmpty(userIdList)) {
            return Lists.newArrayList();
        }
        return sysUserMapper.getByIdList(userIdList);
    }
}
