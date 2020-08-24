package com.example.XTproject.fragments;

import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.widget.TextView;

import com.example.XTproject.R;
import com.example.XTproject.base.BaseFragment;

/*
 *作者：created by Administrator on 2020/3/20 23:06
 *邮箱：1723928492@qq.com
 */
public class AboutFragment extends BaseFragment {
    @Override
    protected void initView() {
       TextView textView = bindViewId(R.id.tv_app_description);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_about;
    }

    @Override
    protected void initData() {

    }
}
