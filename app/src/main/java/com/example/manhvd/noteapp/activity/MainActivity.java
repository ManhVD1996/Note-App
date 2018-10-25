package com.example.manhvd.noteapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.manhvd.noteapp.Helper;
import com.example.manhvd.noteapp.R;
import com.example.manhvd.noteapp.adapter.NoteAdapter;
import com.example.manhvd.noteapp.database.DatabaseNote;
import com.example.manhvd.noteapp.model.Note;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NoteAdapter.OnItemNoteClickListener {

    private Toolbar mToolbar;
    private View mNoNote;
    private RecyclerView mListNote;
    private CardView mCardView;
    private NoteAdapter adapter;
    private ImageView mImgAdd;
    private Handler mHandler;
    private List<Note> list;
    private DatabaseNote databaseNote;

    public static final int REQUEST_CODE = 0x1996;
    public static final String POSITION = "position";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();
        mHandler = new Handler();
        initView();
        loadData();
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();

        if(actionBar != null) {
            actionBar.setCustomView(R.layout.toolbar_main_layout);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);

            mImgAdd = mToolbar.findViewById(R.id.img_add);
            mImgAdd.setOnClickListener(this);
        }
    }

    private void initView() {
        mNoNote = findViewById(R.id.no_note);
        mCardView = (CardView) findViewById(R.id.card_view);
        mListNote = (RecyclerView) findViewById(R.id.list_notes);
        mListNote.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        mListNote.setLayoutManager(layoutManager);

        list = new ArrayList<>();

        adapter = new NoteAdapter(list, MainActivity.this, this);
        mListNote.setAdapter(adapter);
    }

    @Override
    public void onItemNoteClick(View view, int position) {
        databaseNote = new DatabaseNote(this);
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        int pos = position;
        Note note = databaseNote.getNoteById(list.get(position).getId());
        Bundle bundle = new Bundle();
        bundle.putParcelable(Helper.sendDataByMain.NOTE_DATA, note);
        bundle.putParcelableArrayList(Helper.sendDataByMain.NOTE_LIST, (ArrayList<? extends Parcelable>) list);
        bundle.putInt(POSITION, pos);
//        bundle.putSerializable(Helper.sendDataByMain.NOTE_DATA, note);
        intent.putExtra(Helper.sendDataByMain.NOTE, bundle);
        this.startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_add:
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                this.startActivityForResult(intent, REQUEST_CODE);
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            boolean needRefresh = data.getBooleanExtra(Helper.sendDataByMain.NEED_REFRESH,true);
            if(needRefresh) {
                loadData();
            }
        }
    }

    private void loadData() {
        databaseNote = new DatabaseNote(this);
        this.list.clear();
        List<Note> noteList = databaseNote.getAllNote();
        this.list.addAll(noteList);
        this.adapter.notifyDataSetChanged();
    }

    private void checkNoContent() {
        if(list.size() == 0) {
            mNoNote.setVisibility(View.VISIBLE);
            mListNote.setVisibility(View.GONE);
        }
        if(list.size() > 0){
            mNoNote.setVisibility(View.GONE);
            mListNote.setVisibility(View.VISIBLE);
        }
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        loadData();
    }
}
