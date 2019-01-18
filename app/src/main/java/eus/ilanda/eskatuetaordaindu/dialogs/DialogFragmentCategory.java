package eus.ilanda.eskatuetaordaindu.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import eus.ilanda.eskatuetaordaindu.R;
import eus.ilanda.eskatuetaordaindu.manager.DBManager;
import eus.ilanda.eskatuetaordaindu.models.Category;

public class DialogFragmentCategory extends  android.support.v4.app.DialogFragment {


    private EditText cateogryEditText;
    private Category category;
    public final String ACTION_NEW = "new";
    public final String ACTION_EDIT = "edit";


    private OnDialogClick clickListener;

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState){
        final DBManager manager = new DBManager();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        //inflate category layout
        View v  = inflater.inflate(R.layout.dialog_category,null);
        setUpControls(v);
        builder.setView(v).setTitle("Category").setPositiveButton(R.string.dialog_ok, null).setNegativeButton(R.string.dialog_cancel, null);
        AlertDialog dialog = builder.create();
        dialog.show();

        Button accept =  dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button cancel = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(cateogryEditText.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getContext(), "Please insert a category name", Toast.LENGTH_SHORT).show();
                }else {
                    category.setCategoryName(cateogryEditText.getText().toString());
                    dismiss();
                    clickListener.onPositiveClick(category);
                }
            }
        });
        return dialog;
    }

    public void newInstance(OnDialogClick onDialogClick){
        clickListener = onDialogClick;
    }
    private void setUpControls(View v) {
        cateogryEditText = (EditText) v.findViewById(R.id.txt_new_category);
    }
    public void setCategory(Category category){
        this.category = category;
    }

    public interface OnDialogClick{
        void onPositiveClick(Category category);
        void onNegativeClick();
    }
}
