package com.hm.arbitrament.business.apply;

import com.hm.arbitrament.bean.ElecEvidenceResBean;
import com.hm.iou.base.mvp.BaseContract;

import java.util.List;

/**
 * Created by syl on 2019/6/10.
 */

public class SelectValidEvidenceContract {

    public interface View extends BaseContract.BaseView {

        void showInit();

        void hideInit();

        void showInitFailed(String msg);

        void showDataEmpty();

        /**
         * 显示凭证列表
         */
        void showEvidenceList(List<ElecEvidenceResBean> listEvidence);
    }

    public interface Presenter extends BaseContract.BasePresenter {

        /**
         * 凭证列表
         */
        void getEvidenceList(String iouId, String justId);
    }
}
