package net.suntrans.tenement.persistence;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Looney on 2017/11/16.
 * Des:
 */

@Entity(tableName = "users")
public  class User {
    /**
     * id : 59
     * company_id : 3
     * area_id : 5
     * username : st0001
     * nickname : 主人
     * mobile : 132111
     * login : 14
     * manager : 1
     * cover :
     * created_at : 2017-11-09 11:29:45
     * updated_at : 2017-11-15 11:45:49
     * company_name : 三川研发中心
     */
    @PrimaryKey
    @ColumnInfo(name = "id")
    public int id;

    public int company_id;
    public int area_id;
    public String username;
    public String nickname;
    public String mobile;
    public int login;
    public String manager;
    public String cover;
    public String created_at;
    public String updated_at;
    public String company_name;
    public String role_id;
}