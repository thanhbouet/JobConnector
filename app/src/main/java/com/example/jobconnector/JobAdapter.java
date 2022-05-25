package com.example.jobconnector;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class JobAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<JobInfor> inforArrayList;
    private int layout;

    public JobAdapter(Context context, int layout, ArrayList<JobInfor> inforArrayList) {
        this.context = context;
        this.inforArrayList = inforArrayList;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return inforArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    class viewHolder {
        TextView TV_jobName;
        TextView TV_companyName;
        TextView TV_time;
        TextView TV_address;
        ImageView TV_image;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        viewHolder viewHolder = new viewHolder();

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);

            viewHolder.TV_companyName = view.findViewById(R.id.nameCompany);
            viewHolder.TV_jobName = view.findViewById(R.id.TV_nameJob);
            viewHolder.TV_time = view.findViewById(R.id.TV_timeLimit);
            viewHolder.TV_address = view.findViewById(R.id.TV_address);
            viewHolder.TV_image = view.findViewById(R.id.TV_imageCompany);

            view.setTag(viewHolder);
        } else {
            viewHolder = (JobAdapter.viewHolder) view.getTag();
        }

        viewHolder.TV_address.setText(inforArrayList.get(i).getAddress());
        viewHolder.TV_time.setText(inforArrayList.get(i).getTimeLimit());
        viewHolder.TV_jobName.setText(inforArrayList.get(i).getJobName());
        viewHolder.TV_companyName.setText(inforArrayList.get(i).getCompanyName());
        viewHolder.TV_image.setImageBitmap(inforArrayList.get(i).getImageURL());

        return view;
    }
}
