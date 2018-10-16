package com.example.noahliu.super_ver2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class MemberCreate_Activity extends AppCompatActivity {
    EditText edCreateID,edCreatePassWord,edCreatePhone;
    Button btChick;
    private static final String SERVERIP = "120.109.18.143";
    private static final String PORTNO = "";
    private static final String PHPSCRIPT = "/joomla/fatty/CreateNewMemberver2.php?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_create_main);

        edCreateID       = (EditText) findViewById(R.id.edCID);
        edCreatePassWord = (EditText) findViewById(R.id.edCPS);
        edCreatePhone    = (EditText) findViewById(R.id.edCPho);
        btChick          = (Button)   findViewById(R.id.btCSur);
    }//onCreate End
    public void sendInfo(View view){
        String strID = edCreateID.getText().toString();
        String strPassword = edCreatePassWord.getText().toString();
        String strPhone = edCreatePhone.getText().toString();

        String strData ="Account=" + strID + "&Password=" + strPassword+"&Phone="+strPhone;
        Log.v("Bt",strData);
        if (strID.length() > 0 && strPassword.length() > 0){
            new MemberCreate_Activity.HttpRequestAsyncTask_CreateMember(this,SERVERIP,PORTNO,PHPSCRIPT,strData).execute();
        }else {
            Toast.makeText(this,"好像少了點什麼?",Toast.LENGTH_SHORT).show();
        }//end if
    }//end sendScore()
    public class HttpRequestAsyncTask_CreateMember extends AsyncTask<Void,Void,String> {
        private Context context;//指向呼叫此類別的Activity
        ProgressDialog loading;//設定程序進度指示器
        private String strIP;//server的ip
        private String strPort;//server所開通的port，一般為80
        private String strScript;//設定server上所連結執行的php檔案
        private String strData;//POST或GET方法所附帶的輸入參數

        //類別建構子
        public HttpRequestAsyncTask_CreateMember(Context pcontext, String pIP, String pPort,
                                          String pScript, String pValue) {
            //起始化類別資料
            this.context = pcontext;
            this.strIP = pIP;
            this.strPort = pPort;
            this.strScript = pScript;
            this.strData = pValue;
        }//end HttpRequestAsncTask()
        @Override
        protected String doInBackground(Void... params) {
            try {//嘗試連線至server
                //連線設定
                URL ur1 = new URL("http://" + strIP + ":" + strPort + "/" + strScript);
                HttpURLConnection conn = (HttpURLConnection) ur1.openConnection();
                conn.setReadTimeout(15000/*millisecond*/);
                conn.setConnectTimeout(15000/*milliseconds*/);
                conn.setRequestMethod("POST");//使用POST的方法
                conn.setDoInput(true);//設可以接收模式
                conn.setDoOutput(true);//設可以輸出模式
                //建立輸出流
                OutputStream os = conn.getOutputStream();
                //建立輸出緩衝器
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os,"UTF-8"));
                writer.write(strData);//輸出資料
                writer.flush();//清空輸出的資料
                writer.close();//關閉輸出緩衝器
                os.close();//關閉輸出流
                int responseCode = conn.getResponseCode();//取得連線回應
                if (responseCode == HttpURLConnection.HTTP_OK){//連線回應正常
                    InputStream is = conn.getInputStream();//建立輸入流
                    //建立輸入緩衝器
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(is));
                    StringBuffer sb = new StringBuffer("");//宣告與起始化輸入資料接收
                    String line = "";//每行輸入資料存入

                    while ((line = in.readLine()) != null){//讀取每行輸入資料
                        sb.append(line);//每行輸入資料存入
                        break;
                    }//end while
                    in.close();//關閉輸入流
                    return sb.toString();//回傳所接收的輸入資料
                }else{//連線回應不正常
                    return new String("false  :  " + responseCode);
                }//end if .....else
            }catch (Exception e){
                //連線有問題時(如連不上)
                return new String("Exception:  " + e.getMessage());
            }//end try....catch
            //return null;
        }//end doInBackground()
        @Override
        protected void onPreExecute() {
            loading = ProgressDialog.show(this.context, "Please Wait...." , null,true,true);
            // super.onPreExecute();
        }//end onPreExecute()
        @Override
        protected void onPostExecute(String G) {
            loading.dismiss();

            Log.v("Bt",G);
            if(G.contains("success"))
            {
                Toast.makeText(this.context,"恭喜你申請成功!請重新登入~",
                        Toast.LENGTH_SHORT).show();
                finish();

            }else if (G.contains("same"))
            {
                Toast.makeText(this.context,"ㄜ..這支手機有被申請過喔～",
                        Toast.LENGTH_SHORT).show();
            }

        }//end onPostExecute()
    }//end class HttpRequestAsyncTask



    //The button click event end


}
