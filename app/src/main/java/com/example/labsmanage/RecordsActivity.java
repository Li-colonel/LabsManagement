package com.example.labsmanage;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RecordsActivity extends AppCompatActivity {

    private final List<Record> recordList = new ArrayList<>();
    Adapter3 recordAdapter = new Adapter3(recordList);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);

        // toolBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //recycleView
        upList();
        RecyclerView recyclerView = findViewById(R.id.list_record);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(recordAdapter);
        recordAdapter.notifyDataSetChanged();


        //swipeRefreshLayout
        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.royalblue);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Here refresh
                upList();
                Toast.makeText(RecordsActivity.this, "刷新了", Toast.LENGTH_SHORT).show();//显示提示框
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    // tool bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_records, menu);
        return true;
    }

    // tool bar点击事件监听
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            //finish()
            Intent localIntent = new Intent();
            localIntent.setClass(RecordsActivity.this, MainActivity.class);
            RecordsActivity.this.startActivity(localIntent);
            return true;
        } else if (item.getItemId() == R.id.more) {
            initDialog();
            return true;
        } else if (item.getItemId() == R.id.more2) {
            initDialog2();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void initDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 获取布局
        View view = View.inflate(RecordsActivity.this, R.layout.dialog_records_insert, null);

        // 获取布局中的控件
        final EditText R_ID = view.findViewById(R.id.R_id);
        final EditText R_type = view.findViewById(R.id.R_type);
        final EditText R_date = view.findViewById(R.id.R_date);
        final Button btn = view.findViewById(R.id.btn_insert);

        // 设置参数
        builder.setTitle("添加记录").setView(view);

        // 创建对话框
        builder.create();
        final AlertDialog dia = builder.show();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String r_ID = R_ID.getText().toString().trim();
                String r_type = R_type.getText().toString().trim();
                String r_date = R_date.getText().toString().trim();

                new Thread() {
                    @Override
                    public void run() {
                        try {
                            DBUtil db = new DBUtil();
                            db.commonSQL("INSERT INTO records VALUES ( '" + r_ID + "','" + r_type + "','" + r_date + "');");
                            db.commonSQL("UPDATE equipments SET status ='" + r_type + "' WHERE equiID='" + r_ID + "';");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                Toast.makeText(RecordsActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                dia.dismiss();
            }
        });
    }

    private void initDialog2() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 获取布局
        View view = View.inflate(RecordsActivity.this, R.layout.dialog_records_produce, null);

        // 获取布局中的控件
        final EditText Lab_ID = view.findViewById(R.id.lab_id);
        final EditText Equi_type = view.findViewById(R.id.equi_type);
        final TextView numRes = view.findViewById(R.id.records_result);
        final Button btn = view.findViewById(R.id.btn_produce);


        // 设置参数
        builder.setTitle("借出统计").setView(view);

        // 创建对话框
        builder.create();
        builder.show();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lab_ID = Lab_ID.getText().toString().trim();
                String equi_type = Equi_type.getText().toString().trim();

                new Thread() {
                    @Override
                    public void run() {
                        try {
                            DBUtil db = new DBUtil();
                            int res = db.produceSQL("CALL LABM.getRecords('" + lab_ID + "','" + equi_type + "');");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    numRes.setText(String.valueOf(res));
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                Toast.makeText(RecordsActivity.this, "统计成功", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 更新recyclerview数据
    private void upList() {
        new Thread() {
            @Override
            public void run() {
                try {
                    recordList.clear();
                    DBUtil db = new DBUtil();
                    db.getSQLConnection();
                    String sql;
                    sql = "SELECT * FROM records;";
                    recordList.addAll(db.getForList(Record.class, sql));
                    runOnUiThread(new Runnable() {// 返回主线程，这样fresh才有效
                        @Override
                        public void run() {
                            recordAdapter.notifyDataSetChanged();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}