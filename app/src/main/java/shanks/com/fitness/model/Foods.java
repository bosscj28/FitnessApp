package shanks.com.fitness.model;

/**
 * Created by ankitpurohit on 29-06-2017.
 */

public class Foods
{
    private Nutrients[] nutrients;

    private String weight;

    private String measure;

    private String name;

    private String ndbno;

    public Nutrients[] getNutrients ()
    {
        return nutrients;
    }

    public void setNutrients (Nutrients[] nutrients)
    {
        this.nutrients = nutrients;
    }

    public String getWeight ()
    {
        return weight;
    }

    public void setWeight (String weight)
    {
        this.weight = weight;
    }

    public String getMeasure ()
    {
        return measure;
    }

    public void setMeasure (String measure)
    {
        this.measure = measure;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getNdbno ()
    {
        return ndbno;
    }

    public void setNdbno (String ndbno)
    {
        this.ndbno = ndbno;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [nutrients = "+nutrients+", weight = "+weight+", measure = "+measure+", name = "+name+", ndbno = "+ndbno+"]";
    }
}


