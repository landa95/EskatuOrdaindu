package eus.ilanda.eskatuetaordaindu.models;

import java.util.ArrayList;

/**
 * Created by landa on 08/05/2018.
 */

public class User {
    private String uid;
    private String email;
    private String name;
    private Permission permission = new Permission();
    private ArrayList<String> favourites = new ArrayList<String>();

    public User()
    {
        this.favourites = new ArrayList<String>();
    }
    public User(String uid){
        this.uid = uid;
    }
    public User(String uid, String email, String name)
    {
        this.uid = uid;
        this.email = email;
        this.name = name;
        this.favourites = new ArrayList<String>();
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

    public String getNameOnly() {
        String[] nameOnly = name.split(" ");
        return " " + nameOnly[0];
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

    public ArrayList<String> getFavourites() {
        return favourites;
    }

    public void setFavourites(ArrayList<String> favourites) {
        this.favourites = favourites;
    }
}
