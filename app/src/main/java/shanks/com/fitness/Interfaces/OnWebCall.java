package shanks.com.fitness.Interfaces;

/**
 * Created by ankitpurohit on 20-06-2017.
 */

public interface OnWebCall {
    public void OnWebCallSuccess(String userFullData);
    public void OnWebCallError(String errorMessage);
}
