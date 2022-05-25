package com.example.jobconnector;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class PostAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Post> postList;

    public PostAdapter( Context context, int layout, List<Post> postList) {
        this.context = context;
        this.layout = layout;
        this.postList = postList;
    }

    @Override
    public int getCount() {
        return postList.size();
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
        Post post = postList.get(i);

        TextView textCaption = view.findViewById(R.id.caption);
        TextView textDescrible = view.findViewById(R.id.describle);
        ImageView image = view.findViewById(R.id.photoImage); //Sua cai nay
        TextView salary = view.findViewById(R.id.salary);
        TextView location = view.findViewById(R.id.location);
        TextView jobType = view.findViewById(R.id.jobType);
        TextView companyName = view.findViewById(R.id.company_name);


        textCaption.setText(post.getCaption());
        textDescrible.setText(post.getDescrible());
        image.setImageBitmap(post.getImageURL()); //Sua thanh setImageBitmap
        salary.setText(post.getSalary());
        location.setText(post.getLocation());
        jobType.setText(post.getJobType());
        companyName.setText(post.getCompanyName());

        return view;
    }
}
