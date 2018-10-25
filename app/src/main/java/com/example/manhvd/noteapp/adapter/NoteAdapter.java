package com.example.manhvd.noteapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.manhvd.noteapp.R;
import com.example.manhvd.noteapp.activity.DetailActivity;
import com.example.manhvd.noteapp.activity.MainActivity;
import com.example.manhvd.noteapp.model.Note;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Note> noteList;
//    private MainActivity mContext;
    private Context mContext;
    private OnItemNoteClickListener mOnItemNoteClickListener;

    public NoteAdapter(List<Note> noteList, Context mContext, OnItemNoteClickListener mOnItemNoteClickListener) {
        this.noteList = noteList;
        this.mContext = mContext;
        this.mOnItemNoteClickListener = mOnItemNoteClickListener;
    }

    public void setmOnItemNoteClickListener(OnItemNoteClickListener mOnItemNoteClickListener) {
        this.mOnItemNoteClickListener = mOnItemNoteClickListener;
    }

    public class NoteHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mTvItemTitle, mTvItemBody, mTvItemTime;
        CardView mCardViewItem;

        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            mTvItemTitle = (TextView) itemView.findViewById(R.id.tv_item_title);
            mTvItemBody = (TextView) itemView.findViewById(R.id.tv_item_body);
            mTvItemTime = (TextView) itemView.findViewById(R.id.tv_item_time);
            mCardViewItem = (CardView) itemView.findViewById(R.id.card_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnItemNoteClickListener.onItemNoteClick(v, this.getLayoutPosition());
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_note_item, viewGroup, false);
        return new NoteHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof NoteHolder) {
            NoteHolder noteHolder = (NoteHolder) holder;
            Note note = noteList.get(position);
            noteHolder.mTvItemTitle.setText(note.getTitle());
            noteHolder.mTvItemBody.setText(note.getBody());
            noteHolder.mTvItemTime.setText(note.getTime());
            noteHolder.mCardViewItem.setBackgroundColor(Color.parseColor(note.getColor()));
        }
    }

    @Override
    public int getItemCount() {
        return null == noteList ? 0 : noteList.size();
    }

     public interface OnItemNoteClickListener {
        void onItemNoteClick(View view, int position);
     }
}
