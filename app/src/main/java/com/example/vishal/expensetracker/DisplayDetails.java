package com.example.vishal.expensetracker;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DisplayDetails extends AppCompatActivity {

    private static Database mDbHelper;
    private String heading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_details);
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            // go to calling activity
            finish();
        }

        String title = bundle.getString("type");
        heading=title;
        TextView tv = (TextView) findViewById(R.id.type);
        tv.setText(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDbHelper = new Database(getApplicationContext());

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                ExpenseDescription.FeedEntry.description,
                ExpenseDescription.FeedEntry.expense,
                ExpenseDescription.FeedEntry.date
        };

        // Filter results WHERE "title" = 'My Title'
        String selection = ExpenseDescription.FeedEntry.type + " = ?";
        String[] selectionArgs = { title };




        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                ExpenseDescription.FeedEntry.expense + " DESC";

        Cursor cursor = db.query(
                ExpenseDescription.FeedEntry.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        //get expenseContainer
        TableLayout tableContainer = (TableLayout) findViewById(R.id.description_table);
        // get inflater object
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        TableRow heading = (TableRow) inflater.inflate(R.layout.heading_row, tableContainer, false);

        tableContainer.addView(heading);

        while(cursor.moveToNext()) {
            final String description = cursor.getString(
                    cursor.getColumnIndexOrThrow(ExpenseDescription.FeedEntry.description));

            final Integer expense = cursor.getInt(
                    cursor.getColumnIndexOrThrow(ExpenseDescription.FeedEntry.expense));

            final String date = cursor.getString(
                    cursor.getColumnIndexOrThrow(ExpenseDescription.FeedEntry.date));

            TableRow details = (TableRow) inflater.inflate(R.layout.table_rows, tableContainer, false);

            TextView desc = (TextView) details.getChildAt(0);
            TextView exp = (TextView) details.getChildAt(1);
            TextView dateSpent = (TextView) details.getChildAt(2);

            desc.setText(description, TextView.BufferType.NORMAL);
            exp.setText(expense.toString(), TextView.BufferType.NORMAL);
            dateSpent.setText(date, TextView.BufferType.NORMAL);

            tableContainer.addView(details);
        }
        cursor.close();

    }

    @Override
    protected void onDestroy() {
        mDbHelper.close();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        // noinspection SimplifiableIfStatement
        if (id == R.id.sort_exp) {
            //SQLiteDatabase db = mDbHelper.getReadableDatabase();

            // Define a projection that specifies which columns from the database
            // you will actually use after this query.
            String[] projection = {
                    ExpenseDescription.FeedEntry.description,
                    ExpenseDescription.FeedEntry.expense,
                    ExpenseDescription.FeedEntry.date
            };

            // Filter results WHERE "title" = 'My Title'
            String selection = ExpenseDescription.FeedEntry.type + " = ?";
            String[] selectionArgs = { heading };


            // How you want the results sorted in the resulting Cursor
            String sortOrder =
                    ExpenseDescription.FeedEntry.expense + " DESC";

            Cursor cursor = db.query(
                    ExpenseDescription.FeedEntry.TABLE_NAME,                     // The table to query
                    projection,                               // The columns to return
                    selection,                                // The columns for the WHERE clause
                    selectionArgs,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder                                 // The sort order
            );

            //get expenseContainer
            TableLayout tableContainer = (TableLayout) findViewById(R.id.description_table);
            tableContainer.removeAllViews();
            // get inflater object
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            TableRow heading = (TableRow) inflater.inflate(R.layout.heading_row, tableContainer, false);

            tableContainer.addView(heading);

            while(cursor.moveToNext()) {
                final String description = cursor.getString(
                        cursor.getColumnIndexOrThrow(ExpenseDescription.FeedEntry.description));

                final Integer expense = cursor.getInt(
                        cursor.getColumnIndexOrThrow(ExpenseDescription.FeedEntry.expense));

                final String date = cursor.getString(
                        cursor.getColumnIndexOrThrow(ExpenseDescription.FeedEntry.date));

                TableRow details = (TableRow) inflater.inflate(R.layout.table_rows, tableContainer, false);

                TextView desc = (TextView) details.getChildAt(0);
                TextView exp = (TextView) details.getChildAt(1);
                TextView dateSpent = (TextView) details.getChildAt(2);

                desc.setText(description, TextView.BufferType.NORMAL);
                exp.setText(expense.toString(), TextView.BufferType.NORMAL);
                dateSpent.setText(date, TextView.BufferType.NORMAL);

                tableContainer.addView(details);
            }
            cursor.close();
        }

        else if(id ==R.id.sort_date){





            // Define a projection that specifies which columns from the database
            // you will actually use after this query.
            String[] projection = {
                    ExpenseDescription.FeedEntry.description,
                    ExpenseDescription.FeedEntry.expense,
                    ExpenseDescription.FeedEntry.date
            };

            // Filter results WHERE "title" = 'My Title'
            String selection = ExpenseDescription.FeedEntry.type + " = ?";
            String[] selectionArgs = { heading };

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");


            // How you want the results sorted in the resulting Cursor
            String sortOrder =
                    null;
            try {
                sortOrder = sdf.parse(ExpenseDescription.FeedEntry.date) + " DESC";
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Cursor cursor = db.query(
                    ExpenseDescription.FeedEntry.TABLE_NAME,                     // The table to query
                    projection,                               // The columns to return
                    selection,                                // The columns for the WHERE clause
                    selectionArgs,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder                                 // The sort order
            );

            //get expenseContainer
            TableLayout tableContainer = (TableLayout) findViewById(R.id.description_table);
            tableContainer.removeAllViews();
            // get inflater object
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            TableRow heading = (TableRow) inflater.inflate(R.layout.heading_row, tableContainer, false);

            tableContainer.addView(heading);

            while(cursor.moveToNext()) {
                final String description = cursor.getString(
                        cursor.getColumnIndexOrThrow(ExpenseDescription.FeedEntry.description));

                final Integer expense = cursor.getInt(
                        cursor.getColumnIndexOrThrow(ExpenseDescription.FeedEntry.expense));

                final String date = cursor.getString(
                        cursor.getColumnIndexOrThrow(ExpenseDescription.FeedEntry.date));

                TableRow details = (TableRow) inflater.inflate(R.layout.table_rows, tableContainer, false);

                TextView desc = (TextView) details.getChildAt(0);
                TextView exp = (TextView) details.getChildAt(1);
                TextView dateSpent = (TextView) details.getChildAt(2);

                desc.setText(description, TextView.BufferType.NORMAL);
                exp.setText(expense.toString(), TextView.BufferType.NORMAL);
                dateSpent.setText(date, TextView.BufferType.NORMAL);

                tableContainer.addView(details);
            }
            cursor.close();
        }

        return super.onOptionsItemSelected(item);
    }
}
