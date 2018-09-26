package eus.ilanda.eskatuetaordaindu.models;

import java.io.Serializable;

public class Category implements Serializable {

    private int id;

    private String categoryName;

    public Category(){

    }

    public Category(int id, String category){
        this.id = id;
        this.categoryName = category;

    }

    public String getId() {
        return Integer.toString(this.id);
    }

    public void setId(String id) {
        this.id = Integer.parseInt(id);
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;

    }


    @Override
    public String toString() {
        return categoryName;
    }
}
