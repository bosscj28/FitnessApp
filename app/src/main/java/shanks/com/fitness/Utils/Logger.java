package shanks.com.fitness.Utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by ankitpurohit on 20-06-2017.
 */

public class Logger {
    public static void log(String message){
        Log.wtf("ankit",message);
    }

    public static void showToast(Context context,String message){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }
}
