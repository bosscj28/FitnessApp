package shanks.com.fitness.activity;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import shanks.com.fitness.Interfaces.OnWebCall;
import shanks.com.fitness.R;
import shanks.com.fitness.Utils.Logger;
import shanks.com.fitness.Utils.Session;
import shanks.com.fitness.Utils.Utils;
import shanks.com.fitness.Utils.WebCalls;
import shanks.com.fitness.model.MealSchedulerModel;
import shanks.com.fitness.model.StepModel;
import shanks.com.fitness.stesp.StepDetector;
import shanks.com.fitness.stesp.StepListener;

public class Steps extends AppCompatActivity implements SensorEventListener, StepListener {

    Button start_stop;
    TextView steps_count,steps_text;

    private StepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor accel;
    private static final String TEXT_NUM_STEPS = " STEPS";//:
    private static final String TEXT_TOTAL_STEPS = " TOTAL STEPS : ";
    private int numSteps,totalsteps=0;
    Session session;
    ListView listSteps;
    StepModel stepModel;
    List<StepModel> StepList = new ArrayList<StepModel>();
    private StepListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);
        init();
    }

    private void init(){

        session = Session.getSession(Steps.this);

        synchronized (this){
            sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
            accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            simpleStepDetector = new StepDetector();
            simpleStepDetector.registerListener(this);
        }
        listSteps = (ListView)findViewById(R.id.listSteps) ;
        LayoutInflater layoutinflater = getLayoutInflater();
        ViewGroup header = (ViewGroup)layoutinflater.inflate(R.layout.item_steps,listSteps,false);
        listSteps.addHeaderView(header);
        steps_text = (TextView)findViewById(R.id.steps_text);
        stepModel = new StepModel();

        getSteps();

        steps_count = (TextView)findViewById(R.id.steps_count);
        start_stop = (Button)findViewById(R.id.start_stop);
        start_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(start_stop.getText().toString().equalsIgnoreCase("START")){
                    start_stop.setText("STOP");
                    startCount();
                } else {
                    start_stop.setText("START");
                    stopCount();
                }
            }
        });
    }

    private void startCount(){
        numSteps = 0;
        sensorManager.registerListener(Steps.this, accel, SensorManager.SENSOR_DELAY_FASTEST);
    }

    private void stopCount(){
        sensorManager.unregisterListener(Steps.this);

        WebCalls webCalls = new WebCalls(Steps.this);
        webCalls.showProgress(true);
        webCalls.setWebCallListner(new OnWebCall() {
            @Override
            public void OnWebCallSuccess(String userFullData) {
                getSteps();

                String steps = "0"+TEXT_NUM_STEPS;
                steps_count.setText(steps);

                Logger.log(userFullData);
            }

            @Override
            public void OnWebCallError(String errorMessage) {
                Logger.showToast(Steps.this,"Something went wrong please try again later");
            }
        });
        String steps = ""+numSteps;
        webCalls.setSteps(steps,session.getUserId());
    }

    private void getSteps(){
        WebCalls webCalls = new WebCalls(Steps.this);
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
                            Log.d("CJ ", "CJ PRINT IN 2 " + StepList2.get(A).getStepsDate());
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
                        steps_text.setText(steps_text.getText()+String.valueOf(totalsteps));


                    adapter = new StepListAdapter(Steps.this,StepList2);
                    listSteps.setAdapter(adapter);

                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }

            @Override
            public void OnWebCallError(String errorMessage) {
                steps_text.setText("STEPS");
            }
        });
        webCalls.getSteps(session.getUserId());
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector.updateAccel(
                    event.timestamp, event.values[0], event.values[1], event.values[2]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void step(long timeNs) {
        numSteps++;
        String steps = ""+numSteps+TEXT_NUM_STEPS;
        steps_count.setText(steps);
    }

}
