package com.hm.arbitrament.bean;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Data;

/**
 * @author syl
 * @time 2019/6/10 9:04 PM
 */

@Data
public class ElecEvidenceResBean implements Parcelable {


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

    protected ElecEvidenceResBean(Parcel in) {
        this.exEvidenceId = in.readString();
        this.fileType = in.readInt();
        this.name = in.readString();
        this.url = in.readString();
    }

    public static final Parcelable.Creator<ElecEvidenceResBean> CREATOR = new Parcelable.Creator<ElecEvidenceResBean>() {
        @Override
        public ElecEvidenceResBean createFromParcel(Parcel source) {
            return new ElecEvidenceResBean(source);
        }

        @Override
        public ElecEvidenceResBean[] newArray(int size) {
            return new ElecEvidenceResBean[size];
        }
    };
}