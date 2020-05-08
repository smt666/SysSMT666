package smt666.mall.service;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

import smt666.mall.dao.SysAclMapper;
import smt666.mall.dao.SysAclModuleMapper;
import smt666.mall.dao.SysDeptMapper;
import smt666.mall.dto.AclDto;
import smt666.mall.dto.AclModuleLevelDto;
import smt666.mall.dto.DeptLevelDto;
import smt666.mall.model.SysAcl;
import smt666.mall.model.SysAclModule;
import smt666.mall.model.SysDept;
import smt666.mall.utils.LevelUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 计算Tree的类。
 *
 * @author 卢2714065385
 * @date 2020-05-04 6:26
 */
@Service
public class SysTreeService {

    @Resource
    private SysDeptMapper sysDeptMapper;
    @Resource
    private SysAclModuleMapper sysAclModuleMapper;
    @Resource
    private SysCoreService sysCoreService;
    @Resource
    private SysAclMapper sysAclMapper;

    /**
     * 获取用户已经分配份权限信息。
     * @param userId
     * @return
     */
    public List<AclModuleLevelDto> userAclTree(int userId) {
        List<SysAcl> userAclList = sysCoreService.getUserAclList(userId);
        List<AclDto> aclDtoList = Lists.newArrayList();
        for (SysAcl acl : userAclList) {
            AclDto dto = AclDto.adapt(acl);
            dto.setHasAcl(true);
            dto.setChecked(true);
            aclDtoList.add(dto);
        }
        return aclListToTree(aclDtoList);
    }

    public List<AclModuleLevelDto> roleTree(int roleId) {
        // 1、获取出：【当前用户】（已经被分配权限了）的所有权限点信息。
        List<SysAcl> userAclList = sysCoreService.getCurrentUserAclList();
        // 2、获取出：【当前角色】（已经被分配权限了）的所有权限点信息。
        List<SysAcl> roleAclList = sysCoreService.getRoleAclList(roleId);
        /* 3、接下来我们声明一个DTO*/
        List<AclDto> aclDtoList = Lists.newArrayList();
        // 4、定义一个当前用户名下所有权限的ID的一个Set集合 userAclIdSet 。
        /* Set<Integer> userAclIdSet = userAclList.stream().map(sysAcl -> sysAcl.getId()).collect(Collectors.toSet())
        我们使用JDK1.8的高级语法：使用流。
            我们使用List集合中的stream()流调用map方法。这个map方法它就相当于遍历当前这个List列表，然后map方法中的：
            第1个参数，它是我们方法中的一个对象
            第2个参数表示，取出这个对象sysAcl中的id值，然后组装成一个List集合，同时转换成一个Set集合。
        这样一来，我们就通过一行语句，将之前取出来的用户的权限点的所有的ID值，封装成了一个Set集合。
         */
        Set<Integer> userAclIdSet = userAclList.stream().map(sysAcl -> sysAcl.getId()).collect(Collectors.toSet());
        // 5、定义一个当前这个角色相关的所有的权限点的ID的Set集合 roleAclSet 。
        Set<Integer> roleAclIdSet = roleAclList.stream().map(sysAcl -> sysAcl.getId()).collect(Collectors.toSet());
        /* 6、获取出所有的权限点信息。*/
        List<SysAcl> allAclList = sysAclMapper.getAll();
        // 7、然后我们就可以获取出了个Set的并集了（把用户ID集合和角色ID集合合并后，就可以过滤掉重复的ID信息了）。
//        Set<SysAcl> aclSet = new HashSet<>(allAclList);
        for ( SysAcl acl : allAclList ) {
            // 判断每一个AclDto权限点参数中的每个值（checked、hasAcl）是否为true或者false 。
            // 首先，我们获取到要使用的DTO对象。
            AclDto dto = AclDto.adapt(acl);
            // 判断当前的集合中是否包含了了我们即将要进行遍历的AclDto的id属性。
            if (userAclIdSet.contains(acl.getId())) {
                // 如果当前用户的权限点ID集合中有id属性的话，那么我们就将AclDto中的hasAcl属性设置成true代表：【有权限】进行操作。
                dto.setHasAcl(true);
            }
            if (roleAclIdSet.contains(acl.getId())) {
                // 如果当前角色的权限点ID集合中有id属性的话，那么我们就将AclDto中的checked属性设置成true代表：已经被选中了。
                dto.setChecked(true);
            }
            aclDtoList.add(dto);
        }
        // 接下来，我们把当前的aclDtoList转换成我们的权限和角色组成的Tree树。
        return aclListToTree(aclDtoList);
    }

    /**
     * 把当前的aclDtoList转换成我们的权限和角色组成的Tree树。
     * 遍历每一个权限模块树中的模块，把每一个权限进行绑定。
     * @param aclDtoList 我们准备好一个权限模块的Tree数（List<AclDto> aclDtoList）。
     * @return
     */
    public List<AclModuleLevelDto> aclListToTree(List<AclDto> aclDtoList) {
        if (CollectionUtils.isEmpty(aclDtoList)) {
            return Lists.newArrayList();
        }
        // 获取到权限模块列表树。
        List<AclModuleLevelDto> aclModuleLevelList = aclModuleTree();
        // 每一个权限模块对应一个权限点信息。
        Multimap<Integer, AclDto> moduleIdAclMap = ArrayListMultimap.create();
        for ( AclDto acl :  aclDtoList) {
            // 权限点状态(可选数值为：[0]‘禁用’、[1]‘正常
            if (acl.getStatus() == 1) {
                moduleIdAclMap.put(acl.getAclModuleId(), acl);
            }
        }
        bindAclWithOrder(aclModuleLevelList, moduleIdAclMap);
        return aclModuleLevelList;
    }

    /**
     * 递归的去绑定每个权限模块上的权限点的信息。
     * 【带了已经排好序的绑定了权限点信息的一个权限模块树集合】
     * @param aclModuleLevelList
     * @param moduleIdAclMap
     */
    public void bindAclWithOrder(List<AclModuleLevelDto> aclModuleLevelList, Multimap<Integer, AclDto> moduleIdAclMap) {
        // 判断当前的模块是否为空。
        if (CollectionUtils.isEmpty(aclModuleLevelList)) {
            // 直接中止，退出方法。
            return;
        }
        // 不为空，那么就遍历。
        // 根据模块ID，遍历出存储的权限点的列表信息。
        for ( AclModuleLevelDto dto : aclModuleLevelList ) {
            // 获取每一层级的权限模块中的每一个AclDto参数的ID属性。
            List<AclDto> aclDtoList = (List<AclDto>) moduleIdAclMap.get(dto.getId());
            if (CollectionUtils.isNotEmpty(aclDtoList)) {
                // 如果是遍历出的权限点列表是有数据的话，那么我们流需要把启动的这些权限点，依次绑定到我们的权限模块上面。
                // 首先我们要先做一个排序。
                Collections.sort(aclDtoList, aclSeqComparator);
                //排序完成后，我们把当前处理好的权限点列表，放到权限的模块中去。
                dto.setAclDtoList(aclDtoList);
            }
            // 我们有继续将下一个层级的模块绑定上去。
            bindAclWithOrder(dto.getAclModuleList(), moduleIdAclMap);
        }
    }

    /**
     * 取出当前所有的模块。
     * 将每一层组成层级对象。
     * @return
     */
    public List<AclModuleLevelDto> aclModuleTree() {
        List<SysAclModule> aclModuleList = sysAclModuleMapper.getAllAclModule();
        List<AclModuleLevelDto> dtoList = Lists.newArrayList();
        for (SysAclModule aclModule : aclModuleList) {
            dtoList.add(AclModuleLevelDto.adpt(aclModule));
        }
        return aclModuleListToTree(dtoList);
    }

    /**
     * 在把每层模块做成对象后，我们重新组织一次每层的组织结构。
     * 使得我们看他依据当前的层级，获取出当前层级下的子模块。
     * @param dtoList 封装成DTO的AclModuleLevelDto的List模块列表。
     * @return
     */
    public List<AclModuleLevelDto> aclModuleListToTree(List<AclModuleLevelDto> dtoList) {
        if (CollectionUtils.isEmpty(dtoList)) {
            // 如果权限模块为空的话，那么我们就返回一个空的列表即可。
            return Lists.newArrayList();
        }
        // Key    ====    level
        // Value  ====    [aclModule01, aclModule02, aclModule03, ....]
        Multimap<String, AclModuleLevelDto> levelAclModuleMap = ArrayListMultimap.create();
        List<AclModuleLevelDto> rootList = Lists.newArrayList();
        // 遍历出所有位于第1层（首层）的模块，然后统一添加到这个Map中并且进行排序。
        for ( AclModuleLevelDto dto : dtoList ) {
            levelAclModuleMap.put(dto.getLevel(), dto);
            if ( LevelUtil.ROOT.equals(dto.getLevel()) ) {
                rootList.add(dto);
            }
        }
        Collections.sort(rootList, aclModuleSeqComparator);
        transformAclModuleTree(rootList, LevelUtil.ROOT, levelAclModuleMap);
        return rootList;
    }

    /**
     * 从首层开始，不停递归处理下一个层级的子元素的方式，逐层处理方式，直到最后一层处理完毕。
     * @param rootList
     * @param level
     * @param levelAclModuleMap
     */
    public void transformAclModuleTree(List<AclModuleLevelDto> rootList, String level, Multimap<String, AclModuleLevelDto> levelAclModuleMap) {
        for (int i=0; i<rootList.size(); i++){
            AclModuleLevelDto dto = rootList.get(i);
            String nextLevel = LevelUtil.calculateLevel(level, dto.getId());
            List<AclModuleLevelDto> tmpList = (List<AclModuleLevelDto>) levelAclModuleMap.get(nextLevel);
            // 获取到了下一个层级的模块之后开始处理。
            // 首先判断是否有下一级的模块模块。
            if (CollectionUtils.isNotEmpty(tmpList)) {
                // 如果有获取到下一级的权限模块信息，我们才开始处理。
                //> 1、先排序。每个子模块层全都统一按照一致的排序规则进行排序（从小到大）。
                Collections.sort(tmpList, aclModuleSeqComparator);
                //> 2、然后把下一个层级的模块添加进来。
                dto.setAclModuleList(tmpList);
                // 处理完这个下一层后，我们继续进行下下一层的递归处理。
                transformAclModuleTree(tmpList, nextLevel, levelAclModuleMap);
            }
        }
    }

    /**
     * 我们用此方法来返回一个部门的层级树。
     * @return 部门的层级树
     */
    public List<DeptLevelDto> deptTree() {
        // 从数据库中查询出`sys_dept`部门表中的所字段的信息。
        List<SysDept> deptList = sysDeptMapper.getAllDept();
        // 我们把查询到的部门结果，做成一个DTO（即重新组织成一个POJO），准备好数据的传输工作。
        List<DeptLevelDto> dtoList = Lists.newArrayList();
        // 然后对我们开始组织这个DTO，对查询的结果deptList进行遍历，适配进DTO（dtoList）里面。
        for ( SysDept dept : deptList) {
            DeptLevelDto dto = DeptLevelDto.copyToDeptDTO(dept);
            dtoList.add(dto);
        }
        return deptListToTree(dtoList);
    }

    /**
     * <font color="#f0eb16" size=5>将DTO类转换成Tree树形结构的目录</font><br/>
     * 我们写这个方法，它可以将DTO里面的每个字段都列出来做成一个Tree树型结构。
     * @param dtoList 传入一个新建的List（它是组织DTO时用来存放适配字段的容器，我们使用List即可。）
     * @return 返回DeptLevelDto类型的树形列表
     */
    public List<DeptLevelDto> deptListToTree(List<DeptLevelDto> dtoList) {
        if (CollectionUtils.isEmpty(dtoList)) {
            // 如果当前传入的deptLevelList是空值的话，那么我们就直接返回一个List回去。
            return Lists.newArrayList();
        }
        // 如果deptLevelList有值，那么开始组织Tree结构。
        /*
         * 首先我们定义一个特殊的数据结构，我们要把当前的Tree，以它的level为Key，level所对应的部门为Value封装成一个Map集合。
         * 此处我们使用一个高级流，Multimap【import com.google.common.collect.Multimap】。
         * 我们使用ArrayListMultimap调用create()方法来进行初始化的操作。
         *  level  --->   [dept01, dept02, ... ,deptN]    这样的Key-Value结构。
         */
        Multimap<String, DeptLevelDto> levelDeptMap = ArrayListMultimap.create();
        // 获取出第1级部门。
        List<DeptLevelDto> rootList = Lists.newArrayList();
        // 接着遍历deptLevelList 。
        /* 每个值我们都把当前的deptLevelList取出来放到levelDeptMap中。
         * 这样一来，每次取getLevel之后，我都可以把相同level的List取出来。
         * 即，当我们定义好了一个level时，它下面的所有部门都将放在这个levelDeptMap的value里面了，便于之后的运算。
         */
        for ( DeptLevelDto dto : dtoList ) {
            levelDeptMap.put(dto.getLevel(), dto);
            // 判断出第1级部门（首层部门）。
            if (LevelUtil.ROOT.equals(dto.getLevel())) {
                // 如果是第1级部门，那么就把它添加到首层部门中去。
                rootList.add(dto);
            }
        }
        // 对同一个层次或者级别的多个部门进行排序。
        Collections.sort(rootList, new Comparator<DeptLevelDto>(){
            @Override
            public int compare(DeptLevelDto o1, DeptLevelDto o2) {
                // 完成了按照seq从小到大进行排序的设置。
                return o1.getSeq() - o2.getSeq();
            }
        });
        // 每遍历一层之后就会当前层级一个Tree树形结构的组织。
        transformDeptTree(rootList, LevelUtil.ROOT, levelDeptMap);
        // 然后把首层进行返回。
        return rootList;
    }

    /**
     * 通过递归生成Tree树。（不停生成下一级，直到没有下一个层级。）
     * @param deptLevelList
     * @param level
     * @param levelDeptMap
     */
    public void transformDeptTree(
            List<DeptLevelDto> deptLevelList,   /*层级的部门列表*/
            String level,
            Multimap<String, DeptLevelDto> levelDeptMap  /*传入的map是为了继续层级计算的*/
    ){
        // 递归。
        for (int i=0; i<deptLevelList.size(); i++) {
            // 首先遍历当前deptLevelList中的某个元素。
            DeptLevelDto deptLevelDto = deptLevelList.get(i);
            // 处理当前层级的数据。
            String nextLevel = LevelUtil.calculateLevel(level, deptLevelDto.getId());
            // 处理完一层后，就回去处理下一层部门。
            List<DeptLevelDto> tempDeptList = (List<DeptLevelDto>) levelDeptMap.get(nextLevel);
            // 对当前的层级排序等等相关处理。
            if (CollectionUtils.isNotEmpty(tempDeptList)) {
                // 排序
                Collections.sort(tempDeptList, deptSeqComparator);
                // 设置下一层部门
                deptLevelDto.setDeptList(tempDeptList);
                // 进入到下一次后再处理（实现了Tree树的递归操作）。
                transformDeptTree(tempDeptList, nextLevel, levelDeptMap);
            }
        }

    }

    public Comparator<DeptLevelDto> deptSeqComparator = new Comparator<DeptLevelDto>(){
        @Override
        public int compare(DeptLevelDto o1, DeptLevelDto o2) {
            // 完成了按照seq从小到大进行排序的设置。
            return o1.getSeq() - o2.getSeq();
        }
    };

    public Comparator<AclModuleLevelDto> aclModuleSeqComparator = new Comparator<AclModuleLevelDto>(){
        @Override
        public int compare(AclModuleLevelDto o1, AclModuleLevelDto o2) {
            return o1.getSeq() - o2.getSeq() ;
        }
    };

    /**
     * 对权限点的列表信息进行一个排序的操作。
     */
    public Comparator<AclDto> aclSeqComparator = new Comparator<AclDto>(){
        @Override
        public int compare(AclDto o1, AclDto o2) {
            return o1.getSeq() - o2.getSeq() ;
        }
    };
}