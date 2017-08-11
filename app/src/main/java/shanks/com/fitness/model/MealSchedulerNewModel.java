package shanks.com.fitness.model;

/**
 * Created by asus on 8/8/2017.
 */

public class MealSchedulerNewModel {

    String id,Food,Food_type,crabtype,imgurl;
    Float Energy,Protein,Fat;

    public void setId(String Id) {
        this.id = Id;
    }

    public String getId() {
        return id;
    }

    public void setFood(String food) {
        Food = food;
    }

    public String getFood() {
        return Food;
    }

    public void setFood_type(String food_type) {
        Food_type = food_type;
    }
    public String getFood_type() {
        return Food_type;
    }

    public void setCrabtype(String crabtype) {
        this.crabtype = crabtype;
    }

    public String getCrabtype() {
        return crabtype;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setEnergy(Float energy) {
        Energy = energy;
    }

    public Float getEnergy() {
        return Energy;
    }

    public void setFat(Float fat) {
        Fat = fat;
    }

    public Float getFat() {
        return Fat;
    }

    public void setProtein(Float protein) {
        Protein = protein;
    }

    public Float getProtein() {
        return Protein;
    }
}

