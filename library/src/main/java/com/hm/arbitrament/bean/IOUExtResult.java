package com.hm.arbitrament.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import lombok.Data;

/**
 * Created by hjy on 2018/12/27.
 */

@Data
public class IOUExtResult {

    private List<ExtContract> exContractList;       //其他扩展合同集合
    private List<ExtEvidence> exEvidenceList;       //凭证信息集合
    private String pdfUrl;
    private String pdfTitle;
    private String pdfContent;
    private int mainPermission;                     //0-未实名；1-内容无关；2-内容相关

    @Data
    public static class ExtContract {

        private String contractId;     //合同id
        private int status;         //1-等待确认；2-已完成，未查看过文件；3-已完成，已经查看；4-过期
        private String title;       //合同标题
        private String content;     //合同标题下的文字描述

    }

    @Data
    public static class ExtEvidence implements Parcelable {

        private int belongsType;        //1-电子借条，2-电子收条
        private String exEvidenceId;       //凭证id
        private int type;               //0-其他凭证；1-汇款凭证；2-收款凭证
        private String url;

        @Deprecated
        private int operPermission;     //操作权限：0-查看；1-可以操作

        private int checked;            //是否查看过：0-否，1-是
        private String content;
        private int fileType;           //1-图片，2-pdf
        private String name;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.belongsType);
            dest.writeString(this.exEvidenceId);
            dest.writeInt(this.type);
            dest.writeString(this.url);
            dest.writeInt(this.operPermission);
            dest.writeInt(this.checked);
            dest.writeString(this.content);
            dest.writeInt(this.fileType);
            dest.writeString(this.name);
        }

        protected ExtEvidence(Parcel in) {
            this.belongsType = in.readInt();
            this.exEvidenceId = in.readString();
            this.type = in.readInt();
            this.url = in.readString();
            this.operPermission = in.readInt();
            this.checked = in.readInt();
            this.content = in.readString();
            this.fileType = in.readInt();
            this.name = in.readString();
        }

        public static final Creator<ExtEvidence> CREATOR = new Creator<ExtEvidence>() {
            @Override
            public ExtEvidence createFromParcel(Parcel source) {
                return new ExtEvidence(source);
            }

            @Override
            public ExtEvidence[] newArray(int size) {
                return new ExtEvidence[size];
            }
        };
    }

}