package com.xujun.funapp.view.main.fragment.picture;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.orhanobut.logger.Logger;
import com.xujun.funapp.adapters.PictureListAdapter;
import com.xujun.funapp.beans.PictureListBean;
import com.xujun.funapp.common.BaseListFragment;
import com.xujun.funapp.common.Constants.IntentConstants;
import com.xujun.funapp.common.recyclerView.BaseRecyclerAdapter;
import com.xujun.funapp.common.util.ListUtils;
import com.xujun.funapp.model.PictureListModel;
import com.xujun.funapp.view.detail.PictureDetailActivity;

import java.util.ArrayList;

/**
 * @ explain:
 * @ author：xujun on 2016/9/17 20:17
 * @ email：gdutxiaoxu@163.com
 */
public class PictureListFragment extends BaseListFragment<PictureListPresenter>
        implements PictureListContract.View<PictureListBean> {

    private static final String TITLE = "title";
    private static final String ID = "id";

    private static String[] tags = PictureListModel.tags;
    private ArrayList<PictureListBean.TngouBean> mDatas;
    private PictureListAdapter mAdapter;

    public static PictureListFragment newInstance(String title, int id) {
        PictureListFragment pictureListFragment = new PictureListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TITLE, title);
        bundle.putInt(ID, id);
        pictureListFragment.setArguments(bundle);
        return pictureListFragment;
    }

    @Override
    protected BaseRecyclerAdapter getAdapter() {
        mDatas = new ArrayList<>();
        mAdapter = new PictureListAdapter(mContext, mDatas, this);
        return mAdapter;
    }

    @Override
    protected void initListener() {
        super.initListener();
        mAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, RecyclerView.ViewHolder holder, int position) {
                PictureListBean.TngouBean tngouBean = mDatas.get(position);
                readyGo(PictureDetailActivity.class,
                        IntentConstants.DEFAULT_PARCEABLE_NAME, tngouBean);
            }
        });

    }

    @Override
    protected PictureListPresenter setPresenter() {
        return new PictureListPresenter(this, tags[mId]);
    }

    protected void getNextPageData() {
        mPresenter.getPictureList(String.valueOf(++mPage), String.valueOf(mRows), mId);
    }

    protected void getFirstPageData() {
        mPage = 1;
        mPresenter.getPictureList(String.valueOf(mPage), String.valueOf(mRows), mId);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void onSuccess(PictureListBean o) {
        if (false == ListUtils.isEmpty(o.tngou)) {
            if (isRefresh()) {
                mDatas.clear();
            }
            mDatas.addAll(o.tngou);
            mAdapter.notifyDataSetChanged();
            endRefresh(RequestResult.success);
        } else {
            if (isRefresh()) {
                mDatas.clear();
            }
            mDatas.addAll(o.tngou);
            mAdapter.notifyDataSetChanged();
            endRefresh(RequestResult.empty);
        }


    }

    @Override
    public void onError(Throwable throwable) {
        Logger.i(throwable.getMessage());
        endRefresh(RequestResult.error);
    }

    @Override
    public void onLocal(PictureListBean o) {

    }
}
