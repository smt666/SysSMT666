package smt666.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author 卢2714065385
 * @date 2020-05-03 23:17
 */
@Slf4j
public class HttpRequestInterceptor extends HandlerInterceptorAdapter {

    private static final String START_TIME = "requestStartTime";

    /**
     * <font color="yellow" size=5>在请求出来之前，打印出URI请求和请求的参数。</font><br/><br/>
     * <b>方法的使用场景：</b><br/>&nbsp;&nbsp;&nbsp;&nbsp;
     * <font color="#32ff5d" size=4>在请求被处理之前时，我们可以进行的操作。</font>
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 我们获取到当前的请求URI .
        String uri = request.getRequestURI().toString();
        // 如果获取到当前请求中的参数，返回值是一个Map类型。
        Map parameterMap = request.getParameterMap();
        // 我们在请求处理之前记录一下当前的系统时间。
        long startTime = System.currentTimeMillis();
        // 并且把这个时间绑定在request请求中一起传给后台。
        request.setAttribute(START_TIME, startTime);
        // 我们把获取的uri和参数，都打印出来。
        log.info("[Before request handle...]\n\t\t请求的URI为url:\t\t{}\n\t\t请求参数为params:\t{}\n", uri, JsonMapper.objectToString(parameterMap));
        return true;
    }

    /**
     * <font color="#32ff5d" size=4>当请求<font color="#f0eb16" size=6>&nbsp;正常&nbsp;</font>处理了之后，我们可以进行的操作。</font>
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 我们获取到当前的请求URI .
        String uri = request.getRequestURI().toString();
        // 如果获取到当前请求中的参数，返回值是一个Map类型。
        Map parameterMap = request.getParameterMap();
        // 我们在请求处理之后，从请求中取出在请求处理之前所记录的时间。
        long startTime = (long) request.getAttribute(START_TIME);
        // 然后记录当前时间，即：请求已经处理完毕的时间。
        long endTime = System.currentTimeMillis();
        // 我们把获取的uri和参数，都打印出来。
        log.info("OK[request handled Finished]\n\t\t请求的URI为url:\t\t{}\n\t\t请求参数为params:\t{}\n\t\t处理请求总耗时cost:\t{}毫秒\n", uri, JsonMapper.objectToString(parameterMap), endTime-startTime);
    }

    /**
     * <font color="#32ff5d" size=4>当请求被处理了之后（无论是<font color="#f0eb16" size=6>&nbsp;正常&nbsp;</font>或者<font color="#f0eb16" size=6>&nbsp;异常&nbsp;</font>的请求），我们可以进行的操作。</font>
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 我们获取到当前的请求URI .
        String uri = request.getRequestURI().toString();
        // 如果获取到当前请求中的参数，返回值是一个Map类型。
        Map parameterMap = request.getParameterMap();
        // 我们在请求处理之后，从请求中取出在请求处理之前所记录的时间。
        long startTime = (long) request.getAttribute(START_TIME);
        // 然后记录当前时间，即：请求已经处理完毕的时间。
        long endTime = System.currentTimeMillis();
        // 我们把获取的uri和参数，都打印出来。
        log.info("Complete[request handled Finished]\n\t\t请求的URI为url:\t\t{}\n\t\t请求参数为params:\t{}\n\t\t处理请求总耗时cost:\t{}毫秒\n", uri, JsonMapper.objectToString(parameterMap), endTime-startTime);
    }
}
