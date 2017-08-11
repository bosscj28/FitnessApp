package shanks.com.fitness.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.view.Display;
import android.view.WindowManager;

import shanks.com.fitness.model.EditModel;
import shanks.com.fitness.model.ForgotModel;
import shanks.com.fitness.model.LoginModel;
import shanks.com.fitness.model.SignUpModel;

/**
 * Created by ankitpurohit on 20-06-2017.
 */

public class Utils {

    public static String userId = "userId";
    public static String username = "username";//
    public static String contact = "contact";
    public static String age = "age";//
    public static String height = "height";
    public static String width = "width";//
    public static String sex = "sex";
    public static String allergy = "allergy";//
    public static String diease = "diease";//
    public static String veg_nonveg = "veg_nonveg";//
    public static String nu_meal = "nu_meal";//
    public static String userEmail = "userEmail";//
    public static String diet = "diet";
    public static String bmi="bmi";
    public static String calorie="calorie";

    public static String breakfast = "breakfast";
    public static String lunch = "lunch";
    public static String dinner = "dinner";

    private static String BASE_URL = "http://personaldietition.online/";
    private static String LOGIN = "login.php";
    private static String REGISTER = "register.php";
    private static String PROFILE = "profile.php";
    private static String FORGOT = "forget_password.php";
    private static String GET_STEPS = "steps_count.php";
    private static String SET_STEPS = "steps.php";
    private static String MEAL_SCHEDULER = "meal.php";
    private static String BLOG_URL  = "";
    public static String IMAGES_URL = "http://personaldietition.online/Images/";

   // private static String MEAL_SCHEDULER = "http://api.nal.usda.gov/ndb/nutrients/?format=json&api_key=yPwTBWaTOgZpXOpkdb1RuNT82pjrNXu21SPf5r3h&nutrients=205&nutrients=204&nutrients=208&nutrients=269";

    public static final String NO_NET = "Internet is not available";

    public static String POST = "POST";
    public static String GET = "GET";
    public static String PUT = "PUT";

    // shared methods
    public static void setShared(Context context, String name, String value) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(name, value);
        editor.commit();
    }

    // shared methods
    public static void setCalorieShared(Context context, String name, String value) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(name, value);
        editor.commit();
    }

    public static void ClearCalorieShared(Context context, String name, String value) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(name, value);
        editor.commit();
    }
    public static String getCalorieShared(Context context, String name, String defaultValue) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        return prefs.getString(name, defaultValue);

    }

    public static void ClearShared(Context context) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();
    }

    public static String getShared(Context context, String name, String defaultValue) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        return prefs.getString(name, defaultValue);

    }


    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @SuppressWarnings("deprecation")
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display d = wm.getDefaultDisplay();

        return (int) d.getHeight();
    }
    @SuppressWarnings("deprecation")
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display d = wm.getDefaultDisplay();

        return (int) d.getWidth();
    }

    static String getRegisterUrl(SignUpModel model){
       return BASE_URL + REGISTER
                +"?email="+model.getEmail()
                +"&name="+model.getName()
                +"&contact="+model.getContact()
                +"&age="+model.getAge()
                +"&sex="+model.getSex()
                +"&nu_meal="+model.getNumberMeal()
                +"&veg_nonveg="+model.getFoodhabit()
                +"&height="+model.getHeight()
                +"&weight="+model.getWeight()
                +"&allergy="+model.getAllergy()
                +"&diseas="+model.getDisease();
    }

    static String getEditUrl(EditModel model){
        return BASE_URL + PROFILE
                +"?email="+model.getEmail()
                +"&name="+model.getName()
                +"&contact="+model.getContact()
                +"&age="+model.getAge()
                +"&sex="+model.getSex()
                +"&nu_meal="+model.getNumberMeal()
                +"&veg_nonveg="+model.getFoodhabit()
                +"&height="+model.getHeight()
                +"&weight="+model.getWeight()
                +"&allergy="+model.getAllergy()
                +"&diseas="+model.getDisease()
                +"&id="+model.getId();
    }
    static String getForgotUrl(ForgotModel model){
        return BASE_URL + FORGOT
                +"?email="+model.getEmail();
    }

    static String getLoginUrl(LoginModel model){
        return BASE_URL + LOGIN
                +"?email="+model.getEmail()
                +"&password="+model.getPassword();
    }

    static String getAllStepsUrl(String custid){
        return BASE_URL + GET_STEPS
                +"?custid="+custid;
    }

    static String setStepsUrl(String steps,String custid){
        return BASE_URL + SET_STEPS
                +"?custid="+custid
                +"&steps="+steps;
    }

    static String getMealScheduler(){
        return BASE_URL+MEAL_SCHEDULER;
    }
}
