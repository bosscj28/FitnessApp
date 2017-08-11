package shanks.com.fitness.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import shanks.com.fitness.R;
import shanks.com.fitness.Utils.Logger;
import shanks.com.fitness.Utils.Session;

public class Dashboard extends AppCompatActivity {

    LinearLayout linear_diet_chart,linear_steps,linear_meal_counter,linear_health_blog;
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        init();
    }

    private void init(){
        session = Session.getSession(Dashboard.this);
        Logger.log("user id:::"+session.getUserId());
        /*
        linear_profile = (LinearLayout)findViewById(R.id.linear_profile);
        linear_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dashboard.this,Profile.class));
            }
        });*/

        linear_diet_chart = (LinearLayout)findViewById(R.id.linear_diet_chart);
        linear_diet_chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dashboard.this,DietType.class));
            }
        });

        linear_steps = (LinearLayout)findViewById(R.id.linear_steps);
        linear_steps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dashboard.this,Steps.class));
            }
        });

        linear_meal_counter = (LinearLayout)findViewById(R.id.linear_meal_counter);
        linear_meal_counter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dashboard.this,CalorieBurn.class));
            }
        });
        linear_health_blog = (LinearLayout) findViewById(R.id.linear_health_blog);
        linear_health_blog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this,Health_Blog.class));
            }
        });
        /*
        linear_calorie_burn = (LinearLayout)findViewById(R.id.linear_calorie_burn);
        linear_calorie_burn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dashboard.this,CalorieBurn.class));
            }
        });*/
    }
}
