package com.example.labsmanage;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public class LoginActivity extends AppCompatActivity {

    private Spinner userSpinner=null;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViews();
    }

    private void findViews() {
        //Spinner
        userSpinner=(Spinner)findViewById(R.id.spin_1);
        String[] usertype={"学生登录","教师登录"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,usertype);
        userSpinner.setAdapter(adapter);

        //EditText
        final EditText musername=(EditText) findViewById(R.id.edit_text_1);
        final EditText mpassword=(EditText) findViewById(R.id.edit_text_2);
        //button
        Button button1 = (Button) findViewById(R.id.button_1);
        WorkThread wt=new WorkThread();
        wt.start();
        button1.setOnClickListener(v -> {
            String ut = userSpinner.getSelectedItem().toString();
            String no = musername.getText().toString().trim();
            String pw = mpassword.getText().toString().trim();
            Message m=handler.obtainMessage();
            Bundle b=new Bundle();
            b.putString("name",no);
            b.putString("pass",pw);
            b.putString("ut",ut);
            m.setData(b);
            handler.sendMessage(m);
        });


        // status bar
        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.white));  // status bar 颜色
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);  // status bar 文字颜色
        // 隐藏标题栏action bar
        ActionBar actionbar= getSupportActionBar();
        if(actionbar!=null){
            actionbar.hide();
        }
    }

    //登录查询
    class WorkThread extends Thread{
        @SuppressLint("HandlerLeak")
        @Override
        public void run(){
            Looper.prepare();
            handler=new Handler(){
                @Override
                public void handleMessage(Message m){
                    super.handleMessage(m);
                    Bundle b= m.getData();//得到与信息对用的Bundle
                    String name=b.getString("name");//根据键取值
                    String pass=b.getString("pass");
                    String ut=b.getString("ut");
                    DBUtil db=new DBUtil();//调用数据库查询类
                    String ret=db.QuerySQL(name,pass,ut);//得到返回值
                    if (ret.equals("1"))//为1，页面跳转，登陆成功
                    {
                        Intent localIntent = new Intent();
                        localIntent.setClass(LoginActivity.this, MainActivity.class);
                        LoginActivity.this.startActivity(localIntent);
                        Toast.makeText(LoginActivity.this, "登录成功",Toast.LENGTH_SHORT).show();//显示提示框
                        return;
                    }
                    Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                }
            };
            Looper.loop();//Looper循环，通道中有数据执行，无数据堵塞
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}

