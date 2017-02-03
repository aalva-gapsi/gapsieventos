/**
 * 
 */
package mx.com.gapsi.eventos;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import mx.com.gapsi.eventos.exception.LoginException;
import mx.com.gapsi.eventos.facade.Login;
import mx.com.gapsi.eventos.model.Usuario;

/**
 * @author Don
 *
 */
@Ignore
public class LoginTest {
	/** The context. */
	private static ClassPathXmlApplicationContext context = null;
	private Login login;
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		context = new ClassPathXmlApplicationContext("testApplicationContext.xml");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		login = (Login) context.getBean("login");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		String username ="0";
		String password ="0";
		//		fail("Not yet implemented");
//		login
		 Usuario usuario = null;
		try {
			 usuario = login.validarLogin(username, password);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		org.junit.Assert.assertNotNull("Usuario autenticado", usuario);
	}

	/**
	 * @return the login
	 */
	public Login getLogin() {
		return login;
	}

	/**
	 * @param login the login to set
	 */
	public void setLogin(Login loginImpl) {
		this.login = loginImpl;
	}

}
