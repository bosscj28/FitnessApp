package shanks.com.fitness.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import shanks.com.fitness.Interfaces.OnWebCall;
import shanks.com.fitness.R;
import shanks.com.fitness.Utils.Logger;
import shanks.com.fitness.Utils.Session;
import shanks.com.fitness.Utils.WebCalls;
import shanks.com.fitness.model.StepModel;

public class CalorieBurn extends AppCompatActivity {
    RecyclerView listSteps;
    List<StepModel> StepList = new ArrayList<StepModel>();
    private burnAdapter adapter;
    TextView steps_text;
    Session session;
    private int numSteps=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calorie_burn);
        init();
    }

    public void init()
    {
        session = Session.getSession(CalorieBurn.this);
        listSteps = (RecyclerView) findViewById(R.id.RCview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CalorieBurn.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listSteps.setLayoutManager(linearLayoutManager);
        getSteps();
        /*WebCalls webCalls = new WebCalls(CalorieBurn.this);
        webCalls.showProgress(true);
        webCalls.setWebCallListner(new OnWebCall() {
            @Override
            public void OnWebCallSuccess(String userFullData) {
                getSteps();



                Logger.log(userFullData);
            }

            @Override
            public void OnWebCallError(String errorMessage) {
                Logger.showToast(CalorieBurn.this,"Something went wrong please try again later");
            }
        });
        String steps = ""+numSteps;
        webCalls.setSteps(steps,session.getUserId()); */
    }
    private void getSteps(){
        WebCalls webCalls = new WebCalls(CalorieBurn.this);
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
                    ArrayList<StepModel> StepList2 = new ArrayList<StepModel>();
                    int steps;
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
                    /*for(int A=0;i<StepList2.size();A++) {
                        Log.d("CJ ", "CJ PRINT IN 2 " + StepList2.get(A).getSteps());
                    }*/

                    adapter = new burnAdapter(CalorieBurn.this,StepList2);
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

}
