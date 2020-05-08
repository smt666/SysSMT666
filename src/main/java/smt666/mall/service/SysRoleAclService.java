package smt666.mall.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smt666.common.beans.LogType;
import smt666.common.util.IpUtil;
import smt666.common.util.JsonMapper;
import smt666.common.util.RequestHolder;
import smt666.mall.dao.SysLogMapper;
import smt666.mall.dao.SysRoleAclMapper;
import smt666.mall.model.SysLogWithBLOBs;
import smt666.mall.model.SysRoleAcl;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author 卢2714065385
 * @date 2020-05-07 6:22
 */
@Service
public class SysRoleAclService {

    @Resource
    private SysRoleAclMapper sysRoleAclMapper;
    @Resource
    private SysLogMapper sysLogMapper;

    public void changRoleAcls (Integer roleId, List<Integer> aclIdList){
        // 获取出角色分配过的ID 。
        List<Integer> originAclIdList = sysRoleAclMapper.getAclIdListByRoleIdList(Lists.newArrayList(roleId));
        if (originAclIdList.size() == aclIdList.size()) {
            Set<Integer> originAclIdSet = Sets.newHashSet(originAclIdList);
            Set<Integer> aclIdSet = Sets.newHashSet(aclIdList);
            originAclIdSet.removeAll(aclIdSet);
            if (CollectionUtils.isEmpty(originAclIdSet)) {
                return;
            }
        }
        updateRoleAcl(roleId, aclIdList);
        saveRoleAclLog(roleId, originAclIdList, aclIdList);
    }
    @Transactional(rollbackFor = Exception.class)
    public void updateRoleAcl(int roleId, List<Integer> aclIdList) {
        // 删除原来的值。
        sysRoleAclMapper.deleteByRoleId(roleId);
        // 判断，
        if (CollectionUtils.isEmpty(aclIdList)) {
            return;
        }
        List<SysRoleAcl> roleAclList = Lists.newArrayList();
        for (Integer aclId : aclIdList) {
            SysRoleAcl roleAcl = SysRoleAcl.builder()
                    .roleId(roleId)
                    .aclId(aclId)
                    .operator(RequestHolder.getCurrentUser().getUsername())
                    .operateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()))
                    .operateTime(new Date()).build();
            roleAclList.add(roleAcl);
        }
        // 批量插入。
        sysRoleAclMapper.bathInsert(roleAclList);
    }

    private void saveRoleAclLog(int roleId, List<Integer> before, List<Integer> after) {
        SysLogWithBLOBs sysLog = new SysLogWithBLOBs();
        sysLog.setType(LogType.TYPE_ROLE_ACL);
        sysLog.setTargetId(roleId);
        sysLog.setOldValue(before == null ? "" : JsonMapper.objectToString(before));
        sysLog.setNewValue(after == null ? "" : JsonMapper.objectToString(after));
        sysLog.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysLog.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysLog.setOperateTime(new Date());
        sysLog.setStatus(1);
        sysLogMapper.insertSelective(sysLog);
    }

}