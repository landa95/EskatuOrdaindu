package eus.ilanda.eskatuetaordaindu.models;


import android.os.Parcel;
import android.os.Parcelable;

public class OrderItem  implements Parcelable{


    private ItemMenu item;
    private int quantity;

    public OrderItem(){
    }

    public OrderItem(ItemMenu item, int quantity){
        this.item= item;
        this.quantity = quantity;
    }

    public OrderItem(Parcel in){
        readFromParcel(in);
    }


    public ItemMenu getItem() {
        return item;
    }

    public void setItem(ItemMenu item) {
        this.item = item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(this.item,i);
        parcel.writeInt(this.quantity);

    }
    public void readFromParcel(Parcel in){
        item = in.readParcelable(ItemMenu.class.getClassLoader());
        quantity = in.readInt();
    }


    public static final Parcelable.Creator<OrderItem> CREATOR =
            new Parcelable.Creator<OrderItem>(){

                @Override
                public OrderItem createFromParcel(Parcel parcel) {
                    return new OrderItem(parcel);
                }

                public OrderItem[] newArray(int size) {
                    return new OrderItem[size];
                }
            };
}
