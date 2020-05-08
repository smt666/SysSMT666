package smt666.common.filter;

import com.google.common.base.Splitter;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import smt666.common.util.ApplicationContextHelper;
import smt666.common.util.JsonMapper;
import smt666.common.util.RequestHolder;
import smt666.common.vo.JsonResult;
import smt666.mall.model.SysUser;
import smt666.mall.service.SysCoreService;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author 27140
 */
@Slf4j
public class AclControlFilter implements Filter {

    private static Set<String> exclusionUrlSet = Sets.newConcurrentHashSet();

    private final static String noAuthUrl = "/sys/user/noAuth.page";

    /**
     * 读取配置文件中指定的字段，然后解析出来我需要过滤掉的白名单的url列表。
     * @param filterConfig
     * @throws ServletException
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 定义权限拦截的白名单。
        String exclusionUrls = filterConfig.getInitParameter("exclusionUrls");
        // 我们从制度的"exclusionUrls"过滤器（不进行拦截）的白名单中，取出数据后，遍历成一个List集合。
        List<String> exclusionUrlList = Splitter.on(",").trimResults().omitEmptyStrings().splitToList(exclusionUrls);
        exclusionUrlSet = Sets.newConcurrentHashSet(exclusionUrlList);
        exclusionUrlSet.add(noAuthUrl);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // 首先把servlet下的request与response先转换成HttpServlet下的请求与响应对象。
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        // 然后获取到当前进行访问的URI请求（比如：/sys/xxx.page）。
        String servletPath = request.getServletPath();
        Map requestMap = request.getParameterMap();

        // 拿到当前所访问的请求后，我们就可以在准备好的白名单列表中进行判断了。
        if (exclusionUrlSet.contains(servletPath)) {
            // 如果请求，它在URI白名单列表中，那么我们就不进行拦截处理：直接放行。
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        // 如果当前请求不在URI白名单列表中，意味着我们会自动拦截进行处理。
        //那么，我们会先获取当前URI访问的登录用户的信息（我们会第一时间去本地线程ThreadLocal中的RequestHolder缓存中获取登录用户的信息）。
        SysUser sysUser = RequestHolder.getCurrentUser();
        if (sysUser == null) {
            // 如果没有当前的用户信息的话，那么我们做一个【无权访问】的操作。
            // 用户没有登录的提示信息。
            log.info("someone visit {}, but no login, parameter:{}", servletPath, JsonMapper.objectToString(requestMap));
            // 调用自定义的【无权访问】的方法。
            noAuth(request, response);
            return;
        }
        /*
        因为Filter并部署被Spring管理的，因此我们需要从ApplicationContextHelper中先获取到applicationContext上下文对象来获取这个值。
        我们把它取出来后，就可以去访问Spring Bean对象：判断一个用户是否可以访问某个URL的请求。
         */
        SysCoreService sysCoreService = ApplicationContextHelper.popBean(SysCoreService.class);
        if (!sysCoreService.hasUrlAcl(servletPath)) {
            log.info("{} visit {}, but no login, parameter:{}", JsonMapper.objectToString(sysUser), servletPath, JsonMapper.objectToString(requestMap));
            noAuth(request, response);
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    /**
     * 【无权访问】的方法。
     * @param request
     * @param response
     * @throws IOException
     */
    private void noAuth(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String servletPath = request.getServletPath();
        if (servletPath.endsWith(".json")) {
            JsonResult jsonData = JsonResult.failure("没有访问权限，如需要访问，请联系管理员");
            response.setHeader("Content-Type", "application/json");
            response.getWriter().print(JsonMapper.objectToString(jsonData));
        } else {
            clientRedirect(noAuthUrl, response);
        }
    }

    private void clientRedirect(String url, HttpServletResponse response) throws IOException{
        response.setHeader("Content-Type", "text/html");
        response.getWriter().print("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n"
                + "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" + "<head>\n" + "<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\"/>\n"
                + "<title>跳转中...</title>\n" + "</head>\n" + "<body>\n" + "跳转中，请稍候...\n" + "<script type=\"text/javascript\">//<![CDATA[\n"
                + "window.location.href='" + url + "?ret='+encodeURIComponent(window.location.href);\n" + "//]]></script>\n" + "</body>\n" + "</html>\n");
    }

    @Override
    public void destroy() {

    }
}
