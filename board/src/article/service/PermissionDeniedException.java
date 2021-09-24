package article.service;

// 게시글 수정 권한이 없는 경우, 예외 처리를 위한 익셉션 제작
public class PermissionDeniedException extends RuntimeException{

}
