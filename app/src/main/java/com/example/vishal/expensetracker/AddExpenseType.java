package com.example.vishal.expensetracker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;

import static android.R.attr.id;

/**
 * Created by vishal on 2/10/17.
 */

public class AddExpenseType extends DialogFragment {
    public static AddExpenseType newExpense(String title) {
        AddExpenseType frag = new AddExpenseType();
        Bundle args = new Bundle();
        args.putString("title", title);

        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String title = getArguments().getString("title");
        //final Integer id = getArguments().getInt("id");

        //final Integer expense = getArguments().getInt("expense");

        return new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setView(R.layout.dialog_new_exp_type)
                .setPositiveButton(R.string.add,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                String expenseType = ((EditText)getDialog().findViewById(R.id.expense_type_new))
                                        .getText().toString();


                                ((DisplayExpenses)getActivity()).doPositiveClickExp(expenseType);
                            }
                        }
                )
                .setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dismiss();
                            }
                        }
                )
                .create();
    }
}
