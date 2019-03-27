package net.lzzy.cinemanager.fragments;

import android.widget.TextView;

import net.lzzy.cinemanager.R;

/**
 * Created by lzzy_gxy on 2019/3/26.
 * Description:
 */
public class OrdersFragment extends BaseFragment {


    @Override
    protected void populate() {
        TextView tv=find(R.id.fragment_cinemas_tv);

    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_orders;
    }
}