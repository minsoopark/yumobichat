package yumobi.test.chat.model;

public class Chat {
	private String message;
	private boolean fromMe;

	public Chat(String message, boolean fromMe) {
		this.message = message;
		this.fromMe = fromMe;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return this.message;
	}

	public boolean isFromMe() {
		return fromMe;
	}

	public void setFromMe(boolean fromMe) {
		this.fromMe = fromMe;
	}

}
