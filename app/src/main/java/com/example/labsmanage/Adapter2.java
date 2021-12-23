package com.example.labsmanage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter2 extends RecyclerView.Adapter<Adapter2.MyHolder> {

    private List<Lab> mList;//数据源

    Adapter2(List<Lab> list) {
        mList = list;
    }

    /**
     * 自定义的ViewHolder
     * 在这里注册了view
     */
    public static class MyHolder extends RecyclerView.ViewHolder {
        TextView Lab_id;
        TextView Lab_manager;
        TextView Lab_numEqui;

        public MyHolder(View itemView) {
            super(itemView);
            // Define click listener for the ViewHolder's View here

            Lab_id = itemView.findViewById(R.id.lab_id);
            Lab_manager = itemView.findViewById(R.id.lab_manager);
            Lab_numEqui = itemView.findViewById(R.id.lab_equi);
        }
    }

    //创建ViewHolder并返回，后续item布局里控件都是从ViewHolder中取出
    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //将我们自定义的item布局转换为View
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_lab_item, parent, false);
        //将view传递给我们自定义的ViewHolder
        MyHolder holder = new MyHolder(view);
        //返回这个MyHolder实体
        return holder;
    }

    //通过方法提供的ViewHolder，将数据绑定到ViewHolder中
    @Override
    public void onBindViewHolder(MyHolder holder, int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        Lab lab = mList.get(position);
        holder.Lab_id.setText(lab.getID());
        holder.Lab_manager.setText(lab.getManager());
        holder.Lab_numEqui.setText(lab.getNumEqui()+"");
    }

    //获取数据源总的条数
    @Override
    public int getItemCount() {
        return mList.size();
    }
}