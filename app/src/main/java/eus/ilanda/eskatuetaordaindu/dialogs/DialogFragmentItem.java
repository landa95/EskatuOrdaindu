package eus.ilanda.eskatuetaordaindu.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

import eus.ilanda.eskatuetaordaindu.R;
import eus.ilanda.eskatuetaordaindu.models.ItemMenu;

public class DialogFragmentItem extends DialogFragment {

    private String dialogTitle = "";
    private EditText inputItemName;
    private EditText inputItemDescription;
    private NumberPicker inputNumberPicker;
    private NumberPicker inputNumberPicker2;

    private OnDialogClick clickListener;

    public void newInstance(OnDialogClick listener){
        this.clickListener = listener;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View v = inflater.inflate(R.layout.dialog_item_menu, null);
        setUpControls(v);

        builder.setView(v).setTitle("New Item").setPositiveButton(R.string.dialog_ok, null).setNegativeButton(R.string.dialog_cancel, null);
        AlertDialog dialog = builder.create();
        dialog.show();

        Button accept =  dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button cancel = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);

        //Override Accept and Cancel buttons for custom behavior
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inputItemName.getText().toString().trim().isEmpty()|| inputItemDescription.getText().toString().trim().isEmpty()){
                    Toast.makeText(getContext(), "Please insert an item name and a description ", Toast.LENGTH_SHORT).show();
                }else{
                    ItemMenu item = new ItemMenu();
                    double db = getNumberPickers();
                    item.setItemName(inputItemName.getText().toString());
                    item.setItemDetails(inputItemDescription.getText().toString());
                    item.setPrize(db);
                    dismiss();
                    clickListener.onPositiveClick(item);
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        return dialog;
    }
    public double getNumberPickers(){
        double number = inputNumberPicker.getValue();
        double number2 = inputNumberPicker2.getValue();
        number2 = number2/100;
        number = number + number2;
        return number;
    }

    private void setUpControls(View v) {
        //input text ItemMenu name
        inputItemName = (EditText) v.findViewById(R.id.ET_dialog_item_name);

        //input text ItemMenu description
        inputItemDescription =(EditText) v.findViewById(R.id.ET_dialog_item_description);

        //Input prize in double format, double custom picker
        inputNumberPicker= (NumberPicker) v.findViewById(R.id.dialog_item_prize_picker);
        inputNumberPicker2= (NumberPicker) v.findViewById(R.id.dialog_item_prize_picker2);


        NumberPicker.Formatter formatter = new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return Currency.getInstance(Locale.getDefault()).getSymbol().toString() + NumberFormat.getInstance().format((int)i).toString() + ".";
            }
        };

        NumberPicker.Formatter formatter2 = new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return  String.format("%02d",i);
            }
        };
        inputNumberPicker.setFormatter(formatter);
        inputNumberPicker.setMinValue(0);
        inputNumberPicker.setMaxValue(1000);

        inputNumberPicker2.setFormatter(formatter2);
        inputNumberPicker2.setMinValue(0);
        inputNumberPicker2.setMaxValue(99);
    }

    public interface OnDialogClick{
        void onPositiveClick(ItemMenu item);
        void onNegativeClick();
    }

}

