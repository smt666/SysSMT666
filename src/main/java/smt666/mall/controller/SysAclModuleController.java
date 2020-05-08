package smt666.mall.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import smt666.common.vo.JsonResult;
import smt666.mall.service.SysAclModuleService;
import smt666.mall.service.SysTreeService;
import smt666.param.AclModuleParam;

import javax.annotation.Resource;

/**
 * @author 卢2714065385
 * @date 2020-05-05 14:01
 */

@Controller
@RequestMapping("/sys/aclModule")
@Slf4j
public class SysAclModuleController {

    @Resource
    private SysAclModuleService sysAclModuleService;
    @Resource
    private SysTreeService sysTreeService;

    @RequestMapping("/acl.page")
    public ModelAndView page() {
        return new ModelAndView("acl");
    }

    @RequestMapping("/save.json")
    @ResponseBody
    public JsonResult save(AclModuleParam param) {
        sysAclModuleService.save(param);
        return JsonResult.success();
    }

    @RequestMapping("/update.json")
    @ResponseBody
    public JsonResult update(AclModuleParam param) {
        sysAclModuleService.update(param);
        return JsonResult.success();
    }

    @RequestMapping("/tree.json")
    @ResponseBody
    public JsonResult tree() {
        return JsonResult.success(sysTreeService.aclModuleTree());
    }

    @RequestMapping("/delete.json")
    @ResponseBody
    public JsonResult delete(@RequestParam("id") int id) {
        sysAclModuleService.delete(id);
        return JsonResult.success();
    }

}
