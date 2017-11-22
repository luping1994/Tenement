package net.suntrans.tenement.persistence;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Looney on 2017/11/16.
 * Des:
 */

@Entity(tableName = "user")
public class User {

    @PrimaryKey
    public int id;
    public int company_id;
    public int area_id;
    public String username;
    public String nickname;
    public String truename;
    public String cover;
    public String mobile;
    public int login;
    public String company_name;
    public int role_id;
    public String role_name;
    public String role_slug;


}