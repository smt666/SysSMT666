package smt666.mall.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * 用户在登录成功之后开始处理的Controller类。
 * @author 卢2714065385
 * @date 2020-05-04 23:37
 */
@Controller
@RequestMapping("/admin")
public class AdminController {


    @RequestMapping("/index.page")
    public ModelAndView index() {
        return new ModelAndView("admin");
    }
}
