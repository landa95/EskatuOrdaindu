package eus.ilanda.eskatuetaordaindu.models;

/**
 * Created by landa on 08/05/2018.
 */

public class Admin extends User  {
    public Admin(){
        super();
    }

    public Admin(String uid, String email, String name){
        super(uid, email, name);
    }

    public Admin(String uid){
        super(uid);
    }

}
