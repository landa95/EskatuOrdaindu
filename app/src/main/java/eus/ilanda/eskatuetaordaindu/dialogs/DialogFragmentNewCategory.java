package eus.ilanda.eskatuetaordaindu.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import eus.ilanda.eskatuetaordaindu.R;
import eus.ilanda.eskatuetaordaindu.manager.DBManager;

public class DialogFragmentNewCategory extends  android.support.v4.app.DialogFragment {

    DBManager manager = new DBManager();

    String mode = "new";
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v  = inflater.inflate(R.layout.dialog_category,null);
       final EditText text = (EditText) v.findViewById(R.id.txt_new_category);
        builder.setView(v).setTitle("New Category").setPositiveButton("Accept", new DialogInterface.OnClickListener() {
           @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               Toast.makeText(getContext(), text.getText().toString(),Toast.LENGTH_SHORT ).show();
               manager.newCategory(text.getText().toString());
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        return builder.create();
    }

}
