package shanks.com.fitness.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import shanks.com.fitness.R;
import shanks.com.fitness.Utils.Session;
import shanks.com.fitness.Utils.Utils;
import shanks.com.fitness.model.Foods;
import shanks.com.fitness.model.Nutrients;

public class DietChart extends AppCompatActivity {

    Session session;
    ArrayList<Foods> myFoods;
    String foodData = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_chart);
        init();
        Utils.ClearCalorieShared(getApplicationContext(),"Calorie","0");
    }

    private void init(){
        session = Session.getSession(DietChart.this);
        myFoods = new ArrayList<>();

        String from = getIntent().getExtras().getString("from");
        if(from.equalsIgnoreCase(Utils.breakfast)){
            foodData = session.getBreakfastDiet();
        } else if(from.equalsIgnoreCase(Utils.lunch)){
            foodData = session.getLunchDiet();
        } else {
            foodData = session.getDinnerDiet();
        }

//        [{"measure":"0.5 cup","name":"Abiyuch, raw","ndbno":"09427"
// ,"nutrients":[{"gm":"69.0","nutrient":"Energy","nutrient_id":"208","unit":"kcal"
// ,"value":"79"},
// {"gm":"8.55","nutrient":"Sugars, total",
// "nutrient_id":"269","unit":"g","value":"9.75"},
// {"gm":"0.1","nutrient":"Total lipid (fat)",
// "nutrient_id":"204","unit":"g","value":"0.11"},
// {"gm":"17.6","nutrient":"Carbohydrate, by difference","nutrient_id":"205",
// "unit":"g","value":"20.06"}]
// ,"weight":"114.0"},
// {"measure":"1.0 cup","name":"Acerola juice, raw","ndbno":"09002","nutrients":[{"gm":"23.0","nutrient":"Energy","nutrient_id":"208","unit":"kcal","value":"56"},{"gm":"4.5","nutrient":"Sugars, total","nutrient_id":"269","unit":"g","value":"10.89"},{"gm":"0.3","nutrient":"Total lipid (fat)","nutrient_id":"204","unit":"g","value":"0.73"},{"gm":"4.8","nutrient":"Carbohydrate, by difference","nutrient_id":"205","unit":"g","value":"11.62"}],"weight":"242.0"}]

        try{
            JSONArray jarr = new JSONArray(foodData);
            for (int i=0;i<jarr.length();i++){
                JSONObject jobj = jarr.getJSONObject(i);
                Foods foods = new Foods();

                String  measure = jobj.getString("measure");
                foods.setMeasure(measure);

                String name = jobj.getString("name");
                foods.setName(name);

                String ndbno = jobj.getString("ndbno");
                foods.setNdbno(ndbno);

                String weight = jobj.getString("weight");
                foods.setWeight(weight);

                JSONArray innerJar = jobj.getJSONArray("nutrients");
                Nutrients[] nutrients = new Nutrients[innerJar.length()];
                for (int j=0;j<innerJar.length();j++){
                    JSONObject innerJob = innerJar.getJSONObject(j);
                    Nutrients nutrient = new Nutrients();

                    String gm = innerJob.getString("gm");
                    nutrient.setGm(gm);

                    String _nutrient = innerJob.getString("nutrient");
                    nutrient.setNutrient(_nutrient);

                    String nutrient_id = innerJob.getString("nutrient_id");
                    nutrient.setNutrient_id(nutrient_id);

                    String unit = innerJob.getString("unit");
                    nutrient.setUnit(unit);

                    String value = innerJob.getString("value");
                    nutrient.setValue(value);

                    nutrients[j] = nutrient;
                }
                foods.setNutrients(nutrients);

                myFoods.add(foods);
            }

            if(myFoods!=null && myFoods.size()>0)
                populateViews();
        }catch (Exception ex){
            ex.printStackTrace();
        }

//        Gson gson = new Gson();
//        myFoods = gson.fromJson(session.getDiet(),myFoods.getClass());
//        Log.wtf("ankit","::::"+myFoods.size());
//
//        if(myFoods!=null && myFoods.size()>0){
//            populateViews();
//        }
    }

    private void populateViews(){
        if(myFoods.size()>0){
            LinearLayout item = (LinearLayout)findViewById(R.id.main_time_table);
            for (int i=0;i<myFoods.size();i++){

                Foods model = myFoods.get(i);

                View child = getLayoutInflater().inflate(R.layout.day_layout, null);
                TextView day_name = (TextView)child.findViewById(R.id.food_name);
                day_name.setText("Name "+model.getName());

                TextView weight = (TextView)child.findViewById(R.id.food_type);
                weight.setText("Weight "+model.getWeight());

                TextView measure = (TextView)child.findViewById(R.id.carb_type);
                measure.setText("Measure "+model.getMeasure());

                CheckBox check_food = (CheckBox)child.findViewById(R.id.check_food);
                check_food.setVisibility(View.GONE);

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
