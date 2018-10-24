package com.example.manhvd.noteapp.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.manhvd.noteapp.Helper;
import com.example.manhvd.noteapp.R;
import com.example.manhvd.noteapp.database.DatabaseNote;
import com.example.manhvd.noteapp.dialog.InsertPictureDialog;
import com.example.manhvd.noteapp.model.Note;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ManhVD1996";
    public static final String NEED_REFRESH = "needRefresh";
    public static final String DATA = "data";
    public static final int CAMERA_REQUEST_CODE = 1234;
    public static final int STORAGE_REQUEST_CODE = 1111;
    public static final int CAMERA_STORAGE_PERMISSION = 1996;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    private Toolbar mToolbar;
    private ImageView mImgBack, mImgChooseColor, mImgCamera, mImgCheck;
    private ImageView mImgNoteBody;
    private TextView mTvDate, mTvAlarm, mTvTitleNote;
    private EditText mEditTitle, mEditNote;
    private Handler mHandler;
    private DatabaseNote databaseNote;
    private boolean needRefresh;
    private String mTimeItem, mTimeItemFull;
    private InsertPictureDialog pictureDialog;

    private String mPathImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_add);

        initToolbar();
        mHandler = new Handler();

        initView();
        setTimeDetail();
        setTimeItem();
        databaseNote = new DatabaseNote(this);

    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toobar_detail);
        setSupportActionBar(mToolbar);
        ActionBar actionBar =  getSupportActionBar();

        if(actionBar != null) {
            actionBar.setCustomView(R.layout.toolbar_note_add);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);

            mImgBack = mToolbar.findViewById(R.id.img_back);
            mImgBack.setOnClickListener(this);
            mImgCamera = mToolbar.findViewById(R.id.img_camera);
            mImgCamera.setOnClickListener(this);
            mImgChooseColor = mToolbar.findViewById(R.id.img_choose);
            mImgChooseColor.setOnClickListener(this);
            mImgCheck = mToolbar.findViewById(R.id.img_check);
            mImgCheck.setOnClickListener(this);
            mTvTitleNote = mToolbar.findViewById(R.id.tv_title_note);
        }
    }

    private void initView() {
        mImgNoteBody = (ImageView) findViewById(R.id.img_note);
        mImgNoteBody.setOnClickListener(this);
        mTvDate = (TextView) findViewById(R.id.tv_detail_date);

        mEditTitle = (EditText) findViewById(R.id.edt_detail_title);
        mEditTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(mEditTitle.getText().length() == 0) {
                    mTvTitleNote.setText(getString(R.string.main_toolbar_text_title));
                } else {
                    mTvTitleNote.setText(mEditTitle.getText().toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mEditNote = (EditText) findViewById(R.id.edt_detail_note);
        mTvAlarm = (TextView) findViewById(R.id.tv_button_alarm);
        mTvAlarm.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                if(mEditTitle.length() != 0 || mEditNote.length() != 0) {
                    addNoteIntoDatabase();
                    this.needRefresh = true;
                }
                Intent intent = new Intent(AddActivity.this, MainActivity.class);
                Helper.hideKeyboard(this);
                startActivity(intent);
                break;
            case R.id.img_camera:
                showDialog();
                break;
            case R.id.img_choose:
                //TODO
                break;
            case R.id.img_check:
                addNoteIntoDatabase();
                this.needRefresh = true;
                Intent intent1 = new Intent(AddActivity.this, MainActivity.class);
                Helper.hideKeyboard(this);
                startActivity(intent1);
                break;

            case R.id.tv_button_alarm:
                //TODO
                break;
            default:
                break;
        }
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
//                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
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
                mImgNoteBody.setImageBitmap(photo);
                mImgNoteBody.setVisibility(View.VISIBLE);
            } else if(requestCode == STORAGE_REQUEST_CODE ) {
                Uri uri = null;
                if(data != null) {
                    uri = data.getData();
                    mPathImage = String.valueOf(uri);
//                    Toast.makeText(this, mPathImage, Toast.LENGTH_SHORT).show();
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
                .into(mImgNoteBody);
        mImgNoteBody.setVisibility(View.VISIBLE);
    }

    private void setTimeDetail() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat timeFormat = null;
        String strDateFormat = getString(R.string.format_time_detail);
        timeFormat = new SimpleDateFormat(strDateFormat);
        mTimeItemFull = timeFormat.format(calendar.getTime());
        mTvDate.setText(mTimeItemFull);
    }

    private void setTimeItem() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat timeFormat = null;
        String format = getString(R.string.format_time_item);
        timeFormat = new SimpleDateFormat(format);
        mTimeItem = timeFormat.format(calendar.getTime());
    }

    private void addNoteIntoDatabase() {
        String title = mEditTitle.getText().toString();
        if(title.length() == 0 || title.isEmpty()) {
            title = getString( R.string.detail_text_no_title);
        }
        String body = mEditNote.getText().toString();
        Note note = new Note(title, body, mTimeItem, mTimeItemFull);
        databaseNote.addNote(note);

        Log.d(TAG, getString(R.string.log_tag_detail));
    }

    @Override
    public void finish() {
        Intent data = new Intent();
        data.putExtra(NEED_REFRESH, needRefresh);
        this.setResult(Activity.RESULT_OK, data);
        super.finish();
    }
}


