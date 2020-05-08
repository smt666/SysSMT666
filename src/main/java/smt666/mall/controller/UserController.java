package smt666.mall.controller;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import smt666.common.util.ShaSmt;
import smt666.mall.model.SysUser;
import smt666.mall.service.SysUserService;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 卢2714065385
 * @date 2020-05-04 23:05
 */
@Controller
public class UserController {

    @Resource
    private SysUserService sysUserService;

    /**退出登录*/
    @RequestMapping("/logout.page")
    public void logOut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 我们只需把request请求中的session信息清空即可了。
        request.getSession().invalidate();
        // 并且跳转到登录的页面。
        response.sendRedirect("signin.jsp");
    }

    /**
     * 用户登录。
     * @param request
     * @param response
     */
    @RequestMapping("/login.page")
    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        // 判断用户和密码是否正确。
        SysUser sysUser = sysUserService.findByKeyword(username);
        // 设定一个默认的错误提示信息。
        String errorMsg = "";
        // 登录成功之后需要跳转的链接地址。
        String ret = request.getParameter("ret");

        if (StringUtils.isBlank(username)) {
            errorMsg = "请填写您的用户名！";
        } else if (StringUtils.isBlank(password)) {
            errorMsg = "密码不能为空！";
        } else if ( sysUser == null ) {
            errorMsg = "查询不到指定的用户！";
        } else if ( !sysUser.getPassword().equals(ShaSmt.jdksha512(password)) ) {
            errorMsg = "用户名或者密码错误啦。";
        } else if ( sysUser.getStatus() != 1 ) {
            errorMsg = "用户已被冻结，请您联系相关人员解封您的账号！";
        } else {
            // 以上验证过后到这步说明验证都通过了，登录成功！
            // 登录成功后，我们需要把当前用户的信息，存放到session。
            request.getSession().setAttribute("user", sysUser);
            // 然后我们判断，如果当前的ret不为空的话，那么我们要跳转到ret的页面。
            if (StringUtils.isNotBlank(ret)) {
                response.sendRedirect(ret);
            } else {
                // 如果ret是空值的话，那么我们就需要自己指定一个进行跳转的链接。
                // （相当于跳转到了/admin/目录下的首页index.html页面。） // TODO:
                response.sendRedirect("/admin/index.page");
            }
        }
        // 如果当前的用户无法登录的情况下，我们需要把错误的信息显示到页面上去。
        request.setAttribute("error", errorMsg);
        request.setAttribute("username", username);
        if (StringUtils.isNotBlank(ret)) {
            request.setAttribute("ret", ret);
        }
        // 定义客户端跳转的链接。
        String path = "/signin.jsp";
        request.getRequestDispatcher(path).forward(request, response);
        return;
    }

}
