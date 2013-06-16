package yumobi.test.chat.task;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;

public class MessagePutRequest extends AsyncTask<String, Void, HttpResponse> {

	public interface MessagePutRequestCallBack {
		public void completed();

		public void failed();
	}

	private MessagePutRequestCallBack callBack;

	public MessagePutRequest(MessagePutRequestCallBack callBack) {
		this.callBack = callBack;
	}

	@Override
	protected HttpResponse doInBackground(String... contents) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse response = null;
		try {
			HttpPut request = new HttpPut("http://54.250.145.45/a/4/t/5/r");
			StringEntity params = new StringEntity("{\"sender_id\":\""
					+ contents[0] + "\",\"receiver_id\":\"" + contents[1]
					+ "\",\"content\":\"" + contents[2] + "\"}");
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
		if (response.getStatusLine().getStatusCode() >= 400) {
			callBack.failed();
		} else {
			callBack.completed();
		}
	}

}
