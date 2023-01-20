package com.example.labsmanage;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class LoginActivityN extends AppCompatActivity {

    private Spinner userSpinner = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViews();
    }

    private void findViews() {
        //Spinner
        userSpinner = findViewById(R.id.spin_1);
        String[] usertype = {"学生登录", "教师登录"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, usertype);
        userSpinner.setAdapter(adapter);

        //EditText
        final EditText musername = findViewById(R.id.edit_text_1);
        final EditText mpassword = findViewById(R.id.edit_text_2);
        //button
        Button button1 = findViewById(R.id.button_1);
        button1.setOnClickListener(v -> {
            String ut = userSpinner.getSelectedItem().toString();
            String no = musername.getText().toString().trim();
            String pw = mpassword.getText().toString().trim();
            login(no, pw, ut);
        });

        // status bar
        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));  // status bar 颜色
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);  // status bar 文字颜色
        // 隐藏标题栏action bar
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.hide();
        }
    }

    private void login(String no, String pw, String ut) {
        new Thread() {
            @Override
            public void run() {
                try {
                    DBUtil db = new DBUtil();//调用数据库查询类
                    String ret = db.LoginSQL(no, pw, ut);//得到返回值
                    if (ret.equals("1"))//为1，页面跳转，登陆成功
                    {
                        Intent localIntent = new Intent();
                        localIntent.setClass(LoginActivityN.this, MainActivity.class);
                        LoginActivityN.this.startActivity(localIntent);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivityN.this, "登录成功", Toast.LENGTH_SHORT).show();//显示提示框
                            }
                        });
                        return;
                    }
                    Toast.makeText(LoginActivityN.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


    //onKeyDown 方法复写返回键
    private long firstTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        long secondTime = System.currentTimeMillis();
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (secondTime - firstTime < 2000) {
                // system.exit()方法会让系统以异常处理，会自动重启，恢复app，finish本身只finish当前activity，弹出历史栈下一个app
                finish();
            } else {
                Toast.makeText(LoginActivityN.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                firstTime = System.currentTimeMillis();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}