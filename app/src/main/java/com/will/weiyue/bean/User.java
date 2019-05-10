package com.will.weiyue.bean;

import com.will.weiyue.ui.personal.PersonalActivity;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by SDC on 2019/5/8.
 */

public class User extends BmobUser {
    /**
     * 昵称
     */
    private String nickname;


    /**
     * 年龄
     */
    private String age;


    /**
     * 性别
     */
    private String sex;
    /*
    * 签名
    * */
    private String sign;

    /**
     * 头像
     */
    private BmobFile avatar;
    private String avatarUrl;
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public BmobFile getAvatar() {
        return avatar;
    }

    public void setAvatar(BmobFile avatar) {
        this.avatar = avatar;
    }

    public void update(PersonalActivity personalActivity, String objectId, UpdateListener updateListener) {
    }
}
