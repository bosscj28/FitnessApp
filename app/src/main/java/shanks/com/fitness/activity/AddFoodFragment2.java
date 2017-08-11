package shanks.com.fitness.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
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
import shanks.com.fitness.model.MealSchedulerNewModel;
import shanks.com.fitness.model.Nutrients;

/**
 * Created by asus on 7/30/2017.
 */

public class AddFoodFragment2 extends Fragment {
    MealSchedulerNewModel model;
    WebCalls getMeals;
    Button meal_submit;
    ArrayList<MealSchedulerNewModel> myFoods;
    ArrayList<CheckBox> check_foods;
    Session session;
    String from = "";
    View v3;
    String message;
    private Context context;
    private Communicator comm;
    ArrayList<MealSchedulerNewModel> FOODARRAYLIST;
    Spinner viewFrom;
    //false for manual, true for suggested
    boolean viewFromBool = false;

    public AddFoodFragment2(){}
    public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
    public static final AddFoodFragment2 newInstance(String message)
    {
        AddFoodFragment2 f = new AddFoodFragment2();
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
        v3 = inflater.inflate(R.layout.activity_meal_scheduler_new, container, false);
        //message = getArguments().getString(EXTRA_MESSAGE);
        message =  ((DietType) getActivity()).dataforADDFood;
        int data = Integer.parseInt(message);

        if(data==1) {
            from = Utils.breakfast;
            synchronized (this){
                init();
            }
          //  getMeals();

        }else if(data==2) {
            from = Utils.lunch;
            synchronized (this){
                init();
            }
        //    getMeals();


        }else if(data==3){
            from = Utils.dinner;
            synchronized (this){
                init();
            }
          //  getMeals();

        }


        return v3;
    }
    private void init(){

        session = Session.getSession(context);
        viewFrom = (Spinner) v3.findViewById(R.id.spinner);
        final List<String> list = new ArrayList<String>();
        list.add("Suggested Food");
        list.add("Manual Food");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        viewFrom.setAdapter(dataAdapter);
        viewFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(list.get(position).equals("Suggested Food"))
                {
                    viewFromBool = true;
                    getMeals();
                }else  if(list.get(position).equals("Manual Food"))
                {
                    viewFromBool = false;
                    getMeals();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                viewFromBool = false;
            }
        });
        Log.d("CJ PRINT"," MAIN VIEW"+v3);
        model = new MealSchedulerNewModel();
        FOODARRAYLIST = new ArrayList<>();
        myFoods = new ArrayList<>();
        check_foods = new ArrayList<>();
        meal_submit = (Button) v3.findViewById(R.id.meal_submit);
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
                        //myFoods.add((Foods)checkBox.getTag());
                        //Log.d("CJ PRINT","TAG OF CHECKBOX - "+checkBox.getTag());
                        for(int j=0;j<FOODARRAYLIST.size();j++)
                        {
                            if(FOODARRAYLIST.get(j).getId()==checkBox.getTag())
                            {
                                MealSchedulerNewModel m = new MealSchedulerNewModel();
                                m.setId(FOODARRAYLIST.get(j).getId());
                                m.setFood(FOODARRAYLIST.get(j).getFood());
                                m.setFood_type(FOODARRAYLIST.get(j).getFood_type());
                                m.setImgurl(FOODARRAYLIST.get(j).getImgurl());
                                m.setCrabtype(FOODARRAYLIST.get(j).getCrabtype());
                                m.setEnergy(FOODARRAYLIST.get(j).getEnergy());
                                m.setProtein(FOODARRAYLIST.get(j).getProtein());
                                m.setFat(FOODARRAYLIST.get(j).getFat());
                                myFoods.add(m);
                                break;
                            }
                        }
                    }
                }
                if(myFoods!=null && myFoods.size()>0) {
                    Float CalReq = Float.parseFloat(session.getCalorie());
                    Float CalStored = Float.parseFloat(Utils.getCalorieShared(context,"Calorie"+session.getUserId(),"0"));
                    Float CalFood = 0.0f;
                    CalFood = CalStored;
                    for (int i = 0; i < myFoods.size(); i++) {
                        CalFood = CalFood + myFoods.get(i).getEnergy();

                    }
                    if (CalFood <= CalReq) {
                        if (from.equalsIgnoreCase(Utils.breakfast)) {
                            setBreakfastView();
                            session.setBreakfastDiet(getString(myFoods));
                        } else if (from.equalsIgnoreCase(Utils.lunch)) {
                            setLunchView();
                            session.setLunchDiet(getString(myFoods));
                        } else {
                            setDinnerView();
                            session.setDinnerDiet(getString(myFoods));
                        }

                        Toast.makeText(context, "Added to your diet chart", Toast.LENGTH_LONG).show();
                        comm.respond(4);
                        ((DietType) getActivity()).viewPager.setCurrentItem(0, true);
                    }
                    else {
                        Toast.makeText(context, "Selected Food are exceeding reqguired Calories", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(context, "Please select food item from list to your diet chart", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
    private String getString(ArrayList<MealSchedulerNewModel> obj){
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
       /* Gson gson = new Gson();
        model = gson.fromJson(response,MealSchedulerNewModel.class); */
       FOODARRAYLIST.clear();
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
            if(viewFromBool)
            {
                populateViewsSuggested(FOODARRAYLIST);
            }else {
                populateViews(FOODARRAYLIST);
            }
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

        Random rand = new Random();
        int randomNumber;
        if(foods.size()>0){
            LinearLayout item = (LinearLayout) v3.findViewById(R.id.main_time_table);
            item.removeAllViews();
            for (int i=0;i<foods.size();i++){
                if(foods.size()>100) {
                    if(i==100){break;}
                }
                randomNumber = i;
                //randomNumber = rand.nextInt((foods.size()-1)) + 1;
               // Log.d("CJ PRINT ","RANDOM "+randomNumber+" I "+i);

                View child = getActivity().getLayoutInflater().inflate(R.layout.item_new_meals,null);
                TextView day_name = (TextView)child.findViewById(R.id.food_name);
                day_name.setText(foods.get(randomNumber).getFood());

                ImageView img = (ImageView) child.findViewById(R.id.food_image);
                if(!foods.get(randomNumber).getImgurl().equalsIgnoreCase("")) {
                    try {
                        Picasso.with(context).load(Utils.IMAGES_URL+foods.get(randomNumber).getImgurl()).into(img);
                    }catch (Exception e)
                    {
                        img.setImageResource(R.drawable.ic_local_dining_black_24dp);
                    }
                }
                TextView weight = (TextView)child.findViewById(R.id.food_type);
                weight.setText(foods.get(randomNumber).getFood_type());

                TextView measure = (TextView)child.findViewById(R.id.carb_type);
                measure.setText(foods.get(randomNumber).getCrabtype());

                CheckBox check_food = (CheckBox)child.findViewById(R.id.check_food);
                check_food.setTag(foods.get(randomNumber).getId());
                check_foods.add(check_food);

                LinearLayout main_day_table = (LinearLayout)child.findViewById(R.id.main_day_table);

                    for(int j=0;j<3;j++) {
                        View inner_child = getActivity().getLayoutInflater().inflate(R.layout.item_new_meals_row, null);

                        TextView nutrient = (TextView) inner_child.findViewById(R.id.nutrient);
                        if(j==0) {
                            nutrient.setText("" + "Energy");
                        }
                        else if(j==1) {
                            nutrient.setText("" + "Proteins");
                        }else if(j==2)
                        {
                            nutrient.setText(""+"Fat");
                        }

                        TextView unit = (TextView) inner_child.findViewById(R.id.unit);

                        if(j==0) {
                            unit.setText("" + "kcal");
                        }
                        else if(j==1) {
                            unit.setText("" + "Gm");
                        }else if(j==2)
                        {
                            unit.setText("" + "Gm");
                        }

                        TextView value = (TextView) inner_child.findViewById(R.id.value);
                        if(j==0) {
                            value.setText("" + foods.get(randomNumber).getEnergy());
                        }
                        else if(j==1) {
                            value.setText("" + foods.get(randomNumber).getProtein());
                        }else if(j==2)
                        {
                            value.setText("" + foods.get(randomNumber).getFat());
                        }

                        main_day_table.addView(inner_child);
                    }
                item.addView(child);
            }
        }

    }

    private void populateViewsSuggested(ArrayList<MealSchedulerNewModel> foods){

        if(foods.size()>0){
            String prev = session.getCalorie();
            Float Cal = Float.parseFloat(prev);
            Float CalNew = 0.00f ;
            Float Fat = 1.0f;
            Float BMI = Float.parseFloat(session.getBmi());
            Random rand = new Random();
            int randomNumber;
            LinearLayout item = (LinearLayout) v3.findViewById(R.id.main_time_table);
            item.removeAllViews();
            if( BMI < 15) {
                Fat = 10.0f;
            }
            for (int i=0;i<foods.size();i++){
                randomNumber = rand.nextInt((foods.size()-1)) + 1;

                if(foods.get(randomNumber).getFat() < Fat) {
                    if (foods.get(randomNumber).getEnergy() < 100) {
                        CalNew = CalNew + foods.get(randomNumber).getEnergy();
                        if (CalNew > Cal) {
                            break;
                        }
                        // Log.d("CJ PRINT ","RANDOM "+randomNumber+" I "+i);

                        View child = getActivity().getLayoutInflater().inflate(R.layout.item_new_meals, null);
                        TextView day_name = (TextView) child.findViewById(R.id.food_name);
                        day_name.setText(foods.get(randomNumber).getFood());

                        ImageView img = (ImageView) child.findViewById(R.id.food_image);
                        if(!foods.get(randomNumber).getImgurl().equalsIgnoreCase("")) {
                            try {
                                Picasso.with(context).load(Utils.IMAGES_URL+foods.get(randomNumber).getImgurl()).into(img);
                            }catch (Exception e)
                            {
                                img.setImageResource(R.drawable.ic_local_dining_black_24dp);
                            }
                        }

                        TextView weight = (TextView) child.findViewById(R.id.food_type);
                        weight.setText(foods.get(randomNumber).getFood_type());

                        TextView measure = (TextView) child.findViewById(R.id.carb_type);
                        measure.setText(foods.get(randomNumber).getCrabtype());

                        CheckBox check_food = (CheckBox) child.findViewById(R.id.check_food);
                        check_food.setTag(foods.get(randomNumber).getId());
                        check_foods.add(check_food);

                        LinearLayout main_day_table = (LinearLayout) child.findViewById(R.id.main_day_table);

                        for (int j = 0; j < 3; j++) {
                            View inner_child = getActivity().getLayoutInflater().inflate(R.layout.item_new_meals_row, null);

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
                                value.setText("" + foods.get(randomNumber).getEnergy());
                            } else if (j == 1) {
                                value.setText("" + foods.get(randomNumber).getProtein());
                            } else if (j == 2) {
                                value.setText("" + foods.get(randomNumber).getFat());
                            }

                            main_day_table.addView(inner_child);
                        }
                        item.addView(child);
                    }
                }
            }
        }

    }

    public void setBreakfastView() {
        String foodData = session.getBreakfastDiet();
        Log.d("CJ PRINT ", "BREAKFAST DATA - " + foodData);
        try {
            JSONArray jarr = new JSONArray(foodData);
            for (int i = 0; i < jarr.length(); i++) {
                MealSchedulerNewModel model = new MealSchedulerNewModel();
                model.setId(jarr.getJSONObject(i).getString("id"));
                model.setFood(jarr.getJSONObject(i).getString("Food"));
                model.setFood_type(jarr.getJSONObject(i).getString("Food_type"));
                model.setCrabtype(jarr.getJSONObject(i).getString("crabtype"));
                model.setImgurl(jarr.getJSONObject(i).getString("imgurl"));
                model.setEnergy(Float.parseFloat(jarr.getJSONObject(i).getString("Energy")));
                model.setFat(Float.parseFloat(jarr.getJSONObject(i).getString("Protein")));
                model.setProtein(Float.parseFloat(jarr.getJSONObject(i).getString("Fat")));
                myFoods.add(model);
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
                MealSchedulerNewModel model = new MealSchedulerNewModel();
                model.setId(jarr.getJSONObject(i).getString("id"));
                model.setFood(jarr.getJSONObject(i).getString("Food"));
                model.setFood_type(jarr.getJSONObject(i).getString("Food_type"));
                model.setCrabtype(jarr.getJSONObject(i).getString("crabtype"));
                model.setImgurl(jarr.getJSONObject(i).getString("imgurl"));
                model.setEnergy(Float.parseFloat(jarr.getJSONObject(i).getString("Energy")));
                model.setFat(Float.parseFloat(jarr.getJSONObject(i).getString("Protein")));
                model.setProtein(Float.parseFloat(jarr.getJSONObject(i).getString("Fat")));
                myFoods.add(model);
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
                MealSchedulerNewModel model = new MealSchedulerNewModel();
                model.setId(jarr.getJSONObject(i).getString("id"));
                model.setFood(jarr.getJSONObject(i).getString("Food"));
                model.setFood_type(jarr.getJSONObject(i).getString("Food_type"));
                model.setCrabtype(jarr.getJSONObject(i).getString("crabtype"));
                model.setImgurl(jarr.getJSONObject(i).getString("imgurl"));
                model.setEnergy(Float.parseFloat(jarr.getJSONObject(i).getString("Energy")));
                model.setFat(Float.parseFloat(jarr.getJSONObject(i).getString("Protein")));
                model.setProtein(Float.parseFloat(jarr.getJSONObject(i).getString("Fat")));
                myFoods.add(model);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }



}

