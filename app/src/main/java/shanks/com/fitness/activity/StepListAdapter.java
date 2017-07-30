package shanks.com.fitness.activity;


import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import shanks.com.fitness.R;
import shanks.com.fitness.model.StepModel;


public class StepListAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<StepModel> stepItems;


    public StepListAdapter(Activity activity, List<StepModel> stepItems) {
        this.activity = activity;
        this.stepItems = stepItems;
        for(int i=0;i<stepItems.size();i++)
        Log.d("CJ ","CJ PRINT"+stepItems.get(i).getSteps());
    }

    @Override
    public int getCount() {
        return stepItems.size();
    }

    @Override
    public Object getItem(int location) {
        return stepItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {



        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) LayoutInflater
                    .from(activity);
            convertView = inflater.inflate(R.layout.item_steps, parent, false);

        }

        TextView Steps = (TextView) convertView.findViewById(R.id.textView2);
        TextView Dates = (TextView) convertView.findViewById(R.id.textView3);

        Log.d("CJ PRINT","IN MODEL -"+stepItems.get(position).getSteps()+"-"+stepItems.get(position).getStepsDate());
        StepModel s = stepItems.get(position);
        Steps.setText(s.getSteps());


        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Dates.setText(sdf.format(s.getStepsDate()));

        }catch (Exception e)
        {
            e.printStackTrace();
        }

        return convertView;
    }


}