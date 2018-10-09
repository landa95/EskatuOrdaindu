package eus.ilanda.eskatuetaordaindu.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import eus.ilanda.eskatuetaordaindu.R;
import eus.ilanda.eskatuetaordaindu.manager.DBManager;
import eus.ilanda.eskatuetaordaindu.models.Category;

public class DialogFragmentCategory extends  android.support.v4.app.DialogFragment {


    private String tag = "new";
    private String title = "New Category";
    private EditText text;

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState){
        this.setTag(getTag());
        this.setTitle();
        final DBManager manager = new DBManager();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        //inflate category layout
        View v  = inflater.inflate(R.layout.dialog_category,null);
       text = (EditText) v.findViewById(R.id.txt_new_category);
        builder.setView(v).setTitle(title).setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
           @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               String txtInput = text.getText().toString();
               if(txtInput.trim().isEmpty()){
                   Toast.makeText(getContext(), "Please insert a category name", Toast.LENGTH_SHORT).show();
               }else{
                   if (tag.equals("new")){
                       Toast.makeText(getContext(), text.getText().toString(),Toast.LENGTH_SHORT ).show();
                       manager.newCategory(text.getText().toString());
                   }else if (tag.equals("edit")){
                       Bundle bundle = getArguments();
                       Toast.makeText(getContext(), text.getText().toString(),Toast.LENGTH_SHORT ).show();
                       Category cat  = new Category(bundle.getString("categoryId"), bundle.getString("categoryName"));
                       Log.w("DIALOG", cat.getCategoryName() + " " + cat.getId());
                       manager.updateCategory(cat,text.getText().toString());
                   }
               }


            }
        }).setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        return builder.create();
    }

    public void setTag(String tag) {
        if(tag.equals("new") || tag.equals("edit")){
            this.tag = tag;
        }
    }

    public void setTitle(){
        if(tag.equals("new")){
            this.title= "New Category";
        }else if (tag.equals("edit")){
            this.title= "Edit Category";
        }
    }
}
