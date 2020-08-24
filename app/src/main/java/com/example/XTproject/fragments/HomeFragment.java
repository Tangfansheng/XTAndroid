package com.example.XTproject.fragments;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.XTproject.R;
import com.example.XTproject.activity.Environment_Monitor_Activity;
import com.example.XTproject.activity.ForceMonitorListActivity;
import com.example.XTproject.activity.MapTestActivity;
import com.example.XTproject.activity.StressMonitorActivity;
import com.example.XTproject.activity.SynVideoActivity;
import com.example.XTproject.activity.VideoActivity;
import com.example.XTproject.base.BaseFragment;
import com.example.XTproject.model.Function;

/*
 *作者：created by Administrator on 2020/3/20 23:02
 *邮箱：1723928492@qq.com
 */
public class HomeFragment extends BaseFragment {
    private GridView mGridView;
    private static final String TAG = HomeFragment.class.getSimpleName();

    //拿到当前的homefragment
    @Override
    protected void initView() {
        mGridView = bindViewId(R.id.gv_channel);//九宫布局
        mGridView.setAdapter(new FunctionAdapter());
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {//item点击事件
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Log.d(TAG,">> onItemClick "+position);// >>表示以什么开头
                Intent intent;
                switch (position) {
                    case 0:
                        //力监控
                        intent = new Intent(getActivity(), ForceMonitorListActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        //应力监控
                        intent = new Intent(getActivity(), StressMonitorActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        //监控
                        intent = new Intent(getActivity(), VideoActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        //同步性监控
                        intent = new Intent(getActivity(), SynVideoActivity.class);
                        startActivity(intent);
                        break;
                    case 4:
                        //环境监控
                        intent = new Intent(getActivity(), Environment_Monitor_Activity.class);
                        startActivity(intent);
                        break;
                    case 5:
                        intent = new Intent(getActivity(), MapTestActivity.class);
                        startActivity(intent);
                        break;

                    default:
                        //TODO 跳转对应的频道
                        //position从零开始算的
//                        DetailListActivity.LaunchDetailListActivity(getActivity(),position + 1);
                        break;
                }
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initData() {

    }

    class FunctionAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            //控制图标的数量
            return 6;
        }

        @Override
        public Function getItem(int position) {
            return new Function(position + 1, getActivity());
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
//            Channel chanel= (Channel)getItem(position);
            Function function = getItem(position);
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_grid_icon, null);
                holder = new ViewHolder();
                holder.textView = (TextView) convertView.findViewById(R.id.txt_icon);
                holder.imageView = (ImageView) convertView.findViewById(R.id.img_icon);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.textView.setText(function.getFunctionName());
            int id = function.getFunctionID();
            int imgResId = -1;
            switch (id) {
                case Function.Force:
                    imgResId = R.drawable.ic_force;
                    break;
                case Function.Stress:
                    imgResId = R.drawable.ic_stress;
                    break;
                case Function.VideoMonitor:
                    imgResId = R.drawable.ic_camera;
                    break;
                case Function.Syn:
                    imgResId = R.drawable.ic_syn;
                    break;
                case Function.Environment:
                    imgResId = R.drawable.ic_envir;
                    break;
                case Function.Loc:
                    imgResId = R.drawable.ic_loc;
                    break;

            }
            holder.imageView.setImageDrawable(getActivity().getResources().getDrawable(imgResId));
            return convertView;
        }
    }

    class ViewHolder {//条目的布局文件中有什么组件，这里就定义有什么属性
        TextView textView;
        ImageView imageView;
    }

}
