package com.example.labsmanage;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setActivities();
    }

    private void setActivities() {
        CardView cardAc1 = findViewById(R.id.activity1);
        CardView cardAc2 = findViewById(R.id.activity2);
        CardView cardAc3 = findViewById(R.id.activity3);
        CardView cardAc4 = findViewById(R.id.activity4);

        cardAc1.setOnClickListener(v -> {
            Intent localIntent = new Intent();
            localIntent.setClass(MainActivity.this, LabActivity.class);
            MainActivity.this.startActivity(localIntent);
        });
        cardAc2.setOnClickListener(v -> {
            Intent localIntent = new Intent();
            localIntent.setClass(MainActivity.this, EquiActivity.class);
            MainActivity.this.startActivity(localIntent);
        });
        cardAc3.setOnClickListener(v -> {
            Intent localIntent = new Intent();
            localIntent.setClass(MainActivity.this, RecordsActivity.class);
            MainActivity.this.startActivity(localIntent);
        });
        cardAc4.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "更多功能开发中...", Toast.LENGTH_SHORT).show();
//            Intent localIntent = new Intent();
//            localIntent.setClass(MainActivity.this, MoreActivity.class);
//            MainActivity.this.startActivity(localIntent);
        });

    }

    //重写返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            new AlertDialog.Builder(this)
                    .setMessage(("确认返回登陆界面？"))
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent localIntent = new Intent();
                            localIntent.setClass(MainActivity.this, LoginActivityN.class);
                            // 这句话使得A跳到B时，清空活动栈(历史栈)，在活动B返回就可finish()以退出程序
                            localIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            MainActivity.this.startActivity(localIntent);
                        }
                    })
                    .setNegativeButton("取消", null)
                    .show();
            return true;
        } else
            return super.onKeyDown(keyCode, event);
    }
}
