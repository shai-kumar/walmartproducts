package walmart.com.walmartproducts;

import android.content.Context;
import android.os.StatFs;

import java.io.File;

import okhttp3.Cache;
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
    static final int MAX_DISK_CACHE_SIZE = 50 * 1024 * 1024; // 50MB
    static final int MIN_DISK_CACHE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final String WALMART_HTTP_CACHE = "walamrt-cache";

    private static volatile WalmartWebClient instance;
    private WalmartApi mWalmartApi;
    private Context mContext;

    public static WalmartWebClient getInstance(Context context) {
        if(instance == null) {
            synchronized (WalmartWebClient.class) {
                if(instance == null) {
                    instance = new WalmartWebClient(context);
                }
            }
        }
        return instance;
    }

    private WalmartWebClient(Context context) {
        mContext = context;
        buildApi();
    }

    public void buildApi() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        File cacheDir = createCacheDir(mContext, WALMART_HTTP_CACHE);
        Cache cache = new Cache(cacheDir, calculateDiskCacheSize(cacheDir));
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .cache(cache).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WALMART_API_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mWalmartApi = retrofit.create(WalmartApi.class);
    }

    private static File createCacheDir(Context mContext, String cacheName) {
        File cacheDir = new File(mContext.getCacheDir(), cacheName);
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
        return cacheDir;
    }

    private static long calculateDiskCacheSize(File dir) {
        long size = MIN_DISK_CACHE_SIZE;
        try {
            StatFs statFs = new StatFs(dir.getAbsolutePath());
            long available = ((long) statFs.getBlockCount()) * statFs.getBlockSize();
            size = available / 50;
        } catch (IllegalArgumentException ignored) {
        }
        // Bound inside min/max size for disk cache.
        return Math.max(Math.min(size, MAX_DISK_CACHE_SIZE), MIN_DISK_CACHE_SIZE);
    }

    public void getProducts(int pageNumber, final Callback<WalmartResponse> callback) {
        mWalmartApi.getProducts(WALMART_API_KEY, pageNumber, PAGE_SIZE)
                .enqueue(callback);

    }
}
