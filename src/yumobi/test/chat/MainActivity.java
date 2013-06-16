package yumobi.test.chat;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import yumobi.test.R;
import yumobi.test.chat.adpater.ChatAdapter;
import yumobi.test.chat.model.Chat;
import yumobi.test.chat.task.MessageGetRequest;
import yumobi.test.chat.task.MessageGetRequest.MessageGetRequestCallBack;
import yumobi.test.chat.task.MessagePutRequest;
import yumobi.test.chat.task.MessagePutRequest.MessagePutRequestCallBack;
import yumobi.test.chat.task.UserPutRequest;
import yumobi.test.chat.task.UserPutRequest.UserPutRequestCallBack;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.app.Activity;
import android.content.SharedPreferences;

public class MainActivity extends Activity {

	UserPutRequest userPutRequest;
	MessagePutRequest messagePutRequest;

	ListView chatListView;
	ChatAdapter chatAdapter;
	ArrayList<Chat> chatList = new ArrayList<Chat>();

	EditText contentField;
	Button sendButton;

	SharedPreferences pref;

	int userId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		pref = getSharedPreferences("yumobi.test.chat", Activity.MODE_PRIVATE);
		userId = pref.getInt("user_id", -1);

		if (userId == -1) {
			userPutRequest = new UserPutRequest(new UserPutRequestCallBack() {

				@Override
				public void failed() {
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Toast.makeText(MainActivity.this,
									"Failed creating user.",
									Toast.LENGTH_SHORT).show();
						}
					});
				}

				@Override
				public void completed(JSONObject json) {
					SharedPreferences.Editor editor = pref.edit();
					try {
						userId = json.getInt("id");
						editor.putInt("user_id", userId);
						editor.commit();
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});

			userPutRequest.execute("mspark");
		}

		chatListView = (ListView) findViewById(R.id.chatListView);
		contentField = (EditText) findViewById(R.id.contentField);
		sendButton = (Button) findViewById(R.id.sendButton);

		chatAdapter = new ChatAdapter(this, chatList);

		chatListView.setAdapter(chatAdapter);

		loadMessages();

		sendButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String content = contentField.getText().toString();
				contentField.setText("");

				messagePutRequest = new MessagePutRequest(
						new MessagePutRequestCallBack() {

							@Override
							public void failed() {
								runOnUiThread(new Runnable() {

									@Override
									public void run() {
										Toast.makeText(MainActivity.this,
												"Failed sending messages.",
												Toast.LENGTH_SHORT).show();
									}
								});
							}

							@Override
							public void completed() {
								// TODO Auto-generated method stub

							}
						});
				messagePutRequest.execute(userId + "", "9", content);
				loadMessages();
			}
		});
		TimerTask task = new TimerTask() {
			
			@Override
			public void run() {
				loadMessages();
			}
		};
		Timer timer = new Timer();
		timer.schedule(task, 1000, 1000);
	}

	private void loadMessages() {
		MessageGetRequest messageGetRequest = new MessageGetRequest(
				new MessageGetRequestCallBack() {

					@Override
					public void failed() {
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								Toast.makeText(MainActivity.this,
										"Failed loading messages.",
										Toast.LENGTH_SHORT).show();
							}
						});
					}

					@Override
					public void completed(JSONArray jsonArray) {
						final JSONArray array = jsonArray;
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								chatList.clear();
								for (int i = 0; i < array.length(); i++) {
									JSONObject object;
									try {
										object = array.getJSONObject(i);
										int senderId = object
												.getInt("senderId");
										String content = object
												.getString("content");
										boolean fromMe = (senderId == userId) ? true
												: false;
										chatList.add(new Chat(content, fromMe));
									} catch (JSONException e) {
										e.printStackTrace();
									}
								}
								chatAdapter.notifyDataSetChanged();
							}
						});

					}
				});
		messageGetRequest.execute(userId + "", "9");
	}

}
