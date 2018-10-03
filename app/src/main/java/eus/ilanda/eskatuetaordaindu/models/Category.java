package eus.ilanda.eskatuetaordaindu.models;

import java.io.Serializable;

public class Category implements Serializable {

    private String id;

    private String categoryName;

    public Category(){

    }

    public Category(String id, String category){
        this.id = id;
        this.categoryName = category;

    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
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
