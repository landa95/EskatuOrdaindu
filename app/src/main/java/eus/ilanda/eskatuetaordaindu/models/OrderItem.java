package eus.ilanda.eskatuetaordaindu.models;


public class OrderItem {

    private String itemOrderId;
    private ItemMenu item;
    private int quantity;

    public OrderItem(){

    }

    public OrderItem(ItemMenu item, int quantity){
        this.item= item;
        this.quantity = quantity;
    }

    public String getItemOrderId(){
        return itemOrderId;
    }

    public void setItemOrderId(String itemOrderId) {
        this.itemOrderId = itemOrderId;
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
}
