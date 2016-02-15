package walmart.com.walmartproducts;

import android.app.Application;
import android.content.Context;

public class WalmartApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this.getApplicationContext();
    }


    public static Context context() {
        return mContext;
    }

}
