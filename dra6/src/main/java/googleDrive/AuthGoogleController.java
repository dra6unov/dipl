package googleDrive;

import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.DriveScopes;

import main.GoogleAuth;

@Controller
public class AuthGoogleController {
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);

	@RequestMapping("/authgoogle")
	private static Credential auth(final NetHttpTransport HTTP_TRANSPORT) throws Exception {
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
				new InputStreamReader(GoogleAuth.class.getResourceAsStream("/client_secret.json")));

		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
				clientSecrets, SCOPES).setDataStoreFactory(new FileDataStoreFactory(new java.io.File("credentials")))
						.setAccessType("offline").build();
		Credential credential = new AuthorizationCodeInstalledApp(flow,
				new LocalServerReceiver.Builder().setPort(8089).build()).authorize("user");
		System.out.println("credential: " + credential);
		// return credential;
		System.out.println("flow: " + flow);
		// return new AuthorizationCodeInstalledApp(flow, new
		// LocalServerReceiver.Builder().setPort(8089).build())
		// .authorize("user");
		// redirectedUrl("https://accounts.google.com/o/oauth2/auth?access_type=offline&client_id=751667716532-g4vdk25ai7kibi299pkv1tc5j5l2557t.apps.googleusercontent.com&redirect_uri=http://localhost:8089/Callback&response_type=code&scope=https://www.googleapis.com/auth/drive");
		return credential;
	}

}
