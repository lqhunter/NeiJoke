package com.lq.baselibrary.ioc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * author : lqhunter
 * date : 2019/4/23
 * description : view事件 注解的 annotation
 */


// annotation 的位置   FIELD 属性; TYPE 类; CONSTRUCTOR 构造函数
@Target(ElementType.METHOD)

// 什么时候生效 RUNTIME 运行时; CLASS 编译时; SOURCE 源码
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckNet {
}
