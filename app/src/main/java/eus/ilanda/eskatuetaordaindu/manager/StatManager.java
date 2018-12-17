package eus.ilanda.eskatuetaordaindu.manager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import eus.ilanda.eskatuetaordaindu.models.ItemMenu;
import eus.ilanda.eskatuetaordaindu.models.Order;
import eus.ilanda.eskatuetaordaindu.models.OrderItem;

public class StatManager {

    public CallbackStats callbackStats;

    public interface CallbackStats{
        void topDish(String itemMenu);
    }

    private static FirebaseAuth auth = FirebaseAuth.getInstance();
    private static FirebaseDatabase database = FirebaseDatabase.getInstance();

    public StatManager(){}

    public StatManager(CallbackStats callbackStats){
        this.callbackStats = callbackStats;
    }

    public void topDish(){
        Query query = database.getReference("orders");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Order order = new Order();
                ArrayList<OrderItem> orderItems = new ArrayList<>();
                if (dataSnapshot != null){

                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                        order = snapshot.getValue(Order.class);
                        orderItems.addAll(order.getOrderItems());
                    }
                }
                callbackStats.topDish(getTopDish(orderItems));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private String getTopDish(ArrayList<OrderItem> orderItems){
        HashMap<String, Integer> itemMenuConcurrencies = new HashMap<>();
        ItemMenu topItem = orderItems.get(0).getItem();
        Integer  topQuantity = 0;
        for (int i = 0; i< orderItems.size(); i++){

            ItemMenu currentItemMenu = orderItems.get(i).getItem();
            Integer currentQuantity = orderItems.get(i).getQuantity();

            if (!itemMenuConcurrencies.containsKey(orderItems.get(i).getItem().getItemName())){
                itemMenuConcurrencies.put(currentItemMenu.getItemName(), currentQuantity);
                if (orderItems.get(i).getQuantity()> topQuantity){
                    topItem  = currentItemMenu;
                    topQuantity  = orderItems.get(i).getQuantity();
                }
            }else {
                Integer num = itemMenuConcurrencies.get(currentItemMenu.getItemName());
                itemMenuConcurrencies.put(currentItemMenu.getItemName() ,num + currentQuantity);
                if (num+currentQuantity > topQuantity){
                    topQuantity = itemMenuConcurrencies.get(currentItemMenu.getItemName());
                    topItem = currentItemMenu;
                }
            }
        }
       return topItem.getItemName();
    }
}
