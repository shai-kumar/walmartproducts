package walmart.com.walmartproducts;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ProductDetailsActivity extends AppCompatActivity {

    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;
    private static List<ProductsAdapter.Product> mProductList;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        mProductList = getIntent().getParcelableArrayListExtra(
                ProductsActivity.PRODUCT_LIST);
        position = getIntent().getIntExtra(ProductsActivity.PRODUCT_LIST_POSITION, 0);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(position);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            ProductDetailsFragment fragment = new ProductDetailsFragment();
            Bundle args = new Bundle();
            args.putInt(ProductDetailsFragment.ARG_SECTION_NUMBER, position);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return mProductList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getResources().getString(R.string.product_details);
        }
    }

    public static class ProductDetailsFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        @Bind(R.id.product_name)
        TextView mProductName;
        @Bind(R.id.product_short_description)
        TextView mShortDescription;
        @Bind(R.id.product_price)
        TextView mPrice;
        @Bind(R.id.out_of_stock)
        TextView mOutOfStock;
        @Bind(R.id.add_to_cart_button)
        Button mAddToCartButton;
        @Bind(R.id.product_long_description)
        TextView mLongDescription;
        @Bind(R.id.carousel_pager)
        ViewPager mCarouselPager;
        @Bind(R.id.indicator)
        CirclePageIndicator mTitleIndicator;
        @Bind(R.id.rating)
        TextView mRating;
        @Bind(R.id.review)
        TextView mReview;


        CarouselPagerAdapter mCarouselPagerAdapter;
        TagHandler mTagHandler = new TagHandler();


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_product_details, container, false);
            ButterKnife.bind(this, rootView);
            int position = getArguments().getInt(ARG_SECTION_NUMBER);
            ProductsAdapter.Product product = mProductList.get(position);

            /**
             * Image carousel in product details page. End point returns a single product url.
             *  Replicating the url to create a list of 6 images
             */
            List<String> imageUrls = new ArrayList<>(6);
            String productImage = mProductList.get(position).getProductImage();
            imageUrls.add(productImage);
            imageUrls.add(productImage);
            imageUrls.add(productImage);
            imageUrls.add(productImage);
            imageUrls.add(productImage);
            imageUrls.add(productImage);

            mCarouselPagerAdapter = new CarouselPagerAdapter(getActivity(), imageUrls);
            mCarouselPager.setAdapter(mCarouselPagerAdapter);
            mTitleIndicator.setViewPager(mCarouselPager);

            if (product.getProductName() != null) {
                mProductName.setText(Html.fromHtml(product.getProductName()));
            }
            if (product.getShortDescription() != null) {
                mShortDescription.setText(Html.fromHtml(product.getShortDescription(), null,
                        mTagHandler));
            }
            mPrice.setText(product.getPrice());
            if (product.getInStock()) {
                mOutOfStock.setVisibility(View.GONE);
                mAddToCartButton.setVisibility(View.VISIBLE);
            } else {
                mOutOfStock.setVisibility(View.VISIBLE);
                mAddToCartButton.setVisibility(View.INVISIBLE);
            }
            if (product.getLongDescription() != null) {
                mLongDescription.setText(Html.fromHtml(product.getLongDescription()));
            }
            mRating.setText("Rating: " + product.getReviewRating() + " stars");
            mReview.setText(product.getReviewCount() + " reviews");
            return rootView;
        }

    }

}
