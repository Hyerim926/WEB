package member.service;

// 회원 가입을 처리할 떄 동일한 아이디를 갖는 데이터가 이미 존재해서 발생할 떄 익셉션으로 사용한는 
// DuplicateIdException 클래스 작성
public class DuplicateIdException extends RuntimeException {

}
