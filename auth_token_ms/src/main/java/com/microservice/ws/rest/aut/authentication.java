package com.microservice.ws.rest.aut;

import java.sql.DriverManager;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JOptionPane;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.codec.digest.DigestUtils;
import org.codehaus.jettison.json.JSONArray;

import com.microservice.ws.rest.service.filtro;
import com.microservice.ws.rest.vo.User;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;



import org.codehaus.jettison.json.JSONObject;

@Path("/users")
public class authentication {

	public static PreparedStatement ps;
    public static ResultSet res;
    public static final String URL = "jdbc:mysql://localhost:3307/users";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "1234";
    public static JSONArray array = null; 
    
    
public static Connection getConection(){
	 
	Connection con = null;
    
    
    try{
        Class.forName("com.mysql.jdbc.Driver");
        con = (Connection) DriverManager.getConnection(URL, USERNAME, PASSWORD);
        System.out.println( "Conexion exitosa");
    } catch(Exception e){
       System.out.println(e);
    
    }
    return con;
		
	    
	}

@GET
@Path("/valtoken")
public Response valtoken(@QueryParam("iden") String iden,@QueryParam("tok") String tok) throws org.codehaus.jettison.json.JSONException {
	
    
	boolean verificador = false;
	URL url;
	
    try{
    	
    	//System.out.println("hola");
    	
		String token= tok;
		String id= iden;
		
		//System.out.println(token);
		//System.out.println(id);
    	 // Creando un objeto URL
        url = new URL("http://192.168.99.101:8000/users/");

        // Realizando la petici�n GET
        URLConnection con = url.openConnection();

        // Leyendo el resultado
        BufferedReader in = new BufferedReader(new InputStreamReader(
                con.getInputStream()));

        String linea = in.readLine();
       
        JSONArray array = new JSONArray(linea);
        
    	
    	for(int i=0; i<array.length(); i++) {
        	
        	JSONObject coroto = array.getJSONObject(i);
        	//System.out.println(coroto.get("name")+" "+coroto.get("password"));
        	//System.out.println(token);
        	//System.out.println(id);
        	if(coroto.get("auth_token").equals(token) && coroto.get("id").equals(iden)){
        		
        		verificador = true;
        	}
        }
    	
    	//return verificador;
        return Response.ok().entity(String.valueOf(verificador)).build();

    } catch(Exception e){
       System.out.println(e);
       //return false;
       return Response.ok().entity(String.valueOf(verificador)).build();

    }
	
    
	
}


	
	@GET
    @Path("/read")
    @Produces({MediaType.APPLICATION_JSON})
       public ArrayList<User> read(){
        
        
           Connection con = null;
           ArrayList<User> userList = new ArrayList<User>();
           try{
               con = getConection();
               ps = (PreparedStatement) con.prepareStatement("SELECT * FROM user");
               
               
               res = ps.executeQuery();
               
              
               while(res.next()) {
                User tmp = new User();
                tmp.setName(res.getString("name"));
                tmp.setPassword(res.getString("password"));
                userList.add(tmp);
               }
           } catch(Exception e){
              System.out.println(e);
           
           }
     return  userList;
           
       }
	
	
	
	

}
