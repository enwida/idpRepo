package de.enwida.dao;
public class User {

    private String name = null;
    private  String standard = null;
    private int age;
    private String sex = null;
    // Setters and getters are omitted for making the code short.
    @Override
    public String toString() {
        return "User [name=" + name + ", standard=" + standard + ", age=" + age
        + ", sex=" + sex + "]";
    }
}