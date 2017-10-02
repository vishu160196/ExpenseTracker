package com.example.vishal.expensetracker;

import android.provider.BaseColumns;

public final class ExpenseTable {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private ExpenseTable() {}

    /* Inner class that defines the table contents */
    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "ExpenseTable";
        public static final String type = "type";
        public static final String expense = "expense";
    }
}
