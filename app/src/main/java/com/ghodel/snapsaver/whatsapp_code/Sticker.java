package com.ghodel.snapsaver.whatsapp_code;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Sticker implements Parcelable {
    public String imageFileName;
    public List<String> emojis;
    public long size;

    public Sticker(String imageFileName, List<String> emojis) {
        this.imageFileName = imageFileName;
        this.emojis = emojis;
    }

    protected Sticker(Parcel in) {
        imageFileName = in.readString();
        emojis = in.createStringArrayList();
        size = in.readLong();
    }

    public static final Creator<Sticker> CREATOR = new Creator<Sticker>() {
        @Override
        public com.ghodel.snapsaver.whatsapp_code.Sticker createFromParcel(Parcel in) {
            return new com.ghodel.snapsaver.whatsapp_code.Sticker(in);
        }

        @Override
        public com.ghodel.snapsaver.whatsapp_code.Sticker[] newArray(int size) {
            return new com.ghodel.snapsaver.whatsapp_code.Sticker[size];
        }
    };

    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageFileName);
        dest.writeStringList(emojis);
        dest.writeLong(size);
    }
}
