package member.service;

// 현재 암호가 일치하지 않는 경우(보통 암호를 변경할 때, 현재 암호와 변경할 암호를 함께 입력하는데,
// 이때 현재 암호가 일치하지 않으면 암호 변경에 실패한다) 사용할 예외 처리 클래스 작성
public class InvalidPasswordException extends RuntimeException {

	
	
}
