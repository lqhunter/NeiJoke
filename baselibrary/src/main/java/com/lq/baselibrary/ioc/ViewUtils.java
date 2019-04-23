package com.lq.baselibrary.ioc;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * author : lqhunter
 * date : 2019/4/23 0023
 * description :
 */
public class ViewUtils {

    // activity 中使用
    public static void inject(Activity activity) {
        inject(new ViewFinder(activity), activity);

    }

    // 自定义view中可以用
    public static void inject(View view) {
        inject(new ViewFinder(view), view);

    }

    // fragment 中使用
    public static void inject(View view, Object object) {
        inject(new ViewFinder(view), object);
    }


    /**
     * 兼容上面三个
     *
     * @param finder 此类辅助 findViewById
     * @param object 反射需要执行的类
     */
    public static void inject(ViewFinder finder, Object object) {
        //注入属性
        injectField(finder, object);
        //注入事件
        injectEvent(finder, object);
    }

    private static void injectEvent(ViewFinder finder, final Object object) {
        Class<?> clazz = object.getClass();
        // 1.获取 object 类所有方法
        Method[] onClicks = clazz.getDeclaredMethods();
        for (final Method onClick : onClicks) {
            //2. 获取方法的注解
            OnClick annotation = onClick.getAnnotation(OnClick.class);
            if (annotation != null) {
                //3. 获取注解的 value 值
                int[] viewIds = annotation.value();
                for (int viewId : viewIds) {
                    //4. 获取view
                    final View view = finder.findViewById(viewId);
                    if (view != null) {
                        //拓展：检测网络
                        boolean isCheckNet = onClick.getDeclaredAnnotation(CheckNet.class) != null;

                        //5. 设置监听事件
                        view.setOnClickListener(new DeclaredOnClickListener(onClick, object, isCheckNet));
                    }
                }
            }

        }
    }

    private static void injectField(ViewFinder finder, Object object) {
        Class<?> clazz = object.getClass();
        //1. 获取 object 类中的属性
        Field[] fields = clazz.getDeclaredFields();
        for (Field field :
                fields) {

            //2. 获取属性的注解
            ViewById annotation = field.getAnnotation(ViewById.class);
            if (annotation != null) {
                //3. 获取注解的value值
                int viewId = annotation.value();
                //4. findViewById 找到 view
                View view = finder.findViewById(viewId);
                if (view != null) {
                    try {
                        //5. 动态注入找到的 view
                        field.setAccessible(true);//能注入所有的修饰符
                        field.set(object, view);//将 view 赋值给 object 实例对象的 field
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }

    private static class DeclaredOnClickListener implements View.OnClickListener {
        private Method mMethod;
        private Object mObject;
        private boolean mIsCheckNet;


        public DeclaredOnClickListener(Method onClick, Object object, boolean isCheckNet) {
            this.mMethod = onClick;
            this.mObject = object;
            this.mIsCheckNet = isCheckNet;
        }

        //设置监听事件后，点击会回调此方法，此方法中再用反射调用mObject 中 mMethod 方法
        @Override
        public void onClick(View v) {
            //需不需要监测网络
            if (mIsCheckNet) {
                //监测是否有网
                if (!isNetworkConnected(v.getContext())) {//没网就打 Toast ,并且不执行方法
                    Toast.makeText(v.getContext(), "亲，您的网络不太给力！", Toast.LENGTH_SHORT).show();
                    return;
                }

            }

            try {
                mMethod.setAccessible(true);
                /**
                 *调用有 View v 参数的方法
                 *
                 *@OnClick(R.id.text_iv)
                 *public void onClick(View v) {
                 *
                 *}
                 */
                mMethod.invoke(mObject, v);
            } catch (Exception e) {
                e.printStackTrace();

                try {
                    /**
                     * 如果没有带参数的 方法，则找无参数的方法
                     * @OnClick(R.id.text_iv)
                     * public void onClick() {
                     *
                     * }
                     */
                    mMethod.invoke(mObject);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
    }


    public static boolean  isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
}
