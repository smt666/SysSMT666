package smt666.mall.service;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smt666.common.exception.ParamException;
import smt666.common.util.BeanValidator;
import smt666.common.util.IpUtil;
import smt666.common.util.RequestHolder;
import smt666.common.vo.JsonResult;
import smt666.mall.dao.SysAclMapper;
import smt666.mall.dao.SysAclModuleMapper;
import smt666.mall.model.SysAcl;
import smt666.mall.model.SysAclModule;
import smt666.mall.model.SysDept;
import smt666.mall.utils.LevelUtil;
import smt666.param.AclModuleParam;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author 卢2714065385
 * @date 2020-05-05 8:02
 */
@Service
@Slf4j
public class SysAclModuleService {

    @Resource
    public SysAclModuleMapper sysAclModuleMapper;
    @Resource
    private SysAclMapper sysAclMapper;
    @Resource
    private SysLogService sysLogService;

    public void save(AclModuleParam param) {
        BeanValidator.checkParams(param);
        if (checkExist(param.getParentId(), param.getName(), param.getId())) {
            // 如果部门相同的话，那么抛出异常（同一级部门中不允许存在多个重复的部门名称）。
            throw new ParamException("当前同一层级下存在相同名称的权限模块！");
        }
        SysAclModule aclModule = SysAclModule.builder()
                .name(param.getName())
                .parentId(param.getParentId())
                .seq(param.getSeq())
                .status(param.getStatus())
                .remark(param.getRemark()).build();
        aclModule.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()), param.getParentId()));
        aclModule.setOperator(RequestHolder.getCurrentUser().getUsername());
        aclModule.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        aclModule.setOperateTime(new Date());
        // 执行save操作。
        sysAclModuleMapper.insertSelective(aclModule);
        sysLogService.saveAclModuleLog(null, aclModule);
    }

    public void update(AclModuleParam param) {
        BeanValidator.checkParams(param);
        if (checkExist(param.getParentId(), param.getName(), param.getId())) {
            // 如果部门相同的话，那么抛出异常（同一级部门中不允许存在多个重复的部门名称）。
            throw new ParamException("当前同一层级下存在相同名称的权限模块！");
        }
        // 校验传入的id所对应的实体类是否存在。
        SysAclModule before = sysAclModuleMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before, "将要更新的权限模块的相关信息不存在！");
        SysAclModule after = SysAclModule.builder()
                .id(param.getId())
                .name(param.getName())
                .parentId(param.getParentId())
                .seq(param.getSeq())
                .status(param.getStatus())
                .remark(param.getRemark()).build();
        after.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()), param.getParentId()));
        after.setOperator(RequestHolder.getCurrentUser().getUsername());
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        after.setOperateTime(new Date());
        // 执行更新的操作。
        updateWithChild(before, after);
        sysLogService.saveAclModuleLog(before, after);
    }
    @Transactional(rollbackFor = Exception.class)
    public void updateWithChild(SysAclModule before, SysAclModule after) {
        // 我们先对子模块的信息基础更新（因为当前需要更新的权限模块的子模块信息可能已经发生了变化了，所必须得先去更新子模块后才更新当前信息。）。
        // 先判断一下，是否需要去更新子部门的信息。
        // 获取出新部门的level的前缀。
        String newLevelPrefix = after.getLevel();
        // 获取出之前部门的level的前缀。
        String oldLevelPrefix = before.getLevel();
        if ( !newLevelPrefix.equals(oldLevelPrefix) ) {
            // 如果不一致时，我们才需要去更新子部门的信息。
            List<SysAclModule> sysAclModuleList = sysAclModuleMapper.getChildSysAclModulListByLevel(before.getLevel());
            if (CollectionUtils.isNotEmpty(sysAclModuleList)) {
                for (SysAclModule sysAclModule : sysAclModuleList) {
                    // 获取当前模块的level 。
                    String level = sysAclModule.getLevel();
                    if (level.indexOf(oldLevelPrefix) == 0) {
                        level = newLevelPrefix + level.substring(oldLevelPrefix.length());
                        sysAclModule.setLevel(level);
                    }
                    // 批量更新Level 。
                    sysAclModuleMapper.batchUpdateLevel(sysAclModuleList);
                }
            }
            // 然后更新当前部门的信息。
            sysAclModuleMapper.updateByPrimaryKeySelective(after);
        }

        // 然后才可以对当前权限模块的信息进行更新。
        sysAclModuleMapper.updateByPrimaryKeySelective(after);
    }

    public void delete(int aclModuleId) {
        // 先查询一下，当前将要生成的权限模块的信息在数据库中是否存在。
        SysAclModule aclModule = sysAclModuleMapper.selectByPrimaryKey(aclModuleId);
        Preconditions.checkNotNull(aclModule, "待删除的权限模块不存在，无法删除");
        if(sysAclModuleMapper.countByParentId(aclModule.getId()) > 0) {
            throw new ParamException("当前模块下面有子模块，无法删除");
        }
        if (sysAclMapper.countByAclModuleId(aclModule.getId()) > 0) {
            throw new ParamException("当前模块下面有用户权限，无法删除");
        }
        sysAclModuleMapper.deleteByPrimaryKey(aclModuleId);
    }

    private boolean checkExist(Integer parentId, String aclModuleName, Integer deptId) {
        return sysAclModuleMapper.coutByNameAndParentId(parentId, aclModuleName, deptId) >0 ;
    }

    private String getLevel(Integer aclModuleId) {
        SysAclModule sysAclModule = sysAclModuleMapper.selectByPrimaryKey(aclModuleId);
        if (sysAclModule == null) {
            return  null;
        }
        return sysAclModule.getLevel();
    }
}
