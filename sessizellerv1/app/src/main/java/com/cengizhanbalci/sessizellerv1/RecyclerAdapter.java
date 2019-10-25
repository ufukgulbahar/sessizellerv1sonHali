package com.cengizhanbalci.sessizellerv1;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private Context context;
    private List<String> list;
    public RecyclerAdapter(List<String> list, Context context){

        this.list = list;
        this.context = context;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {

        TextView textView = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.textview_layout,parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(textView,context);


        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int i) {

        viewHolder.name.setText(list.get(i));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        Context context;
        TextView name;
        public MyViewHolder( TextView itemView,Context context) {
                super(itemView);
                name = itemView;
                this.context = context;
                itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context,MainActivity.class);
            intent.putExtra("fragmentkey",3);
            intent.putExtra("key",name.getText().toString());
            context.startActivity(intent);
        }
    }

    public void updateList(List<String> newList){
        list = new ArrayList<>();
        list.addAll(newList);
        notifyDataSetChanged();
    }

}
