package smt666.mall.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import smt666.common.vo.JsonResult;
import smt666.mall.dto.DeptLevelDto;
import smt666.mall.service.SysDeptService;
import smt666.mall.service.SysTreeService;
import smt666.param.DeptParam;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 卢2714065385
 * @date 2020-05-04 3:50
 */
@Controller
@RequestMapping("/sys/dept")
@Slf4j
public class SysDeptController {

    @Resource
    private SysDeptService sysDeptService;
    @Resource
    private SysTreeService sysTreeSevice;

    /**
     * 进入部门管理页面。
     * @return
     */
    @RequestMapping("/dept.page")
    public ModelAndView page() {
        return new ModelAndView("dept");
    }

    /**&nbsp;&nbsp;&nbsp;&nbsp;
     * <font color="#f0eb16" size=5>/sys/dept/save.json</font><br/><br/>
     * 处理<font color="#f0eb16" size=5>新增部门信息</font>&nbsp;请求的Controller控制层save()方法。
     * @param param 经过重新组织过的dept部门参数类的封装的对象（它包含了一些部门的参数信息）。
     * @return 返回JSON格式的数据给前端页面。
     */
    @RequestMapping("/save.json")
    @ResponseBody
    public JsonResult saveDept(DeptParam param) {
        sysDeptService.save(param);
        return JsonResult.success();
        // 浏览器 访问：http://localhost:8095/sys/dept/save.json?name=张三&seq=2&remark=备注信息技术部
        // {"ret":true,"msg":null,"data":null}
        // 查看了数据库：传入数据成功。测试通过。
        // 然后我们再次刷新一次浏览器（进行再次添加），浏览器信息变成：
        // {"ret":false,"msg":"当前同一层级下存在相同的名称的部门啦！（请换个新的部门名称输入吧）","data":null}
        // http://localhost:8095/sys/dept/save.json?name=后端开放01&seq=2&parentId=2    ====>   {"ret":true,"msg":null,"data":null} 表示插入成功，
    }

    /**&nbsp;&nbsp;&nbsp;&nbsp;
     * <font color="#f0eb16" size=5>/sys/dept/tree.json</font><br/><br/>
     * 处理<font color="#f0eb16" size=5>生成部门层级树列表信息</font>&nbsp;请求的Controller控制层save()方法。
     * @return
     */
    @RequestMapping("/tree.json")
    @ResponseBody
    public JsonResult tree() {
        List<DeptLevelDto> dtoList = sysTreeSevice.deptTree();
        return JsonResult.success(dtoList);
        // {"ret":true,"msg":null,"data":[{"id":1,"name":"技术部","parentId":0,"level":"0","seq":1,"remark":"技术部","operator":"system","operateTime":1507677700000,"deptList":[{"id":2,"name":"后端开发","parentId":1,"level":"0.1","seq":1,"remark":"后端","operator":"system-update","operateTime":1507766176000,"deptList":[{"id":14,"name":"后端开放01","parentId":2,"level":"0.1.2","seq":2,"remark":"","operator":"adminSystem","operateTime":1588578743000,"deptList":[]}]},{"id":3,"name":"前端开发","parentId":1,"level":"0.1","seq":2,"remark":"","operator":"system-update","operateTime":1507951785000,"deptList":[]},{"id":4,"name":"UI设计","parentId":1,"level":"0.1","seq":3,"remark":"","operator":"system","operateTime":1507766143000,"deptList":[]}]},{"id":11,"name":"产品部","parentId":0,"level":"0","seq":2,"remark":"","operator":"Admin","operateTime":1508165549000,"deptList":[]},{"id":13,"name":"张三","parentId":0,"level":"0","seq":2,"remark":"备注信息技术部","operator":"adminSystem","operateTime":1588578385000,"deptList":[]},{"id":12,"name":"客服部","parentId":0,"level":"0","seq":4,"remark":"","operator":"Admin","operateTime":1508170975000,"deptList":[]}]}
    }

    /**&nbsp;&nbsp;&nbsp;&nbsp;
     * <font color="#f0eb16" size=5>/sys/dept/tree.json</font><br/><br/>
     * 处理<font color="#f0eb16" size=5>更新部门及子部门信息</font>&nbsp;请求的Controller控制层save()方法。
     * @param param
     * @return
     */
    @RequestMapping("/update.json")
    @ResponseBody
    public JsonResult updateDept(DeptParam param) {
        sysDeptService.update(param);
        return JsonResult.success();
        // http://localhost:8095/sys/dept/save.json?id=14&name=后端开放01&seq=2&parentId=2&remark=后端   ====>  {"ret":true,"msg":null,"data":null}
    }

    @RequestMapping("/delete.json")
    @ResponseBody
    public JsonResult delete(@RequestParam("id") int id) {
        sysDeptService.delete(id);
        return JsonResult.success();
    }
}
