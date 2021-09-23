package member.service;


// 회원 가입을 처리할 때 동일한 아이디를 갖는 데이터가 이미 존재해서
// 익셉션 발생할 때 사용하는 DuplicateIdException 클래스 작성함.
public class DuplicateIdException extends RuntimeException {

}
