package com.example.manhvd.noteapp.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.manhvd.noteapp.R;
import com.example.manhvd.noteapp.activity.AddActivity;

public class InsertPictureDialog extends Dialog implements View.OnClickListener {

    private RelativeLayout mReChoose1, mReChoose2;
    private OnOptionListener mOnOptionListener;

    public InsertPictureDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_camera);

        initView();
    }

    private void initView() {
        mReChoose1 = (RelativeLayout) findViewById(R.id.choose_1);
        mReChoose1.setOnClickListener(this);
        mReChoose2 = (RelativeLayout) findViewById(R.id.choose_2);
        mReChoose2.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.choose_1:
                dismiss();
                if(mOnOptionListener != null) {
                    mOnOptionListener.onSelectFromGalley();
                }
                break;
            case R.id.choose_2:
                dismiss();
                if(mOnOptionListener != null) {
                    mOnOptionListener.onSelectTakePicture();
                }
                break;
        }
    }

    public void setSelectLisener(OnOptionListener lisener) {
        mOnOptionListener = lisener;
    }

    public interface OnOptionListener {
        void onSelectFromGalley();
        void onSelectTakePicture();
    }
}
