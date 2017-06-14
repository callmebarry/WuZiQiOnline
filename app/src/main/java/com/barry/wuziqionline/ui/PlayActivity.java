package com.barry.wuziqionline.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.barry.wuziqionline.R;
import com.barry.wuziqionline.widget.WuZiQi;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class PlayActivity extends AppCompatActivity {

    @InjectView(R.id.wuziqi)
    WuZiQi mWuziqi;
    @InjectView(R.id.btn_new)
    Button mBtnNew;
    @InjectView(R.id.btn_backp)
    Button mBtnBackp;
    @InjectView(R.id.btn_back)
    Button mBtnBack;
    @InjectView(R.id.activity_main)
    RelativeLayout mActivityMain;
    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        ButterKnife.inject(this);
        setTitle("五子棋");

    }

    @OnClick({R.id.wuziqi, R.id.btn_new, R.id.btn_backp, R.id.btn_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_new:
                mWuziqi.start();
                break;
            case R.id.btn_backp:
                mWuziqi.back();
                break;
            case R.id.btn_back:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setTitle("提示").setMessage("确定退出本轮比赛?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                return;
            }
        }).create().show();
    }

}
