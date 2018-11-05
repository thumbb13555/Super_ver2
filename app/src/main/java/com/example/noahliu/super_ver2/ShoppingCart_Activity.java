package com.example.noahliu.super_ver2;


import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import android.support.annotation.Nullable;

public class ShoppingCart_Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ListView listView;
    DatabaseHelper myDB;
    ArrayList<User> userList;
    User user;

    public static final String TABLE_NAME = "friends";
    public static final String NAME = "name";
    public static final String TEL = "tel";
    public static final String EMAIL = "email";
    TextView tvID, tvTotle, tvSales, tvamount;
    Button btChickout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        listView = (ListView) findViewById(R.id.lvCart);
        //========List
        btChickout = (Button) findViewById(R.id.chickou);
        myDB = new DatabaseHelper(this);
        userList = new ArrayList<>();
        Cursor data = myDB.getListContents();
        int numRows = data.getCount();
        if (numRows == 0) {
            Toast.makeText(ShoppingCart_Activity.this, "目前購物車是空的喔！", Toast.LENGTH_LONG).show();
        } else {
            int i = 0;
            while (data.moveToNext()) {
                user = new User(data.getString(1), data.getString(2), data.getString(0));
                userList.add(i, user);
                Log.v("BT", "品名：" + data.getString(1) + "  價錢：" + data.getString(2) + "  ID:" + data.getString(0));

            }
            TheListView adapter = new TheListView(this, R.layout.shoppingcart_layout, userList);
            listView.setAdapter(adapter);
        }
        btChickout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(ShoppingCart_Activity.this)
                        .setTitle("下單確認")
                        .setMessage("按下確認鍵即完成下單，確定嗎？")
                        .setPositiveButton("是",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Toast.makeText(getBaseContext(), "請繼續填寫領取地點與時間！", Toast.LENGTH_SHORT).show();
                                    }
                                }).setNegativeButton("否", null).show();
            }


        });
        //========
    }//EndOnCreate


    //-----
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.shopping_cart_, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            finish();
        } else if (id == R.id.nav_food) {
            Intent gofood = new Intent(ShoppingCart_Activity.this, Food_Activity.class);
            startActivity(gofood);
            finish();
        } else if (id == R.id.nav_beverage) {
            Intent gobeverage = new Intent(ShoppingCart_Activity.this, Beverage_Activity.class);
            startActivity(gobeverage);
            finish();
        } else if (id == R.id.nav_ness) {
            Intent golife = new Intent(ShoppingCart_Activity.this, Life_Activity.class);
            startActivity(golife);
            finish();
        } else if (id == R.id.nav_shopping) {

        } else if (id == R.id.nav_content) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}

