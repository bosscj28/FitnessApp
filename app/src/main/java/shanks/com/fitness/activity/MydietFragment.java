package shanks.com.fitness.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import shanks.com.fitness.Interfaces.Communicator;
import shanks.com.fitness.R;
import shanks.com.fitness.Utils.Session;
import shanks.com.fitness.Utils.Utils;
import shanks.com.fitness.model.Foods;
import shanks.com.fitness.model.Nutrients;

/**
 * Created by asus on 7/30/2017.
 */

public class MydietFragment extends Fragment {

    Session session;
    ArrayList<Foods> myFoods_B,myFoods_D,myFoods_L;
    String foodData = "";
    dietAdapter dietAdapterBreakfast,dietAdapterLunch,dietAdapterDinner;
    RecyclerView brekfastRV,lunchRV,dinnerRV;
    private Communicator comm;

    public MydietFragment() {

    }

    private Context context;

    public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
    public static final MydietFragment newInstance(String message)
    {
        MydietFragment f = new MydietFragment();
        Bundle bdl = new Bundle(1);
        bdl.putString(EXTRA_MESSAGE, message);
        f.setArguments(bdl);
        return f;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        comm = (Communicator)context;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v3 = inflater.inflate(R.layout.fragment_my_diet, container, false);
        ImageView break_fast = (ImageView) v3.findViewById(R.id.breakfast_Add);
        break_fast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startDiet(Utils.breakfast);
                comm.respond(1);
                ((DietType) getActivity()).viewPager.setCurrentItem(1, true);
            }
        });

        ImageView lunch = (ImageView) v3.findViewById(R.id.lunchADD);
        lunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startDiet(Utils.lunch);
                comm.respond(2);
                ((DietType) getActivity()).viewPager.setCurrentItem(1, true);
            }
        });

        ImageView dinner = (ImageView) v3.findViewById(R.id.dinner_Add);
        dinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startDiet(Utils.dinner);
                comm.respond(3);
                ((DietType) getActivity()).viewPager.setCurrentItem(1, true);
            }
        });

        brekfastRV = (RecyclerView) v3.findViewById(R.id.breakfastRV);
        lunchRV = (RecyclerView) v3.findViewById(R.id.lunchRV);
        dinnerRV = (RecyclerView) v3.findViewById(R.id.dinnerRV);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        brekfastRV.setLayoutManager(linearLayoutManager);

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(context);
        linearLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        lunchRV.setLayoutManager(linearLayoutManager2);

        LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(context);
        linearLayoutManager3.setOrientation(LinearLayoutManager.VERTICAL);
        dinnerRV.setLayoutManager(linearLayoutManager3);
        init();

        return v3;
    }

    private void init() {
        session = Session.getSession(context);
        myFoods_B = new ArrayList<>();
        myFoods_D = new ArrayList<>();
        myFoods_L = new ArrayList<>();

        setBreakfastView();
        setLunchView();
       setDinnerView();

    }
    public void setBreakfastView(){
        foodData = session.getBreakfastDiet();
        myFoods_B.clear();
        try {
            if (!foodData.equals("null")) {

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

                    myFoods_B.add(foods);
                }

                if (myFoods_B != null && myFoods_B.size() > 0) {
                    dietAdapterBreakfast = new dietAdapter(context, myFoods_B,Utils.breakfast);
                    brekfastRV.setAdapter(dietAdapterBreakfast);


                }
            }

            } catch(Exception ex){
                ex.printStackTrace();
            }

    }
    public void setLunchView(){
        foodData = session.getLunchDiet();
        myFoods_L.clear();
        try {
            if (!foodData.equals("null")) {

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

                    myFoods_L.add(foods);
                }

                if (myFoods_L != null && myFoods_L.size() > 0){
                    dietAdapterLunch = new dietAdapter(context,myFoods_L,Utils.lunch);
                    lunchRV.setAdapter(dietAdapterLunch);

                }
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
    public void setDinnerView(){
        foodData = session.getDinnerDiet();
        myFoods_D.clear();
        try {
            if (!foodData.equals("null")) {

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

                    myFoods_D.add(foods);
                }

                if (myFoods_D != null && myFoods_D.size() > 0) {
                    dietAdapterDinner = new dietAdapter(context, myFoods_D,Utils.dinner);
                    dinnerRV.setAdapter(dietAdapterDinner);

                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}
