package com.hm.arbitrament.bean;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Data;

/**
 * @author syl
 * @time 2019/6/10 9:04 PM
 */

@Data
public class ElecEvidenceRes implements Parcelable {


    /**
     * exEvidenceId : string
     * fileType : 0
     * name : string
     * url : string
     */

    private String exEvidenceId;
    private int fileType;
    private String name;
    private String url;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.exEvidenceId);
        dest.writeInt(this.fileType);
        dest.writeString(this.name);
        dest.writeString(this.url);
    }

    protected ElecEvidenceRes(Parcel in) {
        this.exEvidenceId = in.readString();
        this.fileType = in.readInt();
        this.name = in.readString();
        this.url = in.readString();
    }

    public static final Parcelable.Creator<ElecEvidenceRes> CREATOR = new Parcelable.Creator<ElecEvidenceRes>() {
        @Override
        public ElecEvidenceRes createFromParcel(Parcel source) {
            return new ElecEvidenceRes(source);
        }

        @Override
        public ElecEvidenceRes[] newArray(int size) {
            return new ElecEvidenceRes[size];
        }
    };
}