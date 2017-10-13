package com.microservice.ws.rest.service;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.codec.digest.DigestUtils;
import org.codehaus.jettison.json.JSONArray;

import com.microservice.ws.rest.vo.User;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.sun.research.ws.wadl.Response;

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





public class filtro implements Filter{
	@Context
    UriInfo uriInfo;
	
	public static final String URL = "jdbc:mysql://localhost:3307/users";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "1234";
    
    public static PreparedStatement ps;
    public static ResultSet res;
    
    public String token="";
	public String id="";
	public String uri;
    
    /*
	
    @POST
	@Path("/create")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
    
	public User create(User vo){
	    
		String nick = vo.getName();
		
	    String pass = vo.getPassword(); 
	    String enPass=DigestUtils.md5Hex(pass); 
	    
	    
	    

	    Connection con = null;
	    try{
	        con = getConection();
	        ps = (PreparedStatement) con.prepareStatement("INSERT INTO user (name, password) VALUES(?,?)");
	        ps.setString(1, nick);
	        ps.setString(2, enPass);
	        
	        int res = ps.executeUpdate();
	        if (res > 0){
	            System.out.println("Usuario creado con exito");
	        } else {
	            System.out.println("Error al crear usuario");
	        }
	    } catch(Exception e){
	       System.out.println(e);
	    
	    }
		return vo;
	    
	}


    	
	
    
   

    
    
    
   	@GET
	@Path("/valtoken")
   	
   	@Consumes({MediaType.APPLICATION_OCTET_STREAM})
   	@Produces({MediaType.APPLICATION_OCTET_STREAM})
   	
   	
	public boolean valtoken (@Context HttpServletRequest request, HttpServletResponse response){
	    System.out.println(request);
   		//Recibimos los datos
		//String entrada = en;
	    
	    
	    
		
	    //Si el token y el id que se recibe esta dentro de la base de datos  devuelve true, de lo contrario devuelve false
		boolean verificador = false;
		URL url;
		
	    try{
	    	//transformamos el string a JSON y obtenemos los valores del id y el token
	    	//JSONObject ent = new JSONObject(entrada);
			//Guardamos los valores en las siguientes variables
			//String token=ent.getString("name");
			//String id= ent.getString("password");
	    	String token="";
	    	String id="";
	    	
	    	//response.setContentType("text/html");
			//PrintWriter out = response.getWriter();
			//out.println("Headers<hr/>");
			Enumeration<String> headerNames = request.getHeaderNames();
			while (headerNames.hasMoreElements()) {
				String headerName = headerNames.nextElement();
				//out.print("Header Name: <em>" + headerName);
				String headerValue = request.getHeader(headerName);
				//out.print("</em>, Header Value: <em>" + headerValue);
				//out.println("</em><br/>");
				if(headerName.equals("auth_token")){
					token = headerValue;
				}
				if(headerName.equals("user_id")){
					id = headerValue;
				}
			}
				
	    	 // Creando un objeto URL
            url = new URL("http://localhost:8080/Users_ms/services/users/read");
 
            // Realizando la petición GET
            URLConnection con = url.openConnection();
 
            // Leyendo el resultado
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            //Guardamos el contenido de la URL en esta variable
            String linea = in.readLine();
            // El contenido es un arreglo de JSON asi que lo asignamos a una variable de tipo JSON array
           // para poder comparar luego todos los valores en la lista con los datos que tenemos   
            JSONArray array = new JSONArray(linea);
            
	    	
	    	for(int i=0; i<array.length(); i++) {
	        	
	        	JSONObject elem = array.getJSONObject(i);
	        	//System.out.println(coroto.get("name")+" "+coroto.get("password"));
	        	//System.out.println(token);
	        	//System.out.println(id);
	        	if(elem.get("password").equals(id) && elem.get("name").equals(token)){
	        		
	        		verificador = true;
	        	}
	        }
	    	
	    	return verificador;
	        
	    } catch(Exception e){
	       System.out.println(e);
	       return false;
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
    
    @DELETE
    @Path("/delete/{id}")
    public void delete(@PathParam("id") long id) {
    	
    	Connection con = null;
    	try{
	        con = getConection();
	        ps = (PreparedStatement) con.prepareStatement("DELETE FROM user where id = ?");
	        ps.setLong(1, id);
	       
	        
	        int res = ps.executeUpdate();
	        
	    } catch(Exception e){
	       System.out.println(e);
	    
	    }
    }

    @PUT
    @Path("/update/{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public void update(User vo, @PathParam("id") long id ) {
    	
    	int res2;
    	
    	String nick = vo.getName();
		String pass = vo.getPassword(); 
	    String enPass=DigestUtils.md5Hex(pass); 
    	
    	Connection con = null;
    	try{
	        
    		con = getConection();
	        if((pass.equals(null)  || pass.equals("") ) && (!nick.equals(null) || !nick.equals(""))) {
	        	ps = (PreparedStatement) con.prepareStatement("UPDATE user SET name = ? WHERE id= ?");
	        	ps.setString(1, nick);
	        	ps.setLong(2, id);
	        }
	        if((nick.equals(null)  || nick.equals("") ) && (!pass.equals(null) || !pass.equals(""))) {
	        	ps = (PreparedStatement) con.prepareStatement("UPDATE user SET password = ? WHERE id= ?");
	        	ps.setString(1, enPass);
	        	ps.setLong(2, id);
	        }
	        if((!pass.equals(null)  || !pass.equals("") ) && (!nick.equals(null) || !nick.equals(""))) {
	        	ps = (PreparedStatement) con.prepareStatement("UPDATE user SET name= ?, password = ? WHERE id= ?");
	        	ps.setString(1, nick);
	        	ps.setString(2, enPass);
	        	ps.setLong(3, id);
	        }


	        res2 = ps.executeUpdate();
	        
	       
	        
	    } catch(Exception e){
	       System.out.println(e);
	    
	    }
		
    }
*/
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2)
			throws IOException, ServletException {
		
		HttpServletRequest request = (HttpServletRequest) arg0;
		ServletRequest request2 = null;
		HttpServletRequest request3 = null;
		
		 uri =  request.getRequestURI();
		 
		
	
		//System.out.println(uri+" inicial");
		
		
		
		
		boolean verificador = false;
		URL url;
		
	    try{
	    	//transformamos el string a JSON y obtenemos los valores del id y el token
	    	//JSONObject ent = new JSONObject(entrada);
			//Guardamos los valores en las siguientes variables
			//String token=ent.getString("name");
			//String id= ent.getString("password");
	    	
			//System.out.println("Hol");
			
	    	//response.setContentType("text/html");
			//PrintWriter out = response.getWriter();
			//out.println("Headers<hr/>");
			Enumeration<String> headerNames = request.getHeaderNames();
			while (headerNames.hasMoreElements()) {
				String headerName = headerNames.nextElement();
				//out.print("Header Name: <em>" + headerName);
				String headerValue = request.getHeader(headerName);
				//out.print("</em>, Header Value: <em>" + headerValue);
				//out.println("</em><br/>");
				if(headerName.equals("aut_token")){
					token = headerValue;
					System.out.println(token);
				}
				if(headerName.equals("user_id")){
					id = headerValue;
					System.out.println(id);
				}
				
			}
			
			
			System.out.println(uri + " prueba");
			request2 = new HttpServletRequestWrapper(request) {@Override public String getRequestURI() {return uri;}};

			request3 = (HttpServletRequest) request2;
			String uri3 = request3.getRequestURI();
			System.out.println(uri3+" Esta es la uri de la r3");
			//System.out.println((request == null) ? "soy null" : "no null");
			RequestDispatcher rd = request.getRequestDispatcher("valtoken");
			//rd.forward(request, arg1);
			if (request.getQueryString()!= null){ 
				//System.out.println("entra al if");
				arg2.doFilter(request, arg1);} 
			else {
					uri= uri+"?iden="+id+"&tok="+token;
					//System.out.println("entra al else");
					//System.out.println(uri);
					((HttpServletResponse) arg1).sendRedirect(uri);}
			
			//arg2.doFilter(request, arg1);
			
			
			//System.out.println(((HttpServletRequest) request2).getRequestURI());
			
		    //arg2.doFilter(request, arg1);
				/*
	    	 // Creando un objeto URL
            url = new URL("http://localhost:8080/Users_ms/services/users/read");
			System.out.println("Ho");

            // Realizando la petición GET
            URLConnection con = url.openConnection();
 
            // Leyendo el resultado
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            //Guardamos el contenido de la URL en esta variable
            String linea = in.readLine();
            // El contenido es un arreglo de JSON asi que lo asignamos a una variable de tipo JSON array
           // para poder comparar luego todos los valores en la lista con los datos que tenemos   
            JSONArray array = new JSONArray(linea);
            
            System.out.println("Hi");
	    	for(int i=0; i<array.length(); i++) {
	    		System.out.println("Hu");
	        	JSONObject elem = array.getJSONObject(i);
	        	//System.out.println(coroto.get("name")+" "+coroto.get("password"));
	        	System.out.println(token);
	        	System.out.println(id);
	        	if(elem.get("password").equals(id) && elem.get("name").equals(token)){
	        		
	        		verificador = true;
	        	}
	        }
	    	
	    	*/
	        
	    } catch(Exception e){
	       System.out.println(e);
	       arg2.doFilter(request, arg1);
	       
	    }
	    //System.out.println(request.getRequestURI());
	    //System.out.println(((HttpServletRequest) request2).getRequestURI());
	    //HttpServletRequest request3 = (HttpServletRequest) request2;
	    
	    
	}
	
	/*
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
           
       }*/

	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}
	
}





