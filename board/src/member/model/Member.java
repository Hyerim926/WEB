package member.model;

import java.util.Date;

public class Member {
	private String id;
	private String name;
	private String password;
	private Date regDate;
	
	public Member(String id, String name, String password, Date regDate) {
		super();
		this.id = id;
		this.name = name;
		this.password = password;
		this.regDate = regDate;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getPassword() {
		return password;
	}

	public Date getRegDate() {
		return regDate;
	}

	// 암호 변경 기능을 구현하는 matchPassword() 메서드를 정의함
	public boolean matchPassword(String pwd) {
		return password.equals(pwd);
	}
	
	// 암호 변경 기능을 구현하기 위해 Member와 MemberDao 두 클래스에 코드추가
	// 먼저, Member 클래스에 changePassword() 메서드를 추가한다
	public void changePassword(String newPwd) {
		this.password = newPwd;
	}
	
}
