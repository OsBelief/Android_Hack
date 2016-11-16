package com.android.hack.index;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.hack.R;
import com.android.hack.jni.JniActivity;
import com.android.hack.notification.NotificationActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tianzhihen on 2016/11/9.
 */

public class IndexActivity extends AppCompatActivity {
    private ListView mListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_index);
        mListView = (ListView) findViewById(R.id.lv_index);

        List<String> list = new ArrayList<>();
        list.add("1.Android JNI编程");
        list.add("2.Android通知");
        IndexBaseAdapter adapter = new IndexBaseAdapter(list);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        Intent intent = new Intent(IndexActivity.this, JniActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        Intent intent2 = new Intent(IndexActivity.this, NotificationActivity.class);
                        startActivity(intent2);
                        break;
                }
            }
        });
    }

    private class IndexBaseAdapter extends BaseAdapter {
        private List<String> mList;

        public IndexBaseAdapter(List<String> list) {
            mList = list;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public String getItem(int i) {
            return mList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder vh;
            if (view == null) {
                view = getLayoutInflater().inflate(R.layout.item_list_index, null);

                vh = new ViewHolder();
                vh.mTextView = (TextView) view.findViewById(R.id.tv_index_item);
                view.setTag(vh);
            } else {
                vh = (ViewHolder) view.getTag();
            }
            vh.mTextView.setText(getItem(i));
            return view;
        }
    }

    private class ViewHolder {
        public TextView mTextView;
    }
}
