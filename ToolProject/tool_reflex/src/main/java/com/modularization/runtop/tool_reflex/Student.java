package com.modularization.runtop.tool_reflex;

/**
 * Created by runTop on 2018/6/14.
 */
public class Student {
    private int idNum;
    public String name;
    public int age;
    protected String sex;

    public Student() {
    }

    public Student(String name, int age, String sex) {
        this.name = name;
        this.age = age;
        this.sex = sex;
    }

    private String copyIdNum(String head) {
        return idNum + head;
    }

    public String showMess() {
        return name + age + sex;
    }


}
