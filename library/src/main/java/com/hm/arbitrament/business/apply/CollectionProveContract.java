package com.hm.arbitrament.business.apply;

import com.hm.iou.base.mvp.BaseContract;

import java.util.List;

/**
 * 催收证明
 */

public class CollectionProveContract {

    public interface View extends BaseContract.BaseView {

        void showData(List<String> list);
    }

    public interface Presenter extends BaseContract.BasePresenter {
        /**
         * 初始化
         */
        void init();
    }
}
