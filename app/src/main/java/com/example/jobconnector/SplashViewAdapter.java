package com.example.jobconnector;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;



import java.util.List;

public class SplashViewAdapter extends PagerAdapter {
    private List<SplashItem> splashItems;
    private Context mContext;

    public SplashViewAdapter(List<SplashItem> splashItems, Context mContext) {
        this.splashItems = splashItems;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layoutScreen = inflater.inflate(R.layout.item_splash, null);

        ImageView imgSlide = layoutScreen.findViewById(R.id.topImg);
        TextView title = layoutScreen.findViewById(R.id.title);
        TextView description = layoutScreen.findViewById(R.id.description);

        title.setText(splashItems.get(position).getTitle());
        description.setText(splashItems.get(position).getDescription());
        imgSlide.setImageResource(splashItems.get(position).getImgView());

        container.addView(layoutScreen);

        return layoutScreen;
    }

    @Override
    public int getCount() {
        return splashItems.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((View) object);
    }
}