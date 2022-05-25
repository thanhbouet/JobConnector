package com.example.jobconnector;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.BitSet;

public class RetrieveForOne extends AsyncTask<String, Void, Bitmap> {
    public RetrieveForOne(mListener listener) {
        this.mListener = listener;
    }
    private mListener mListener;
    public interface mListener {
        void onImageLoaded(Bitmap bitmap);
        void onError();

    }
    @Override
    protected Bitmap doInBackground(String... strings) {
        //input: String, output Bitmap
        try {
            URL url = new URL(strings[0]);
            InputStream inputStream = url.openStream();
            return BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (bitmap != null) {
            mListener.onImageLoaded(bitmap);
        } else {
            mListener.onError();
        }
    }
}
