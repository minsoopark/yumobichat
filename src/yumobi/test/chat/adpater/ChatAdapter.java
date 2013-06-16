package yumobi.test.chat.adpater;

import java.util.ArrayList;

import yumobi.test.R;
import yumobi.test.chat.model.Chat;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;

public class ChatAdapter extends BaseAdapter {

	private Activity mActivity;
	private ArrayList<Chat> chatList;

	public ChatAdapter(Activity mActivity, ArrayList<Chat> chatList) {
		this.mActivity = mActivity;
		this.chatList = chatList;
	}

	@Override
	public int getCount() {
		return this.chatList.size();
	}

	@Override
	public Object getItem(int index) {
		return this.chatList.get(index);
	}

	@Override
	public long getItemId(int index) {
		return index;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Chat chat = this.chatList.get(position);
		View v = convertView;
		LayoutInflater li = (LayoutInflater) mActivity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ChatViewHolder viewHolder;

		if (v == null) {
			v = li.inflate(R.layout.each_message, null);
			viewHolder = new ChatViewHolder();

			viewHolder.messageTextView = (TextView) v
					.findViewById(R.id.messageTextView);

			v.setTag(viewHolder);
		} else {
			viewHolder = (ChatViewHolder) v.getTag();
		}

		if (chat.isFromMe()) {
			viewHolder.messageTextView
					.setLayoutParams(new FrameLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT, Gravity.RIGHT
									| Gravity.CENTER_VERTICAL));
			viewHolder.messageTextView.setTextColor(Color.WHITE);
			viewHolder.messageTextView
					.setBackgroundResource(R.drawable.mysaying);
		} else {
			viewHolder.messageTextView
					.setLayoutParams(new FrameLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT, Gravity.LEFT
									| Gravity.CENTER_VERTICAL));
			viewHolder.messageTextView.setTextColor(Color.BLACK);
			viewHolder.messageTextView
					.setBackgroundResource(R.drawable.friendsaying);
		}
		viewHolder.messageTextView.setText(chat.getMessage());

		return v;
	}

	class ChatViewHolder {
		TextView messageTextView;
	}

}
