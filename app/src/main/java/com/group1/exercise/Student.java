package com.group1.exercise;

public class Student {
    public String id;
    public String firstName;
    public String lastName;
    public int age;
    public String email;

    public Student() {} // Firebase 需要空构造

    public Student(String id, String firstName, String lastName, int age, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.email = email;
    }
}

