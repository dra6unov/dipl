package main;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.ProcessBuilder.Redirect;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.IOUtils;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

@Controller
public class GoogleAuth {
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);
	private static final String authForm = "https://accounts.google.com/o/oauth2/auth?access_type=offline&client_id=751667716532-g4vdk25ai7kibi299pkv1tc5j5l2557t.apps.googleusercontent.com&redirect_uri=http://localhost:8089/Callback&response_type=code&scope=https://www.googleapis.com/auth/drive";

	@RequestMapping("/auth")
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
		//return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver.Builder().setPort(8089).build())
		//		.authorize("user");
		//redirectedUrl("https://accounts.google.com/o/oauth2/auth?access_type=offline&client_id=751667716532-g4vdk25ai7kibi299pkv1tc5j5l2557t.apps.googleusercontent.com&redirect_uri=http://localhost:8089/Callback&response_type=code&scope=https://www.googleapis.com/auth/drive");
		return credential;
		
	}

	@RequestMapping("/drive")
	public static String main(Model model, HttpServletResponse response) throws Exception {
		final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
		Drive drive = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, auth(HTTP_TRANSPORT)).setApplicationName("dra6")
				.build();
		
		
		
		FileList result = drive.files().list().setPageSize(10).setFields("nextPageToken, files(id,name)").execute();
		List<File> files = result.getFiles();
		List<String> filesName = new ArrayList<String>();
		if (files == null || files.isEmpty()) {
			System.out.println("no files found");
		} else {
			System.out.println("Files: ");
			for (File file : files) {
				System.out.printf("%s (%s)\n", file.getName(), file.getId());
				model.addAttribute("fileName", file.getName());
				filesName.add(file.getName());
			}
		}

		model.addAttribute("lists", files);
		model.addAttribute("filesName", filesName);
		System.out.println("Переменная files" + files);
		System.out.println("filesName: " + filesName);
		System.out.println("HTTP_TRANSPORT: " + HTTP_TRANSPORT);
		System.out.println("Drive: " + drive);
		return "home";
	}
	
	@RequestMapping("/")
	public static String home(Model model) {
		return "home";
	}
	
	@RequestMapping("/link")
	public static String link(Model model) {
		return "redirect:https://accounts.google.com/o/oauth2/auth?access_type=offline&client_id=751667716532-g4vdk25ai7kibi299pkv1tc5j5l2557t.apps.googleusercontent.com&redirect_uri=http://localhost:8089/Callback&response_type=code&scope=https://www.googleapis.com/auth/drive";
	}

	@RequestMapping("/up")
	public static String upload(@RequestParam("file") MultipartFile multipartFile, Model model) throws Exception {
		// Path path = Paths.get(filepath.getOriginalFilename());
		final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
		Drive drive = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, auth(HTTP_TRANSPORT)).setApplicationName("dra6")
				.build();
		File fileMeta = new File();
		String fileName = multipartFile.getOriginalFilename().toString(); // Получаем название файла
		fileMeta.setName(fileName); // устанавливаем название
		java.io.File tmpFile = java.io.File.createTempFile("UploadFile", ".tmp");

		InputStream in = multipartFile.getInputStream();
		FileOutputStream fos = new FileOutputStream(tmpFile);
		IOUtils.copy(in, fos);
		in.close();
		fos.close();
		tmpFile.deleteOnExit();

		FileContent mediaContent = new FileContent(null, tmpFile);
		File file = drive.files().create(fileMeta, mediaContent).setFields("id").execute();

		// проверка разных значений
		System.out.println("tmpFile: " + tmpFile);
		System.out.println("file: " + file);
		System.out.println("fileMeta: " + fileMeta);
		System.out.println("mediaContent: " + mediaContent);
		System.out.println("multipartFile: " + multipartFile);
		System.out.println("fileName: " + fileName);

		model.addAttribute("UploadedFile", file);
		return "redirect:/";

	}

	@RequestMapping("/down")
	public String download(Model model) throws Exception {
		final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
		Drive drive = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, auth(HTTP_TRANSPORT)).setApplicationName("dra6")
				.build();
		try {
			String fileId = "1Zr4aM29VtYURZ61z-3LP2e3mCdM384g0";
			OutputStream os = new ByteArrayOutputStream();
			drive.files().get(fileId).executeMediaAndDownloadTo(os);

			// System.out.println("os" + os);
			System.out.println("file downloaded");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "redirect:/";

	}
}
