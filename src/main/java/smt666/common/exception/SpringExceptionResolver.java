package smt666.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import smt666.common.vo.JsonResult;

/**
 * <font color="yellow" size=5>全局的Spring管理的Http的异常处理类</font><br/>
 * 我们自定义的这个SpringExceptionResolver类实现了HandlerExceptionResolver接口：<br/>
 * 表示如果这个类它被spring管理的话，那么我们的全局化的异常，<br/>
 * 在Http返回的时候，就会被此处SpringExceptionResolver捕获处理。<br/>
 * 因此，我们在当前实现异常的处理逻辑即可。<br/>
 * @author 卢2714065385
 */
@Slf4j
public class SpringExceptionResolver implements HandlerExceptionResolver {

    /**
     * 我们提供了2种方案：<br/>
     * 【方案一】，我们从request中取出来的提交了请求的header请求头信息，来进行判断。<br/>
     * 【方案二】我们直接通过接口。比如我们通过指定了系统里：所有页面相关的请求一律采用后缀为.page结尾；<br/>
     * 所有数据相关的请求都使用.json为后缀结尾，这样一来，url就分别归纳为【xxx.json】和【xxx.page】两大类的请求模式了。<br/>
     * We provide 2 options:<br/>
     * [Scenario One], we took the request header information from the request and submitted it for judgment.<br/>
     * All data-related requests end with .json as a suffix. In this way, the url is summarized into two main types of request modes: [xxx.json] and [xxx.page].<br/>
     * @param request 我们可以依据request获取到url的请求参数。
     * @param response
     * @param handler
     * @param ex
     * @return
     */
    @Override
    public ModelAndView resolveException(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response, Object handler, Exception ex) {
        String url = request.getRequestURL().toString();
        // 请求将会返回一个结果。
        ModelAndView modelAndView ;
        // 如果我们遇到了不期望它出现的异常时，我们可以先设置一个未知异常的默认（提示）值。
        String defaultMsg = "System Error 未知错误，稍后重试！Unknown error, try again later!";
        // 接下来，我们判断请求的类型，是页面相关的请求，还是数据相关的请求。
        /**
         * 我们提供了2种方案：<br/>
         * 【方案一】，我们从request中取出来的提交了请求的header请求头信息，来进行判断。<br/>
         * 【方案二】我们直接通过接口。比如我们通过指定了系统里：所有页面相关的请求一律采用后缀为.page结尾；<br/>
         * 所有数据相关的请求都使用.json为后缀结尾，这样一来，url就分别归纳为【xxx.json】和【xxx.page】两大类的请求模式了。<br/>
         * We provide 2 options:<br/>
         * [Scenario One], we took the request header information from the request and submitted it for judgment.<br/>
         * All data-related requests end with .json as a suffix. In this way, the url is summarized into two main types of request modes: [xxx.json] and [xxx.page].<br/>
         */
        // 这里我们要求项目中所有请求JSON数据的url的结尾都以.json为后缀。
        if (url.endsWith(".json")) {
            log.info("正在处理.json后缀的url请求[Processing url request with .json suffix]");
            // 如果当前出现的异常，它是我们自己定义的异常的话，那么msg就使用当前此处我们定义的msg进行返回。
            if ( ex instanceof PermissionException || ex instanceof ParamException ) {
                log.error("【运行异常啦】正在对.json的url请求异常进行解析，当前出现的异常，它正是我们自己定义的PermissionException【权限校验失败】或者ParamException【参数校验失败】异常。。。");
                JsonResult result = JsonResult.failure(ex.getMessage());
                // 把返回值配置成使用JSON感受进行返回，jsonView是固定值，它与[springMVC-servlet.xml]配置文件中的【<bean id="jsonView"...】的id的值必须一致！
                modelAndView = new ModelAndView("jsonView", result.toMap());
            } else {
                log.error("【运行异常啦】正在对.json的url请求异常进行解析，[未知异常]Unknown exception,[，url的地址是：]the URL address is:"+ url, ex);
                JsonResult result = JsonResult.failure(defaultMsg);
                modelAndView = new ModelAndView("jsonView", result.toMap());
            }
        } else if (url.endsWith(".page")) {
            log.error("正在处理.page后缀的url请求[Processing url request with .page suffix]，【异常】url的地址是："+ url, ex);
            /*这里我们要求项目中所有请求页面的url的结尾都以.page为后缀。*/
            JsonResult result = JsonResult.failure(defaultMsg);
            // 此处如果这个全球是个异常全球的话，我们在使用"exception"值后，就必须在指定文件夹/views/目录下有一个对应的exception.jsp页面才可以。
            modelAndView = new ModelAndView("exception", result.toMap());
        } else {
            log.error("[else]处理??.??后缀的url请求异常，[未知异常]Unknown exception,[，url的地址是：]the URL address is:"+ url, ex);
            JsonResult result = JsonResult.failure(defaultMsg);
            modelAndView = new ModelAndView("jsonView", result.toMap());
        }
        // 将处理后的数据进行返回。
        return modelAndView;
    }
}
