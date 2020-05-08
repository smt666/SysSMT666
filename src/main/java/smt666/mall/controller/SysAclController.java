package smt666.mall.controller;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import smt666.common.beans.PageQuery;
import smt666.common.vo.JsonResult;
import smt666.mall.model.SysRole;
import smt666.mall.service.SysAclService;
import smt666.mall.service.SysRoleService;
import smt666.param.AclParam;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author 卢2714065385
 * @date 2020-05-05 7:37
 */
@Controller
@RequestMapping("/sys/acl/")
@Slf4j
public class SysAclController {

    @Resource
    private SysAclService sysAclService;
    @Resource
    private SysRoleService sysRoleService;

    @RequestMapping("/save.json")
    @ResponseBody
    public JsonResult saveAclModule(AclParam param) {
        sysAclService.save(param);
        return JsonResult.success();
    }

    @RequestMapping("/update.json")
    @ResponseBody
    public JsonResult updateAcl(AclParam param) {
        sysAclService.update(param);
        return JsonResult.success();
    }

    /**
     * 列出指定权限模块中的所有权限点的信息。
     * @param aclModuleId 权限模块的id
     * @param pageQuery 分页信息
     * @return
     */
    @RequestMapping("/page.json")
    @ResponseBody
    public JsonResult list(@RequestParam("aclModuleId") Integer aclModuleId, PageQuery pageQuery) {
        return JsonResult.success(sysAclService.getPageByAclModuleId(aclModuleId, pageQuery));
    }

    @RequestMapping("/acls.json")
    @ResponseBody
    public JsonResult acls(@RequestParam("aclId") int aclId) {
        Map<String, Object> map = Maps.newHashMap();
        List<SysRole> roleList = sysRoleService.getRoleListByAclId(aclId);
        map.put("roles", roleList);
        map.put("users", sysRoleService.getUserListByRoleList(roleList));
        return JsonResult.success(map);
    }
}
