package smt666.common.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**&nbsp;&nbsp;&nbsp;&nbsp;
 * 在项目中，如果获取到的数据是JSON格式返回的数据时，
 * 这个返回值一定是一个JsonResult类型的返回值。<br/>
 * [@Getter]利用IDEA-Lombok使用这个注解来自动创建成员属性的Get方法。<br/>
 * [@Setter]利用IDEA-Lombok使用这个注解来自动创建成员属性的Set方法。<br/>
 * @author 卢2714065385
 */
@Getter
@Setter
public class JsonResult {
    /**
     * &nbsp;&nbsp;&nbsp;&nbsp;当我们正常返回了JsonResult类型的返回值时，我们就会先判断这个ret是true还是false 。<br/>
     * 如果是true代表，当前请求它是正常处理的。
     */
    private boolean ret;
    /** &nbsp;&nbsp;&nbsp;&nbsp;
     * 如果需要从接口中获取数据，那么再ret为false（即没有正确处理了请求）的情况下，
     * 我们就会通过这个msg属性来告诉后台，因为说明原因没有正常处理请求的信息。<br/>&nbsp;&nbsp;&nbsp;&nbsp;
     * 当然我们也可以在ret为true（正常处理了请求）时，给出成功处理的提示信息。*/
    private String msg;
    /** 如果需要从接口中获取数据，那么再ret为true（即正常处理了请求拿到JsonResult类型的返回值）的前提下，
     * 再获取这个JsonResult对象里面的data数据。 */
    private Object data;

    /**
     * 判断：处理请求的方法，是否执行成功的提示方法。
     * 【构造器】如果只传入一个结果（没有msg信息）的话，那么直接将传入的结果赋值给ret成员属性。
     * @param ret 传入的结果
     */
    public JsonResult(boolean ret){
        this.ret = ret;
    };

    /**
     * 处理请求的方法执行成功了的提示方法。
     * 执行结果是成功了的时候，我们可以调用当前这个方法，表示执行成功了。<br/>
     * @return 返回执行成功的信息，没有任何数据，也不会给前端传递任何信息。
     */
    public static JsonResult success(){
        return new JsonResult(true);
    }

    /**
     * 处理请求的方法执行成功了的提示方法。
     * 执行结果是成功了的时候，我们可以调用当前这个方法，表示执行成功了。<br/>
     * 我们在不需要把执行成功的信息通知给前端页面的话，直接将数据返回即可的情况下，调用次方法。
     * @param object
     * @return 只返回JSON数据给前端。处理请求方法执行成功的提示，不会传给前端。
     */
    public static JsonResult success(Object object){
        JsonResult jsonResult = new JsonResult(true);
        jsonResult.data = object;
        return jsonResult;
    }

    /**
     * 处理请求的方法执行成功了的提示方法。
     * 执行结果是成功了的时候，我们可以调用当前这个方法，表示执行成功了。<br/>
     * 当我们需要给前端页面一个执行是否成功的消息提示，并且将数据传给前端的情况CIA，调用此方法。
     * @param object
     * @param msg
     * @return 返回JSON数据给前端，并且把处理请求方法执行成功的提示也传给前端。
     */
    public static JsonResult success(Object object, String msg){
        JsonResult jsonResult = new JsonResult(true);
        jsonResult.data = object;
        jsonResult.msg = msg;
        return jsonResult;
    }

    /**
     * 处理请求的方法执行失败了的提示方法。
     * @param msg
     * @return
     */
    public static JsonResult failure(String msg){
        JsonResult jsonResult = new JsonResult(false);
        jsonResult.msg = msg;
        return jsonResult;
    }

    /**
     * 我们添加这个自定义的toMap()方法，是因为，我们将来在使用modelAndView构建对象时，
     * 【new ModelAndView(String viewName, Map<String, ?> model)】的model参数是一个Map类型的参数。
     * 所以准备了这个toMap()方法。<br/><br/>
     * We added this custom toMap () method because when we use modelAndView to construct objects in the future, the model parameter of [new ModelAndView (String viewName, Map <String,?> Model)] is a Map type parameter. So I prepared this toMap () method.<br/>
     * @return 返回一个封装了ret、msg、data属性的HashMap对象（resultMap）。<br/><br/>Return a HashMap object (resultMap) that encapsulates ret, msg, and data attributes.
     */
    public Map<String, Object> toMap(){
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("ret", ret);
        resultMap.put("msg", msg);
        resultMap.put("data", data);
        return resultMap;
    }
}
