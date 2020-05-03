package smt666.mall.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import smt666.common.exception.ParamException;
import smt666.common.exception.PermissionException;
import smt666.common.util.ApplicationContextHelper;
import smt666.common.util.BeanValidator;
import smt666.common.util.JsonMapper;
import smt666.common.vo.JsonResult;
import smt666.mall.dao.SysAclModuleMapper;
import smt666.mall.model.SysAclModule;
import smt666.param.TestVo;

import java.util.Map;

/**
 * @author 卢2714065385
 * @date 2020-05-03 18:27
 */
@RestController
@RequestMapping("/test")
@Slf4j
public class TestController {

    @RequestMapping("/hello")
    public JsonResult testConnection() {
        log.info("Hello ,正在处理【/test/hello】url请求。");
        return JsonResult.success("TestController类测试成功啦。");
    }

    @RequestMapping("/hello1.json")
    public JsonResult testConnections() {
        log.info("Hello ,正在处理【/test/hello.json】url请求。");
        return JsonResult.success("TestController类测试成功啦。");
    }
    @RequestMapping("/hello2.json")
    public JsonResult testConnectionsww() {
        log.info("Hello ,正在处理【/test/hello.json】url请求。");
//        throw new PermissionException("我们故意抛出异常来测试");  // 浏览器 {"ret":false,"msg":"我们故意抛出异常来测试","data":null}
//        throw new RuntimeException("我们故意抛出异常来测试");     // 浏览器 {"ret":false,"msg":"System Error 未知错误，稍后重试！Unknown error, try again later!","data":null}
        return JsonResult.success("TestController类测试成功啦。");
    }

    //============================================================================
    /***/
    @RequestMapping("/validate001.json")
    public JsonResult validate (TestVo vo) {
        log.info("Hello ,正在处理【/test/validate001.json】url请求。");
        try {
            Map<String, String> map = BeanValidator.validateObject(vo);
//            if ( map!=null && map.entrySet().size()>0 ) {
            if (MapUtils.isNotEmpty(map)) {
                // 说明map是有值的。
                //把值输出一下。
                for ( Map.Entry<String, String> entry: map.entrySet() ) {
                    log.info("{}->{}", entry.getKey(), entry.getValue());
                }
            }
        } catch (Exception e){
        }
        return JsonResult.success("TestController类测试成功啦。");
        // 浏览器 {"ret":true,"msg":null,"data":"TestController类测试成功啦。"}
        // 21:46:33.263 [http-nio-8095-exec-9] INFO smt666.mall.controller.TestController - id->id不能为空值[You have not entered the id value yet!]。
        // 21:46:33.263 [http-nio-8095-exec-9] INFO smt666.mall.controller.TestController - msg->不能为空
        // 21:46:33.263 [http-nio-8095-exec-9] INFO smt666.mall.controller.TestController - str->不能为空
    }
    @RequestMapping("/validate002.json")
    public JsonResult t1 (TestVo vo) throws ParamException {
        log.info("Hello ,正在处理【/test/validate.json】url请求。");
        BeanValidator.checkParams(vo);
        return JsonResult.success("TestController类测试成功啦。");
    }
    //============================================================================

    @RequestMapping("/getApplicationContext.json")
    public JsonResult testApplicationContextHelper (TestVo vo) throws  ParamException{
        log.info("Hello ,正在处理【/test/getApplicationContext.json】url请求。");
        SysAclModuleMapper moduleMapper = ApplicationContextHelper.popBean(SysAclModuleMapper.class);
        SysAclModule module = moduleMapper.selectByPrimaryKey(1);
        log.info(JsonMapper.objectToString(module));
        BeanValidator.checkParams(vo);
        return JsonResult.success("TestController类测试成功啦。");
    }

    @RequestMapping("/t3.json")
    public JsonResult t3 (TestVo vo) {
        log.info("Hello ,正在处理【/test/validate.json】url请求。");
        return JsonResult.success("TestController类测试成功啦。");
    }

    @RequestMapping("/t4.json")
    public JsonResult t4 (TestVo vo) {
        log.info("Hello ,正在处理【/test/validate.json】url请求。");
        return JsonResult.success("TestController类测试成功啦。");
    }
}
