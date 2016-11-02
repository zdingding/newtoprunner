package com.toprunner.ubii.toprunner.bean;

public class User {
/*
	 * id 用户ID userName 用户名 passWord 密码 avatar 头像 weight 体重 height 身高 age 年龄 sex
	 * 性别 save1 save2
	 */
    /*
	 * id 用户ID userName 用户名 passWord 密码 avatar 头像 weight 体重 height 身高 age 年龄 sex
	 * 性别 save1 save2
	 */

//@ID(autoincrement = true)
private int id;

    private String userName;

    private String passWord;

    private String avatar;

    private double weight;

    private double height;

    private int age;

    private String sex;

    private String save1;

    private String save2;
    private int status; // 是否登陆 LOGIN 表示已经登陆了 OFF 表示没有登陆

    //用于用户注册的确认密码
    private String passWord2;

    public String getPassWord2() {
        return passWord2;
    }

    public void setPassWord2(String passWord2) {
        this.passWord2 = passWord2;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
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

    public String getSave1() {
        return save1;
    }

    public void setSave1(String save1) {
        this.save1 = save1;
    }

    public String getSave2() {
        return save2;
    }

    public void setSave2(String save2) {
        this.save2 = save2;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
