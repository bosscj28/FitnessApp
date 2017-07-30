package shanks.com.fitness.model;

import java.util.Date;

/**
 * Created by asus on 7/29/2017.
 */

public class StepModel {
    private String steps="";
    private Date date;

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public String getSteps() {
        return this.steps;
    }

    public void setStepsDate(Date date) {
        this.date = date;
    }

    public Date getStepsDate() {
        return this.date;
    }


}
