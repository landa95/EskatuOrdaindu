package eus.ilanda.eskatuetaordaindu.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import eus.ilanda.eskatuetaordaindu.R;

public class DialogFragmentEditDelete extends DialogFragment {

    private String title;

    private OnEditDeleteClick listener;

    public void newInstance(OnEditDeleteClick listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(this.title);
        builder.setItems(R.array.dialog_options_list, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.onClick(dialogInterface, i);
            }
        });

        return builder.create();
    }

    public void setTitle(String title){
        this.title=title;
    }

    public interface OnEditDeleteClick{
        void onClick(DialogInterface dialogInterface, int i);
    }
}
