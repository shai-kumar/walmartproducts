package walmart.com.walmartproducts;

/**
 * Created by skuma46 on 4/17/15.
 */

import android.content.Context;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

import auto.parcel.AutoParcel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProductsAdapter extends BaseAdapter {

    @AutoParcel
    public static abstract class Product implements Parcelable{
        public abstract String getProductId();
        @Nullable abstract String getProductName();
        @Nullable abstract String getShortDescription();
        @Nullable abstract String getLongDescription();
        @Nullable abstract String getPrice();
        @Nullable abstract String getProductImage();
        @Nullable abstract Double getReviewRating();
        abstract int getReviewCount();
        abstract boolean getInStock();

        static Product create(
                String productId,
                String productName,
                String shortDescription,
                String longDescription,
                String price,
                String productImage,
                Double reviewRating,
                int reviewCount,
                boolean inStock) {

            return new AutoParcel_ProductsAdapter_Product(
                     productId,
                     productName,
                     shortDescription,
                     longDescription,
                     price,
                     productImage,
                     reviewRating,
                     reviewCount,
                     inStock);
        }
    }

    private Context mContext;
    private ArrayList<Product> mProductList = new ArrayList<>();
    private int mLastFetchedPage;
    private int mTotalProductCount;
    private boolean isFetchingNextPage;

    public ProductsAdapter(Context context) {
        mContext = context;
        showProgressBar();
        WalmartWebClient.getInstance(mContext)
                .getProducts(mLastFetchedPage + 1, new Callback<WalmartResponse>() {
            @Override
            public void onResponse(Call<WalmartResponse> call, Response<WalmartResponse> response) {
                dismissProgressBar();
                if(response.isSuccess()) {
                    mTotalProductCount = response.body().totalProducts;
                    mLastFetchedPage = response.body().pageNumber;
                    mProductList.addAll(Arrays.asList(response.body().products));
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<WalmartResponse> call, Throwable t) {
                dismissProgressBar();
            }
        });
    }

    public ArrayList<Product> getProductList() {
        return mProductList;
    }

    public int getCount() {
        return mProductList.size();
    }

    public Product getItem(int position) {
        return mProductList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.product_item, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.mProductImage = (ImageView) convertView.findViewById(R.id.list_item_image);
            viewHolder.mProductName = (TextView) convertView.findViewById(R.id.list_item_name);
            convertView.setTag(viewHolder);
        }
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        if(mProductList.get(position).getProductName() != null) {
            viewHolder.mProductName.setText(Html.fromHtml(mProductList.get(position).getProductName()));
        }
        if(mProductList.get(position).getProductImage() != null) {
        Picasso.with(mContext)
                .load(mProductList.get(position).getProductImage())
                .resizeDimen(R.dimen.product_image_size, R.dimen.product_image_size)
                .placeholder(R.drawable.walmart_logo)
                .error(R.drawable.walmart_logo)
                .into(viewHolder.mProductImage);
        }
        return convertView;
    }
    private class ViewHolder {
        ImageView mProductImage;
        TextView mProductName;
    }

    public void fetchNextPage() {
        if(isFetchingNextPage) {
            return;
        }
        if(mProductList.size() >= mTotalProductCount) {
            Toast.makeText(mContext, "No more products.", Toast.LENGTH_SHORT).show();
            return;
        }
        isFetchingNextPage = true;
        showProgressBar();
        WalmartWebClient.getInstance(mContext)
                .getProducts(mLastFetchedPage + 1, new Callback<WalmartResponse>() {
            @Override
            public void onResponse(Call<WalmartResponse> call, Response<WalmartResponse> response) {
                dismissProgressBar();
                if(response.isSuccess()) {
                    mTotalProductCount = response.body().totalProducts;
                    mLastFetchedPage = response.body().pageNumber;
                    mProductList.addAll(Arrays.asList(response.body().products));
                    notifyDataSetChanged();
                }
                isFetchingNextPage = false;
            }

            @Override
            public void onFailure(Call<WalmartResponse> call, Throwable t) {
                dismissProgressBar();
                isFetchingNextPage = false;
            }
        });
    }

    private void dismissProgressBar() {
        ((ProductsActivity) mContext).dismissProgressBar();
    }

    private void showProgressBar() {
        ((ProductsActivity) mContext).showProgressBar();
    }
}