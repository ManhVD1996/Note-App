package com.example.manhvd.noteapp.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.example.manhvd.noteapp.R;

public class ChooseColorDialog extends Dialog implements View.OnClickListener{

    private int pos;
    private TextView mTvChooseWhite, mTvChooseOrange, mTvChooseGreen, mTvChooseBlue;
    private ChangeBackground mChangeBackground;

    public ChooseColorDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_choose_color);

        initView();
    }

    private void initView() {
        mTvChooseWhite = (TextView) findViewById(R.id.view_white);
        mTvChooseWhite.setOnClickListener(this);
        mTvChooseOrange = (TextView) findViewById(R.id.view_orange);
        mTvChooseOrange.setOnClickListener(this);
        mTvChooseGreen = (TextView) findViewById(R.id.view_green);
        mTvChooseGreen.setOnClickListener(this);
        mTvChooseBlue = (TextView) findViewById(R.id.view_blue);
        mTvChooseBlue.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_white:
                dismiss();
                if(mChangeBackground != null) {
                    mChangeBackground.changeColorWhile();
                }
                break;
            case R.id.view_orange:
                dismiss();
                if(mChangeBackground != null) {
                    mChangeBackground.changeColorOrange();
                }
                break;
            case R.id.view_green:
                dismiss();
                if(mChangeBackground != null) {
                    mChangeBackground.changeColorGreen();
                }
                break;
            case R.id.view_blue:
                dismiss();
                if(mChangeBackground != null) {
                    mChangeBackground.changeColorBlue();
                }
                break;
        }
    }

    public void setmChangeBackground(ChangeBackground mChangeBackground) {
        this.mChangeBackground = mChangeBackground;
    }

    public interface ChangeBackground {
        void changeColorWhile();
        void changeColorOrange();
        void changeColorGreen();
        void changeColorBlue();

    }
}
