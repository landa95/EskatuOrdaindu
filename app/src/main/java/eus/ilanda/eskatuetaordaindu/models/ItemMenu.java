package eus.ilanda.eskatuetaordaindu.models;

import android.os.Parcel;
import android.os.Parcelable;

public class ItemMenu implements Parcelable {

    private String id;
    private String itemName;
    private String itemDetails;
    private double prize;
    private String category;
    private String imageURL;

    public ItemMenu(){

    }

    public ItemMenu(Parcel p){
        category = "";
        readFromParcel(p);
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
        if (prize >= 0){
            this.prize = prize;
        }
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.id);
        parcel.writeString(this.itemName);
        parcel.writeString(this.itemDetails);
        parcel.writeDouble(this.prize);

    }
    public void readFromParcel(Parcel in){
        id = in.readString();
        itemName = in.readString();
        itemDetails = in.readString();
        prize = in.readDouble();

    }

    public static final Parcelable.Creator<ItemMenu> CREATOR =
            new Parcelable.Creator<ItemMenu>(){

                @Override
                public ItemMenu createFromParcel(Parcel parcel) {
                    return new ItemMenu(parcel);
                }

                public ItemMenu[] newArray(int size) {
                    return new ItemMenu[size];
                }
            };
}
