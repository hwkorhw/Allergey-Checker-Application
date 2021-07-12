package com.example.allergyprevention;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private ArrayList<item> mList;
    private LayoutInflater mInflate;
    private Context mContext;
    private static OnItemClickListener mListener = null ;

    public MyAdapter(Context context, ArrayList<item> itmes) {
        this.mList = itmes;
        this.mInflate = LayoutInflater.from(context);
        this.mContext = context;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position) ;
    }

    // OnItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener ;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflate.inflate(R.layout.item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //binding
        holder.name_t.setText(mList.get(position).name);
        holder.nutrite_t.setText(mList.get(position).nutrite);
        holder.allergy_t.setText(mList.get(position).allergy);

        //Click event
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public item getItem(int p){
        return mList.get(p);
    }
    //ViewHolder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public Button name_t;
        public TextView nutrite_t;
        public TextView allergy_t;

        public MyViewHolder(View itemView) {
            super(itemView);

            name_t = itemView.findViewById(R.id.name);
            nutrite_t = itemView.findViewById(R.id.nutrite);
            allergy_t = itemView.findViewById(R.id.allergy);

            name_t.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if(position != RecyclerView.NO_POSITION){
                        if(mListener !=null){
                            mListener.onItemClick(v,position);
                        }
                    }

                }
            });
        }
    }

}