package eus.ilanda.eskatuetaordaindu.models;

public class ItemMenu {

    private String id;
    private String itemName;
    private String itemDetails;
    private double prize;
    private String category;

    public ItemMenu(){

    }

    public ItemMenu(String id, String itemName, String itemDetails, double prize, String category){
        this.id = id;
        this.itemName = itemName;
        this.itemDetails = itemDetails;
        this.prize = prize;
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDetails() {
        return itemDetails;
    }

    public void setItemDetails(String itemDetails) {
        this.itemDetails = itemDetails;
    }

    public double getPrize() {
        return prize;
    }

    public void setPrize(double prize){
        if (prize <= 0){
            this.prize = prize;
        }
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


}
