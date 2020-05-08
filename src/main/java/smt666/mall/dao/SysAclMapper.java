package smt666.mall.dao;

import org.apache.ibatis.annotations.Param;
import smt666.common.beans.PageQuery;
import smt666.mall.model.SysAcl;

import java.util.List;

/**
 * @author 27140
 */
public interface SysAclMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysAcl record);

    int insertSelective(SysAcl record);

    SysAcl selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysAcl record);

    int updateByPrimaryKey(SysAcl record);

    /**
     * 统计出指定的权限模块id中的权限点的总权限点数量。
     * @param aclModuleId 权限模块id（查询的条件）。
     * @return 指定模块下权限点的总数。
     */
    int countByAclModuleId(@Param("aclModuleId") int aclModuleId);

    /**
     * 依据权限模块的id查询当前模块中的所有的权限点信息。
     * @param aclModuleId 指定的权限模块的id
     * @param page 给定分页的参数
     * @return 经过分页了的指定权限模块下的所有的权限点的列表信息。
     */
    List<SysAcl> getPageByAclModuleId(
            @Param("aclModuleId") int aclModuleId,
            @Param("page") PageQuery page);

    /**
     * 根据指定的权限模块id查询当前模块下指定权限名称的权限点信息是否存在。
     * @param aclModuleId 权限模块的id
     * @param name 权限点的名称
     * @param id 权限点的id
     * @return 返回值大于0，就代表指定权限模块下的指定名称的权限信息已经存在了。
     */
    int countByNameAndAclModuleId(
            @Param("aclModuleId") int aclModuleId,
            @Param("name") String name,
            @Param("id") Integer id);

    List<SysAcl> getAll();

    List<SysAcl> getByIdList(@Param("idList") List<Integer> idList);

    List<SysAcl> getByUrl(@Param("url") String url);
}