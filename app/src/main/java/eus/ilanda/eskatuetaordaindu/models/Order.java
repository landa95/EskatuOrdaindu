package eus.ilanda.eskatuetaordaindu.models;

import java.util.ArrayList;

public class Order{

    private String orderId;
    private int tableNumber;
    private String userId;
    private String timestamp;
    private String details;
    private boolean paid;
    private ArrayList<OrderItem> orderItems = new ArrayList<>();
    public Order(){}

    public Order(int tableNumber, String userId, String details, ArrayList<OrderItem> orderItems){
        this.tableNumber = tableNumber;
        this.userId = userId;
        this.details = details;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public ArrayList<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(ArrayList<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public OrderItem getOrderItem(int i){
        return this.orderItems.get(i);
    }

    public double getTotalPrice(){
        double price = 0;
        for (int i = 0; i< orderItems.size(); i++){
            price = price + (orderItems.get(i).getItem().getPrize() * orderItems.get(i).getQuantity());
        }
        return price;
    }
}
