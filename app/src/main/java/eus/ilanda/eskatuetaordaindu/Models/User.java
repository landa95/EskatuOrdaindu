package eus.ilanda.eskatuetaordaindu.Models;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by landa on 08/05/2018.
 */

public class User {
    private String uid;
    private String email;
    private String name;

    private Permission permission = new Permission();

    public User()
    {

    }

    public User(String uid){
        this.uid = uid;
    }

    public User(String uid, String email, String name)
    {
        this.uid = uid;
        this.email = email;
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }
}
