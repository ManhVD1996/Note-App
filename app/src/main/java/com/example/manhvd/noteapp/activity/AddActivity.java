package com.example.manhvd.noteapp.activity;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.manhvd.noteapp.Helper;
import com.example.manhvd.noteapp.R;
import com.example.manhvd.noteapp.database.DatabaseNote;
import com.example.manhvd.noteapp.dialog.ChooseColorDialog;
import com.example.manhvd.noteapp.dialog.DateTimePickerDialog;
import com.example.manhvd.noteapp.dialog.InsertPictureDialog;
import com.example.manhvd.noteapp.model.Note;
import com.example.manhvd.noteapp.utils.view.CustomLine;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = "ManhVD1996";
    public static final String NEED_REFRESH = "needRefresh";
    public static final String DATA = "data";
    public static final int CAMERA_REQUEST_CODE = 1234;
    public static final int STORAGE_REQUEST_CODE = 1111;
//    public static final int CAMERA_STORAGE_PERMISSION = 1996;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    private RelativeLayout mLayout;
    private Toolbar mToolbar;
    private ImageView mImgBack, mImgChooseColor, mImgCamera, mImgCheck;
    private ImageView mImgNoteBody;
    private TextView mTvDate, mTvAlarm, mTvTitleNote;
    private CustomLine mEditTitle, mEditNote;
    private Spinner mSpinDay, mSpinTime;
    private ImageView mImgClose;

    private  ArrayAdapter<String> adapterDay;
    private ArrayAdapter<String> adapterTime;

    private Handler mHandler;
    private DatabaseNote databaseNote;
    private boolean needRefresh;
    private String mTimeItem, mTimeItemFull;
    private InsertPictureDialog pictureDialog;
    private ChooseColorDialog colorDialog;

    private String mCodeColor;
    private String mPathImage;
    private List<String> dayList;
    private List<String> timeList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_add);

        initToolbar();
        mHandler = new Handler();

        initView();
        setTimeDetail();
        setTimeItem();
        initSpin();
        mCodeColor = Helper.Color.COLOR_DEFAULT;
        databaseNote = new DatabaseNote(this);

    }

    private void initSpin() {
        mSpinDay = (Spinner) findViewById(R.id.spin_day);
        mSpinDay.setOnItemSelectedListener(this);
        mSpinTime = (Spinner) findViewById(R.id.spin_time);
        mSpinTime.setOnItemSelectedListener(this);
        mImgClose = (ImageView) findViewById(R.id.img_close);
        mImgClose.setOnClickListener(this);

        dayList = new ArrayList<>();
        dayList.add("Today");
        dayList.add("Tomorrow");
        dayList.add("Next thứ 5");
        dayList.add("Other...");
        adapterDay = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dayList);
        adapterDay.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        mSpinDay.setAdapter(adapterDay);

        timeList = new ArrayList<>();
        timeList.add("09:00");
        timeList.add("13:00");
        timeList.add("17:00");
        timeList.add("20:00");
        timeList.add("Other");
        adapterTime = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, timeList);
        adapterTime.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        mSpinTime.setAdapter(adapterTime);

        mSpinDay.setVisibility(View.GONE);
        mSpinTime.setVisibility(View.GONE);
        mImgClose.setVisibility(View.GONE);

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
        mLayout = (RelativeLayout) findViewById(R.id.layout_add);
        mImgNoteBody = (ImageView) findViewById(R.id.img_note);
        mImgNoteBody.setOnClickListener(this);
        mTvDate = (TextView) findViewById(R.id.tv_detail_date);

        mEditTitle = (CustomLine) findViewById(R.id.edt_detail_title);
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

        mEditNote = (CustomLine) findViewById(R.id.edt_detail_note);
        mTvAlarm = (TextView) findViewById(R.id.tv_button_alarm);
        mTvAlarm.setOnClickListener(this);
        mTvAlarm.setVisibility(View.VISIBLE);
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
                showChangeColorDialog();
                break;
            case R.id.img_check:
                addNoteIntoDatabase();
                this.needRefresh = true;
                Intent intent1 = new Intent(AddActivity.this, MainActivity.class);
                Helper.hideKeyboard(this);
                startActivity(intent1);
                break;

            case R.id.tv_button_alarm:
                mTvAlarm.setVisibility(View.GONE);
                mSpinDay.setVisibility(View.VISIBLE);
                mSpinTime.setVisibility(View.VISIBLE);
                mImgClose.setVisibility(View.VISIBLE);
                break;

            case R.id.img_close:
                mTvAlarm.setVisibility(View.VISIBLE);
                mSpinDay.setVisibility(View.GONE);
                mSpinTime.setVisibility(View.GONE);
                mImgClose.setVisibility(View.GONE);
                break;

            default:
                break;
        }
    }

    private void showChangeColorDialog() {
        colorDialog = new ChooseColorDialog(this);
        colorDialog.setmChangeBackground(new ChooseColorDialog.ChangeBackground() {
            @Override
            public void changeColorWhile() {
                mLayout.setBackgroundColor(Color.parseColor(Helper.Color.WHILE));
                mCodeColor = Helper.Color.WHILE;
            }

            @Override
            public void changeColorOrange() {
                mLayout.setBackgroundColor(Color.parseColor(Helper.Color.ORANGE));
                mCodeColor = Helper.Color.ORANGE;
            }

            @Override
            public void changeColorGreen() {
                mLayout.setBackgroundColor(Color.parseColor(Helper.Color.GREEN));
                mCodeColor = Helper.Color.GREEN;
            }

            @Override
            public void changeColorBlue() {
                mLayout.setBackgroundColor(Color.parseColor(Helper.Color.BLUE));
                mCodeColor = Helper.Color.BLUE;
            }
        });
        colorDialog.show();
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
        Note note = new Note(title, body, mTimeItem, mTimeItemFull, mCodeColor);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
        switch (parent.getId()) {
            case R.id.spin_day:
                if(position == parent.getCount()-1) {
                    DateTimePickerDialog.showDateDialog(this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            String datePicker = String.valueOf(dayOfMonth) + "/" + String.valueOf((month+1)) + "/" + String.valueOf(year);
                            dayList.set(position, datePicker);
                            adapterDay.notifyDataSetChanged();
                        }
                    });

                }
                break;

            case R.id.spin_time:
                if(position == parent.getCount()-1) {
                    DateTimePickerDialog.showTimeDialog(this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            String timePicker = String.valueOf(hourOfDay) + ":" + String.valueOf(minute);
                            timeList.set(position, timePicker);
                            adapterTime.notifyDataSetChanged();
                        }
                    });
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}


