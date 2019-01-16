package com.txl.bin.txl.adapater;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.txl.bin.txl.R;
import com.txl.bin.txl.activity.DetailActivity;
import com.txl.bin.txl.bean.Contact;

import java.util.List;

public class RecyclerviewAdapter extends RecyclerView.Adapter<RecyclerviewAdapter.ViewHolder> {
    private Context context;
    private List<Contact> contacts;

    public RecyclerviewAdapter(Context context,List<Contact> data){
        this.context = context;
        this.contacts = data;
    }
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recyclerview,parent,false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.tv_name.setText(contacts.get(position).getName());
        holder.iv_head.setImageResource(R.drawable.fish);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,DetailActivity.class);
                intent.putExtra("name",contacts.get(position).getName());
                intent.putExtra("phone",contacts.get(position).getPhone());
                intent.putExtra("email",contacts.get(position).getEmail());
                context.startActivity(intent);
                ((Activity)context).finish();
            }
        });
    }
    @Override
    public int getItemCount() {
        return contacts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tv_name;
        public ImageView iv_head;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            iv_head=itemView.findViewById(R.id.iv_head);
        }

    }
}
