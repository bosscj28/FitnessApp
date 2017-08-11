package shanks.com.fitness.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONObject;

import shanks.com.fitness.Interfaces.OnWebCall;
import shanks.com.fitness.R;
import shanks.com.fitness.Utils.Logger;
import shanks.com.fitness.Utils.Session;
import shanks.com.fitness.Utils.Utils;
import shanks.com.fitness.Utils.WebCalls;
import shanks.com.fitness.model.EditModel;
import shanks.com.fitness.model.SignUpModel;

public class Profile extends AppCompatActivity implements OnWebCall {

     EditText edit_name,edit_email,edit_phone,edit_weight,edit_height,edit_age,edit_foodhabit,edit_nu_meal,edit_allergy,edit_disease;
    RadioGroup radioGroup_sex,radioGroup_foodhabit;
    Session session;
    Button save;
    String user_nameString,user_emailString,user_phoneString,user_weightString,user_heightString,user_ageString,user_foodhabitString,user_allergyString,user_diseaseString,user_sexString,user_nu_mealString;
    private final String Fill_user_name = "Please enter user name";
    private final String Fill_user_nu_meal = "Please enter number of meal";
    private final String Fill_user_correct_email = "Please enter valid email";
    private final String Fill_user_email = "Please enter user email";
    private final String Fill_user_phone = "Please enter user phone number";
    private final String Fill_user_phone_invalid = "Please enter valid number";
    private final String Fill_user_weight = "Please enter user weight";
    private final String Fill_user_height = "Please enter user height";
    private final String Fill_user_sex = "Please enter user sex";
    private final String Fill_user_age = "Please enter user age";
    private final String Fill_user_foodhabit = "Please enter user foodhabit";
    private final String Fill_user_allergy = "Please enter user allergy";
    private final String Fill_user_disease = "Please enter user disease";
    private final String Network_not_connected = "Internet Not Connected! Please Try Again";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        init();
    }

    private void init(){

        session = Session.getSession(Profile.this);

        edit_name = (EditText)findViewById(R.id.edit_name);
        edit_email = (EditText)findViewById(R.id.edit_email);
        edit_phone = (EditText)findViewById(R.id.edit_phone);
        edit_height = (EditText)findViewById(R.id.edit_height);
        edit_weight = (EditText)findViewById(R.id.edit_weight);
        edit_age = (EditText)findViewById(R.id.edit_age);
        edit_allergy = (EditText)findViewById(R.id.edit_allergy);
        edit_disease = (EditText)findViewById(R.id.edit_disease);
        edit_nu_meal = (EditText)findViewById(R.id.edit_nu_meal);
        edit_email.setEnabled(false);

        edit_name.setText(""+session.getUsername());
        edit_email.setText(""+session.getUserEmail());
        edit_phone.setText(""+session.getContact());
        edit_age.setText(""+session.getAge());
        edit_height.setText(""+session.getHeight());
        edit_allergy.setText(""+session.getAllergy());
        edit_weight.setText(""+session.getWidth());
        edit_nu_meal.setText(""+session.getNu_meal());
        edit_disease.setText(""+session.getDiease());
        radioGroup_sex  = (RadioGroup)findViewById(R.id.radioGroup_sex_edit);
        radioGroup_foodhabit = (RadioGroup)findViewById(R.id.radioGroup_foodhabit_edit);
        user_foodhabitString=user_sexString="";
        String sex = session.getSex();
        Log.d("CJ PRINT","SEX - "+sex.trim());
        if(sex.trim().equals("male"))
        {
           radioGroup_sex.check(R.id.radioButton_male);
            user_sexString = "male";
        }else if(sex.trim().equals("female"))
        {
            radioGroup_sex.check(R.id.radioButton_femal);
            user_sexString = "female";
        }

        String foodhabbit = session.getVeg_nonveg();
        if(foodhabbit.trim().equals("veg"))
        {
          radioGroup_foodhabit.check(R.id.radioButton_veg_edit);
            user_foodhabitString = "veg";
        }else if(foodhabbit.trim().equals("non-veg"))
        {
            radioGroup_foodhabit.check(R.id.radioButton_nonveg_edit);
            user_foodhabitString = "non-veg";
        }

        radioGroup_sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if(checkedId == R.id.radioButton_male) {
                    user_sexString = "male";
                } else if(checkedId == R.id.radioButton_femal) {
                    user_sexString="female";
                }
                else {
                    user_sexString="";
                }
            }
        });
        radioGroup_foodhabit.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if(checkedId == R.id.radioButton_veg_edit) {
                    user_foodhabitString = "veg";
                } else if(checkedId == R.id.radioButton_nonveg_edit) {
                    user_foodhabitString="non-veg";
                }
                else {
                   user_foodhabitString="";
                }
            }
        });



        save = (Button)findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // int no_meals = Integer.parseInt(edit_diet_plan.getText().toString());
                //int setAlarm = (Integer)(18/no_meals);
                user_nameString = edit_name.getText().toString().trim();
                user_emailString = edit_email.getText().toString().trim();
                user_phoneString = edit_phone.getText().toString().trim();
                user_weightString = edit_weight.getText().toString().trim();
                user_heightString = edit_height.getText().toString().trim();
                user_ageString = edit_age.getText().toString().trim();
                user_allergyString = edit_allergy.getText().toString().trim();
                user_diseaseString = edit_disease.getText().toString().trim();
                user_nu_mealString = edit_nu_meal.getText().toString().trim();

                if(user_nameString.equalsIgnoreCase("")){
                    Logger.showToast(Profile.this,Fill_user_name);
                } else if(user_emailString.equalsIgnoreCase("")){
                    Logger.showToast(Profile.this,Fill_user_email);
                } else if(!(isValidEmail(user_emailString))){
                    Logger.showToast(Profile.this,Fill_user_correct_email);
                } else if(user_phoneString.equalsIgnoreCase("")){
                    Logger.showToast(Profile.this,Fill_user_phone);
                }else if(user_phoneString.length()!=10){
                    Logger.showToast(Profile.this,Fill_user_phone_invalid);
                }else if(user_weightString.equalsIgnoreCase("")){
                    Logger.showToast(Profile.this,Fill_user_weight);
                }else if(user_heightString.equalsIgnoreCase("")){
                    Logger.showToast(Profile.this,Fill_user_height);
                }else if(user_ageString.equalsIgnoreCase("")){
                    Logger.showToast(Profile.this,Fill_user_age);
                } else if(user_sexString.equalsIgnoreCase("")){
                    Logger.showToast(Profile.this,Fill_user_sex);
                }else if(user_foodhabitString.equalsIgnoreCase("")){
                    Logger.showToast(Profile.this,Fill_user_foodhabit);
                }else if(user_nu_mealString.equalsIgnoreCase("")){
                    Logger.showToast(Profile.this,Fill_user_nu_meal);
                }else if(user_allergyString.equalsIgnoreCase("")){
                    Logger.showToast(Profile.this,Fill_user_allergy);
                }else if(user_diseaseString.equalsIgnoreCase("")){
                    Logger.showToast(Profile.this,Fill_user_disease);
                } else {
                    if(isOnline(Profile.this)) {
                       EditModel model = new EditModel();
                        model.setEmail(user_emailString);
                        model.setContact(user_phoneString);
                        model.setName(user_nameString);
                        model.setAge(user_ageString);
                        model.setHeight(user_heightString);
                        model.setWeight(user_weightString);
                        model.setSex(user_sexString);
                        model.setFoodhabit(user_foodhabitString);
                        model.setNumberMeal(user_nu_mealString);
                        model.setAllergy(user_allergyString);
                        model.setDisease(user_diseaseString);
                        model.setId(session.getUserId());


                        WebCalls webCalls = new WebCalls(Profile.this);
                        webCalls.setWebCallListner(Profile.this);
                        webCalls.showProgress(true);
                        webCalls.EditUser(model);
                    }
                    else
                    {
                        Logger.showToast(Profile.this,Network_not_connected);
                    }
                }
            }
        });
    }
    private boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        }
        return false;
    }
    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
    @Override
    public void OnWebCallSuccess(String userFullData) {
        try{
            synchronized (this) {

                JSONObject jobj = new JSONObject(userFullData);

                String username = jobj.getString("name");
                session.setUsername(username);

                String contact = jobj.getString("contact");
                session.setContact(contact);

                String id = jobj.getString("id");
                session.setUserId(id);

                String age = jobj.getString("age");
                session.setAge(age);

                String height = jobj.getString("height");
                session.setHeight(height);

                String widht = jobj.getString("weight");
                session.setWidth(widht);

                String sex = jobj.getString("sex");
                session.setSex(sex);

                String allergy = jobj.getString("allergy");
                session.setAllergy(allergy);

                String diease = jobj.getString("diseas");
                session.setDiease(diease);

                String veg_nonveg = jobj.getString("veg_nonveg");
                session.setVeg_nonveg(veg_nonveg);

                String nu_meal = jobj.getString("nu_meal");
                session.setNu_meal(nu_meal);

                //String email = jobj.getString("email");
                //session.setUserEmail(email);

            }



        }catch (Exception ex){
            ex.printStackTrace();
        }
        startActivity(new Intent(Profile.this,MainDashboard.class));
        finish();
    }

    @Override
    public void OnWebCallError(String errorMessage) {
        Logger.showToast(Profile.this,errorMessage);
    }

}
