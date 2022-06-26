package com.example.jobconnector;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
        ImageView TV_image,addFavorite;

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
            viewHolder.addFavorite = view.findViewById(R.id.saveJob);

            view.setTag(viewHolder);
        } else {
            viewHolder = (JobAdapter.viewHolder) view.getTag();
        }

        viewHolder.TV_address.setText(inforArrayList.get(i).getAddress());
        viewHolder.TV_time.setText(inforArrayList.get(i).getTimeLimit());
        viewHolder.TV_jobName.setText(inforArrayList.get(i).getJobName());
        viewHolder.TV_companyName.setText(inforArrayList.get(i).getCompanyName());
        viewHolder.TV_image.setImageBitmap(inforArrayList.get(i).getImageURL());
        viewHolder.addFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFavorite(inforArrayList.get(i).getJobName(),inforArrayList.get(i).getCompanyName());
            }
        });

        return view;
    }

    private void addFavorite(String jobName, String companyName) {
        if (MainActivity.worker.equals("Employer")) {
            Toast.makeText(context,"Only for job seeker",Toast.LENGTH_SHORT).show();
            return;
        }
        if (BaseApplication.favoriteJob.contains(jobName+"."+companyName)) {
            Toast.makeText(context,"This job was existed in favorite list",Toast.LENGTH_SHORT).show();
            return;
        }
        String url = context.getString(R.string.domain) + "/recruitment/add_favorite.php";
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    if (response.equals("Update successfully\r\n")) {
                        BaseApplication.favoriteJob.add(jobName+"."+companyName);
                    }
                    Toast.makeText(context,response,Toast.LENGTH_SHORT).show();
                },
                Throwable::printStackTrace) {
            @NonNull
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();
                param.put("username",MainActivity.username);
                param.put("job_name",jobName);
                param.put("company_name",companyName);

                return param;
            }
        };

        requestQueue.add(request);
    }
}
