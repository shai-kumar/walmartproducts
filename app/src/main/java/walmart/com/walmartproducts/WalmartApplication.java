package walmart.com.walmartproducts;

import android.app.Application;
import android.content.Context;

public class WalmartApplication extends Application {

    private static Context mContext;
    private static Application mApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this.getApplicationContext();
        mApplication = this;
    }


    public static Context context() {
        return mContext;
    }

    public static Context application() {
        return mApplication;
    }

}
