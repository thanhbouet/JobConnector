package com.example.jobconnector;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class RecruitAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private List<String> applyList;

    public RecruitAdapter(Context context, int layout, List<String> applyList) {
        this.context = context;
        this.layout = layout;
        this.applyList = applyList;
    }

    @Override
    public int getCount() {
        return applyList.size();
    }

    @Override
    public Object getItem(int i) {
        return layout;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(layout, null);
        String username = applyList.get(i);

        TextView usernameInf = view.findViewById(R.id.username_inf);
        usernameInf.setText("New Application from user: " + username);

        return view;
    }
}
