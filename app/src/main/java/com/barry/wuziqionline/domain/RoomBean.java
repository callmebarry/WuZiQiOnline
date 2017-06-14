package com.barry.wuziqionline.domain;

import android.graphics.Point;

import java.util.List;

/**
 * Created by Administrator on 2017/6/14.
 */

public class RoomBean {
    /**
     * appKey : c1ff88a817ead777179406064a3f0b24
     * tableName : Room
     * objectId : 51c5f3e697
     * action : updateRow
     * data : {"createdAt":"2017-06-14 20:54:51","mBlackArray":[],"mIsGameOver":false,"mIsWhite":false,"mIsWhiteWinner":false,"mWhiteArray":[],"objectId":"51c5f3e697","roomid":112,"status":2,"updatedAt":"2017-06-14 20:55:30","who":2}
     */

    private String appKey;
    private String tableName;
    private String objectId;
    private String action;
    /**
     * createdAt : 2017-06-14 20:54:51
     * mBlackArray : []
     * mIsGameOver : false
     * mIsWhite : false
     * mIsWhiteWinner : false
     * mWhiteArray : []
     * objectId : 51c5f3e697
     * roomid : 112
     * status : 2
     * updatedAt : 2017-06-14 20:55:30
     * who : 2
     */

    private DataBean data;

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private String createdAt;
        private boolean mIsGameOver;
        private boolean mIsWhite;
        private boolean mIsWhiteWinner;
        private String objectId;
        private int roomid;
        private int status;
        private String updatedAt;
        private int who;
        private List<Point> mBlackArray;
        private List<Point> mWhiteArray;

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public boolean isMIsGameOver() {
            return mIsGameOver;
        }

        public void setMIsGameOver(boolean mIsGameOver) {
            this.mIsGameOver = mIsGameOver;
        }

        public boolean isMIsWhite() {
            return mIsWhite;
        }

        public void setMIsWhite(boolean mIsWhite) {
            this.mIsWhite = mIsWhite;
        }

        public boolean isMIsWhiteWinner() {
            return mIsWhiteWinner;
        }

        public void setMIsWhiteWinner(boolean mIsWhiteWinner) {
            this.mIsWhiteWinner = mIsWhiteWinner;
        }

        public String getObjectId() {
            return objectId;
        }

        public void setObjectId(String objectId) {
            this.objectId = objectId;
        }

        public int getRoomid() {
            return roomid;
        }

        public void setRoomid(int roomid) {
            this.roomid = roomid;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public int getWho() {
            return who;
        }

        public void setWho(int who) {
            this.who = who;
        }

        public List<Point> getMBlackArray() {
            return mBlackArray;
        }

        public void setMBlackArray(List<Point> mBlackArray) {
            this.mBlackArray = mBlackArray;
        }

        public List<Point> getMWhiteArray() {
            return mWhiteArray;
        }

        public void setMWhiteArray(List<Point> mWhiteArray) {
            this.mWhiteArray = mWhiteArray;
        }
    }
}
