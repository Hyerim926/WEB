package jdbc;

import java.sql.DriverManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.commons.dbcp2.ConnectionFactory;
import org.apache.commons.dbcp2.DriverManagerConnectionFactory;
import org.apache.commons.dbcp2.PoolableConnection;
import org.apache.commons.dbcp2.PoolableConnectionFactory;
import org.apache.commons.dbcp2.PoolingDriver;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

// 커넥션 풀 초기화 서블릿 클래스
public class DBCPInit extends HttpServlet {

	@Override
	public void init() throws ServletException {
		loadJDBCDriver();
		initConnectionPool();
	}

	private void loadJDBCDriver() {
		try { // 커넥션 풀이 내부에서 사용할 JDBC 드라이버를 로딩한다. 예제의 경우, MySQL에 연결 MySQL용 드라이버를 로딩한다.
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException ex) {
			throw new RuntimeException("fail to load JDBC Driver", ex);
		}

	}

	private void initConnectionPool() {
		try {
			String jdbcUrl = "jdbc:mysql://localhost:3306/guestbook?" + "characterEncoding=UTF-8&serverTimezone=UTC";

			String username = "jspexam";
			String pw = "jsppw";

			// 커넥션 풀이 새로운 커넥션을 생성할 때 사용할 커넥션 팩토리(ConnectionFactory)를 생성한다.
			ConnectionFactory connFactory =
					// MySQL 연결에 사용할 jdbcUrl, username, pw를 생성자로 지정한다.
					new DriverManagerConnectionFactory(jdbcUrl, username, pw);

			// PoolableConnection을 생성하는 팩토리를 생성한다.
			// DBCP는 커넥션 풀에 커넥션을 보관할 때 PoolableConnection을 사용한다.
			// 이 클래스는 내부적으로 실제 커넥션을 담고 있으며,
			// 커넥션 풀을 관리하는 데 필요한 기능을 추가로 제공한다.
			// 예를들면, close() 메서드를 실행하면 실제 커넥션을 종료하지 않고 풀에 커넥션을 반환한다.
			PoolableConnectionFactory poolableConnFactory = new PoolableConnectionFactory(connFactory, null);
			poolableConnFactory.setValidationQuery("select 1");
			// DB 커넥션이 유효한지 여부를 검사할 때 사용할 쿼리를 지정합니다.
			// 이것은 특정 시간마다 트랜잭션 DB 세션 재접속을 검증하는 validationQuery를 실행하게 됩니다.
			// 오라클의 경우 : validationQuery="select 1 from dual" 환경을 셋팅합니다.

			// 커넥션 풀의 설정정보를 생성한다.
			GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
			// 유효 커넥션 검사 주기(1000L*60L*5L) 설정
			poolConfig.setTimeBetweenEvictionRunsMillis(1000L * 60L * 5L);

			// 풀에 보관중인 커넥션 유효 검사 설정(true)
			poolConfig.setTestWhileIdle(true);

			// 커넥션 최소 개수 (4) 설정
			poolConfig.setMinIdle(4);

			// 커넥션 최대 개수 (50) 설정
			poolConfig.setMaxTotal(50);

			// 커넥션 풀을 생성한다. 생성자는 PoolableConnection을
			// 사용할 팩토리(PoolableConnectionFactory)와
			// 커넥션 풀 설정(GenericObkectPoolConfig)을 파라미터로 전달 받는다.
			GenericObjectPool<PoolableConnection> connectionPool = 
					new GenericObjectPool<>(poolableConnFactory, poolConfig);
			// PoolableConnectionFactory에서도
			// GenericObjectPool에서 생성한 커넥션 풀을 연결한다.
			poolableConnFactory.setPool(connectionPool);

			Class.forName("org.apache.commons.dbcp2.PoolingDriver");

			PoolingDriver driver = (PoolingDriver) DriverManager.getDriver("jdbc:apache:commons:dbcp:");
			// 커넥션 풀 드라이버에 GenericObjectPool에서 생성한 커넥션 풀을 등록한다.
			// 이때, "chap14"를 커넥션 풀 이름으로 주었는데,
			// 이 경우 프로그램에서 사용하는 JDBC URL은
			// "jdbc:apache:commons:dbcp:chap14"가 된다.

			driver.registerPool("guestbook", connectionPool);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

}
