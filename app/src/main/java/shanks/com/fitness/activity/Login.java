package shanks.com.fitness.activity;

import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import shanks.com.fitness.Interfaces.OnWebCall;
import shanks.com.fitness.R;
import shanks.com.fitness.Utils.Logger;
import shanks.com.fitness.Utils.Session;
import shanks.com.fitness.Utils.WebCalls;
import shanks.com.fitness.model.LoginModel;

public class Login extends AppCompatActivity implements OnWebCall{

    Button login_signup,login_button;
    EditText user_name,password;
    String userNameString = "";
    String passwordString = "";
    Session session;
    TextView forgot_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        init();
    }

    private void init(){

        session = Session.getSession(Login.this);
        forgot_password = (TextView)findViewById(R.id.forgot_password);
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this,ForgotPassword.class));
            }
        });

        user_name = (EditText)findViewById(R.id.user_name);
        password = (EditText)findViewById(R.id.password);

        login_signup = (Button)findViewById(R.id.login_signup);
        login_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendToRegister();

            }
        });

        login_button = (Button)findViewById(R.id.login_button);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userNameString = user_name.getText().toString().trim();
                passwordString = password.getText().toString().trim();

                if(userNameString.equalsIgnoreCase("")){
                    Logger.showToast(Login.this,"Please enter username");
                } else if(passwordString.equalsIgnoreCase("")){
                    Logger.showToast(Login.this,"Please enter password");
                } else {
                    LoginModel model = new LoginModel();
                    model.setEmail(userNameString);
                    model.setPassword(passwordString);

                    WebCalls webCalls = new WebCalls(Login.this);
                    webCalls.showProgress(true);
                    webCalls.setWebCallListner(Login.this);
                    webCalls.login(model);
                }
            }
        });

    }

    private void sendToRegister(){
        startActivity(new Intent(Login.this,Register.class));
        finish();
    }

    private void sendToDashBoard(){
        startActivity(new Intent(Login.this,MainDashboard.class));
        finish();
    }


    @Override
    public void OnWebCallSuccess(String userFullData) {
        try{
            synchronized (this){
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

                session.setUserEmail(""+userNameString);
            }

            sendToDashBoard();

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void OnWebCallError(String errorMessage) {
            Logger.showToast(Login.this,errorMessage);
    }
}
