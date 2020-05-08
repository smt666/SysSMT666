package smt666.common.util;


import smt666.mall.model.SysUser;

import javax.servlet.http.HttpServletRequest;

/**
 * <font color="#f0eb16" size=5>ThreadLocal</font><br/><br/>
 * Synchronized用于线程间的数据共享，而ThreadLocal则用于线程间的数据隔离。<br/>
 * 所以ThreadLocal的应用场合，最适合的是按线程多实例（每个线程对应一个实例）的对象的访问，并且这个对象很多地方都要用到。<br/>
 * ThreadLocal，连接ThreadLocalMap和Thread。来处理Thread的TheadLocalMap属性，<br/>
 * 包括init初始化属性赋值、get对应的变量，set设置变量等。通过当前线程，获取线程上的ThreadLocalMap属性，对数据进行get、set等操作。<br/>
 * ThreadLocalMap，用来存储数据，采用类似hashmap机制，存储了以threadLocal为key，需要隔离的数据为value的Entry键值对数组结构。<br/>
 * ThreadLocalMap是ThreadLocal内部类，由ThreadLocal创建，Thread有ThreadLocal.ThreadLocalMap类型的属性。<br/>
 * @author 27140
 */
public class RequestHolder {

    private static final ThreadLocal<SysUser> userHolder = new ThreadLocal<SysUser>();

    private static final ThreadLocal<HttpServletRequest> requestHolder = new ThreadLocal<HttpServletRequest>();

    public static void add(SysUser sysUser) {
        userHolder.set(sysUser);
    }

    public static void add(HttpServletRequest request) {
        requestHolder.set(request);
    }

    public static SysUser getCurrentUser() {
        return userHolder.get();
    }

    public static HttpServletRequest getCurrentRequest() {
        return requestHolder.get();
    }

    public static void remove() {
        userHolder.remove();
        requestHolder.remove();
    }
}
