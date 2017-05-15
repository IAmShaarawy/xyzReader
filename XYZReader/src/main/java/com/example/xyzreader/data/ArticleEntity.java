package com.example.xyzreader.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by elshaarawy on 15-May-17.
 */

public class ArticleEntity implements Parcelable {
    String _id,title,published_date,author,thumb_url,photo_url,aspect_ratio,body;

    public ArticleEntity(String _id, String title, String published_date, String author, String thumb_url, String photo_url, String aspect_ratio, String body) {
        this._id = _id;
        this.title = title;
        this.published_date = published_date;
        this.author = author;
        this.thumb_url = thumb_url;
        this.photo_url = photo_url;
        this.aspect_ratio = aspect_ratio;
        this.body = body;
    }

    protected ArticleEntity(Parcel in) {
        _id = in.readString();
        title = in.readString();
        published_date = in.readString();
        author = in.readString();
        thumb_url = in.readString();
        photo_url = in.readString();
        aspect_ratio = in.readString();
        body = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeString(title);
        dest.writeString(published_date);
        dest.writeString(author);
        dest.writeString(thumb_url);
        dest.writeString(photo_url);
        dest.writeString(aspect_ratio);
        dest.writeString(body);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ArticleEntity> CREATOR = new Creator<ArticleEntity>() {
        @Override
        public ArticleEntity createFromParcel(Parcel in) {
            return new ArticleEntity(in);
        }

        @Override
        public ArticleEntity[] newArray(int size) {
            return new ArticleEntity[size];
        }
    };

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublished_date() {
        return published_date;
    }

    public void setPublished_date(String published_date) {
        this.published_date = published_date;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getThumb_url() {
        return thumb_url;
    }

    public void setThumb_url(String thumb_url) {
        this.thumb_url = thumb_url;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }

    public String getAspect_ratio() {
        return aspect_ratio;
    }

    public void setAspect_ratio(String aspect_ratio) {
        this.aspect_ratio = aspect_ratio;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
