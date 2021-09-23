package auth.service;

// LoginService 클래스는 사용자가 입력한 아이디와 암호가 올바른지 검사한다.
// 아이디와 암호가 일치하면 로그인 한 사용자 정보를 담은 User 객체를 리턴한다.
// 이에, LoginService에서 필요로 하는 클래스인 User 클래스를 작성한다.
public class User {
	
	private String id;
	private String name;
	

	public User(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

}
