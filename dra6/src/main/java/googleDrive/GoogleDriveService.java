package googleDrive;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleRefreshTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import dropBox.DropBoxService;
import entity.Auth;
import entity.Google_file;
import repos.AuthRepo;
import repos.Google_fileRepo;

@Service
public class GoogleDriveService {
	private static final Logger LOG = LoggerFactory.getLogger(DropBoxService.class);
	
	
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);
	//private static GoogleAuthorizationCodeFlow flow;
	
	@Autowired
	AuthRepo authRepo;
	
	@Autowired
	Google_fileRepo googleRepo;
	
	@Value("${google.app.secret}")
	private String appSecret;
	
	@Value("${google.app.id}")
	private String appId;
	
	private GoogleAuthorizationCodeFlow googleFlow(NetHttpTransport httpTransport) throws IOException {
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(GoogleDriveService.class.getResourceAsStream("/client_secret.json")));
		GoogleAuthorizationCodeFlow flow;
		flow= new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
				.setDataStoreFactory(new FileDataStoreFactory(new java.io.File("credentials"))).setAccessType("offline")
				.build();
		
		return flow;
	}
	
	public String googleAuthUrl(NetHttpTransport httpTransport) throws IOException {
		GoogleAuthorizationCodeFlow flow = googleFlow(httpTransport);
		String Url = flow.newAuthorizationUrl().setRedirectUri("http://localhost:8081").build();
		LOG.info("url: " + Url);
		return Url;
	}
	
	public Credential credential(NetHttpTransport httpTransport) throws IOException {
		GoogleAuthorizationCodeFlow flow = googleFlow(httpTransport);
		Credential credential = new AuthorizationCodeInstalledApp(flow,
				new LocalServerReceiver.Builder().setPort(8089).build()).authorize("user");
		return credential;
	}
	
	public void getRefreshToken(NetHttpTransport httpTransport, HttpSession session) throws IOException {
		Credential cred = credential(httpTransport);
		Optional<Auth> data = authRepo.findById((Long) session.getAttribute("user_id"));
		if(data.get().getGoogle_token()==null) {
			Auth auth = data.get();
			auth.setGoogle_token(cred.getRefreshToken());
			authRepo.save(auth);
		}
	}
	
	public Credential refreshAccessToken(NetHttpTransport httpTransport, HttpSession session) throws IOException {
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(GoogleDriveService.class.getResourceAsStream("/client_secret.json")));
		Optional<Auth> data = authRepo.findById((Long) session.getAttribute("user_id"));
		String refreshToken = data.get().getGoogle_token();
		TokenResponse response = new GoogleRefreshTokenRequest(httpTransport, JSON_FACTORY, refreshToken, appId, appSecret).execute();
		Credential cred = credential(httpTransport);
		return  cred.setAccessToken(response.getAccessToken());
	}
	
	public void googleUpload(NetHttpTransport httpTransport, MultipartFile file, HttpSession session) throws IOException {
		String name = file.getOriginalFilename();
		Credential cred = refreshAccessToken(httpTransport, session);
		Drive drive = new Drive.Builder(httpTransport, JSON_FACTORY, cred).setApplicationName("dra6").build();
		
		File fileMeta = new File();
		fileMeta.setName(name);
		
		
		InputStream inputStream = file.getInputStream();
		InputStreamContent content = new InputStreamContent(null, new BufferedInputStream(inputStream));
		
		File file_upl = drive.files().create(fileMeta, content).setFields("id").execute();
		
		LOG.info("file id: " + file_upl.getId());
		Long id = (Long) session.getAttribute("file_id");
		Google_file google_file = new Google_file(id,file_upl.getId());
		googleRepo.save(google_file);
		
	}
	
}
