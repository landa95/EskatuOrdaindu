package eus.ilanda.eskatuetaordaindu.Models;

/**
 * Created by landa on 08/05/2018.
 */

public class Owner extends User {
    private String NAN;

    public Owner(){
        super();
    }

    public Owner(String uid){
        super(uid);
    }

    public Owner(String uid, String email, String name, String NAN){
        super(uid, email,name);
        this.NAN = NAN;
    }

    public String getNAN() {
        return NAN;
    }

    public void setNAN(String NAN) {
        this.NAN = NAN;
    }
}
