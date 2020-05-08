package smt666.mall.dao;

import org.apache.ibatis.annotations.Param;
import smt666.mall.model.SysRoleUser;

import java.util.List;

public interface SysRoleUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysRoleUser record);

    int insertSelective(SysRoleUser record);

    SysRoleUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysRoleUser record);

    int updateByPrimaryKey(SysRoleUser record);

    List<Integer> getRoleIdListByUserId(@Param("userId") int userId);

    /**
     * 根据角色ID查询出该角色下所有的用户信息。
     * @param roleId 角色ID
     * @return 对应的所用户信息组成的一个列表。
     */
    List<Integer> getUserIdListByRoleId(@Param("roleId") int roleId);

    void deleteByRoleId(@Param("roleId") int roleId);

    void batchInsert(@Param("roleUserList") List<SysRoleUser> roleUserList);

    List<Integer> getUserIdListByRoleIdList(@Param("roleIdList") List<Integer> roleIdList);
}