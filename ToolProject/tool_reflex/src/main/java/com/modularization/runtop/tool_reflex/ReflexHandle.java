package com.modularization.runtop.tool_reflex;

import android.util.Log;

/**
 * 反射操作
 * 参考：https://blog.csdn.net/ljphhj/article/details/12858767
 * https://www.sczyh30.com/posts/Java/java-reflection-1/
 * Created by runTop on 2018/6/14.
 */
public class ReflexHandle {

    /**
     * 获取Class对象
     */
    public void getClassObj() throws ClassNotFoundException {
        //1、通过堆内存中的实例对象获取
        Student student = new Student();
        Class class1 = student.getClass();
        //2、任何一个类都有一个隐含的静态成员变量class
        Class class2 = Student.class;
        //3、通过一个类的全限定名获取，这种方式会出发类加载。
        Class class3 = Class.forName("com.modularization.runtop.tool_reflex.Student");
    }

    /**
     * 获取类的包名和类名
     */
    public void demo1() throws ClassNotFoundException {
        Class classStu = Student.class;
        Log.i("获取", "包名：" + classStu.getPackage().getName() + "，" + "类名：" + classStu.getName());
    }

    /**
     * 验证堆内的实例对象是否是相应的Class类型，可以代替instanceof关键字的判断。
     */
    public void demo2() throws ClassNotFoundException {
        Class classStu = Student.class;
        Student student = new Student();
        Log.i("验证", "结果：" + classStu.isInstance(student));
    }


}
