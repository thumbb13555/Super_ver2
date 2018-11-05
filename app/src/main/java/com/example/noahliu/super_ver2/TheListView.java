package com.example.noahliu.super_ver2;

import android.content.Context;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.internal.StringResourceValueReader;

import java.util.ArrayList;

public class TheListView extends ArrayAdapter<User> {

    private LayoutInflater mInflater;
    private ArrayList<User> users;
    private int mViewResourceId;
    private int amount = 0;
    DatabaseHelper myDB;




    public TheListView(Context context, int textViewResourceId, ArrayList<User> users) {
        super(context, textViewResourceId, users);
        this.users = users;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mViewResourceId = textViewResourceId;

    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = mInflater.inflate(mViewResourceId, null);
        final User user = users.get(position);



        if (user != null) {
            final TextView title = (TextView) convertView.findViewById(R.id.tvName);
            final TextView sale = (TextView) convertView.findViewById(R.id.tvSale);
            final TextView txId = (TextView) convertView.findViewById(R.id.txtId);
            Button btD = (Button) convertView.findViewById(R.id.btDel);
            Button btPlus = (Button) convertView.findViewById(R.id.btPlus);
            Button btMin = (Button) convertView.findViewById(R.id.btMiner);
            final TextView Amount = (TextView) convertView.findViewById(R.id.amount);

            if (txId != null) {
                txId.setText(user.getFavFood());
            }
            if (title != null) {
                title.setText(user.getFirstName());
            }
            if (sale != null) {
                sale.setText(user.getLastName());
            }

            btD.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myDB = new DatabaseHelper(getContext());
                    SQLiteDatabase db = myDB.getWritableDatabase();
                    String id = txId.getText().toString();
                    db.delete("Cart", "ID=" + id, null);
                    remove(getItem(position));
                    Toast.makeText(getContext(),"已刪除商品！",Toast.LENGTH_SHORT).show();
                    db.close();



                }
            });
            btPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    amount += 1;
                    Amount.setText(String.valueOf(amount));

                }
            });
            btMin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    amount -= 1;
                    if (amount < 0) {
                        amount += 1;
                    } else {
                        Amount.setText(String.valueOf(amount));
                    }
                }
            });

        }//End if

        return convertView;

    }

}

class User {
    private String FirstName;
    private String LastName;
    private String FavFood;

    public User(String fName, String lName, String fFood) {
        FirstName = fName;
        LastName = lName;
        FavFood = fFood;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getFavFood() {
        return FavFood;
    }

    public void setFavFood(String favFood) {
        FavFood = favFood;
    }
}