package smt666.mall.controller;

import com.google.common.collect.Maps;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import smt666.common.beans.PageQuery;
import smt666.common.vo.JsonResult;
import smt666.mall.service.SysRoleService;
import smt666.mall.service.SysTreeService;
import smt666.mall.service.SysUserService;
import smt666.param.UserParam;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author 卢2714065385
 * @date 2020-05-04 18:13
 */
@Controller
@RequestMapping("/sys/user")
public class SysUserController {

    @Resource
    private SysUserService sysUserService;
    @Resource
    private SysTreeService sysTreeService;
    @Resource
    private SysRoleService sysRoleService;

    @RequestMapping("/noAuth.page")
    public ModelAndView noAuth() {
        return new ModelAndView("noAuth");
    }

    /**&nbsp;&nbsp;&nbsp;&nbsp;
     * <font color="#f0eb16" size=5>/sys/user/save.json</font><br/><br/>
     * 处理<font color="#f0eb16" size=5>用户注册</font>&nbsp;请求的Controller控制层save()方法。
     * @param param 需要注册的用户信息。
     * @return 返回JSON格式的数据给前端页面。
     */
    @RequestMapping("/save.json")
    @ResponseBody
    public JsonResult saveUser(UserParam param) {
        sysUserService.save(param);
        return JsonResult.success();
    }

    /**&nbsp;&nbsp;&nbsp;&nbsp;
     * <font color="#f0eb16" size=5>/sys/user/update.json</font><br/><br/>
     * 处理<font color="#f0eb16" size=5>更新用户的信息</font>&nbsp;请求的Controller控制层save()方法。
     * @param param
     * @return
     */
    @RequestMapping("/update.json")
    @ResponseBody
    public JsonResult updateUser(UserParam param) {
        sysUserService.update(param);
        return JsonResult.success();
    }

    @RequestMapping("/page.json")
    @ResponseBody
    public JsonResult page(@RequestParam("deptId") int deptId, PageQuery query) {
        return JsonResult.success(sysUserService.getPageByDeptId(deptId, query));
    }

    @RequestMapping("/acls.json")
    @ResponseBody
    public JsonResult acls(@RequestParam("userId") int userId) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("acls", sysTreeService.userAclTree(userId));
        map.put("roles", sysRoleService.getRoleListByUserId(userId));
        return JsonResult.success(map);
    }
}
