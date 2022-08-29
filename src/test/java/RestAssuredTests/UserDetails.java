package RestAssuredTests;

import org.apache.commons.io.IOUtils;
import org.json.simple.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.http.ContentType;
import static io.restassured.matcher.RestAssuredMatchers.matchesXsdInClasspath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

public class UserDetails {

	//@Test
	public void GET_Users() {
		
		given().
		when().
			get("https://reqres.in/api/users?page=2").
		then().
			statusCode(200).
//			assertThat().body("data[4].first_name", equalTo("George")).
			assertThat().body("data.last_name", hasItem("Lawson")).
			header("content-type", "application/json; charset=utf-8").
			log().all();
	}
	
	//@Test
	public void GET_Users_Filter() {
		
		given().
		when().
			get("https://reqres.in/api/users?page=2").
		then().
			statusCode(200).
			assertThat().body("data.findAll { data -> data.id > 5 && data.id < 10 }", hasItem("{last_name=Lawson}")).
			header("content-type", "application/json; charset=utf-8").
			log().all();
	}
	
	public static HashMap<Object, Object> post = new HashMap<Object, Object>();
	public static HashMap<Object, Object> put = new HashMap<Object, Object>();

	@BeforeClass
	public void postdata() {
		post.put("name", "Raman Kumar G");
		post.put("job", "Manager");
		
		put.put("employee_name", "Raman Kumar G");
		put.put("employee_salary", "20000");
		put.put("employee_age", "38");
	}
	
	//@Test
	public void POST_User() {
		
		given().
			contentType("application/json").
			body(post).
			
		when().
			post("https://reqres.in/api/users").
		then().
			statusCode(201).
			assertThat().body("name", is("Raman Kumar")).
			header("content-type", "application/json; charset=utf-8").
			log().all();
	}
	
//	@Test
		public void POST_User_JSON() {
		
		JSONObject request = new JSONObject();
		request.put("name", "Raman K");
		request.put("job", "Manager");
		
		baseURI = "https://reqres.in/api";
		basePath = "/users";
		
		System.out.println(request);	// Same as 	System.out.println(request.toJSONString());
			
			given().
				contentType("application/json").
				body(request).
				
			when().
				post("/").
			then().
				statusCode(201).
				assertThat().body("name", is("Raman K")).
				header("content-type", "application/json; charset=utf-8").
				log().all();
		}
		
		@Test
		public void POST_User_From_JSON_file() throws IOException {
		
		baseURI = "https://reqres.in/api";
		basePath = "/users";
		
		File file = new File("./JSONRequest/request.json");
		
		if(file.exists()) {
			System.out.println(" >> File Exists ");
		}
		FileInputStream fis = new FileInputStream(file);
		String requestBody = IOUtils.toString(fis, "UTF-8");
		
			given().
				contentType("application/json").
				body(requestBody).
				
			when().
				post("/").
			then().
				statusCode(201).
				assertThat().body("name", is("Raj Kumar")).
				header("content-type", "application/json; charset=utf-8").
				log().all();
		}
	
//	@Test
	public void PUT_User() {
		
		given().
			contentType("application/json").
			body(put).
			
		when().
			put("https://dummy.restapiexample.com/public/api/v1/update/20").
		then().
			statusCode(200).
			assertThat().
			body("data.employee_name", equalTo("Raman Kumar G")).
			body("message",equalTo("Successfully! Record has been updated.")).
			header("content-type", "application/json").
			log().all();
	}
	
	//@Test
		public void JSON_Schema_Validator() {
			
		baseURI = "https://reqres.in/api";
		basePath = "/users?page=2";
		
			given().
			when().
				get("/").
			then().
				assertThat().body(matchesJsonSchemaInClasspath("schema.json")).
				statusCode(200);
		}
	
	//@Test
	public void ValidateSoapXML() throws IOException {
		
	baseURI = "https://www.crcind.com";
	
	File file = new File("./SoapRequest/Add.xml");
	
	if(file.exists()) {
		System.out.println(" >> File Exists ");
	}
	FileInputStream fis = new FileInputStream(file);
	String requestBody = IOUtils.toString(fis, "UTF-8");
	
		given().
			contentType("text/xml").
			accept(ContentType.XML).
			body(requestBody).
		when().
			post("/csp/samples/SOAP.Demo.cls?soap_method=AddInteger&Arg1=1&Arg2=2").
		then().
			statusCode(200).log().all().
			and().
				body("//*:AddIntegerResult.text()", equalTo("3")).
			and().
				assertThat().body(matchesXsdInClasspath("xmlvalidate.xsd"));
	}
}
