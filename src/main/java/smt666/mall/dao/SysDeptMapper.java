package smt666.mall.dao;

import org.apache.ibatis.annotations.Param;
import smt666.mall.model.SysDept;

import java.util.List;

/**
 * @author 27140
 */
public interface SysDeptMapper {
    int deleteByPrimaryKey(@Param("id") Integer id);

    int insert(SysDept record);

    int insertSelective(SysDept record);

    SysDept selectByPrimaryKey(@Param("id") Integer id);

    int updateByPrimaryKeySelective(SysDept record);

    int updateByPrimaryKey(SysDept record);

    /**
     * 查询部门表中所有的字段信息。
     * @return
     */
    List<SysDept> getAllDept();

    List<SysDept> getChildDeptListByLevel(@Param("level") String level);

    /** 批量更新部门列表信息。 */
    void batchUpdateLevel(@Param("sysDeptList") List<SysDept> sysDeptList);

    /**
     * 如果id不为null空值的话，那么就依据上级部门ID来统计表中的总记录数量：有几条数据（包括null的记录）
     * @param parentId 上级部门的ID 。
     * @param name 部门名称。
     * @param id 部门的ID。
     * @return 等于0或者大于0的数字。大于0代表已经存在这条记录了。
     */
    int countByNameAndParentId(@Param("parentId") Integer parentId, @Param("name") String name, @Param("id") Integer id);

    /**
     * 我们依据当前部门的ID去查询：有没有哪个部门的parentId上级部门ID与查询的结果有数据，来判断当前部门是否存在子部门。
     * @param id
     * @return
     */
    int countByParentId(@Param("deptId") Integer id);
}