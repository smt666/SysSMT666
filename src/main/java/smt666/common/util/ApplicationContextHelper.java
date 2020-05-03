package smt666.common.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * <font color="yellow">获取<font color="#ff2fbb">applicationContext</font>上下文的工具类。</font><br/>Tool class for obtaining applicationContext context.
 * 在我们写好ApplicationContextHelper类并且添加了【@Component("applicationContextHelper")】注解后，我们需要在[springMVC-servlet.xml]配置文件中配置当前类。
 * @author 卢2714065385
 * @date 2020-05-03 16:52
 */
@Component("applicationContextHelper")
public class ApplicationContextHelper implements ApplicationContextAware {

    /**
     * <font color="yellow">定义一个全局的ApplicationContext对象 。</font><br/>
     * Define a global ApplicationContext object
     */
    private static ApplicationContext applicationContext;

    /**
     * <font color="yellow">重写方法，使得ApplicationContextAware变成一个静态的成员变量。</font><br/><br/>
     * <font color="yellow">因为此方法所在的当前的类添加了【@Component("applicationContextHelper")】注解，
     * 所以系统启动时就会把我们传入的这个ApplicationContext对象context注入到注入到当前ApplicationContextHelper类里面。
     * 所以，我们先定义一个全局的静态变量ApplicationContext applicationContext，并且通过此方法进行初始化。</font><br/>
     * [@Component ("applicationContextHelper")] annotation is added to the current class where this method is located, so the ApplicationContext object context we pass in will be injected into the current ApplicationContextHelper class when the system starts. * Therefore, we first define a global static variable ApplicationContext applicationContext, and initialized by this method.
     * @param context <font color="yellow">我们传入的ApplicationContext对象context</font>
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        applicationContext = context;
    }

    /**
     * <font color="yellow">从applicationContext上下文里面获取Spring中applicationContext上下文的bean的方法。</font><br/>
     * @param clazz <font color="yellow">传入的Class类型。表示只传入一个【类型】参数</font><br/>The incoming Class type. Indicates that only one [type] parameter is passed in
     * @param <T> <font color="yellow">返回值类型</font><br/>Return value type
     * @return <font color="yellow">输入什么类型就返回什么类型的applicationContext上下文bean对象。</font><br/>What type of input will return what type of applicationContext context bean object.
     */
    public static <T> T popBean(Class<T> clazz) {
        if (applicationContext==null) {
            return null;
        }
        return applicationContext.getBean(clazz);
    }

    /**
     * <font color="yellow">从applicationContext上下文里面获取Spring中applicationContext上下文的bean的方法。</font><br/>
     * @param name <font color="yellow">传了一个name字符串的参数</font><br/>
     * @param clazz <font color="yellow">传入的Class类型。</font><br/>The incoming Class type. Indicates that only one [type] parameter is passed in
     * @param <T> <font color="yellow">返回值类型</font><br/>Return value type
     * @return <font color="yellow">输入什么类型就返回什么类型的applicationContext上下文bean对象。</font><br/>What type of input will return what type of applicationContext context bean object.
     */
    public static <T> T popBean(String name, Class<T> clazz) {
        if (applicationContext==null) {
            return null;
        }
        return applicationContext.getBean(name, clazz);
    }
}
