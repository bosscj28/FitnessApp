package shanks.com.fitness.Utils;

import android.app.ProgressDialog;
import android.content.Context;

import org.json.JSONObject;

import shanks.com.fitness.Interfaces.OnWebCall;
import shanks.com.fitness.model.EditModel;
import shanks.com.fitness.model.ForgotModel;
import shanks.com.fitness.model.LoginModel;
import shanks.com.fitness.model.SignUpModel;

/**
 * Created by ankitpurohit on 20-06-2017.
 */

public class WebCalls implements CallService.OnServiceCall{

    private Context context;
    private ProgressDialog pd;
    private OnWebCall OnWebCall;

    private final int CASE_REGISTER =0;
    private final int CASE_LOGIN =1;
    private final int CASE_GET_TOTAL_STEPS =2;
    private final int CASE_SET_TOTAL_STEPS =3;
    private final int CASE_GET_MEAL_SCHEDULER =4;
    private final int CASE_EDIT =5;
    private final int CASE_FORGOT =6;

    private boolean autoDismiss;

    public void showProgress(boolean autoDismiss){
        this.autoDismiss = autoDismiss;
        pd = ProgressDialog.show(context,"Loading ...","Please wait");
    }

    private void hideProgress(){
        if(autoDismiss){
            if(pd!=null && pd.isShowing()){
                pd.dismiss();
            }
        }
    }

    public void doneProgress(){
        if(pd!=null && pd.isShowing()){
            pd.dismiss();
        }
    }

    public void setWebCallListner(OnWebCall onWebCall){
        this.OnWebCall = onWebCall;
    }

    public WebCalls(Context context) {
        this.context = context;
    }

    private boolean checkNet(){
        return Utils.isNetworkAvailable(context);
    }

    public void login(LoginModel model){
        doWebCall(CASE_LOGIN,Utils.getLoginUrl(model));
    }

    public void SignUp(SignUpModel model){
        doWebCall(CASE_REGISTER,Utils.getRegisterUrl(model));
    }
    public void EditUser(EditModel model){
        doWebCall(CASE_EDIT,Utils.getEditUrl(model));
    }
    public void ForgotPassword(ForgotModel model){doWebCall(CASE_EDIT,Utils.getForgotUrl(model));}

    public void getSteps(String custid){
        doWebCall(CASE_GET_TOTAL_STEPS,Utils.getAllStepsUrl(custid));
    }
    public void setSteps(String steps,String custid){
        doWebCall(CASE_SET_TOTAL_STEPS,Utils.setStepsUrl(steps,custid));
    }

    public void getMeals(){
        doWebCall(CASE_GET_MEAL_SCHEDULER,Utils.getMealScheduler());
    }

    private void doWebCall(int requestCode,String url){
        if(!checkNet()) {
            hideProgress();
            if(OnWebCall!=null)
            OnWebCall.OnWebCallError(Utils.NO_NET);
        }
        else new CallService(requestCode,url,Utils.GET, WebCalls.this).execute();
    }

    @Override
    public void onServiceCall(int requestCode, String response) {
        hideProgress();
        Logger.log(response);
        switch(requestCode){
            case CASE_REGISTER:
                handleRegister(response);
                break;
            case CASE_LOGIN:
                handleLogin(response);
                break;
            case CASE_GET_TOTAL_STEPS:
                handleSteps(response);
                break;
            case CASE_SET_TOTAL_STEPS:
                handleSteps(response);
                break;
            case CASE_GET_MEAL_SCHEDULER:
                handleMeals(response);
                break;
            case CASE_EDIT:
                handleEdit(response);
            case CASE_FORGOT:
                handleForgot(response);
        }
    }

    private void handleLogin(String response){
        try{
            JSONObject jobj = new JSONObject(response);
            String _response= jobj.getString("response");
            String message = jobj.getString("message");
            if(OnWebCall!=null){
                if(_response.equalsIgnoreCase("200")){
                    OnWebCall.OnWebCallSuccess(response);
                } else {
                    OnWebCall.OnWebCallError(message);
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
            if(OnWebCall!=null)
                OnWebCall.OnWebCallError(ex.toString());
        }
    }
    private void handleForgot(String response){
        try{
            JSONObject jobj = new JSONObject(response);
            String _response= jobj.getString("response");
            String message = jobj.getString("message");
            if(OnWebCall!=null){
                if(_response.equalsIgnoreCase("200")){
                    OnWebCall.OnWebCallSuccess(message);
                } else {
                    OnWebCall.OnWebCallError(message);
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
            if(OnWebCall!=null)
                OnWebCall.OnWebCallError(ex.toString());
        }
    }

    private void handleRegister(String response){
        try{
            JSONObject jobj = new JSONObject(response);
            String _response = jobj.getString("response");
            String message = jobj.getString("message");
            if(OnWebCall!=null){
                if(_response.equalsIgnoreCase("200")){
                    OnWebCall.OnWebCallSuccess(message);
                } else {
                    OnWebCall.OnWebCallError(message);
                }
            }

        }catch (Exception ex){
            ex.printStackTrace();
            if(OnWebCall!=null)
            OnWebCall.OnWebCallError(ex.toString());
        }
    }

    private void handleEdit(String response){
        try{
            JSONObject jobj = new JSONObject(response);
            String _response = jobj.getString("response");
            String message = jobj.getString("message");
            if(OnWebCall!=null){
                if(_response.equalsIgnoreCase("200")){
                    OnWebCall.OnWebCallSuccess(response);
                } else {
                    OnWebCall.OnWebCallError(message);
                }
            }

        }catch (Exception ex){
            ex.printStackTrace();
            if(OnWebCall!=null)
                OnWebCall.OnWebCallError(ex.toString());
        }
    }

    private void handleSteps(String response){
        try{
            try{
                if(OnWebCall!=null)
                    OnWebCall.OnWebCallSuccess(response);

            }catch (Exception ex){
                ex.printStackTrace();
                if(OnWebCall!=null)
                    OnWebCall.OnWebCallError(ex.toString());
            }
            /*JSONObject jobj = new JSONObject(response);
            String _response = jobj.getString("response");
            String message = jobj.getString("message");
            if(OnWebCall!=null){
                if(_response.equalsIgnoreCase("200")){
                    OnWebCall.OnWebCallSuccess(response);
                } else {
                    OnWebCall.OnWebCallError(message);
                }
            }*/

        }catch (Exception ex){
            ex.printStackTrace();
            if(OnWebCall!=null)
                OnWebCall.OnWebCallError(ex.toString());
        }
    }

    private void handleMeals(String response){
        try{
            if(OnWebCall!=null)
                OnWebCall.OnWebCallSuccess(response);

        }catch (Exception ex){
            ex.printStackTrace();
            if(OnWebCall!=null)
                OnWebCall.OnWebCallError(ex.toString());
        }
    }

}
