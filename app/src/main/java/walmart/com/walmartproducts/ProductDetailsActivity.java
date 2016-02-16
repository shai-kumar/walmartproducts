package walmart.com.walmartproducts;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

public class ProductDetailsActivity extends AppCompatActivity {

    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;
    private  List<ProductsAdapter.Product> mProductList;
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
            args.putParcelable(ProductDetailsFragment.ARG_PRODUCT, mProductList.get(position));
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

}
