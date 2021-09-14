package guestbook.model;

public class Message {
	
	private int id;
	private String guestName;
	private String password;
	private String message;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getGuestName() {
		return guestName;
	}
	
	public void setGuestName(String guestName) {
		this.guestName = guestName;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	// hasPassword() 메서드는 방명록 메시지에 암호가 지정되어있는지 확인
	public boolean hasPassword() {
		return password != null && !password.isEmpty();
	}
	// matchPassword() 메서드는 파라미터로 전달받은 pwd가
	// 암호를 지정한 password 필드와 같은지 확인 처리함
	public boolean matchPassword(String pwd) {
		return password != null && password.equals(pwd);
	}
}
