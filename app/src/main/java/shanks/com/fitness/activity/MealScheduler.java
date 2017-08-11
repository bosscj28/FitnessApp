package shanks.com.fitness.activity;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import shanks.com.fitness.model.Nutrients;

public class MealScheduler extends AppCompatActivity {

    MealSchedulerModel model;
    WebCalls getMeals;
    Button meal_submit;
    ArrayList<Foods> myFoods;
    ArrayList<CheckBox> check_foods;
    Session session;
    String from = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_scheduler);
        synchronized (this){
            init();
        }
        getMeals();
    }

    private void init(){
        session = Session.getSession(MealScheduler.this);

        from = getIntent().getExtras().getString("from");

        model = new MealSchedulerModel();
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

                    Toast.makeText(MealScheduler.this,"Added to your diet chart",Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MealScheduler.this,"Please select food item from list to your diet chart",Toast.LENGTH_LONG).show();
                }

            }
        });
    }


    private String getString(ArrayList<Foods> obj){
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

    private void getMeals(){
        getMeals = new WebCalls(MealScheduler.this);
        getMeals.showProgress(false);
        getMeals.setWebCallListner(new OnWebCall() {
            @Override
            public void OnWebCallSuccess(String userFullData) {
                try{
                    parseMeals(userFullData);
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }

            @Override
            public void OnWebCallError(String errorMessage) {
                Logger.showToast(MealScheduler.this,errorMessage);
            }
        });
        getMeals.getMeals();
    }

    private void parseMeals(String response) throws Exception{
        Gson gson = new Gson();
        model = gson.fromJson(response,MealSchedulerModel.class);
        synchronized (this){
            populateViews(model.getReport().getFoods());
        }
        new CountDownTimer(2000,1000){
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                getMeals.doneProgress();
                Log.wtf("ankit"," ::: got it");
            }
        }.start();



    }

    private void populateViews(Foods[] foods){
        if(foods.length>0){
            LinearLayout item = (LinearLayout)findViewById(R.id.main_time_table);
            for (int i=0;i<foods.length;i++){

                Foods model = foods[i];

                View child = getLayoutInflater().inflate(R.layout.day_layout, null);
                TextView day_name = (TextView)child.findViewById(R.id.food_name);
                day_name.setText("Name "+model.getName());

                TextView weight = (TextView)child.findViewById(R.id.food_type);
                weight.setText("Weight "+model.getWeight());

                TextView measure = (TextView)child.findViewById(R.id.carb_type);
                measure.setText("Measure "+model.getMeasure());

                CheckBox check_food = (CheckBox)child.findViewById(R.id.check_food);
                check_food.setTag(model);
                check_foods.add(check_food);

                LinearLayout main_day_table = (LinearLayout)child.findViewById(R.id.main_day_table);

                Nutrients[] innerModels = model.getNutrients();
                for (int j=0;j<innerModels.length;j++){
                    Nutrients innerModel = innerModels[j];
                    View inner_child = getLayoutInflater().inflate(R.layout.time_row, null);

                    TextView nutrient_id = (TextView)inner_child.findViewById(R.id.nutrient_id);
                    nutrient_id.setText(""+innerModel.getNutrient_id());

                    TextView nutrient = (TextView)inner_child.findViewById(R.id.nutrient);
                    nutrient.setText(""+innerModel.getNutrient());

                    TextView unit = (TextView)inner_child.findViewById(R.id.unit);
                    unit.setText(""+innerModel.getUnit());

                    TextView value = (TextView)inner_child.findViewById(R.id.value);
                    value.setText(""+innerModel.getValue());

                    TextView gm = (TextView)inner_child.findViewById(R.id.gm);
                    gm.setText(""+innerModel.getGm());

                    main_day_table.addView(inner_child);
                }

                item.addView(child);
            }
        }

    }

}
