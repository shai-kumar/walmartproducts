package walmart.com.walmartproducts;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ProgressBar;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ProductsActivity extends AppCompatActivity {

    public static final String PRODUCT_LIST = "product_list";
    public static final String PRODUCT_LIST_POSITION = "product_list_position";

    @Bind(R.id.progress_bar)
    ProgressBar mProgressBar;
    @Bind(R.id.product_list)
    AbsListView mProductList;

    private ProductsAdapter mProductAdapter;
    private int previousLastVisibleItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        ButterKnife.bind(this);

        mProductAdapter = new ProductsAdapter(this);
        mProductList.setAdapter(mProductAdapter);
        mProductList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(getApplicationContext(), ProductDetailsActivity.class));
                // Limit view pager to 50 items on either side of position, to avoid TransactionTooLargeException
                ArrayList<ProductsAdapter.Product> productList;
                if (mProductAdapter.getProductList().size() > 100) {
                    productList = new ArrayList<>(mProductAdapter.getProductList()
                            .subList(
                                    Math.max(position - 50, 0),
                                    Math.min(position + 50, mProductAdapter.getProductList().size())
                            ));
                    if (position - 50 < 0) {
                        intent.putExtra(PRODUCT_LIST_POSITION, position);
                    } else {
                        intent.putExtra(PRODUCT_LIST_POSITION, 50);
                    }
                } else {
                    productList = mProductAdapter.getProductList();
                    intent.putExtra(PRODUCT_LIST_POSITION, position);

                }
                intent.putParcelableArrayListExtra(PRODUCT_LIST, productList);
                startActivity(intent);
            }
        });
        mProductList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {
                final int lastVisibileItem = firstVisibleItem + visibleItemCount;
                if (visibleItemCount > 0 && lastVisibileItem == totalItemCount)
                    if (previousLastVisibleItem != lastVisibileItem)
                        mProductAdapter.fetchNextPage();
                previousLastVisibleItem = lastVisibileItem; // to handle repeated onScroll calls
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_products, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void dismissProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }

    public void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }
}
