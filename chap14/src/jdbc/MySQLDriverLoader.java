package jdbc;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public class MySQLDriverLoader extends HttpServlet {

	// 서블릿을 초기화할 때 호출되는 init() 메서드를 구현한다.
	@Override
	public void init(ServletConfig config) throws ServletException {
		try {
			// MySQL JDBC 드라이버를 로딩한다.
			Class.forName("com.mysql.cj.jdbc.Driver");
			//JDBC 드라이버 로딩 과정에서 문제가 있을 경우 익셉션을 발생시켜준다.
		} catch (Exception ex) {
			throw new ServletException(ex);
		}

	}
}
