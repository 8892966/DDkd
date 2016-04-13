package com.example.user.ddkd.text;

/**
 * Created by Administrator on 2016/4/13.
 */
public class UserInfo {
    private long userphone;
    private String schoolname;
    private String username;
    private String usersex;
    private int detailssum;
    private String DJ;
    private long userno;

    public String getUsersex() {
        return usersex;
    }

    public int getDetailssum() {
        return detailssum;
    }

    public long getUserno() {
        return userno;
    }

    public long getUserphone() {
        return userphone;
    }

    public String getDJ() {
        return DJ;
    }

    public String getSchoolname() {
        return schoolname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsersex(String usersex) {
        this.usersex = usersex;
    }

    public void setDetailssum(int detailssum) {
        this.detailssum = detailssum;
    }

    public void setDJ(String DJ) {
        this.DJ = DJ;
    }

    public void setSchoolname(String schoolname) {
        this.schoolname = schoolname;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUserno(long userno) {
        this.userno = userno;
    }

    public void setUserphone(long usnerphone) {
        this.userphone = userphone;
    }

}
