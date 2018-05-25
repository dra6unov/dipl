package main;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.eclipse.jdt.internal.compiler.flow.FlowContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;

@Controller
public class Testing {
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);
	private static HttpTransport httpTransport;
	private static Drive drive;
	
	public GoogleClientSecrets clientSecrets() throws Exception {
		GoogleClientSecrets clientSecrets= GoogleClientSecrets.load(JSON_FACTORY,
				new InputStreamReader(GoogleAuth.class.getResourceAsStream("/client_secret.json")));
		return clientSecrets;
	}
	
	public GoogleAuthorizationCodeFlow flow(final NetHttpTransport HTTP_TRANSPORT) throws Exception{
		GoogleAuthorizationCodeFlow flow= new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
				clientSecrets(), SCOPES).setDataStoreFactory(new FileDataStoreFactory(new java.io.File("credentials")))
				.setAccessType("offline").build();
		return flow;
	}
	
	private Credential auth(final NetHttpTransport HTTP_TRANSPORT) throws Exception{
		Credential credential = new AuthorizationCodeInstalledApp(flow(HTTP_TRANSPORT),
				new LocalServerReceiver.Builder().setPort(8089).build()).authorize("user");
		return credential;
	}
	
	/*private static Credential auth(final NetHttpTransport HTTP_TRANSPORT) throws Exception {
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
				new InputStreamReader(GoogleAuth.class.getResourceAsStream("/client_secret.json")));
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
				clientSecrets, SCOPES).setDataStoreFactory(new FileDataStoreFactory(new java.io.File("credentials")))
						.setAccessType("offline").build();
		Credential credential = new AuthorizationCodeInstalledApp(flow,
				new LocalServerReceiver.Builder().setPort(8089).build()).authorize("user");
		return credential;
	}*/
	
	@RequestMapping("/t")
	public  String tets(final NetHttpTransport HTTP_TRANSPORT, HttpServletResponse resp) throws Exception {
		Credential credential = auth(HTTP_TRANSPORT);
		return "redirect:https://accounts.google.com/o/oauth2/auth?access_type=offline&client_id=751667716532-g4vdk25ai7kibi299pkv1tc5j5l2557t.apps.googleusercontent.com&redirect_uri=http://localhost:8089/Callback&response_type=code&scope=https://www.googleapis.com/auth/drive";
		
		
		//Credential credential = auth(HTTP_TRANSPORT);
		//System.out.println("credential: " + credential);
		/*if (auth(HTTP_TRANSPORT).) {
			resp.sendRedirect(flow(HTTP_TRANSPORT).newAuthorizationUrl().setRedirectUri("http://localhost:8081").build());
		}*/
	}
	
	/*public String getURL(final NetHttpTransport HTTP_TRANSPORT) throws Exception{
		String url = flow(HTTP_TRANSPORT).newAuthorizationUrl().setRedirectUri("http://localhost:8081").build();
		return url;
	}*/
	
	/*@RequestMapping("/t")
	public void setCode(String code) throws Exception{
		final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
		GoogleTokenResponse response = flow(HTTP_TRANSPORT).newTokenRequest(code).setRedirectUri("http://localhost:8081").execute();
		GoogleCredential credential = new GoogleCredential().setFromTokenResponse(response);
		
		System.out.println("credential: " + credential);
		System.out.println("respone: " + response);
		//Create a new authorized API client
		drive = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).build();
	}*/

}
