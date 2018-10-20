package eus.ilanda.eskatuetaordaindu.adapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import eus.ilanda.eskatuetaordaindu.R;
import eus.ilanda.eskatuetaordaindu.models.Category;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private int layout;
    private List<Category> categories;
    private OnItemClickListener listener;

    public CategoryAdapter(int layout,List<Category> list , OnItemClickListener listener){

        this.layout = layout;
        this.categories = list;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
            holder.bind(categories.get(position), listener);
        }

            @Override
            public int getItemCount() {
                return this.categories.size();
            }

            public void setCategories(List<Category> categories){
                this.categories = categories;
            }

            public List<Category> getCategories(){
                return this.categories;
            }

            //View Holder pattern
            public static class ViewHolder extends RecyclerView.ViewHolder {
                private TextView categoryName;

                public ViewHolder(View v) {
                    super(v);
                    this.categoryName = (TextView) v.findViewById(R.id.txtCategory);
                }

                public void bind(final Category category, final OnItemClickListener listener){
            categoryName.setText(category.getCategoryName());

            itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    listener.onItemClick(category, getAdapterPosition());
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    listener.onItemLongClick(category, getAdapterPosition());
                    return true;
                }
            });

        }

    }

    public interface OnItemClickListener {
        void onItemClick(Category category, int position);
        void onItemLongClick(Category category, int position);
    }

}
