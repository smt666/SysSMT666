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
import smt666.mall.dao.SysDeptMapper;
import smt666.mall.dao.SysUserMapper;
import smt666.mall.model.SysDept;
import smt666.mall.utils.LevelUtil;
import smt666.param.DeptParam;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author 卢2714065385
 * @date 2020-05-04 3:55
 */
@Service
@Slf4j
public class SysDeptService {

    @Resource
    private SysDeptMapper sysDeptMapper;
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysLogService sysLogService;

    /**
     * <font color="#f0eb16" size=5>新增部门信息</font>&nbsp;的Service业务层save()方法。
     * @param deptParam
     */
    public void save(DeptParam deptParam){
        BeanValidator.checkParams(deptParam);
        if (checkDeptNameExists(deptParam.getParentId(), deptParam.getName(), deptParam.getId())) {
            // 如果部门相同的话，那么抛出异常（同一级部门中不允许存在多个重复的部门名称）。
            throw new ParamException("当前同一层级下存在相同的名称的部门啦！（请换个新的部门名称输入吧）");
        }
        // 因为我们在model的SysDept类使用了@Builder注解，所此处我们可以使用建造者模式精准的构建指定属性的对象。
        SysDept dept = SysDept.builder()
                .name(deptParam.getName())
                .parentId(deptParam.getParentId())
                .seq(deptParam.getSeq())
                .remark(deptParam.getRemark()).build();
        dept.setLevel(LevelUtil.calculateLevel(getLevel(deptParam.getParentId()), deptParam.getParentId()));
        // TODO  我们先默认填入一个字符串，表示需要登录后才能写入的操作者。
        dept.setOperator(RequestHolder.getCurrentUser().getUsername());
        dept.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        dept.setOperateTime(new Date());
        // 数据都准备好后，执行用户数据入库操作。
        sysDeptMapper.insertSelective(dept);
        sysLogService.saveDeptLog(null, dept);
        log.info("[SysDeptService]\n\t\t操作员：{}" + dept.getOperator() + "\n\t\tIP地址为：" + dept.getOperateIp() + "\n\t\t当前时间：" + dept.getOperateTime());
    }

    /**
     * <font color="#f0eb16" size=5>更新部门及子部门的信息</font>。
     * @param param
     */
    public void update(DeptParam param) {
        BeanValidator.checkParams(param);
        if (checkDeptNameExists(param.getParentId(), param.getName(), param.getId())) {
            // 如果部门相同的话，那么抛出异常（同一级部门中不允许存在多个重复的部门名称）。
            throw new ParamException("当前同一层级下存在相同的名称的部门啦！（请换个新的部门名称输入吧）");
        }
        // 先判断一下我们将要进行更新的部门是否存在。
        SysDept before = sysDeptMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before, "抱歉，待更新的部门不存在！（请先添加部门）");
        // 接下来我们需要判断一下，同一个层级下是否存在相同名称的部门。
        if (checkDeptNameExists(param.getParentId(), param.getName(), param.getId())) {
            // 如果部门相同的话，那么抛出异常（同一级部门中不允许存在多个重复的部门名称）。
            throw new ParamException("当前同一层级下存在相同的名称的部门啦！（请换个新的部门名称输入吧）");
        }
        SysDept after = SysDept.builder()
                .id(param.getId())
                .name(param.getName())
                .parentId(param.getParentId())
                .seq(param.getSeq())
                .remark(param.getRemark()).build();
        after.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()), param.getParentId()));
        // TODO
        after.setOperator(RequestHolder.getCurrentUser().getUsername());
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        after.setOperateTime(new Date());
        updateWithChild(before, after);
        sysLogService.saveDeptLog(before, after);
        log.info("[SysDeptService]\n\t\t操作员：{}" + after.getOperator() + "\n\t\tIP地址为：" + after.getOperateIp() + "\n\t\t当前时间：" + after.getOperateTime());
    }

    /**
     * 我们必须添加事务，在更新部门信息时，同时更新当前部门和子部门信息。要么都成功要么都失败。
     * @param before
     * @param after
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateWithChild(SysDept before, SysDept after) {
        // 先判断一下，是否需要去更新子部门的信息。
        // 获取出新部门的level的前缀。
        String newLevelPrefix = after.getLevel();
        // 获取出之前部门的level的前缀。
        String oldLevelPrefix = before.getLevel();
        if ( !after.getLevel().equals(before.getLevel()) ) {
            // 如果不一致时，我们才需要去更新子部门的信息。
            List<SysDept> deptList = sysDeptMapper.getChildDeptListByLevel(before.getLevel());
            if (CollectionUtils.isNotEmpty(deptList)) {
                for ( SysDept dept: deptList ) {
                    // 获取当前部门的level 。
                    String level = dept.getLevel();
                    if ( level.indexOf(oldLevelPrefix)==0 ) {
                        level = newLevelPrefix + level.substring(oldLevelPrefix.length());
                        dept.setLevel(level);
                    }
                }
                // 批量更新Level 。
                sysDeptMapper.batchUpdateLevel(deptList);
            }
        }
        // 然后更新当前部门的信息。
        sysDeptMapper.updateByPrimaryKey(after);
    }

    /**
     * 同一级部门中不允许出现相同的部门名称。大于0代表已经存在这条记录了,返回true 。
     * @param parentId 上级部门ID（如果相同说明在同一级部门中）。
     * @param deptName 需要进行检查的部门的名称（检查这个给定的部门名称是否唯一）。
     * @param deptId 部门的ID（它用于咋子update更新操作时需要依据部门的ID进行更新）。
     * @return boolean值（返回true代表有多个相同的部门名称！返回false则表示部门名称在同一级部门中是唯一的）
     */
    private boolean checkDeptNameExists(Integer parentId, String deptName, Integer deptId){
        return sysDeptMapper.countByNameAndParentId(parentId, deptName, deptId) > 0;
    }

    /**
     * <font color="#f0eb16" size=5>列出部门层级树</font>。<br/><br/>
     * 依据部门ID查询，如果有查询到那么就返回上一级部门的level 。<br/>
     * 如果查询不到说明当前就是第1级，那么就返回空值，表示这是首层部门。
     * @param deptId
     * @return
     */
    private String getLevel(Integer deptId) {
        SysDept dept = sysDeptMapper.selectByPrimaryKey(deptId);
        if (dept == null) {
            return  null;
        }
        return dept.getLevel();
    }

    public void delete(int deptId) {
        SysDept dept = sysDeptMapper.selectByPrimaryKey(deptId);
        Preconditions.checkNotNull(dept, "待删除的部门不存在，无法删除");
        if (sysDeptMapper.countByParentId(dept.getId()) > 0) {
            throw new ParamException("当前部门下面有子部门，无法删除");
        }
        // 如果没有子部门，那么继续判断当前部门下是否有用户的存在。
        if(sysUserMapper.countByDeptId(dept.getId()) > 0) {
            throw new ParamException("当前部门下面有用户，无法删除");
        }
        sysDeptMapper.deleteByPrimaryKey(deptId);
    }

}
