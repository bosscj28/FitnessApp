package shanks.com.fitness.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

import shanks.com.fitness.Interfaces.Communicator;
import shanks.com.fitness.Interfaces.OnWebCall;
import shanks.com.fitness.R;
import shanks.com.fitness.Utils.Logger;
import shanks.com.fitness.Utils.Session;
import shanks.com.fitness.Utils.Utils;
import shanks.com.fitness.Utils.WebCalls;
import shanks.com.fitness.model.Foods;
import shanks.com.fitness.model.MealSchedulerModel;
import shanks.com.fitness.model.Nutrients;

/**
 * Created by asus on 7/30/2017.
 */

public class AddFoodFragment extends Fragment {
    MealSchedulerModel model;
    WebCalls getMeals;
    Button meal_submit;
    ArrayList<Foods> myFoods;
    ArrayList<CheckBox> check_foods;
    Session session;
    String from = "";
    View v3;
    String message;
    private Context context;
    private Communicator comm;

    public AddFoodFragment(){}
    public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
    public static final AddFoodFragment newInstance(String message)
    {
        AddFoodFragment f = new AddFoodFragment();
        f.message = message;
        Bundle bdl = new Bundle(1);
        bdl.clear();
        bdl.putString(EXTRA_MESSAGE, message);
        f.setArguments(bdl);
        return f;
    }


    public void change(int data,Context context){
        this.context = context;

         }


    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        this.context=context;
        comm = (Communicator)context;

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v3 = inflater.inflate(R.layout.activity_meal_scheduler, container, false);
        //message = getArguments().getString(EXTRA_MESSAGE);
        message =  ((DietType) getActivity()).dataforADDFood;
        int data = Integer.parseInt(message);

        if(data==1) {
            from = Utils.breakfast;
            synchronized (this){
                init();
            }
            getMeals();

        }else if(data==2) {
            from = Utils.lunch;
            synchronized (this){
                init();
            }
            getMeals();


        }else if(data==3){
            from = Utils.dinner;
            synchronized (this){
                init();
            }
            getMeals();

        }


        return v3;
    }
    private void init(){

        session = Session.getSession(context);
        Log.d("CJ PRINT"," MAIN VIEW"+v3);
        model = new MealSchedulerModel();
        myFoods = new ArrayList<>();
        check_foods = new ArrayList<>();
        meal_submit = (Button) v3.findViewById(R.id.meal_submit);
        meal_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                   // myFoods.clear();
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
                        setBreakfastView();
                        session.setBreakfastDiet(getString(myFoods));
                    } else if(from.equalsIgnoreCase(Utils.lunch)){
                        setLunchView();
                        session.setLunchDiet(getString(myFoods));
                    } else {
                        setDinnerView();
                        session.setDinnerDiet(getString(myFoods));
                    }

                    Toast.makeText(context,"Added to your diet chart",Toast.LENGTH_LONG).show();
                    comm.respond(4);
                    ((DietType) getActivity()).viewPager.setCurrentItem(0, true);
                } else {
                    Toast.makeText(context,"Please select food item from list to your diet chart",Toast.LENGTH_LONG).show();
                }

            }
        });
    }
    private String getString(ArrayList<Foods> obj){
        Gson gson = new Gson();
        return gson.toJson(obj);
    }
    private void getMeals(){
        getMeals = new WebCalls(getActivity());
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
                Logger.showToast(context,errorMessage);
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
        Random rand = new Random();
        int randomNumber;
        if(foods.length>0){
            LinearLayout item = (LinearLayout) v3.findViewById(R.id.main_time_table);
                      for (int i=0;i<foods.length;i++){
                if(foods.length>100) {
                    if(i==100){break;}
                }

                randomNumber = rand.nextInt(foods.length) + 1;
                Log.d("CJ PRINT ","RANDOM "+randomNumber+" I "+i);
                Foods model = foods[randomNumber];

                View child = getActivity().getLayoutInflater().inflate(R.layout.day_layout,null);
                TextView day_name = (TextView)child.findViewById(R.id.day_name);
                day_name.setText("Name "+model.getName());

                TextView weight = (TextView)child.findViewById(R.id.weight);
                weight.setText("Weight "+model.getWeight());

                TextView measure = (TextView)child.findViewById(R.id.measure);
                measure.setText("Measure "+model.getMeasure());

                CheckBox check_food = (CheckBox)child.findViewById(R.id.check_food);
                check_food.setTag(model);
                check_foods.add(check_food);

                LinearLayout main_day_table = (LinearLayout)child.findViewById(R.id.main_day_table);

                Nutrients[] innerModels = model.getNutrients();
                for (int j=0;j<innerModels.length;j++){
                    Nutrients innerModel = innerModels[j];
                    View inner_child = getActivity().getLayoutInflater().inflate(R.layout.time_row, null);

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

    public void setBreakfastView(){
        String foodData = session.getBreakfastDiet();
        try {
            JSONArray jarr = new JSONArray(foodData);
            for (int i = 0; i < jarr.length(); i++) {
                JSONObject jobj = jarr.getJSONObject(i);
                Foods foods = new Foods();

                String measure = jobj.getString("measure");
                foods.setMeasure(measure);

                String name = jobj.getString("name");
                foods.setName(name);

                String ndbno = jobj.getString("ndbno");
                foods.setNdbno(ndbno);

                String weight = jobj.getString("weight");
                foods.setWeight(weight);

                JSONArray innerJar = jobj.getJSONArray("nutrients");
                Nutrients[] nutrients = new Nutrients[innerJar.length()];
                for (int j = 0; j < innerJar.length(); j++) {
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


        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
    public void setLunchView(){
        String foodData = session.getLunchDiet();
        try {
            JSONArray jarr = new JSONArray(foodData);
            for (int i = 0; i < jarr.length(); i++) {
                JSONObject jobj = jarr.getJSONObject(i);
                Foods foods = new Foods();

                String measure = jobj.getString("measure");
                foods.setMeasure(measure);

                String name = jobj.getString("name");
                foods.setName(name);

                String ndbno = jobj.getString("ndbno");
                foods.setNdbno(ndbno);

                String weight = jobj.getString("weight");
                foods.setWeight(weight);

                JSONArray innerJar = jobj.getJSONArray("nutrients");
                Nutrients[] nutrients = new Nutrients[innerJar.length()];
                for (int j = 0; j < innerJar.length(); j++) {
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

            } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
    public void setDinnerView(){
        String foodData = session.getDinnerDiet();
        try {
            JSONArray jarr = new JSONArray(foodData);
            for (int i = 0; i < jarr.length(); i++) {
                JSONObject jobj = jarr.getJSONObject(i);
                Foods foods = new Foods();

                String measure = jobj.getString("measure");
                foods.setMeasure(measure);

                String name = jobj.getString("name");
                foods.setName(name);

                String ndbno = jobj.getString("ndbno");
                foods.setNdbno(ndbno);

                String weight = jobj.getString("weight");
                foods.setWeight(weight);

                JSONArray innerJar = jobj.getJSONArray("nutrients");
                Nutrients[] nutrients = new Nutrients[innerJar.length()];
                for (int j = 0; j < innerJar.length(); j++) {
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

           /* if (myFoods != null && myFoods.size() > 0){
                dietAdapter = new dietAdapter(context,myFoods);
                dinnerRV.setAdapter(dietAdapter);
            }*/

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}
