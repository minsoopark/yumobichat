package yumobi.test.chat.task;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;

import android.os.AsyncTask;

public class MessageGetRequest extends AsyncTask<String, Void, HttpResponse> {

	public interface MessageGetRequestCallBack {
		public void completed(JSONArray jsonArray);
		public void failed();
	}

	private MessageGetRequestCallBack callBack;

	public MessageGetRequest(MessageGetRequestCallBack callBack) {
		this.callBack = callBack;
	}

	@Override
	protected HttpResponse doInBackground(String... contents) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse response = null;
		try {
			StringBuilder url = new StringBuilder("http://54.250.145.45/a/4/t/5/r");
			url.append("?multifilter=or&where=");
			url.append("sender_id(eq)" + contents[0]);
			url.append("&where=");
			url.append("receiver_id(eq)" + contents[0]);
			url.append("&order=created_at.asc");
			
			HttpGet request = new HttpGet(url.toString());
			request.addHeader("Content-Type", "application/json");
			request.addHeader("Accept", "application/json");
			request.addHeader("X-SS-User-Id", "1");

			response = httpClient.execute(request);

		} catch (Exception ex) {

		} finally {
			httpClient.getConnectionManager().shutdown();
		}

		return response;
	}

	@Override
	protected void onPostExecute(HttpResponse response) {
		if (response.getStatusLine().getStatusCode() >= 400) {
			callBack.failed();
		} else {
			JSONArray jsonArray;
			try {
				jsonArray = new JSONArray(EntityUtils.toString(response.getEntity()));
				callBack.completed(jsonArray);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
