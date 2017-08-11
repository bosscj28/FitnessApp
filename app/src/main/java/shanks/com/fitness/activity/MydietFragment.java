package shanks.com.fitness.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import shanks.com.fitness.Interfaces.Communicator;
import shanks.com.fitness.Interfaces.OnWebCall;
import shanks.com.fitness.R;
import shanks.com.fitness.Utils.Session;
import shanks.com.fitness.Utils.Utils;
import shanks.com.fitness.Utils.WebCalls;
import shanks.com.fitness.model.Foods;
import shanks.com.fitness.model.MealSchedulerNewModel;
import shanks.com.fitness.model.Nutrients;
import shanks.com.fitness.model.StepModel;

/**
 * Created by asus on 7/30/2017.
 */

public class MydietFragment extends Fragment {

    Session session;
    ArrayList<MealSchedulerNewModel> myFoods_B,myFoods_D,myFoods_L;
    String foodData = "";
    dietAdapter dietAdapterBreakfast,dietAdapterLunch,dietAdapterDinner;
    RecyclerView brekfastRV,lunchRV,dinnerRV;
    private Communicator comm;
    Float TotalCalories=0.0f;
    TextView Calories,goal,burn,left;
    List<StepModel> StepList = new ArrayList<StepModel>();
    int totalsteps=0;
    int burnCalories=0;

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
        Calories = (TextView) v3.findViewById(R.id.calorieView);
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

        goal = (TextView) v3.findViewById(R.id.goal);
        burn = (TextView) v3.findViewById(R.id.burn);
        left = (TextView) v3.findViewById(R.id.left);



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
        goal.setText(session.getCalorie());
        getSteps();
        myFoods_B = new ArrayList<>();
        myFoods_D = new ArrayList<>();
        myFoods_L = new ArrayList<>();

        try {

            Utils.ClearCalorieShared(context,"Calorie"+session.getUserId(),"0");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        setBreakfastView();
        setLunchView();
        setDinnerView();

        String prev = Utils.getCalorieShared(context,"Calorie"+session.getUserId(),"0");
        Calories.setText(prev);

    }
    public void setBreakfastView(){
        foodData = session.getBreakfastDiet();
        myFoods_B.clear();
        try {
            if (!foodData.trim().equals("")) {

                JSONArray jarr = new JSONArray(foodData);
                for (int i = 0; i < jarr.length(); i++) {
                    MealSchedulerNewModel model = new MealSchedulerNewModel();
                    model.setId(jarr.getJSONObject(i).getString("id"));
                    model.setFood(jarr.getJSONObject(i).getString("Food"));
                    model.setFood_type(jarr.getJSONObject(i).getString("Food_type"));
                    model.setCrabtype(jarr.getJSONObject(i).getString("crabtype"));
                    model.setImgurl(jarr.getJSONObject(i).getString("imgurl"));
                    model.setEnergy(Float.parseFloat(jarr.getJSONObject(i).getString("Energy")));

                    String value = jarr.getJSONObject(i).getString("Energy");
                    String prev = Utils.getCalorieShared(context,"Calorie"+session.getUserId(),"0");
                    TotalCalories = Float.parseFloat(prev) + Float.parseFloat(value);
                    Utils.setCalorieShared(context,"Calorie"+session.getUserId(),String.valueOf(TotalCalories));

                    model.setFat(Float.parseFloat(jarr.getJSONObject(i).getString("Protein")));
                    model.setProtein(Float.parseFloat(jarr.getJSONObject(i).getString("Fat")));
                    myFoods_B.add(model);
                }

                if (myFoods_B != null && myFoods_B.size() > 0) {
                    dietAdapterBreakfast = new dietAdapter(context, myFoods_B,Utils.breakfast,Calories,left,burn);
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
            if (!foodData.trim().equals("")) {

                JSONArray jarr = new JSONArray(foodData);
                for (int i = 0; i < jarr.length(); i++) {
                    MealSchedulerNewModel model = new MealSchedulerNewModel();
                    model.setId(jarr.getJSONObject(i).getString("id"));
                    model.setFood(jarr.getJSONObject(i).getString("Food"));
                    model.setFood_type(jarr.getJSONObject(i).getString("Food_type"));
                    model.setCrabtype(jarr.getJSONObject(i).getString("crabtype"));
                    model.setImgurl(jarr.getJSONObject(i).getString("imgurl"));
                    model.setEnergy(Float.parseFloat(jarr.getJSONObject(i).getString("Energy")));
                    String value = jarr.getJSONObject(i).getString("Energy");
                    String prev = Utils.getCalorieShared(context,"Calorie"+session.getUserId(),"0");
                    TotalCalories = Float.parseFloat(prev) + Float.parseFloat(value);
                    Utils.setCalorieShared(context,"Calorie"+session.getUserId(),String.valueOf(TotalCalories));

                    model.setFat(Float.parseFloat(jarr.getJSONObject(i).getString("Protein")));
                    model.setProtein(Float.parseFloat(jarr.getJSONObject(i).getString("Fat")));
                    myFoods_L.add(model);
                }

                if (myFoods_L != null && myFoods_L.size() > 0){
                    dietAdapterLunch = new dietAdapter(context,myFoods_L,Utils.lunch,Calories,left,burn);
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
            if (!foodData.trim().equals("")) {

                JSONArray jarr = new JSONArray(foodData);
                for (int i = 0; i < jarr.length(); i++) {
                    MealSchedulerNewModel model = new MealSchedulerNewModel();
                    model.setId(jarr.getJSONObject(i).getString("id"));
                    model.setFood(jarr.getJSONObject(i).getString("Food"));
                    model.setFood_type(jarr.getJSONObject(i).getString("Food_type"));
                    model.setCrabtype(jarr.getJSONObject(i).getString("crabtype"));
                    model.setImgurl(jarr.getJSONObject(i).getString("imgurl"));
                    model.setEnergy(Float.parseFloat(jarr.getJSONObject(i).getString("Energy")));
                    String value = jarr.getJSONObject(i).getString("Energy");

                    String prev = Utils.getCalorieShared(context,"Calorie"+session.getUserId(),"0");
                    TotalCalories = Float.parseFloat(prev) + Float.parseFloat(value);
                    Utils.setCalorieShared(context,"Calorie"+session.getUserId(),String.valueOf(TotalCalories));

                    model.setFat(Float.parseFloat(jarr.getJSONObject(i).getString("Protein")));
                    model.setProtein(Float.parseFloat(jarr.getJSONObject(i).getString("Fat")));
                    myFoods_D.add(model);
                }

                if (myFoods_D != null && myFoods_D.size() > 0) {
                    dietAdapterDinner = new dietAdapter(context, myFoods_D,Utils.dinner,Calories,left,burn);
                    dinnerRV.setAdapter(dietAdapterDinner);

                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void getSteps(){
        totalsteps=0;
        WebCalls webCalls = new WebCalls(context);
        webCalls.showProgress(true);
        webCalls.setWebCallListner(new OnWebCall() {
            @Override
            public void OnWebCallSuccess(String userFullData) {
                try{
                    JSONArray jarr = new JSONArray(userFullData);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                    Date d = sdf.parse("2017-01-01");
                    StepList.clear();
                    for (int i = 0; i < jarr.length(); i++)
                    {
                        StepModel s = new StepModel();
                        JSONObject jsonObj = jarr.getJSONObject(i);
                        s.setSteps(jsonObj.getString("steps"));

                        try {
                            d  = sdf.parse(jsonObj.getString("date"));
                        } catch (ParseException ex) {
                            ex.printStackTrace();
                        }

                        s.setStepsDate(d);

                        StepList.add(s);
                        Log.d("CJ ","CJ PRINT IN"+StepList.get(i).getSteps());

                    }
                    List<StepModel> StepList2 = new ArrayList<StepModel>();
                    int steps = 0;
                    SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy");
                    String stepString="";
                    // Log.d("ASHU","...."+StepList.size());
                    int i=0,j;
                    while(i<StepList.size())
                    {
                        int k=i;
                        steps=0;
                        StepModel s = new StepModel();
                        for(j=k;j<StepList.size();j++,k++){

                            Date Di,Dj;
                            Di = StepList.get(i).getStepsDate();
                            Dj = StepList.get(j).getStepsDate();

                            String sdi = sdf2.format(Di);
                            String sdj = sdf2.format(Dj);

                            if(sdi.equals(sdj)) {
                                steps = steps + Integer.parseInt(StepList.get(j).getSteps());
                                stepString = String.valueOf(steps);

                                if (j == (StepList.size()-1)) {
                                    s.setSteps(stepString);
                                    s.setStepsDate(StepList.get(i).getStepsDate());
                                    StepList2.add(s);
                                    break;
                                }
                            }
                            else
                            {
                                s.setSteps(stepString);
                                s.setStepsDate(StepList.get(i).getStepsDate());
                                StepList2.add(s);
                                i=k-1;

                                break;
                            }
                        }
                        if (j == (StepList.size()-1))
                            break;
                        i++;
                    }



                   /* for(int A=0;i<StepList2.size();A++) {
                       // Log.d("CJ ", "CJ PRINT IN 2 " + StepList2.get(A).getStepsDate());
                        Date STEPDATE = StepList2.get(i).getStepsDate();
                        String SD = sdf2.format(STEPDATE);

                        if(STEPDATE.equals(SD))
                        {
                            totalsteps = totalsteps + Integer.parseInt(StepList2.get(i).getSteps());

                        }

                    }*/
                    Date TodayDate = new Date();
                    String TD = sdf2.format(TodayDate);

                    for(int A=0;A<StepList2.size();A++)
                    {
                        Log.d("CJ ", "CJ PRINT IN 2 " + StepList2.get(A).getStepsDate());
                        Date STEPDATE = StepList2.get(A).getStepsDate();
                        String SD = sdf2.format(STEPDATE);

                        if(TD.equals(SD))
                        {
                            totalsteps = totalsteps + Integer.parseInt(StepList2.get(A).getSteps());

                        }

                    }
                    burnCalories = totalsteps/20;
                    burn.setText("");
                    burn.setText(String.valueOf(burnCalories));
                    String prev = Utils.getCalorieShared(context,"Calorie"+session.getUserId(),"0");
                    Float Cal = Float.parseFloat(prev) - burnCalories;
                    if(Cal>0)
                    {
                        left.setText(String.valueOf(Cal));
                    }else
                    {
                        left.setText("0");
                    }


                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }

            @Override
            public void OnWebCallError(String errorMessage) {
               burn.setText("0");
            }
        });
        webCalls.getSteps(session.getUserId());
    }

}
