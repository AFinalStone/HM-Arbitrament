package com.hm.arbitrament.business.apply;

import com.hm.arbitrament.bean.GetCollectionProveResBean;
import com.hm.iou.base.mvp.BaseContract;

import java.io.File;
import java.util.List;

/**
 * 催收证明
 */

public class InputCollectionProveContract {

    public interface View extends BaseContract.BaseView {

        /**
         * 显示催收证明列表
         *
         * @param list
         */
        void showProveList(List<GetCollectionProveResBean> list);

        /**
         * 显示图片
         *
         * @param fileId
         * @param fileUrl
         */
        void showImage(String fileId, String fileUrl);
    }

    public interface Presenter extends BaseContract.BasePresenter {
        /**
         * 初始化
         */
        void getCollectionProvelist();

        /**
         * 上传图片
         */
        void uploadImage(File file);
    }
}
