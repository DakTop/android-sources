package com.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 跨进程使用的数据实体类，必须实现Parcelable接口，
 * 而且必须定义一个同名的.aidl文件。
 * Created by runTop on 2018/7/13.
 */
public class Book implements Parcelable {
    private int id;
    private String name;

    public Book() {
    }

    protected Book(Parcel in) {
        id = in.readInt();
        name = in.readString();
    }

    public Book(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    //如果需要支持定向tag为out,inout，就要重写该方法
    public void readFromParcel(Parcel dest) {
        //注意，此处的读值顺序应当是和writeToParcel()方法中一致的
        this.id = dest.readInt();
        this.name = dest.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
