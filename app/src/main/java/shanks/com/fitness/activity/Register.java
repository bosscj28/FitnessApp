package shanks.com.fitness.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import shanks.com.fitness.Interfaces.OnWebCall;
import shanks.com.fitness.R;
import shanks.com.fitness.Utils.Logger;
import shanks.com.fitness.Utils.WebCalls;
import shanks.com.fitness.model.SignUpModel;

public class Register extends AppCompatActivity implements OnWebCall{

    RadioGroup radioGroup_sex,radioGroup_foodhabit;
    Button login_signup;
    EditText user_name,user_email,user_phone,user_weight,user_height,user_age,user_foodhabit,user_nu_meal,user_allergy,user_disease;
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
        setContentView(R.layout.activity_register);
        init();
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
    private void init(){

        user_name = (EditText)findViewById(R.id.user_name);
        user_email = (EditText)findViewById(R.id.user_email);
        user_phone = (EditText)findViewById(R.id.user_phone);
        user_height = (EditText)findViewById(R.id.user_height);
        user_weight = (EditText)findViewById(R.id.user_weight);
        user_age = (EditText)findViewById(R.id.user_age);
        user_allergy = (EditText)findViewById(R.id.user_allergy);
        user_disease = (EditText)findViewById(R.id.user_disease);
        user_nu_meal = (EditText)findViewById(R.id.user_nu_meal);
        user_foodhabitString=user_sexString="";
        radioGroup_sex = (RadioGroup)findViewById(R.id.radioGroup_sex);
        radioGroup_foodhabit = (RadioGroup)findViewById(R.id.radioGroup_foodhabit);
        radioGroup_sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if(checkedId == R.id.radioButton2) {
                    user_sexString = "male";
                } else if(checkedId == R.id.radioButton) {
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
                if(checkedId == R.id.radioButton_veg) {
                    user_foodhabitString = "veg";
                } else if(checkedId == R.id.radioButton_nonveg) {
                    user_foodhabitString="non-veg";
                }
                else {
                    user_foodhabitString="";
                }
            }
        });
        login_signup = (Button)findViewById(R.id.login_signup);
        login_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                user_nameString = user_name.getText().toString().trim();
                user_emailString = user_email.getText().toString().trim();
                user_phoneString = user_phone.getText().toString().trim();
                user_weightString = user_weight.getText().toString().trim();
                user_heightString = user_height.getText().toString().trim();
                user_ageString = user_age.getText().toString().trim();
                user_allergyString = user_allergy.getText().toString().trim();
                user_diseaseString = user_disease.getText().toString().trim();
                user_nu_mealString = user_nu_meal.getText().toString().trim();

                if(user_nameString.equalsIgnoreCase("")){
                    Logger.showToast(Register.this,Fill_user_name);
                } else if(user_emailString.equalsIgnoreCase("")){
                    Logger.showToast(Register.this,Fill_user_email);
                } else if(!(isValidEmail(user_emailString))){
                    Logger.showToast(Register.this,Fill_user_correct_email);
                } else if(user_phoneString.equalsIgnoreCase("")){
                    Logger.showToast(Register.this,Fill_user_phone);
                }else if(user_phoneString.length()!=10){
                    Logger.showToast(Register.this,Fill_user_phone_invalid);
                }else if(user_weightString.equalsIgnoreCase("")){
                    Logger.showToast(Register.this,Fill_user_weight);
                }else if(user_heightString.equalsIgnoreCase("")){
                    Logger.showToast(Register.this,Fill_user_height);
                }else if(user_ageString.equalsIgnoreCase("")){
                    Logger.showToast(Register.this,Fill_user_age);
                } else if(user_sexString.equalsIgnoreCase("")){
                    Logger.showToast(Register.this,Fill_user_sex);
                }else if(user_foodhabitString.equalsIgnoreCase("")){
                    Logger.showToast(Register.this,Fill_user_foodhabit);
                }else if(user_nu_mealString.equalsIgnoreCase("")){
                    Logger.showToast(Register.this,Fill_user_nu_meal);
                }else if(user_allergyString.equalsIgnoreCase("")){
                    Logger.showToast(Register.this,Fill_user_allergy);
                }else if(user_diseaseString.equalsIgnoreCase("")){
                    Logger.showToast(Register.this,Fill_user_disease);
                } else {
                    if(isOnline(Register.this)) {
                        SignUpModel model = new SignUpModel();
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

                        WebCalls webCalls = new WebCalls(Register.this);
                        webCalls.setWebCallListner(Register.this);
                        webCalls.showProgress(true);
                        webCalls.SignUp(model);
                    }
                    else
                    {
                        Logger.showToast(Register.this,Network_not_connected);
                    }
                }

            }
        });
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
        Logger.showToast(Register.this,userFullData);
        startActivity(new Intent(Register.this,Login.class));
        finish();
    }

    @Override
    public void OnWebCallError(String errorMessage) {
        Logger.showToast(Register.this,errorMessage);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Register.this,Login.class));
        finish();
    }
}
