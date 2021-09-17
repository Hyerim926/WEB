package member.service;

// 현재 암호가 일치하지 않는 경우(암호 변경시 보통 현재 암호와 변경할 암호를 함께 입력하는데
// 이때 현재 암호가 일치하지 않으면 암호변경에 실패)
public class InvalidPasswordException extends RuntimeException{

}
