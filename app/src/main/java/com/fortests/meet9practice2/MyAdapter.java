package com.fortests.meet9practice2;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    List<Note> mNotepad;

    //testing? delete later
 /*   public MyAdapter() {
        mNotepad = new ArrayList<>();
        mNotepad.add(new Note("1","11111"));
        mNotepad.add(new Note("2","22222"));
        mNotepad.add(new Note("3","33333"));
    }*/

    public MyAdapter(List<Note> notepad) {
        mNotepad = notepad;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_rv, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {
        holder.mName.setText(mNotepad.get(position).getName());
        holder.mContent.setText(mNotepad.get(position).getContent());
        holder.mTime.setText(mNotepad.get(position).getTime().toString());
    }

    @Override
    public int getItemCount() {
        return mNotepad.size();
    }

    public void setNotes(List<Note> notes) {
        mNotepad = notes;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mName;
        TextView mTime;
        TextView mContent;

        public MyViewHolder(View itemView) {
            super(itemView);

            mName = itemView.findViewById(R.id.name);
            mTime = itemView.findViewById(R.id.time);
            mContent = itemView.findViewById(R.id.content);


        }
    }
}
