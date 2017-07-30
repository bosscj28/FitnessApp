package shanks.com.fitness.Utils;

import android.content.Context;

/**
 * Created by ankitpurohit on 20-06-2017.
 */

public class Session {
    private static Session instance = null;
    Context context;

    public static Session getSession(Context context) {
        if(instance == null) {
            instance = new Session(context);
        }
        return instance;
    }
    public static void ClearSession(Context context)
    {
        Utils.ClearShared(context);
    }

    protected Session(Context context) {
        this.context = context;
    }

    public String getUserId() {
        return Utils.getShared(context,Utils.userId,"");
    }

    public void setUserId(String userId) {
        Utils.setShared(context,Utils.userId,userId);
    }

    public String getUsername() {
        return Utils.getShared(context,Utils.username,"");
    }

    public void setUsername(String username) {
        Utils.setShared(context,Utils.username,username);
    }

    public String getContact() {
        return Utils.getShared(context,Utils.contact,"");
    }

    public void setContact(String contact) {
        Utils.setShared(context,Utils.contact,contact);
    }


    public String getAge() {
        return Utils.getShared(context,Utils.age,"");
    }

    public void setAge(String age) {
        Utils.setShared(context,Utils.age,age);
    }


    public String getHeight() {
        return Utils.getShared(context,Utils.height,"");
    }

    public void setHeight(String height) {
        Utils.setShared(context,Utils.height,height);
    }

    public String getWidth() {
        return Utils.getShared(context,Utils.width,"");
    }

    public void setWidth(String width) {
        Utils.setShared(context,Utils.width,width);
    }


    public String getSex() {
        return Utils.getShared(context,Utils.sex,"");
    }

    public void setSex(String sex) {
        Utils.setShared(context,Utils.sex,sex);
    }


    public String getAllergy() {
        return Utils.getShared(context,Utils.allergy,"");
    }

    public void setAllergy(String allergy) {
        Utils.setShared(context,Utils.allergy,allergy);
    }

    public String getDiease() {
        return Utils.getShared(context,Utils.diease,"");
    }

    public void setDiease(String diease) {
        Utils.setShared(context,Utils.diease,diease);
    }

    public String getVeg_nonveg() {
        return Utils.getShared(context,Utils.veg_nonveg,"");
    }

    public void setVeg_nonveg(String veg_nonveg) {
        Utils.setShared(context,Utils.veg_nonveg,veg_nonveg);
    }

    public String getNu_meal() {
        return Utils.getShared(context,Utils.nu_meal,"");
    }

    public void setNu_meal(String nu_meal) {
        Utils.setShared(context,Utils.nu_meal,nu_meal);
    }

    public String getUserEmail() {
        return Utils.getShared(context,Utils.userEmail,"");
    }

    public void setUserEmail(String userEmail) {
        Utils.setShared(context,Utils.userEmail,userEmail);
    }


//    public String getDiet() {
//        return Utils.getShared(context,Utils.diet,"");
//    }
//
//    public void setDiet(String diet) {
//        Utils.setShared(context,Utils.diet,diet);
//    }

//    private String breakfastDiet = "";
//    private String lunchDiet = "";
//    private String dinnerDiet = "";

    public String getBreakfastDiet() {
        return Utils.getShared(context,Utils.breakfast,"");
    }

    public void setBreakfastDiet(String breakfastDiet) {
        Utils.setShared(context,Utils.breakfast,breakfastDiet);
    }

    public String getLunchDiet() {
        return Utils.getShared(context,Utils.lunch,"");
    }

    public void setLunchDiet(String lunchDiet) {
        Utils.setShared(context,Utils.lunch,lunchDiet);
    }

    public String getDinnerDiet() {
        return Utils.getShared(context,Utils.dinner,"");
    }

    public void setDinnerDiet(String dinnerDiet) {
        Utils.setShared(context,Utils.dinner,dinnerDiet);
    }
}
