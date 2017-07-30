package shanks.com.fitness.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import shanks.com.fitness.R;
import shanks.com.fitness.Utils.Utils;

public class MealTypeScheduler extends AppCompatActivity {

    Button break_fast,lunch,dinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_type_scheduler);
        init();
    }

    private void init(){
        break_fast = (Button)findViewById(R.id.break_fast);
        break_fast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMealScheduler(Utils.breakfast);
            }
        });

        lunch = (Button)findViewById(R.id.lunch);
        lunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMealScheduler(Utils.lunch);
            }
        });

        dinner = (Button)findViewById(R.id.dinner);
        dinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMealScheduler(Utils.dinner);
            }
        });
    }

    private void startMealScheduler(String from){
        Intent intent = new Intent(MealTypeScheduler.this,MealScheduler.class);
        intent.putExtra("from",from);
        startActivity(intent);
    }
}
