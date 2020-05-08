package smt666.mall.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import smt666.common.beans.PageQuery;
import smt666.common.vo.JsonResult;
import smt666.mall.service.SysLogService;
import smt666.param.SearchLogParam;

import javax.annotation.Resource;

/**
 * @author 27140
 */
@Controller
@RequestMapping("/sys/log")
public class SysLogController {

    @Resource
    private SysLogService sysLogService;

    @RequestMapping("/log.page")
    public ModelAndView page() {
        return new ModelAndView("log");
    }

    @RequestMapping("/recover.json")
    @ResponseBody
    public JsonResult recover(@RequestParam("id") int id) {
        sysLogService.recover(id);
        return JsonResult.success();
    }

    @RequestMapping("/page.json")
    @ResponseBody
    public JsonResult searchPage(SearchLogParam param, PageQuery page) {
        return JsonResult.success(sysLogService.searchPageList(param, page));
    }
}
