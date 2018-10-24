package com.example.manhvd.noteapp.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.manhvd.noteapp.Helper;
import com.example.manhvd.noteapp.R;
import com.example.manhvd.noteapp.database.DatabaseNote;
import com.example.manhvd.noteapp.dialog.InsertPictureDialog;
import com.example.manhvd.noteapp.model.Note;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String NEED_REFRESH = "needRefresh";
    public static final String DATA = "data";
    public static final int CAMERA_REQUEST_CODE = 1234;
    public static final int STORAGE_REQUEST_CODE = 1111;
//    public static final int CAMERA_STORAGE_PERMISSION = 1996;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    public static final String POSITION = "position";

    private Toolbar mToolbar;
    private ImageView mImgDetailBack, mImgDetailCamera, mImgDetailColor, mImgDetailCheck;
    private TextView mTvDetailTitle;

    private EditText mEdtDetailTitle, mEdtDetailBody;
    private TextView mTvDetailTime;

    private ImageView mImgDetailBottomBack, mImgDetailBottomShare, mImgDetailBottomDel, mImgDetailBottomNext;
    private ImageView mImgDetailBody;
    private Context mContext;
    private Boolean isRefresh;

    private int id, position, count;
    private String mTitle, mBody, mTime;
    private String timeUpdate, timeFullUpdate;
    private DatabaseNote mDatabaseNote;
    private InsertPictureDialog pictureDialog;
    Note mNote;
    private List<Note> noteList;

    private String mPathImage;
    private ShareActionProvider mShareActionProvider;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        initToolbar();
        initView();
        noteList = new ArrayList<Note>();
        getData();
        mDatabaseNote = new DatabaseNote(this);
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toobar_detail_note);
        setSupportActionBar(mToolbar);
        ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setCustomView(R.layout.toolbar_note_add);
            ab.setDisplayShowTitleEnabled(false);
            ab.setDisplayShowCustomEnabled(true);

            mImgDetailBack = (ImageView) mToolbar.findViewById(R.id.img_back);
            mImgDetailBack.setOnClickListener(this);
            mTvDetailTitle = (TextView) mToolbar.findViewById(R.id.tv_title_note);
            mImgDetailCamera = (ImageView) mToolbar.findViewById(R.id.img_camera);
            mImgDetailCamera.setOnClickListener(this);
            mImgDetailColor = (ImageView) mToolbar.findViewById(R.id.img_choose);
            mImgDetailColor.setOnClickListener(this);
            mImgDetailCheck = (ImageView) mToolbar.findViewById(R.id.img_check);
            mImgDetailCheck.setOnClickListener(this);
        }
    }

    private void initView() {
        mImgDetailBody = (ImageView) findViewById(R.id.img_detail_note);
        mTvDetailTime = (TextView) findViewById(R.id.tv_detail_date_note);
        mEdtDetailTitle = (EditText) findViewById(R.id.edt_detail_title_note);
        mEdtDetailBody = (EditText) findViewById(R.id.edt_detail_body_note);
        mEdtDetailTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(mEdtDetailTitle.getText().length() == 0) {
                    mTvDetailTitle.setText(getString(R.string.main_toolbar_text_title));
                } else {
                    mTvDetailTitle.setText(mEdtDetailTitle.getText().toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mImgDetailBottomBack = (ImageView) findViewById(R.id.img_bottom_note_back);
        mImgDetailBottomBack.setOnClickListener(this);
        mImgDetailBottomShare = (ImageView) findViewById(R.id.img_bottom_note_share);
        mImgDetailBottomShare.setOnClickListener(this);
        mImgDetailBottomDel = (ImageView) findViewById(R.id.img_bottom_note_delete);
        mImgDetailBottomDel.setOnClickListener(this);
        mImgDetailBottomNext = (ImageView) findViewById(R.id.img_bottom_note_next);
        mImgDetailBottomNext.setOnClickListener(this);
    }

    private void getData() {
        Bundle bundle = getIntent().getBundleExtra(Helper.sendDataByMain.NOTE);
//        mNote = (Note) bundle.getSerializable(Helper.sendDataByMain.NOTE_DATA);
        mNote = (Note) bundle.getParcelable(Helper.sendDataByMain.NOTE_DATA);
        noteList = bundle.getParcelableArrayList(Helper.sendDataByMain.NOTE_LIST);
        position = bundle.getInt(POSITION);
        setData();
    }

    private void setData() {
        mTitle = String.valueOf(mNote.getTitle());
        mBody = String.valueOf(mNote.getBody());
        mTime = String.valueOf(mNote.getTimeFull());

        mTvDetailTitle.setText(mTitle);
        mTvDetailTime.setText(mTime);
        mEdtDetailTitle.setText(mTitle);
        mEdtDetailBody.setText(mBody);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_new:
                Intent intent = new Intent(DetailActivity.this, AddActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                if(mEdtDetailTitle.getText().toString().equals(mTitle) && mEdtDetailBody.getText().toString().equals(mBody)) {
                    this.onBackPressed();
                } else {
                    updateNoteIntoDatabase();
                    this.isRefresh = true;
                    Intent intent = new Intent(DetailActivity.this, MainActivity.class);
                    Helper.hideKeyboard(this);
                    startActivity(intent);
                }
                break;

            case R.id.img_camera:
                showDialog();
                break;

            case R.id.img_choose:
                //TODO
                break;

            case R.id.img_check:
                updateNoteIntoDatabase();
                this.isRefresh = true;
                Intent intent = new Intent(DetailActivity.this, MainActivity.class);
                Helper.hideKeyboard(this);
                startActivity(intent);
                break;

            case R.id.img_bottom_note_back:
                count =noteList.size()-1;
                position--;
                if(position >= 0 && position <= count) {
                    mNote = noteList.get(position);
                    setData();
                } else {
                    mImgDetailBottomBack.setClickable(false);
                }
                break;

            case R.id.img_bottom_note_share:
                shareFuction();
                break;

            case R.id.img_bottom_note_delete:
                showDeleteDialog();
                break;

            case R.id.img_bottom_note_next:
                count = noteList.size()-1;
                position++;
                if(position <= count && position >= 0) {
                    mNote = noteList.get(position);
                    setData();
                } else {
                    mImgDetailBottomNext.setClickable(false);
                }
                break;

            default:
                break;
        }
    }

    private void shareFuction() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);

        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, mEdtDetailTitle.getText().toString());
//        shareIntent.putExtra(Intent.EXTRA_TEXT, mEdtDetailTitle.getText().toString());
        shareIntent.putExtra(Intent.EXTRA_TEXT, mEdtDetailBody.getText().toString());
        startActivity(Intent.createChooser(shareIntent, "Share with"));
    }

    private void updateNoteIntoDatabase() {
        String title = mEdtDetailTitle.getText().toString();
        if(title.length() == 0 || title.isEmpty()) {
            title = getString( R.string.detail_text_no_title);
        }
        String body = mEdtDetailBody.getText().toString();
        this.mNote.setTitle(title);
        this.mNote.setBody(body);
        this.mNote.setTime(Helper.setTime(timeUpdate));
        this.mNote.setTimeFull(Helper.setTimeFull(timeFullUpdate));
        mDatabaseNote.update(mNote);
    }

    private void showDeleteDialog() {
//        ContextThemeWrapper themeWrapper = new ContextThemeWrapper(this, R.style.Theme_AlertDialog);
        final AlertDialog.Builder deleteDialogBuilder = new AlertDialog.Builder(this);
        deleteDialogBuilder.setTitle(getString(R.string.text_title_delete));
        deleteDialogBuilder.setMessage(getString(R.string.text_message_delete));
        deleteDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                delete(mNote);
                Intent intent = new Intent(DetailActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        deleteDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        deleteDialogBuilder.show();
    }

    private void delete(Note note) {
        DatabaseNote databaseNote = new DatabaseNote(this);
        databaseNote.deleteNote(note);
        this.isRefresh = true;
    }

    private void showDialog() {
        pictureDialog = new InsertPictureDialog(this);

        pictureDialog.setSelectLisener(new InsertPictureDialog.OnOptionListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onSelectFromGalley() {
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                } else {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
                }
            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onSelectTakePicture() {
                Intent photoPickerIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                photoPickerIntent.addCategory(Intent.CATEGORY_OPENABLE);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, STORAGE_REQUEST_CODE);
            }
        });
        pictureDialog.show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }

        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if(requestCode == CAMERA_REQUEST_CODE) {
                Bitmap photo = (Bitmap) data.getExtras().get(DATA);
                mImgDetailBody.setImageBitmap(photo);
                mImgDetailBody.setVisibility(View.VISIBLE);
            } else if(requestCode == STORAGE_REQUEST_CODE ) {
                Uri uri = null;
                if(data != null) {
                    uri = data.getData();
                    mPathImage = String.valueOf(uri);
                    setImageNote(uri);
                }
            }
        }
    }

    private void setImageNote(Uri image) {
        Glide.with(this)
                .load(image)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(mImgDetailBody);
        mImgDetailBody.setVisibility(View.VISIBLE);
    }

    @Override
    public void finish() {
        Intent data = new Intent();
        data.putExtra(NEED_REFRESH, isRefresh);
        this.setResult(Activity.RESULT_OK, data);
        super.finish();
    }
}
