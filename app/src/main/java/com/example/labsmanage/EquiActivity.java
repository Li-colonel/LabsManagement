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


public class EquiActivity extends AppCompatActivity {

    private final List<Equi> equiList = new ArrayList<>();
    MyRecycleViewAdapter equiAdapter = new MyRecycleViewAdapter(equiList);
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equi);

        // toolBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //recycleView
        RecyclerView recyclerView = findViewById(R.id.list_equi);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(equiAdapter);
        equiAdapter.notifyDataSetChanged();
//        upList();


        //swipeRefreshLayout
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.royalblue);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                upList();
                Toast.makeText(EquiActivity.this, "刷新了", Toast.LENGTH_SHORT).show();//显示提示框
            }
        });
        //实现了进入页面刷新的动画
        //TODO 待推广进入页面刷新动画
        swipeRefreshLayout.setRefreshing(true);
        upList();
    }

    // tool bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_equi, menu);
        return true;
    }

    // tool bar点击事件监听
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            //finish()
            Intent localIntent = new Intent();
            localIntent.setClass(EquiActivity.this, MainActivity.class);
            EquiActivity.this.startActivity(localIntent);
            return true;
        } else if (item.getItemId() == R.id.more1) {
            initDialog();
            return true;
        } else if (item.getItemId() == R.id.more2) {
            initDialog2();
            return true;
        } else if (item.getItemId() == R.id.more3) {
            initDialog3();
            return true;
        } else if (item.getItemId() == R.id.more4) {
            initDialog4();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // 更新recyclerview数据
    private void upList() {
        new Thread() {
            @Override
            public void run() {
                try {
                    equiList.clear();
                    DBUtil db = new DBUtil();
                    db.getSQLConnection();
                    String sql;
                    sql = "SELECT * FROM equipments;";
                    equiList.addAll(db.getForList(Equi.class, sql));
                    runOnUiThread(new Runnable() {// 返回主线程，这样fresh才有效
                        @Override
                        public void run() {
                            equiAdapter.notifyDataSetChanged();
                            // 实现了进入页面刷新的动画
                            if (swipeRefreshLayout.isRefreshing()) {
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void upList(String condition) {
        new Thread() {
            @Override
            public void run() {
                try {
                    equiList.clear();
                    DBUtil db = new DBUtil();
                    db.getSQLConnection();
                    String sql;
                    sql = "SELECT * FROM equipments WHERE " + condition + ";";
                    equiList.addAll(db.getForList(Equi.class, sql));
                    runOnUiThread(new Runnable() {// 返回主线程，这样fresh才有效
                        @Override
                        public void run() {
                            equiAdapter.notifyDataSetChanged();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void initDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 获取布局
        View view = View.inflate(EquiActivity.this, R.layout.dialog_equi_query, null);

        // 获取布局中的控件
        final EditText EquiID = view.findViewById(R.id.equi_id);
        final Button btn = view.findViewById(R.id.btn_query);
        final Button btn2 = view.findViewById(R.id.btn_delete);

        // 设置参数
        builder.setTitle("查询设备").setView(view);

        // 创建对话框
        builder.create();
        final AlertDialog dia = builder.show();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String equiID = EquiID.getText().toString().trim();
                upList("equiID =" + equiID);
                Toast.makeText(EquiActivity.this, "查询成功", Toast.LENGTH_SHORT).show();
                dia.dismiss();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String equiID = EquiID.getText().toString().trim();
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            DBUtil db = new DBUtil();
                            db.commonSQL("DELETE FROM equipments WHERE equiID='" + equiID + "';");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                Toast.makeText(EquiActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                dia.dismiss();
            }
        });
    }

    private void initDialog2() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 获取布局
        View view = View.inflate(EquiActivity.this, R.layout.dialog_equi_info, null);

        // 获取布局中的控件
        final EditText EquiID = view.findViewById(R.id.equi_id);
        final EditText EquiLab = view.findViewById(R.id.equi_lab);
        final EditText EquiType = view.findViewById(R.id.equi_type);
        final EditText EquiState = view.findViewById(R.id.equi_state);
        final Button btn1 = view.findViewById(R.id.btn_insert);
        final Button btn2 = view.findViewById(R.id.btn_update);

        // 设置参数
        builder.setTitle("设备管理").setView(view);

        // 创建对话框
        builder.create();
        final AlertDialog dia = builder.show();

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String equiID = EquiID.getText().toString().trim();
                String equiLab = EquiLab.getText().toString().trim();
                String equiType = EquiType.getText().toString().trim();
                String equiState = EquiState.getText().toString().trim();

                new Thread() {
                    @Override
                    public void run() {
                        try {
                            DBUtil db = new DBUtil();
                            db.commonSQL("INSERT INTO equipments VALUES ( " + equiID + ",'" + equiLab + "','" + equiType + "','" + equiState + "');");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                dia.dismiss();
                Toast.makeText(EquiActivity.this, "新增成功", Toast.LENGTH_SHORT).show();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String equiID = EquiID.getText().toString().trim();
                String equiLab = EquiLab.getText().toString().trim();
                String equiType = EquiType.getText().toString().trim();
                String equiState = EquiState.getText().toString().trim();
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            DBUtil db = new DBUtil();
                            db.commonSQL("UPDATE equipments SET " + "labID='" + equiLab + "'," + "type='"
                                    + equiType + "'," + "status='" + equiState + "' WHERE equiID='"
                                    + equiID + "';");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                dia.dismiss();
                Toast.makeText(EquiActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initDialog3() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(EquiActivity.this, R.layout.dialog_equi_loss, null);

        builder.setTitle("报损").setView(view);
        builder.create();
        final AlertDialog dia = builder.show();

        final TextView EquiID = view.findViewById(R.id.equi_id);
        final Button btn1 = view.findViewById(R.id.btn_loss);
        final Button btn2 = view.findViewById(R.id.btn_unLoss);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String equiID = EquiID.getText().toString().trim();

                new Thread() {
                    @Override
                    public void run() {
                        try {
                            DBUtil db = new DBUtil();
                            db.commonSQL("UPDATE equipments SET " + "status='损坏'" + "WHERE equiID='"
                                    + equiID + "';");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                dia.dismiss();
                Toast.makeText(EquiActivity.this, "报损成功", Toast.LENGTH_SHORT).show();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String equiID = EquiID.getText().toString().trim();

                new Thread() {
                    @Override
                    public void run() {
                        try {
                            DBUtil db = new DBUtil();
                            db.commonSQL("UPDATE equipments SET " + "status='正常'" + "WHERE equiID='"
                                    + equiID + "';");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                dia.dismiss();
                Toast.makeText(EquiActivity.this, "恢复成功", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initDialog4() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(EquiActivity.this, R.layout.dialog_equi_view, null);

        builder.setTitle("视图演示").setView(view);
        builder.create();
        builder.show();

        final TextView Num1 = view.findViewById(R.id.view1);
        final TextView Num2 = view.findViewById(R.id.view2);
        final TextView Num3 = view.findViewById(R.id.view3);
        final TextView Num4 = view.findViewById(R.id.view4);
        new Thread() {
            @Override
            public void run() {
                try {
                    DBUtil db = new DBUtil();
                    int num1 = db.produceSQL("SELECT COUNT(*) FROM type1;");
                    int num2 = db.produceSQL("SELECT COUNT(*) FROM type2;");
                    int num3 = db.produceSQL("SELECT COUNT(*) FROM type3;");
                    int num4 = db.produceSQL("SELECT COUNT(*) FROM type4;");
                    runOnUiThread(new Runnable() {// 返回主线程更新数据
                        @Override
                        public void run() {
                            Num1.setText(String.valueOf(num1));
                            Num2.setText(String.valueOf(num2));
                            Num3.setText(String.valueOf(num3));
                            Num4.setText(String.valueOf(num4));
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
        Toast.makeText(EquiActivity.this, "SELECT COUNT VIEW", Toast.LENGTH_SHORT).show();
    }
}