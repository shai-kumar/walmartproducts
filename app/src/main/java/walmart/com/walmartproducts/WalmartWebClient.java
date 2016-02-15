package walmart.com.walmartproducts;

import android.content.Context;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class WalmartWebClient {

    public static final String TAG = "WalmartWebClient";

    public static final String WALMART_API_URL = "https://walmartlabs-test.appspot.com/_ah/api/walmart/v1/";
    public static final String WALMART_API_KEY = "b1b834e8-89c5-4d70-aa26-301f69565781";
    private static final int PAGE_SIZE = 30;

    private WalmartApi mWalmartApi;


    private enum Singleton {
        CLIENT;
        private final WalmartWebClient client;

        Singleton() {
            this.client = new WalmartWebClient(WalmartApplication.context());
        }
    }

    public static WalmartWebClient client() {
        return Singleton.CLIENT.client;
    }

    private WalmartWebClient(Context context) {
        buildApis();
    }

    public void buildApis() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WALMART_API_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mWalmartApi = retrofit.create(WalmartApi.class);

    }

    public void getProducts(int pageNumber, final Callback<WalmartResponse> callback){
        mWalmartApi.getProducts(WALMART_API_KEY, pageNumber, PAGE_SIZE)
                    .enqueue(callback);

    }
}
