package eus.ilanda.eskatuetaordaindu.models;

/**
 * Created by landa on 05/06/2018.
 */

public class Permission {

    private boolean owner;
    private boolean admin;

    public Permission(){
        owner = false;
        admin = false;
    }

    public boolean isOwner() {
        return owner;
    }

    public void setOwner(boolean owner) {
        this.owner = owner;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}
