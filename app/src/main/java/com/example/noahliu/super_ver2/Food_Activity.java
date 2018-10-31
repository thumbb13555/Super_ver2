package com.example.noahliu.super_ver2;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.database.sqlite.SQLiteDatabase;

import com.facebook.stetho.Stetho;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Food_Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    String urladdress="http://120.109.18.143/joomla/fatty/ResultMembershipdata2.php";
    String[] title;
    String[] script;
    String[] img;
    ListView listView;
    BufferedInputStream is;
    String line=null;
    String result = null;
    DatabaseHelper  myDB;
        public static final String TABLE_NAME="friends";
    public static final String NAME="name";
    public static final String TEL="tel";
    public static final String EMAIL="email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        myDB = new DatabaseHelper(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        listView = (ListView) findViewById(R.id.lview);

        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));

        collectData();
        CustomListView customListView = new CustomListView(this,title,script,img);
        listView.setAdapter(customListView);
        Stetho.initializeWithDefaults(this);


    }
    private  void collectData()
    {
        try{
            URL url = new URL(urladdress);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            is=new BufferedInputStream(con.getInputStream());




        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }


        try{
            BufferedReader br=new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            while ((line=br.readLine())!=null){
                sb.append(line+"\n");

            }
            is.close();
            result=sb.toString();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        //JSON

        try{
            JSONArray ja = new JSONArray(result);
            JSONObject jo =null;
            title = new String[ja.length()];
            script = new String[ja.length()];
            img = new String[ja.length()];

            for(int i=0;i<=ja.length();i++)
            {
                jo=ja.getJSONObject(i);
                title[i] = jo.getString("title");
                script[i] = jo.getString("script");
                img[i] = jo.getString("img");
//                title[i]=jo.getString("name");
//                script[i]=jo.getString("email");
//                img[i]=jo.getString("photo");

            }


        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    public class CustomListView extends ArrayAdapter<String> {


        private  String[] title;
        private String[] script;
        private String[] img;
        private Activity context;
        Bitmap bitmap;
        public CustomListView(Activity context,String[] title,String[] script,String[] img) {


            super(context,R.layout.layout,title);
            this.context=context;
            this.title=title;
            this.script=script;
            this.img=img;
            Log.v("BT","Here "+img);
        }

        @NonNull
        @Override

        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
            View r=convertView;
            ViewHolder viewHolder=null;
            if(r==null){
                LayoutInflater layoutInflater=context.getLayoutInflater();
                r=layoutInflater.inflate(R.layout.layout,null,true);
                viewHolder = new ViewHolder(r);
                r.setTag(viewHolder);
            }
            else{
                viewHolder=(ViewHolder)r.getTag();


            }

            viewHolder.tvw1.setText(title[position]);
            viewHolder.tvw2.setText(script[position]);

            new GetImageFromURL(viewHolder.ivw).execute(img[position]);


            return r;
        }

        class ViewHolder{

            TextView tvw1;
            TextView tvw2;
            ImageView ivw;
            Button Cart;
            ViewHolder(View v){
                tvw1=(TextView)v.findViewById(R.id.tvprofilename);
                tvw2=(TextView)v.findViewById(R.id.email);
                ivw = (ImageView) v.findViewById(R.id.imageView);
                Cart = (Button) v.findViewById(R.id.btCart);
                Cart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String Title = tvw1.getText().toString();
                        String Sale = tvw2.getText().toString();
                        Log.v("BT",Title+Sale);
                        boolean insertData = myDB.addData(Title,Sale);
                        if(insertData==true){
                            Toast.makeText(Food_Activity.this,"加入購物車！",Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(Food_Activity.this,"Something went wrong :(.",Toast.LENGTH_LONG).show();
                        }


                    }
                });

            }

        }

        public  class GetImageFromURL extends AsyncTask<String,Void, Bitmap>
        {

            ImageView img;
            public GetImageFromURL(ImageView imgv)
            {
                this.img = imgv;
            }

            @Override
            protected Bitmap doInBackground(String... url) {

                String urldisplay = url[0];
                bitmap=null;

                try{
                    InputStream ist = new java.net.URL(urldisplay).openStream();
                    bitmap = BitmapFactory.decodeStream(ist);
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }


                return bitmap;
            }

            @Override
            protected  void onPostExecute(Bitmap bitmap){
                super.onPostExecute(bitmap);
                img.setImageBitmap(bitmap);

            }
        }
    }
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
        getMenuInflater().inflate(R.menu.main, menu);
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

        } else if (id == R.id.nav_beverage) {
            Intent gobeverage = new Intent(Food_Activity.this,Beverage_Activity.class);
            startActivity(gobeverage);
            finish();
        } else if (id == R.id.nav_ness) {
            Intent golife = new Intent(Food_Activity.this,Life_Activity.class);
            startActivity(golife);
            finish();
        } else if (id == R.id.nav_shopping) {
            Intent goShopping = new Intent(Food_Activity.this,ShoppingCart_Activity.class);
            startActivity(goShopping);
            finish();
        } else if (id == R.id.nav_content) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    

}

