package de.enwida.dao;
public class User {

    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStandard() {
		return standard;
	}
	public void setStandard(String standard) {
		this.standard = standard;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
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