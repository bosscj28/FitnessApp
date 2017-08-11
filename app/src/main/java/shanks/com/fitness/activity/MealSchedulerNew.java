package shanks.com.fitness.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

import shanks.com.fitness.Interfaces.OnWebCall;
import shanks.com.fitness.R;
import shanks.com.fitness.Utils.Logger;
import shanks.com.fitness.Utils.Session;
import shanks.com.fitness.Utils.Utils;
import shanks.com.fitness.Utils.WebCalls;
import shanks.com.fitness.model.Foods;
import shanks.com.fitness.model.MealSchedulerModel;
import shanks.com.fitness.model.MealSchedulerNewModel;

public class MealSchedulerNew extends AppCompatActivity {

    MealSchedulerNewModel model;
    WebCalls getMeals;
    Button meal_submit;
    ArrayList<Foods> myFoods;
    ArrayList<CheckBox> check_foods;
    Session session;
    String from = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_scheduler_new);
        synchronized (this){
            init();
        }
        //getMeals();
    }

    public void init(){
        session = Session.getSession(MealSchedulerNew.this);

        from = getIntent().getExtras().getString("from");

        model = new MealSchedulerNewModel();
        myFoods = new ArrayList<>();
        check_foods = new ArrayList<>();
        meal_submit = (Button)findViewById(R.id.meal_submit);
        meal_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    myFoods.clear();
                }catch (Exception e){
                    e.printStackTrace();
                }

                for (int i=0;i<check_foods.size();i++){
                    CheckBox checkBox = check_foods.get(i);
                    if(checkBox.isChecked()){
                        myFoods.add((Foods)checkBox.getTag());
                    }
                }
                if(myFoods!=null && myFoods.size()>0){
                    if(from.equalsIgnoreCase(Utils.breakfast)){
                        session.setBreakfastDiet(getString(myFoods));
                    } else if(from.equalsIgnoreCase(Utils.lunch)){
                        session.setLunchDiet(getString(myFoods));
                    } else {
                        session.setDinnerDiet(getString(myFoods));
                    }

                    Toast.makeText(MealSchedulerNew.this,"Added to your diet chart",Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MealSchedulerNew.this,"Please select food item from list to your diet chart",Toast.LENGTH_LONG).show();
                }

            }
        });

    }
    private String getString(ArrayList<Foods> obj){
        Gson gson = new Gson();
        return gson.toJson(obj);
    }
    private void getMeals(){
        getMeals = new WebCalls(MealSchedulerNew.this);
        getMeals.showProgress(false);
        getMeals.setWebCallListner(new OnWebCall() {
            @Override
            public void OnWebCallSuccess(String userFullData) {
                try{
                   // parseMeals(userFullData);
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }

            @Override
            public void OnWebCallError(String errorMessage) {
                Logger.showToast(MealSchedulerNew.this,errorMessage);
            }
        });
        getMeals.getMeals();
    }
}
