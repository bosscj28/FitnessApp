package shanks.com.fitness.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Bundle;

import shanks.com.fitness.R;
import shanks.com.fitness.Utils.Session;

public class Splash extends Activity {

    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        init();
    }

    private void init(){

        synchronized (this){
            session = Session.getSession(Splash.this);
        }

        new CountDownTimer(3000,1000){
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                if(session.getUserId().equalsIgnoreCase("")){
                    startActivity(new Intent(Splash.this,Login.class));
                    finish();
                } else {
                    startActivity(new Intent(Splash.this,MainDashboard.class));
                    finish();
                }

            }
        }.start();
    }
}
