package member.service;

import java.util.Map;

// MemberDao를 이용해서 실제로 회원 가입 기능을 처리하는 서비스 클래스(JoinRequest)
// 먼저 JoinRequest 클래스를 만들며 이 클래스는 JoinService가 회원 가입 기능을 구현할 떄
// 필요한 요청 데이터를 담는 클래스로 활용함ㄴ
public class JoinRequest {

	// 회원 가입 기능을 구현할 떄 필요한 요청 데이터를 보관하는 필드로 아이디, 암호, 암호 확인 값을 저장함
	private String id;
	private String name;
	private String password;
	private String confirmPassword;

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
	
	public boolean isPasswordEqualToConfirm() {
		return password != null && password.equals(confirmPassword);
	}
	
	// 각 필드의 데이터가 유효한지 검사하기 위해 validate() 메서드 생성
	// 파라미터로 전달받는 errors 맵 객체는 에러 정보를 담기 위해 사용
	// 예를 들면, id 필드값이 올바르지 않는 경우 errors 맵 객체에 "id"키의 값으로 TRUE 값을 추가함
	// 이 errors 파라미터는 뒤에서 살펴볼 JoinHandler에서 생성해서 전달
	// isPasswordEqualToConfirm() 메서드를 이용해서 암호와 확인값이 일치하지 않으면 "nonMatch"라는 에러키 추가
	// 즉, validate() 메서드는 값이 올바른지 검사하는 기능 제공
	// 값이 올바르지 않으면 파라미터로 전달받은 errors 맵 객체에 (키, True) 쌍을 추가. 이때 키는 어떤 에러가 발생했는지 의미함
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
	
	// value 값이 없는 경우, errors 맵 객체의 fieldName 키에 TRUE를 값으로 추가함
	private void checkEmpty(Map<String, Boolean> errors, String value, String fieldName) {
		if(value == null || value.isEmpty()) {
			errors.put(fieldName, Boolean.TRUE);
		}
	}

}
