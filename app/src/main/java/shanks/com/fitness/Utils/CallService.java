package shanks.com.fitness.Utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CallService extends AsyncTask<Void, String, String> {

	OnServiceCall OnServiceCall;
	String urlStr;
	String method = "";
	int requestCode;

	public interface OnServiceCall {
		public void onServiceCall(int requestCode,String response);

	}

	public CallService(int requestCode,String urlStr, String method, OnServiceCall OnServicecall) {
		// TODO Auto-generated constructor stub
		this.urlStr = urlStr;
		this.OnServiceCall = OnServicecall;
		this.method = method;
		this.requestCode = requestCode;
	}

	@Override
	protected String doInBackground(Void... params) {
		// TODO Auto-generated method stub
		return getData(method);
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		OnServiceCall.onServiceCall(requestCode,result);
	}

	private String getData(String method) {

		try {
			urlStr = urlStr.replace(" ","%20");
			Logger.log(urlStr);

			URL url = new URL(urlStr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(45000);
			conn.setConnectTimeout(90000);
			conn.setRequestMethod(method);
			conn.setDoInput(true);
			conn.connect();
			Log.wtf("ankit",":::"+conn.getResponseCode());
			if(conn.getResponseCode()==400){
				InputStream is = conn.getErrorStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(
						is, "UTF-8"));

				String data = null;
				String webPage = "";
				while ((data = reader.readLine()) != null) {
					webPage += data + "\n";
				}
				return webPage;
			}
			InputStream is = conn.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "UTF-8"));

			String data = null;
			String webPage = "";
			while ((data = reader.readLine()) != null) {
				webPage += data + "\n";
			}
			return webPage;
		} catch (Exception e) {
			e.printStackTrace();
			return e.toString();
		}
	}

}
