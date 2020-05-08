package smt666.mall.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import smt666.common.util.StringUtil;
import smt666.common.vo.JsonResult;
import smt666.mall.model.SysUser;
import smt666.mall.service.*;
import smt666.param.RoleParam;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author 卢2714065385
 * @date 2020-05-06 20:41
 */
@Controller
@RequestMapping("/sys/role")
public class SysRoleController {

    @Resource
    private SysRoleService sysRoleService;
    @Resource
    private SysTreeService sysTreeService;
    @Resource
    private SysRoleAclService sysRoleAclService;
    /*引入角色与用户的业务层对象*/
    @Resource
    private SysRoleUserService sysRoleUserService;
    @Resource
    private SysUserService sysUserService;
    /**
     * 通过访问【/sys/role/sole.page】进入到角色管理的操作页面中。
     * @return
     */
    @RequestMapping("/role.page")
    public ModelAndView page(){
        return new ModelAndView("role");
    }

    /**
     * 增加
     * @param param
     * @return
     */
    @RequestMapping("/save.json")
    @ResponseBody
    public JsonResult save(RoleParam param) {
        sysRoleService.save(param);
        return JsonResult.success();
    }

    /**
     * 修改。
     * @param param
     * @return
     */
    @RequestMapping("/update.json")
    @ResponseBody
    public JsonResult update(RoleParam param) {
        sysRoleService.update(param);
        return JsonResult.success();
    }

    /**
     * 查询。
     * @return
     */
    @RequestMapping("/list.json")
    @ResponseBody
    public JsonResult list() {
        return JsonResult.success(sysRoleService.getAll());
    }

    @RequestMapping("/roleTree.json")
    @ResponseBody
    public JsonResult roleAclTree(@RequestParam("roleId") Integer roleId) {
        return JsonResult.success(sysTreeService.roleTree(roleId));
    }

    @RequestMapping("/changeAcls.json")
    @ResponseBody
    public JsonResult changeAcls(
            @RequestParam("roleId") int roleId,
            @RequestParam(value = "aclIds", required = false, defaultValue = "") String aclIds
            ) {
        List<Integer> aclIdsList = StringUtil.splitToListInt(aclIds);
        sysRoleAclService.changRoleAcls(roleId, aclIdsList);
        return JsonResult.success();
    }

    @RequestMapping("/changeUsers.json")
    @ResponseBody
    public JsonResult changeUsers(
            @RequestParam("roleId") int roleId,
            @RequestParam(value = "userIds", required = false, defaultValue = "") String userIds) {
        List<Integer> userIdList = StringUtil.splitToListInt(userIds);
        sysRoleUserService.changeRoleUsers(roleId, userIdList);
        return JsonResult.success();
    }

    /**
     * 获取用户相关的数据。
     * @param roleId 角色ID
     * @return
     */
    @RequestMapping("/users.json")
    @ResponseBody
    public JsonResult users(@RequestParam("roleId") int roleId) {
        // 根据给定的roleId角色ID查询数据表获取出选中了角色的状态的对应的用户列表的数据。
        List<SysUser> selectedUserList = sysRoleUserService.getListByRoleId(roleId);
        // 获取为选中状态的用户的列表（总的用户列表 - 已经选中的用户列表）。
        List<SysUser> allUserList = sysUserService.getAll();
        // ==========获取出未选中的用户列表。===========================
        List<SysUser> unselectedUserList = Lists.newArrayList();
        /*Set<Integer> selectedUserIdSet = selectedUserList.stream().map(sysUser -> sysUser.getId()).collect(Collectors.toSet());*/
        Set<Integer> selectedUserIdSet = selectedUserList.stream().map(SysUser::getId).collect(Collectors.toSet());
        for(SysUser sysUser : allUserList) {
            // private Integer status ===== 只能选择【[0]禁用、[1]正常、[2]黑名单】其中之一的数字
            if (sysUser.getStatus() == 1 && !((Set) selectedUserIdSet).contains(sysUser.getId())) {
                unselectedUserList.add(sysUser);
            }
        }
        /* 已选中的用户列表里面，如果这个用户的getStatus状态不等于1的时候，那么我们是否需要展示呢？
        这个是取决于用户的效果，如果我们不希望进行展示的话，那么只需要添加这句即可，进行一个Filter过滤操作即可。
        【selectedUserList = selectedUserList.stream().filter(sysUser -> sysUser.getStatus() != 1).collect(Collectors.toList())】
            ==== 含义,如果状态不等于1那么我们就不展示（即，状态不等于1的时候就执行过滤操作）。*/
        Map<String, List<SysUser>> map = Maps.newHashMap();
        /*【【role.jsp[461行]】var renderedSelect = Mustache.render(selectedUsersTemplate, {userList: result.data.selected})】
        * 这个"selected"就是传递到了JSP页面中的[result.data]属性中的。*/
        map.put("selected", selectedUserList);
        map.put("unselected", unselectedUserList);
        // 我们把已选的与未选的存放在Map容器中，便于前端页面可以使用。
        return JsonResult.success(map);
    }
}
