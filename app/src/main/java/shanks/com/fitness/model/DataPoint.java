package shanks.com.fitness.model;

import com.jjoe64.graphview.series.DataPointInterface;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.provider.ContactsContract;

import java.io.Serializable;
import java.util.Date;

/**
 * default data point implementation.
 * This stores the x and y values.
 *
 * @author jjoe64
 */
public class DataPoint implements DataPointInterface, Serializable {
    private static final long serialVersionUID=1428263322645L;

    private double x;
    private double y;


    public DataPoint()
    {

    }
    public DataPoint(double x, double y) {
        this.x=x;
        this.y=y;
    }

    public DataPoint(Date x, double y) {
        this.x = x.getTime();
        this.y = y;
    }

    public void setXY(Date x,double y) {
        this.x = x.getTime();
        this.y = y;
    }


    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public String toString() {
        return "["+x+"/"+y+"]";
    }
}
