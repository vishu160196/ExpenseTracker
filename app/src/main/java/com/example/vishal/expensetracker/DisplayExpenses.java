package com.example.vishal.expensetracker;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.security.AccessController.getContext;

public class DisplayExpenses extends AppCompatActivity {

    private Database mDbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_expenses);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create the fragment and show it as a dialog.
                AddExpenseType newExpense = AddExpenseType.newExpense("Add new expense type");
                newExpense.show(getFragmentManager(), "launchAddExpenseTypeDialog");
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        mDbHelper = new Database(getApplicationContext());
        final SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
            "rowid",
            ExpenseTable.FeedEntry.type,
            ExpenseTable.FeedEntry.expense
            };

        // Filter results WHERE "title" = 'My Title'
        //String selection = FeedEntry.COLUMN_NAME_TITLE + " = ?";
        //String[] selectionArgs = { "My Title" };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
            ExpenseTable.FeedEntry.expense + " DESC";

        Cursor cursor = db.query(
            ExpenseTable.FeedEntry.TABLE_NAME,                     // The table to query
            projection,                               // The columns to return
            null,                                // The columns for the WHERE clause
            null,                            // The values for the WHERE clause
            null,                                     // don't group the rows
            null,                                     // don't filter by row groups
            sortOrder                                 // The sort order
            );

        while(cursor.moveToNext()){

            final long rowId = cursor.getLong(
                    cursor.getColumnIndexOrThrow("rowid"));
            final int expense = cursor.getInt(
                    cursor.getColumnIndexOrThrow(ExpenseTable.FeedEntry.expense));
            final String type = cursor.getString(
                    cursor.getColumnIndexOrThrow(ExpenseTable.FeedEntry.type));

            //get expenseContainer
            LinearLayout expenseContainer = (LinearLayout) findViewById(R.id.expense_container);
            // get inflater object
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());

            final Button expenseType = (Button) inflater.inflate(R.layout.expenditure_type_button, expenseContainer, false);
            final LinearLayout expenseDetails = (LinearLayout) inflater.inflate(R.layout.expenditure_type, expenseContainer, false);
            //final Integer disappearButtons = expenseDetails.generateViewId();
            //expenseDetails.setId(disappearButtons);
            expenseType.setText(type);
            TextView money = (TextView) expenseDetails.getChildAt(0);

            Integer exp=expense;
            money.setText(exp.toString());
            final Integer id = money.generateViewId();
            money.setId(id);

            LinearLayout ll = (LinearLayout) expenseDetails.getChildAt(1);


            Button newButton = (Button) ll.getChildAt(0);
            Button view = (Button) ll.getChildAt(1);
            Button delete = (Button) ll.getChildAt(2);

            newButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Create the fragment and show it as a dialog.
                    AddExpense newExpense = AddExpense.newExpense(type, expense, id);
                    newExpense.show(getFragmentManager(), "launchAddExpenseDialog");
                }
            });

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent viewDetails = new Intent(getApplicationContext(), DisplayDetails.class);
                    viewDetails.putExtra("type", type);
                    startActivity(viewDetails);
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Define 'where' part of query.
                    String selection = ExpenseTable.FeedEntry.type + " = ?";
                    // Specify arguments in placeholder order.
                    String[] selectionArgs = { type };
                    // Issue SQL statement.
                    db.delete(ExpenseTable.FeedEntry.TABLE_NAME, selection, selectionArgs);
                    expenseType.setVisibility(View.GONE);
                    expenseDetails.setVisibility(View.GONE);
                }
            });
            expenseContainer.addView(expenseType);
            expenseContainer.addView(expenseDetails);

        }
        cursor.close();

    }

    public void doPositiveClick(final String expenseDescription, final int expenseAmount, final String type, final Integer expense, final Integer id){
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        String dateSpent = dateFormat.format(date);

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(ExpenseDescription.FeedEntry.type, type);
        values.put(ExpenseDescription.FeedEntry.description, expenseDescription);
        values.put(ExpenseDescription.FeedEntry.expense, expenseAmount);
        values.put(ExpenseDescription.FeedEntry.date, dateSpent);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(ExpenseDescription.FeedEntry.TABLE_NAME, null, values);

        Integer newExpense = expenseAmount + expense;
        //SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // New value for one column
        ContentValues valuesUpdate = new ContentValues();
        valuesUpdate.put(ExpenseTable.FeedEntry.expense, newExpense);

        // Which row to update, based on the title
        String selection = ExpenseTable.FeedEntry.type + " = ?";
        String[] selectionArgs = { type };

        int count = db.update(
                ExpenseTable.FeedEntry.TABLE_NAME,
                valuesUpdate,
                selection,
                selectionArgs);

        TextView tv= (TextView) findViewById(id);
        tv.setText(newExpense.toString());
    }

    @Override
    protected void onDestroy() {
        mDbHelper.close();
        super.onDestroy();
    }

    public void doPositiveClickExp(String expenseType) {
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(ExpenseTable.FeedEntry.type, expenseType);
        values.put(ExpenseTable.FeedEntry.expense, 0);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(ExpenseTable.FeedEntry.TABLE_NAME, null, values);
    }
}
