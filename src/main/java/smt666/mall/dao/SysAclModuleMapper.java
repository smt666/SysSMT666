package smt666.mall.dao;

import org.apache.ibatis.annotations.Param;
import smt666.mall.model.SysAclModule;

import java.util.List;

public interface SysAclModuleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysAclModule record);

    int insertSelective(SysAclModule record);

    SysAclModule selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysAclModule record);

    int updateByPrimaryKey(SysAclModule record);


    int coutByNameAndParentId(
            @Param("parentId") Integer parentId,
            @Param("name") String aclModuleName,
            @Param("id") Integer id);

    List<SysAclModule> getChildSysAclModulListByLevel(@Param("level") String level);

    void batchUpdateLevel(@Param("sysAclModuleList") List<SysAclModule> sysAclModuleList);

    List<SysAclModule> getAllAclModule();

    int countByParentId(@Param("aclModuleId") int aclModuleId);
}