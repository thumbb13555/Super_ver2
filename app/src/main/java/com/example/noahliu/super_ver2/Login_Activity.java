package com.example.noahliu.super_ver2;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

public class Login_Activity extends AppCompatActivity {

    private static final String SERVERIP = "120.109.18.143";
    private static final String PORTNO = "";
    private static final String PHPSCRIPT = "/joomla/fatty/Login.php";
    private String strIP;//server的ip
    private String strPort;//server所開通的port，一般為80
    private String strScript;//設定server上所連結執行的php檔案
    private String strData;//POST或GET方法所附帶的輸入參數
    EditText edIDNumber,edPassWord;
    Button btLogin2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);

        edIDNumber = (EditText) findViewById(R.id.edID);
        edPassWord = (EditText) findViewById(R.id.edPas);
        btLogin2   = (Button)   findViewById(R.id.btLog2);
    }//End OnCreate
    public void sendScore(View view){
        String strID = edIDNumber.getText().toString();
        String strPassword = edPassWord.getText().toString();
        String strData ="Account=" + strID + "&Password=" + strPassword;

        if (strID.length() > 0 && strPassword.length() > 0){
            new HttpRequestAsyncTask_login(this,SERVERIP,PORTNO,PHPSCRIPT,strData).execute();
        }else {
            Toast.makeText(this,"好像少了點什麼?",Toast.LENGTH_SHORT).show();
        }//end if
    }//end sendScore()
    public class HttpRequestAsyncTask_login extends AsyncTask<Void,Void,String> {
        private Context context;//指向呼叫此類別的Activity
        ProgressDialog loading;//設定程序進度指示器
        private String strIP;//server的ip
        private String strPort;//server所開通的port，一般為80
        private String strScript;//設定server上所連結執行的php檔案
        private String strData;//POST或GET方法所附帶的輸入參數

        //類別建構子
        public HttpRequestAsyncTask_login(Context pcontext, String pIP, String pPort,
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
        protected void onPostExecute(String l) {
            loading.dismiss();
            Log.v("Bt",l);
            if (l.contains("login_error"))
            {
                Toast.makeText(this.context,"登入失敗", Toast.LENGTH_SHORT).show();

            }else
            {
                String str = "已登入";
                Toast.makeText(this.context,"歡迎！ "+l, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Login_Activity.this,MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("Login_OK",l);
                intent.putExtras(bundle);
                Log.v("BT","The login has been successful"+l);
                startActivity(intent);
                finish();
                Welcome_Activity.closefromlogin.finish();//關閉第一個Activity
            }
        }//end onPostExecute()
    }//end class HttpRequestAsyncTask



    //The button click event end

}
