package com.example.raide_000.transactionkeeper;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * currenttransactions.java
 *
 * A current transaction is a transaction stored in the database until the user deletes it. This
 * class stores all of the current transactions.
 *
 * Peter Meglis
 * 5 February 2017
 */
public class CurrentTransactions extends AppCompatActivity {

    public static ArrayList<TextView> textViews = new ArrayList<TextView>();
    public static LinearLayout layout;
    private ImageButton menu;
    public static Data data = new Data();
    private boolean dateList = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_transactions);

        // Popup menu for more settings like sort and clear
        menu = (ImageButton) findViewById(R.id.menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(CurrentTransactions.this, menu);
                popup.getMenuInflater()
                        .inflate(R.menu.popup_menu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.deleted) {
                            Graphics.switchTo(CurrentTransactions.this, RecentlyDeleted.class);
                        }
                        else if (item.getItemId() == R.id.clear) {
                            data.clearTransactions();
                            textViews = new ArrayList<TextView>();
                            dateList = true;
                            finish();
                            startActivity(getIntent());
                        }
                        else if (item.getItemId() == R.id.sortDate) {
                            data.sortByDate();
                            layout.removeAllViews();
                            dateList = true;
                            updateTransactions();
                            display();
                        }
                        else if (item.getItemId() == R.id.sortAccount) {
                            data.sortByAccount();
                            layout.removeAllViews();
                            dateList = false;
                            updateTransactions();
                            display();
                        }
                        return true;
                    }
                });

                popup.show();
            }
        });


        layout = (LinearLayout) findViewById(R.id.layout);
        updateTransactions();
        display();
    }


    /*
     * Removes all of textViews from the page.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (TextView t : textViews) {
            layout.removeView(t);
        }
    }

    /*
     * Adds a transaction to the data.
     */
    public static void addTransaction(final Transaction t) {
        data.addTransaction(t);
    }

    /*
     * Adds a transaction's textView to the page
     */
    public static void addTextView(final Transaction t) {
        final TextView textView = new TextView(MyApplication.getAppContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 0, 10);

        textView.setLayoutParams(layoutParams);
        textView.setTextAlignment(LinearLayout.TEXT_ALIGNMENT_VIEW_START);
        textView.setTextColor(Color.parseColor(CurrentTransactions.data.getAccountColor(t.getAccount())));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setBackgroundResource(R.drawable.current_transaction);
        textView.setText(t.getName() + "\n" + t.getAccount() + " " + t.getAmount() + "\n" + t.getDate());
        textView.setLines(3);

        textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                CurrentTransactions.removeTransaction(textView, t);
                return false;
            }
        });

        textViews.add(0, textView);
    }

    /*
     * Removes a transaction and its textView from the data.
     */
    public static void removeTransaction(TextView v, Transaction t) {
        data.removeTransaction(t);
        v.setVisibility(View.GONE);
        RecentlyDeleted.addTransaction(v);
        textViews.remove(v);
    }

    /*
     * Updates the transactions from the data and adds their textViews to the page. Also
     * adds date separators in between transactions.
     */
    public void updateTransactions() {
        ArrayList<Transaction> list = data.getTransactions();
        textViews = new ArrayList<TextView>();
        String currentDate = "";
        for (Transaction t : list) {
            if (dateList && !currentDate.equals(t.getJustDate())) {
                currentDate = t.getJustDate();
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                        LinearLayout.LayoutParams.FILL_PARENT);

                TextView separator = new TextView(MyApplication.getAppContext());
                separator.setLayoutParams(layoutParams);
                separator.setTextColor(Color.BLACK);
                separator.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                separator.setText("---------------" + t.getJustDate() + "---------------");
                separator.setTextAlignment(LinearLayout.TEXT_ALIGNMENT_CENTER);
                separator.setPadding(0,0,0,20);
                textViews.add(0, separator);
            }
            addTextView(t);
        }
    }

    /*
     * Actually displays the list of textviews.
     */
    public void display() {
        for (TextView t : textViews) {
            layout.addView(t);
        }
    }




}
