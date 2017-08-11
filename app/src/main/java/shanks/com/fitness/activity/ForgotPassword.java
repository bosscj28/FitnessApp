package shanks.com.fitness.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;

import shanks.com.fitness.Interfaces.OnWebCall;
import shanks.com.fitness.R;
import shanks.com.fitness.Utils.Logger;
import shanks.com.fitness.Utils.Utils;
import shanks.com.fitness.Utils.WebCalls;
import shanks.com.fitness.model.ForgotModel;
import shanks.com.fitness.model.SignUpModel;

public class ForgotPassword extends AppCompatActivity implements OnWebCall {

    EditText email;
    Button submit;
    private final String Fill_user_email = "Please enter user email";
    private final String Fill_user_correct_email = "Please enter valid email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        email = (EditText) findViewById(R.id.email_forgot);
        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailStr = email.getText().toString().trim();
                if(emailStr.equalsIgnoreCase("")){
                    Logger.showToast(ForgotPassword.this,Fill_user_email);
                } else if(!(Utils.isValidEmail(emailStr))){
                    Logger.showToast(ForgotPassword.this,Fill_user_correct_email);
                } else {
                    ForgotModel model = new ForgotModel();
                    model.setEmail(emailStr);
                    WebCalls webCalls = new WebCalls(ForgotPassword.this);
                    webCalls.setWebCallListner(ForgotPassword.this);
                    webCalls.showProgress(true);
                    webCalls.ForgotPassword(model);

                }
            }
        });

    }
    @Override
    public void OnWebCallSuccess(String userFullData) {
        try{
            synchronized (this){
                Logger.showToast(ForgotPassword.this,"Password Sended to your email id and Mobile Number.");
           }

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void OnWebCallError(String errorMessage) {
        Logger.showToast(ForgotPassword.this,errorMessage);
    }
}
