package shanks.com.fitness.activity;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import shanks.com.fitness.R;
import shanks.com.fitness.Utils.Utils;
import shanks.com.fitness.model.Foods;
import shanks.com.fitness.model.StepModel;


public class burnAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    private LayoutInflater inflater;
    ArrayList<StepModel> stepItems;

    public burnAdapter(Context context, ArrayList<StepModel> stepItems) {
        super();
        this.context = context;
        this.stepItems = stepItems;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_steps, parent, false);
        burnAdapter.MyHolder holder = new burnAdapter.MyHolder(view, context);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // Get current position of item in recyclerview to bind data and assign values from list
        MyHolder myHolder = (MyHolder) holder;
        StepModel current = stepItems.get(position);
        int stepInt = Integer.parseInt(current.getSteps());

        stepInt = stepInt / 20;
        myHolder.Sr.setText(String.valueOf(position+1));
        myHolder.StepsTV.setText(String.valueOf(stepInt)+" Calories");
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            myHolder.DatesTV.setText(sdf.format(current.getStepsDate()));

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    // return total item from List
    @Override
    public int getItemCount() {
        return stepItems.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        TextView StepsTV, DatesTV,Sr;

        // create constructor to get widget reference
        public MyHolder(View itemView, final Context context) {
            super(itemView);
            final Context context1 = context;
            Sr = (TextView) itemView.findViewById(R.id.textView6);
            DatesTV = (TextView) itemView.findViewById(R.id.textView2);
            StepsTV = (TextView) itemView.findViewById(R.id.textView3);

        }

    }
}
// return total item from List
