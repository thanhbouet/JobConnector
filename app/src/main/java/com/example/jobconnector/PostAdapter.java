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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        ImageView save = view.findViewById(R.id.saveJob);


        textCaption.setText(post.getCaption());
        textDescrible.setText(html2text(post.getDescrible()));
        image.setImageBitmap(post.getImageURL()); //Sua thanh setImageBitmap
        salary.setText(post.getSalary());
        location.setText(post.getLocation());
        jobType.setText(post.getJobType());
        companyName.setText(post.getCompanyName());
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFavorite(post.getCaption(), post.getCompanyName());
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
                        Toast.makeText(context, "Added to favorite", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                    }

                },
                Throwable::printStackTrace) {
            @NonNull
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();
                param.put("username", MainActivity.username);
                param.put("job_name", jobName);
                param.put("company_name", companyName);

                return param;
            }
        };
        requestQueue.add(request);
    }

    public String html2text(String html2) {
        String html = html2;
        html = html.replaceAll("\\\\n"," ");
        html = html.replaceAll("\\\\r"," ");
        html = html.replaceAll("&nbsp;"," ");
        html = html.replaceAll("&amp;"," ");
        return html;
    }
}
