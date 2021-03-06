package com.example.labsmanage;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LabActivity extends AppCompatActivity {

    private List<Lab> labList = new ArrayList<>();
    Adapter2 labAdapter = new Adapter2(labList);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab);

        // toolBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //recycleView
        upList();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list_lab);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(labAdapter);
        labAdapter.notifyDataSetChanged();


        //swipeRefreshLayout
        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.royalblue);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Here refresh
                upList();
                Toast.makeText(LabActivity.this, "刷新了", Toast.LENGTH_SHORT).show();//显示提示框
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    // tool bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lab, menu);
        return true;
    }

    // tool bar点击事件监听
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            //finish()
            Intent localIntent = new Intent();
            localIntent.setClass(LabActivity.this, MainActivity.class);
            LabActivity.this.startActivity(localIntent);
            return true;
        } else if (item.getItemId() == R.id.more1) {
            initDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void initDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 获取布局
        View view = View.inflate(LabActivity.this, R.layout.dialog_lab_update, null);

        // 获取布局中的控件
        final EditText LabID = (EditText) view.findViewById(R.id.lab_id);
        final EditText LabManager = (EditText) view.findViewById(R.id.lab_manager);
        final Button btn = (Button) view.findViewById(R.id.btn_update);

        // 设置参数
        builder.setTitle("实验室负责人").setView(view);

        // 创建对话框
        builder.create();
        final AlertDialog dia = builder.show();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String labID = LabID.getText().toString().trim();
                String labManager = LabManager.getText().toString().trim();
                new Thread() {
                    public void run() {
                        try {
                            DBUtil db = new DBUtil();
                            db.commonSQL("UPDATE labs SET " + "manager='" + labManager + "'WHERE labID='"
                                    + labID + "';");
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }.start();
                Toast.makeText(LabActivity.this, "查询成功", Toast.LENGTH_SHORT).show();
                dia.dismiss();
            }
        });
    }

    // 更新recyclerview数据
    private void upList() {
        new Thread() {
            public void run() {
                try {
                    labList.clear();
                    DBUtil db = new DBUtil();
                    db.getSQLConnection();
                    String sql;
                    sql = "SELECT * FROM labs;";
                    labList.addAll(db.getForList(Lab.class, sql));
                    runOnUiThread(new Runnable() {// 返回主线程，这样fresh才有效
                        @Override
                        public void run() {
                            labAdapter.notifyDataSetChanged();
                        }
                    });
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }.start();
    }
}