package ltx.dollarnotification.helpers;

import android.app.Application;
import android.content.Context;


public class App extends Application {

    private static Context currentContext;

    @Override
    public void onCreate() {
        super.onCreate();
        currentContext = this;
    }

    public static Context getContext(){
        return currentContext;
    }
}
