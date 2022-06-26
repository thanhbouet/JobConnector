package com.example.jobconnector;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RetrieveImageTask extends AsyncTask<List<String>,Void, List<Bitmap>> {
    final String STORAGE_IMAGE_URL = BaseApplication.domain + "/image_storage/images/";

    public RetrieveImageTask(Listener listener) {
        this.mListener = listener;
    }

    public interface Listener {
        void onImageLoaded(List<Bitmap> bitmap);
        void onError();

    }

    private Listener mListener;
    @Override
    //Cai nay chuyen thanh List<Bitmap>
    protected List<Bitmap> doInBackground(List<String>... strings) {

        //input: String, output Bitmap
        List<Bitmap> decodeBitmapList = new ArrayList<>();
        try {
            //"http://10.0.2.2/image_storage/images/India.jpg"
            List<String> imageURL = strings[0];
            for(int i = 0; i < imageURL.size(); i++) {
                URL url = new URL(STORAGE_IMAGE_URL + imageURL.get(i));
                InputStream inputStream = url.openStream();
                decodeBitmapList.add(BitmapFactory.decodeStream(inputStream));
                inputStream.close();
            }
            return decodeBitmapList;
            //return BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<Bitmap> bitmap) {
        if (bitmap != null) {
            mListener.onImageLoaded(bitmap);
        } else {
            mListener.onError();
        }
    }
}
