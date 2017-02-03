package example.jpa;

import java.io.IOException;
import java.util.Set;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.springframework.stereotype.Component;

import com.ibm.json.java.JSON;
import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;

import mx.com.gapsi.eventos.facade.Login;
import mx.com.gapsi.eventos.facade.LoginImpl;
import mx.com.gapsi.eventos.model.Usuario;


// This class define the RESTful API to fetch the database service information
// <basepath>/api/dbinfo
//@Component
@Path("/dbinfo")
public class HelloResource {

//	@Inject
	LoginImpl loginImpl;
	@GET
	public String getInformation() throws Exception, IOException {
		// load all system environments
		//JSONObject sysEnv = new JSONObject(System.getenv());

		// 'VCAP_APPLICATION' is in JSON format, it contains useful information about a deployed application
		// String envApp = System.getenv("VCAP_APPLICATION");

		// 'VCAP_SERVICES' contains all the credentials of services bound to this application.
		String envServices = System.getenv("VCAP_SERVICES");

        if (envServices == null) {
        	  return("VCAP_SERVICES not found");
        	  
          }
		
        JSONObject obj =  (JSONObject) JSON.parse(envServices);
        
        String thekey = null;
        Set<String> keys = obj.keySet();
        System.out.println ("Searching through VCAP keys");
  	  // Look for the VCAP key that holds the SQLDB information
        for (String eachkey : keys) {
      	  if (eachkey.contains("sqldb")) {
      		  thekey = eachkey;
      	  }
        }
        if (thekey == null) {
      	  return("Cannot find any SQLDB service in the VCAP_SERVICES");
      	  
        }
        
        JSONArray list = (JSONArray) obj.get (thekey);
        obj = (JSONObject) list.get(0);
        String name = (String) obj.get("name");
        obj = (JSONObject) obj.get ("credentials");
        String databaseHost = (String) obj.get ("host");
        String databaseName = (String) obj.get ("db");
        Long port = (Long) obj.get ("port");
//        String user = (String) obj.get ("username"); 
//        String password = (String) obj.get ("password");
        String jdbcurl = (String) obj.get("jdbcurl");
        
        
        JSONObject DBInfoObj = new JSONObject();

        DBInfoObj.put("name", name);
        DBInfoObj.put("host", databaseHost);
        DBInfoObj.put("db", databaseName);
        DBInfoObj.put("port", port);
        DBInfoObj.put("jdbcurl", jdbcurl);
        
//        LoginImpl login = new LoginImpl();
        boolean loginOk ;
        Usuario usuario = new Usuario();
        usuario.setContrasenia("0");
        usuario.setUsuario("0");
        
        if (loginImpl !=null) {
			final Usuario validarLogin = loginImpl.validarLogin(usuario);
			DBInfoObj.put("login", validarLogin.getUsuario());
		}
		return DBInfoObj.toString();
        
        
        
	}
	/**
	 * @return the login
	 */
	public Login getLogin() {
		return loginImpl;
	}
	/**
	 * @param login the login to set
	 */
	public void setLogin(LoginImpl loginImpl) {
		this.loginImpl = loginImpl;
	}
}