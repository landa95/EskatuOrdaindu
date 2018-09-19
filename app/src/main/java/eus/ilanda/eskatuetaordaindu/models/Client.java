package eus.ilanda.eskatuetaordaindu.models;

/**
 * Created by landa on 08/05/2018.
 */

public class Client  extends User{

    private String country;
    private int age;

    public Client(){
        super();
    }

    public Client(String uid, String email, String name, String country, int age){
        super(uid, email, name);
        this.country = country;
        this.age = age;

    }

    public Client(String uid){
        super(uid);
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

}
