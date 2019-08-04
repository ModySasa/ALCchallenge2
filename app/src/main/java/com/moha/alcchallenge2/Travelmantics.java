package com.moha.alcchallenge2;

public class Travelmantics {

    String mTitle,mMessage,mImageUrl;
    int price;

    public Travelmantics() {
    }

    public Travelmantics(String mTitle, String mMessage, String mImageUrl, int price) {
        this.mTitle = mTitle;
        this.mMessage = mMessage;
        this.mImageUrl = mImageUrl;
        this.price = price;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmMessage() {
        return mMessage;
    }

    public void setmMessage(String mMessage) {
        this.mMessage = mMessage;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
