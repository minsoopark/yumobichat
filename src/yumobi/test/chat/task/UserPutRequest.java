package yumobi.test.chat.task;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.os.AsyncTask;

public class UserPutRequest extends AsyncTask<String, Void, HttpResponse> {
	public interface UserPutRequestCallBack {
		public void completed(JSONObject json);
		public void failed();
	}
	
	private UserPutRequestCallBack callBack;
	
	public UserPutRequest(UserPutRequestCallBack callBack) {
		this.callBack = callBack;
	}

	@Override
	protected HttpResponse doInBackground(String... contents) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse response = null;
		try {
			HttpPut request = new HttpPut(
					"http://54.250.145.45/a/4/t/6/r");
			StringEntity params = new StringEntity(
					"{\"username\":\"" + contents[0] + "\"}");
			request.addHeader("Content-Type", "application/json");
			request.addHeader("Accept", "application/json");
			request.addHeader("X-SS-User-Id", "1");
			request.setEntity(params);

			response = httpClient.execute(request);

		} catch (Exception ex) {
			
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
		
		return response;
	}

	@Override
	protected void onPostExecute(HttpResponse response) {
		if(response.getStatusLine().getStatusCode() >= 400) {
			callBack.failed();
		} else {
			try {
				JSONObject json = new JSONObject(EntityUtils.toString(response.getEntity()));
				callBack.completed(json);
			} catch (Exception e) {
				
			}
		}
	}
}
