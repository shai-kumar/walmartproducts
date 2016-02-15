package walmart.com.walmartproducts;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by skuma46 on 2/14/16.
 */
public class CarouselPagerAdapter extends PagerAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;
    List<String> mImageUrls = new ArrayList<>();

    public CarouselPagerAdapter(Context context, List<String> imageUrls) {
        mContext = context;
        mImageUrls = imageUrls;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mImageUrls.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.carousel_item, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.carousel_image);
        Picasso.with(mContext)
                .load(mImageUrls.get(position))
                .placeholder(R.drawable.walmart_logo)
                .error(R.drawable.walmart_logo)
                .into(imageView);
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}

