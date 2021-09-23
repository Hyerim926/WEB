package article.command;

public class EqulasIgnorecase_Ex {

	public static void main(String[] args) {

		String str = "Jang";
		
		// equals : 같은 값 확인할때, 대소문자 구분을 한다.		
		System.out.println("JANG".equals(str)); // false

		// equalsIgnoreCase : 같은 값 확인할때, 대소문자 구분을 하지 않는다.
		System.out.println("JANG".equalsIgnoreCase(str)); // true
		
	}

}
