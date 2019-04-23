package com.lq.baselibrary.ioc;

import android.app.Activity;
import android.view.View;

/**
 * author : lqhunter
 * date : 2019/4/23 0023
 * description : view 的 findViewById 的辅助类,
 */
public class ViewFinder {

    private Activity mActivity;
    private View mView;

    public ViewFinder(Activity activity) {
        this.mActivity = activity;
    }

    public ViewFinder(View view) {
        this.mView = view;
    }


    /**
     * 通过 id 找到对应的 View
     *
     * @param viewId
     * @return
     */
    public View findViewById(int viewId) {
        return mActivity != null ? mActivity.findViewById(viewId) : mView.findViewById(viewId);
    }
}
