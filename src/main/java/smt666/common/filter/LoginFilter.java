package smt666.common.filter;

import lombok.extern.slf4j.Slf4j;
import smt666.common.util.RequestHolder;
import smt666.mall.model.SysUser;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <font color="#f0eb16" size=5>拦截所有需要用户进行登录的请求</font>。<br/>
 * 当请求过来时，我们需要判断用户是否已经登录了。
 * 如果用户没有登录，那么就让页面跳转到登录页面。
 * 如果用户已经登录，那么获取出登录了的用户信息，我们可以存放到ThreadLocal线程中。
 * 当我们写一个Filter时，我们需要在web.xml文件中进行配置。
 * @author 卢2714065385
 * @date 2020-05-05 6:20
 */
@Slf4j
public class LoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // 我们需要先把ServletRequest request对象转换成HttpServletRequest request对象，把ServletResponse response对象转换成HttpServletResponse response对象。
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        // 判断，
        SysUser sysUser = (SysUser) request.getSession().getAttribute("user");
        if (sysUser == null) {
            // 如果使用request从session中，没有获取到的用户信息的话，说明用户没有登录。那么我们就让页面跳转到登录页面。
            response.sendRedirect("/signin.jsp");
            return ;
        }
        // 如果用户已经登录，那么就获取登录用户的信息，存放到ThreadLocal中。我们就可以阿紫后续需要使用用户信息的场景下直接从ThreadLocal中获取用户信息即可。
        RequestHolder.add(sysUser);
        RequestHolder.add(request);
        // 调用一下doFilter过滤链，让请求继续向前走。
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
