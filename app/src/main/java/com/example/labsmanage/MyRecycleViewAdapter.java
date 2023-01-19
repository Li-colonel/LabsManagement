package com.example.labsmanage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyRecycleViewAdapter extends RecyclerView.Adapter<MyRecycleViewAdapter.MyHolder> {

    private final List<Equi> mList;//数据源

    MyRecycleViewAdapter(List<Equi> list) {
        mList = list;
    }

    /**
     * 自定义的ViewHolder
     * 在这里注册了view
     */
    public static class MyHolder extends RecyclerView.ViewHolder {
        TextView equi_id;
        TextView equi_lab;
        TextView equi_type;
        TextView equi_state;

        public MyHolder(View itemView) {
            super(itemView);
            // Define click listener for the ViewHolder's View

            equi_id = itemView.findViewById(R.id.equi_id);
            equi_lab = itemView.findViewById(R.id.equi_lab);
            equi_type = itemView.findViewById(R.id.equi_type);
            equi_state = itemView.findViewById(R.id.equi_state);
        }
    }

    //创建ViewHolder并返回，后续item布局里控件都是从ViewHolder中取出
    @Override
    @NonNull
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //将我们自定义的item布局转换为View
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_equi_item, parent, false);
        //将view传递给我们自定义的ViewHolder
        //返回这个MyHolder实体
        return new MyHolder(view);
    }

    //通过方法提供的ViewHolder，将数据绑定到ViewHolder中
    @Override
    public void onBindViewHolder(MyHolder holder, int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        Equi equi = mList.get(position);
        holder.equi_id.setText(equi.getID());
        holder.equi_lab.setText(equi.getLab());
        holder.equi_type.setText(equi.getType());
        holder.equi_state.setText(equi.getState());
    }

    //获取数据源总的条数
    @Override
    public int getItemCount() {
        return mList.size();
    }
}
