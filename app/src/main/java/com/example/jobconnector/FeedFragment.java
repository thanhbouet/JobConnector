package com.example.jobconnector;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FeedFragment extends Fragment {
    ListView listView;
    ArrayList<Post> postList = new ArrayList<>();
    List<String> imageUrlList = new ArrayList<>();
    List<Bitmap> bitmapList = new ArrayList<>();
    String finalData;
    PostAdapter adapter;
    ImageView imageView;
    RetrieveImageTask.Listener listener = new RetrieveImageTask.Listener() {
        @Override
        public void onImageLoaded(List<Bitmap> bitmap) {
            try {
                ArrayList<String> posts = jsonStringToArray(finalData);
                List<String> prs;
                for (String s : posts) {
                    prs = getParams(s);
                    //post(id,caption,company,descripton,img,salary,type,location)
                    postList.add(new Post(Integer.parseInt(prs.get(0)),prs.get(1),prs.get(2), prs.get(7), null, prs.get(3),prs.get(4),prs.get(5)));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Toast.makeText(getActivity(),"Adapter is set",Toast.LENGTH_SHORT).show();
            System.out.println("Size of bitmap :" + bitmap.size());
            bitmapList = bitmap;
            for (int i = 0; i < postList.size(); i++) {
                postList.get(i).setImageURL(bitmapList.get(i));
            }
            adapter = new PostAdapter(getActivity(), R.layout.post_layout, postList);
            listView.setAdapter(adapter);
        }

        @Override
        public void onError() {
            System.out.println("Error");
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_layout, container, false);
        return root;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = view.findViewById(R.id.postList);
        adapter = new PostAdapter(getActivity(), R.layout.post_layout, postList);
        listView.setAdapter(adapter);
        imageView = view.findViewById(R.id.refresh_icon);
        imageView.setOnClickListener(v -> fetchData());
        fetchData();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String nameCompany = postList.get(position).getCompanyName();
                String nameJob = postList.get(position).getCaption();

                Intent intent = new Intent(getActivity(), JobDetails.class);
                intent.putExtra("nameCompany", nameCompany);
                intent.putExtra("nameJob", nameJob);
                startActivity(intent);
            }
        });
    }
    public ArrayList<String> jsonStringToArray(String jsonString) throws JSONException {

        ArrayList<String> stringArray = new ArrayList<>();

        JSONArray jsonArray = new JSONArray(jsonString);

        for (int i = 0; i < jsonArray.length(); i++) {
            stringArray.add(jsonArray.getString(i));
        }

        return stringArray;
    }

    //Viet lai ham nay bang JSONObject.getString
    public List<String> getParams(String s) {
        StringBuilder prs = new StringBuilder(s);
        while (prs.indexOf("\"") != -1) {
            prs.deleteCharAt(prs.indexOf("\""));
        }
        prs.delete(prs.indexOf("id"),prs.indexOf("id") +3);
        prs.delete(prs.indexOf("caption"),prs.indexOf("caption") + 8);
        prs.delete(prs.indexOf("companyName"),prs.indexOf("companyName") + 12);
        prs.delete(prs.indexOf("salary"),prs.indexOf("salary") + 7);
        prs.delete(prs.indexOf("jobType"),prs.indexOf("jobType") + 8);
        prs.delete(prs.indexOf("location"),prs.indexOf("location") + 9);
        prs.delete(prs.indexOf("imageURL"),prs.indexOf("imageURL") + 9);
        prs.delete(prs.indexOf("describle"),prs.indexOf("describle") + 10);

        prs.deleteCharAt(0);
        prs.deleteCharAt(prs.length()-1);
        String[] arrRs = prs.toString().split(",",8);
        List<String> arrResult = Arrays.asList(arrRs);
        if (arrResult.get(7).length() > 150) {
            StringBuilder cutString = new StringBuilder(arrResult.get(7));
            cutString.delete(150,cutString.length());
            cutString.append("...<Click for more deltail>");
            arrResult.set(7,cutString.toString());
        }
        return arrResult;
    }

    private void fetchData() {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);
        progressDialog.setTitle("Loading");
        progressDialog.show();

        imageUrlList.clear();
        finalData = "";
        postList.clear();
        bitmapList.clear();
        String uRl = getString(R.string.domain) + "/fetchdata/jobdata.php";
        StringRequest request = new StringRequest(Request.Method.GET, uRl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    imageUrlList = getImageURL(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new RetrieveImageTask(listener).execute(imageUrlList);
                finalData = response;
            }
        }, new Response.ErrorListener() {
            @Override public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                System.out.println(error.toString());
            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(30000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getmInstance(getActivity()).addToRequestQueue(request);
    }
    public List<String> getImageURL(String jsonString) throws JSONException {
        JSONArray jsonArray = new JSONArray(jsonString);
        List<String> urlResult = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            urlResult.add(jsonArray.getJSONObject(i).getString("imageURL"));
        }

        return urlResult;
    }
}
