package shanks.com.fitness.activity;

import android.content.Context;
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

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Random;

import shanks.com.fitness.Interfaces.Communicator;
import shanks.com.fitness.Interfaces.OnWebCall;
import shanks.com.fitness.R;
import shanks.com.fitness.Utils.Logger;
import shanks.com.fitness.Utils.Session;
import shanks.com.fitness.Utils.Utils;
import shanks.com.fitness.Utils.WebCalls;
import shanks.com.fitness.model.MealSchedulerNewModel;

public class SuggestedFood extends AppCompatActivity {
    MealSchedulerNewModel model;
    WebCalls getMeals;
    Session session;
    ArrayList<MealSchedulerNewModel> FOODARRAYLIST;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_food);
        init();
        getMeals();
    }
    private void init(){
        session = Session.getSession(SuggestedFood.this);
        TextView cal = (TextView) findViewById(R.id.textView10);
        cal.setText(cal.getText()+" "+session.getCalorie());
        model = new MealSchedulerNewModel();
        FOODARRAYLIST = new ArrayList<>();

    }
    private void getMeals(){
        getMeals = new WebCalls(SuggestedFood.this);
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
                Logger.showToast(SuggestedFood.this,errorMessage);
            }
        });
        getMeals.getMeals();
    }

    private void parseMeals(String response) throws Exception{
       /* Gson gson = new Gson();
        model = gson.fromJson(response,MealSchedulerNewModel.class);*/
        JSONArray jarr = new JSONArray(response);
        for(int i=0;i<jarr.length();i++)
        {
            MealSchedulerNewModel model = new MealSchedulerNewModel();
            model.setId(jarr.getJSONObject(i).getString("id"));
            model.setFood(jarr.getJSONObject(i).getString("Food"));
            model.setFood_type(jarr.getJSONObject(i).getString("Food_type"));
            model.setCrabtype(jarr.getJSONObject(i).getString("crabtype"));
            model.setImgurl(jarr.getJSONObject(i).getString("imgurl"));
            model.setEnergy(Float.parseFloat(jarr.getJSONObject(i).getString("Energy")));
            model.setFat(Float.parseFloat(jarr.getJSONObject(i).getString("Protein")));
            model.setProtein(Float.parseFloat(jarr.getJSONObject(i).getString("Fat")));
            FOODARRAYLIST.add(model);
        }
        synchronized (this){
            populateViews(FOODARRAYLIST);
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

    private void populateViews(ArrayList<MealSchedulerNewModel> foods){

        if(foods.size()>0){
            LinearLayout item = (LinearLayout) findViewById(R.id.main_time_table);
            String prev = session.getCalorie();
            Float Cal = Float.parseFloat(prev);
            Float CalNew = 0.00f ;
            for (int i=0;i<foods.size();i++){
                if(foods.get(i).getEnergy() < 100) {
                    CalNew = CalNew + foods.get(i).getEnergy();
                    if(CalNew > Cal) { break; }
                    View child = SuggestedFood.this.getLayoutInflater().inflate(R.layout.item_new_meals, null);
                    TextView day_name = (TextView) child.findViewById(R.id.food_name);
                    day_name.setText(foods.get(i).getFood());

                    TextView weight = (TextView) child.findViewById(R.id.food_type);
                    weight.setText(foods.get(i).getFood_type());

                    TextView measure = (TextView) child.findViewById(R.id.carb_type);
                    measure.setText(foods.get(i).getCrabtype());

                    CheckBox checkBox = (CheckBox) child.findViewById(R.id.check_food);
                    checkBox.setVisibility(View.GONE);
                    LinearLayout main_day_table = (LinearLayout) child.findViewById(R.id.main_day_table);

                    for (int j = 0; j < 3; j++) {
                        View inner_child = SuggestedFood.this.getLayoutInflater().inflate(R.layout.item_new_meals_row, null);

                        TextView nutrient = (TextView) inner_child.findViewById(R.id.nutrient);
                        if (j == 0) {
                            nutrient.setText("" + "Energy");
                        } else if (j == 1) {
                            nutrient.setText("" + "Proteins");
                        } else if (j == 2) {
                            nutrient.setText("" + "Fat");
                        }

                        TextView unit = (TextView) inner_child.findViewById(R.id.unit);

                        if (j == 0) {
                            unit.setText("" + "kcal");
                        } else if (j == 1) {
                            unit.setText("" + "Gm");
                        } else if (j == 2) {
                            unit.setText("" + "Gm");
                        }

                        TextView value = (TextView) inner_child.findViewById(R.id.value);
                        if (j == 0) {
                            value.setText("" + foods.get(i).getEnergy());
                        } else if (j == 1) {
                            value.setText("" + foods.get(i).getProtein());
                        } else if (j == 2) {
                            value.setText("" + foods.get(i).getFat());
                        }

                        main_day_table.addView(inner_child);
                    }
                    item.addView(child);
                }
            }
        }

    }
}
