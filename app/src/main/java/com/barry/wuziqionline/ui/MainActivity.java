package com.barry.wuziqionline.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.barry.wuziqionline.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.btn_local)
    Button mBtnLocal;
    @InjectView(R.id.btn_online)
    Button mBtnOnline;
    @InjectView(R.id.activity_main)
    RelativeLayout mActivityMain;
    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        setTitle("五子棋");
    }

    @OnClick({R.id.btn_local, R.id.btn_online})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_local:
                Intent intent=new Intent(this,PlayActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_online:
                Intent intent1=new Intent(this,OnlineActivity.class);
                startActivity(intent1);
                break;
        }
    }
}
