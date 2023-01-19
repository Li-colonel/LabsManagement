package com.example.labsmanage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter3 extends RecyclerView.Adapter<Adapter3.MyHolder> {

    private final List<Record> mList;//数据源

    Adapter3(List<Record> list) {
        mList = list;
    }

    /**
     * 自定义的ViewHolder
     * 在这里注册了view
     */
    public static class MyHolder extends RecyclerView.ViewHolder {
        TextView R_id;
        TextView R_type;
        TextView R_date;

        public MyHolder(View itemView) {
            super(itemView);
            // Define click listener for the ViewHolder's View here

            R_id = itemView.findViewById(R.id.R_id);
            R_type = itemView.findViewById(R.id.R_type);
            R_date = itemView.findViewById(R.id.R_date);
        }
    }

    //创建ViewHolder并返回，后续item布局里控件都是从ViewHolder中取出
    @Override
    @NonNull
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //将我们自定义的item布局转换为View
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_record_item, parent, false);
        //将view传递给我们自定义的ViewHolder
        //并返回这个MyHolder实体
        return new MyHolder(view);
    }

    //通过方法提供的ViewHolder，将数据绑定到ViewHolder中
    @Override
    public void onBindViewHolder(MyHolder holder, int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        Record lab = mList.get(position);
        holder.R_id.setText(lab.getID());
        holder.R_type.setText(lab.getType());
        holder.R_date.setText(lab.getDate());
    }

    //获取数据源总的条数
    @Override
    public int getItemCount() {
        return mList.size();
    }
}
