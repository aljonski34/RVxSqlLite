package com.example.assignment1;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;




public class CustomAdapter extends RecyclerView.Adapter <CustomAdapter.ViewHolder> {

  private  Context context;
    private ArrayList<Notes> NoteList;

   private OnItemClickListener onItemClickListener;




    public CustomAdapter(Context context,ArrayList<Notes> NoteList){

        this.context = context;
        this.NoteList = NoteList;

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
    public interface  OnItemClickListener{
        void onItemClick(int position);
    }

    @NonNull
    @Override
    public CustomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerviewadapter,parent,false);

        return new ViewHolder(view,onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
                Notes note = NoteList.get(position);
                holder.titles.setText("Title: " + note.getTitle());
                holder.notes.setText("Note: " + note.getNotes());
                holder.dates.setText("Date: " + note.getDate());
                holder.times.setText("Time: " + note.getTime());
                holder.itemView.setOnClickListener(view -> onItemClickListener.onItemClick((position)));

    }

    @Override
    public int getItemCount(){
    return NoteList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView titles,notes,dates,times;
        CardView card;

        public ViewHolder(@NonNull View itemView, final OnItemClickListener onItemClickListener) {
            super(itemView);

            titles = itemView.findViewById(R.id.title);
            notes = itemView.findViewById(R.id.note);
            dates  = itemView.findViewById(R.id.date);
            times = itemView.findViewById(R.id.time);
            card = itemView.findViewById(R.id.card);

            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (onItemClickListener != null) {
                        int position = getAdapterPosition();

                        if (position != RecyclerView.NO_POSITION) {
                            onItemClickListener.onItemClick(position);
                        }
                    }
                }
            });

            }

    }
    }





