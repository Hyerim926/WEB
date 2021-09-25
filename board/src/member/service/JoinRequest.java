package member.service;

import java.util.Map;

// MemberDao를 이용해서 실제로 회원 가입 기능을 처리하는
// 서비스 클래스 코드를 만들려고 함
// 먼저 JoinRequest 클래스를 만들려고 함.
// 이 클래스는 JoinService가 회원 가입 기능을 구현할 때 필요한
// 요청 데이터를 담는 클래스로 활용할 것임.
public class JoinRequest {

	// 회원 가입 기능을 구현할 때 필요한 요청 데이터를 보관하는 필드로
	// 아이디, 이름, 암호, 암호 확인 값을 저장함.
	private String id;
	private String name;
	private String password;
	private String confirmPassword;
	
	// 각 필드를 위한 get/set 메서드 정의함.
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getConfirmPassword() {
		return confirmPassword;
	}
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	
	// password 필드와 confirmPassword 필드 값이 같은지 검사함.
	// 필드 데이터가 유효한지 여부를 검사할 때 사용하는
	// isPasswordEqualToConfirm() 메서드 선언함.
	public boolean isPasswordEqualToConfirm() {
		// password값이 null이 아니거나 confirmPassword와 같지 않을 때 password를 리턴함
		return password != null && password.equals(confirmPassword);
	}
	
	// 각 필드의 데이터가 유효한지 검사하는 validate() 메서드를 선언함.
	// 파라미터로 전달받는 erros 맵 객체는 에러 정보를 담기 위해 사용함.
	// 예를 들어, id 필드 값이 올바르지 않는 경우 errors 맵 객체에
	// "id"키의 값으로 TRUE 값을 추가함. 이 errors 파라미터는 다음에 만드는
	// JoinHandler에서 생성해서 전달할 것임.
	// 다음 65행 ~ 67행까지는 앞서, 47행에서 정의한
	// isPasswordEqualToConfirm() 메서드를 이용해서
	// 암호와 확인값이 일치하지 않으면 "notMatch"라는 에러 키를 추가함을 의미함.
	// 즉, validate() 메서드는 값이 올바른지 검사하는 기능을 제공함.
	// 값이 올바르지 않으면 파라미터로 전달받은 errors 맵 객체에 (키, True) 쌍을
	// 추가함. 이때 키는 어떤 에러가 발생했는지를 의미함.
	public void validate(Map<String, Boolean> errors) {
		checkEmpty(errors, id, "id");
		checkEmpty(errors, name, "name");
		checkEmpty(errors, password, "password");
		checkEmpty(errors, confirmPassword, "confirmPassword");
		if(!errors.containsKey("confirmPassword")) {
			if(!isPasswordEqualToConfirm()) {
				errors.put("notMatch", Boolean.TRUE);
			}
		}
	}

	// value 값이 없는 경우, errors 맵 객체의 fieldName 키에
	// TRUE 값을 추가함을 의미함.
	private void checkEmpty(Map<String, Boolean> errors,
			String value, String fieldName) {
		if(value == null || value.isEmpty()) {
			errors.put(fieldName, Boolean.TRUE);
		}
	}
}
