package com.barry.wuziqionline.ui;

import android.app.ProgressDialog;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.barry.wuziqionline.R;
import com.barry.wuziqionline.domain.Room;
import com.barry.wuziqionline.domain.RoomBean;
import com.barry.wuziqionline.widget.WuZiQionline;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobRealTimeData;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.ValueEventListener;

public class OnlineActivity extends AppCompatActivity {

    @InjectView(R.id.wuziqi)
    WuZiQionline mWuziqi;
    @InjectView(R.id.btn_new)
    Button mBtnNew;
    @InjectView(R.id.btn_back)
    Button mBtnBack;
    @InjectView(R.id.activity_main)
    RelativeLayout mActivityMain;
    private String TAG = "OnlineActivity";
    private AlertDialog mDialog;

    private ProgressDialog mProgressDialog;
    private BmobRealTimeData mRtd;
    private Gson mGson;

    private int who = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online);
        ButterKnife.inject(this);
        setTitle("五子棋(在线对战)");
        mGson = new Gson();
        final View inflate = LayoutInflater.from(this).inflate(R.layout.dailog_createroom, null);
        mDialog = new AlertDialog.Builder(this)
                .setTitle("进入房间")
                .setView(inflate).setCancelable(false)
                .show();
        final RadioButton play = (RadioButton) inflate.findViewById(R.id.player1);





        inflate.findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        inflate.findViewById(R.id.btn_join).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (play.isChecked()) {
                    who = 1;
                } else {
                    who = 2;
                }
                Log.i(TAG, "onCreate: 选择玩家"+who);
                hidKeyboard();
                TextView rooid = (TextView) inflate.findViewById(R.id.ed_roomid);
                final String id = rooid.getText().toString();
                if (id != null && id.length() > 0) {

                    //查询有没有该房间
                    BmobQuery<Room> bmobQuery = new BmobQuery<Room>().addWhereEqualTo("roomid", Integer.valueOf(id)).addWhereEqualTo("status", 1);
                    bmobQuery.findObjects(new FindListener<Room>() {
                        @Override
                        public void done(final List<Room> list, BmobException e) {

                            if (e == null) {
                                Log.i(TAG, "done: 11111111111111111" + list.size());
                                //有没有该房间
                                for (Room r : list) {
                                    Log.i(TAG, "done:111111111111 " + r.getRoomid());
                                }
                                if (list.size() != 0) {
                                    if (list.get(0).getStatus() != 2) {
                                        Room room = list.get(0);
                                        setListener(room.getObjectId());
                                        room.setStatus(2);
                                        room.update(list.get(0).getObjectId(), new UpdateListener() {

                                            @Override
                                            public void done(BmobException e) {
                                                if (e == null) {



                                                    Toast.makeText(getApplicationContext(), "进入成功", Toast.LENGTH_SHORT).show();
                                                    hideProgressDialog();
                                                    mDialog.hide();
                                                } else {
                                                    Toast.makeText(getApplicationContext(), "进入失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    } else {
                                        Toast.makeText(getApplicationContext(), "该房间已满人", Toast.LENGTH_SHORT).show();
                                    }
                                } else {

//创建房间
                                    Room r = new Room();
                                    r.setStatus(1);
                                    r.setWho(1);
                                    r.setRoomid(Integer.valueOf(id));
                                    r.save(new SaveListener<String>() {
                                        @Override
                                        public void done(final String s, BmobException e) {
                                            if (e == null) {
                                                setListener(s);
                                                showProgressDialog("等待玩家进入");

                                            } else {
                                                Toast.makeText(getApplicationContext(), "房间创建失败", Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    });
                                }

                            } else {
                                Toast.makeText(getApplicationContext(), "失败" + e.getMessage(), Toast.LENGTH_SHORT).show();


                            }
                        }
                    });
                }


            }
        });


    }

    void showProgressDialog(String msg) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
        }
        mProgressDialog.setMessage(msg);
        mProgressDialog.show();
    }

    void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
        mDialog.hide();
    }

    @OnClick({R.id.btn_new, R.id.btn_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_new:
                break;
            case R.id.btn_back:
                break;
        }
    }

    void hidKeyboard() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                View view = getCurrentFocus();
                if (view != null) {
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        }).start();

    }


    void setListener(final String sss) {
        mRtd = new BmobRealTimeData();
        mRtd.start(new ValueEventListener() {
            @Override
            public void onDataChange(JSONObject data) {
                Log.i(TAG, "onDataChange: " + data);
                RoomBean roomBean = mGson.fromJson(data.toString(), RoomBean.class);
                RoomBean.DataBean da = roomBean.getData();
                if (da.getStatus() == 2) {
                    hideProgressDialog();
                    Room r = new Room();
                    r.setObjectId(da.getObjectId());
                    r.setStatus(2);
                    r.setRoomid(da.getRoomid());
                    r.setBlackArray((ArrayList<Point>) da.getMBlackArray());
                    r.setWhiteArray((ArrayList<Point>) da.getMWhiteArray());
                    r.setGameOver(da.isMIsGameOver());
                    r.setWhiteWinner(da.isMIsWhiteWinner());
                    Log.i(TAG, "onDataChange: 玩家"+who+" 轮到："+da.getWho());
                    mWuziqi.input(r,who,da.getWho());

                }

            }

            @Override
            public void onConnectCompleted(Exception ex) {
                if (ex == null && mRtd.isConnected()) {
                    mRtd.subRowUpdate("Room", sss);
                    Log.d("bmob", "连接成功:");
                }


            }
        });
    }
}


