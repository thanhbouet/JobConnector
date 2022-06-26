package com.example.jobconnector;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class FavoriteAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private List<String> jobNameList;
    private List<String> companyList;

    public FavoriteAdapter(Context context, int layout, List<String> jobNameList, List<String> companyList) {
        this.context = context;
        this.layout = layout;
        this.jobNameList = jobNameList;
        this.companyList = companyList;
    }

    @Override
    public int getCount() {
        return jobNameList.size();
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
        String jobName = jobNameList.get(i);
        String companyName = companyList.get(i);

        TextView usernameInf = view.findViewById(R.id.job_inf);
        usernameInf.setText(jobName);

        TextView companyInf = view.findViewById(R.id.company_inf);
        companyInf.setText(companyName);

        return view;
    }
}
