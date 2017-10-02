package com.example.vishal.expensetracker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.DialogFragment;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by vishal on 2/10/17.
 */

public class AddExpense extends DialogFragment {

    public static AddExpense newExpense(String title, Integer expense, Integer id) {
        AddExpense frag = new AddExpense();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putInt("id", id);
        args.putInt("expense", expense);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String title = getArguments().getString("title");
        final Integer id = getArguments().getInt("id");

        final Integer expense = getArguments().getInt("expense");

        return new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setView(R.layout.dialog)
                .setPositiveButton(R.string.add,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                String expenseDescription = ((EditText)getDialog().findViewById(R.id.expense_description))
                                        .getText().toString();
                                int expenseAmount = Integer.parseInt(((EditText)getDialog().findViewById(R.id.expense_amount))
                                        .getText().toString());

                                ((DisplayExpenses)getActivity()).doPositiveClick(expenseDescription, expenseAmount, title, expense, id);
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
