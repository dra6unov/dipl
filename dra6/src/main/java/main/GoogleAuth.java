package main;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.GenericUrl;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.ProcessBuilder.Redirect;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.PathContainer.Separator;
import org.springframework.stereotype.Controller;
import org.springframework.test.web.client.RequestExpectationManager;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.media.MediaHttpDownloader;
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

import ch.qos.logback.core.net.SyslogOutputStream;
import entity.User_file;
import entity.User_group;
import repos.Google_fileRepo;
import repos.User_fileRepo;
import repos.User_groupRepo;
import repos.User_infRepo;

@Controller
public class GoogleAuth {
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);
	private static GoogleAuthorizationCodeFlow flow;

	@Autowired
	private User_fileRepo fileRepo;

	@Autowired
	private Google_fileRepo googleRepo;

	@Autowired
	private User_groupRepo groupRepo;

	@RequestMapping("/auth")
	private static Credential auth(final NetHttpTransport HTTP_TRANSPORT) throws Exception {
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
				new InputStreamReader(GoogleAuth.class.getResourceAsStream("/client_secret.json")));

		flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
				.setDataStoreFactory(new FileDataStoreFactory(new java.io.File("credentials"))).setAccessType("offline")
				.build();
		final GoogleAuthorizationCodeRequestUrl url = flow.newAuthorizationUrl();
		Credential credential = new AuthorizationCodeInstalledApp(flow,
				new LocalServerReceiver.Builder().setPort(8089).build()).authorize("user");
		System.out.println("credential: " + credential);

		// return credential;
		System.out.println("flow: " + flow);
		System.out.println("url: " + url.setRedirectUri("http://localhost:8081").build()); // строка для авторизации
																							// диска и возврат на
																							// главную
		// return new AuthorizationCodeInstalledApp(flow, new
		// LocalServerReceiver.Builder().setPort(8089).build())
		// .authorize("user");
		// redirectedUrl("https://accounts.google.com/o/oauth2/auth?access_type=offline&client_id=751667716532-g4vdk25ai7kibi299pkv1tc5j5l2557t.apps.googleusercontent.com&redirect_uri=http://localhost:8089/Callback&response_type=code&scope=https://www.googleapis.com/auth/drive");
		return credential;

	}

	/*
	 * private static Credential auth(final NetHttpTransport HTTP_TRANSPORT) throws
	 * Exception { GoogleClientSecrets clientSecrets =
	 * GoogleClientSecrets.load(JSON_FACTORY, new
	 * InputStreamReader(GoogleAuth.class.getResourceAsStream("/client_secret.json")
	 * )); flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT,
	 * JSON_FACTORY, clientSecrets, SCOPES).setDataStoreFactory(new
	 * FileDataStoreFactory(new java.io.File("credentials")))
	 * .setAccessType("offline").build(); Credential credential = new
	 * AuthorizationCodeInstalledApp(flow, new
	 * LocalServerReceiver.Builder().setPort(8089).build()).authorize("user"); final
	 * GoogleAuthorizationCodeRequestUrl url = flow.newAuthorizationUrl();
	 * 
	 * return credential; }
	 */

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
	public static String home(HttpServletResponse response, HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		//String name = session.getAttribute("user").toString();
		if (session.getAttribute("user")==null) {
			model.addAttribute("grats", "Вы должны авторизироваться");
			return "reg";
		} else {
			String name = session.getAttribute("user").toString();
			model.addAttribute("username", name);
			System.out.println("name: " + session.getAttribute("user"));
			return "home";
		}
	}

	/*
	 * @RequestMapping("/link") public static String link(Model model, final
	 * NetHttpTransport HTTP_TRANSPORT) throws Exception {
	 * 
	 * //redirectedUrl(authForm); auth(HTTP_TRANSPORT); return
	 * "redirect:https://accounts.google.com/o/oauth2/auth?access_type=offline&client_id=751667716532-g4vdk25ai7kibi299pkv1tc5j5l2557t.apps.googleusercontent.com&redirect_uri=http://localhost:8089/Callback&response_type=code&scope=https://www.googleapis.com/auth/drive";
	 * //return "home" //return //return auth(HTTP_TRANSPORT); }
	 */
	
	@Autowired
	private User_infRepo infRepo;
	
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
		
		User_file user_file = new User_file(fileName, )
		// User_file user_file = new User_file()

		// проверка разных значений
		System.out.println("tmpFile: " + tmpFile);
		System.out.println("file: " + file);
		System.out.println("fileMeta: " + fileMeta);
		System.out.println("mediaContent: " + mediaContent);
		System.out.println("multipartFile: " + multipartFile);
		System.out.println("fileName: " + fileName);
		System.out.println("file id: " + file.getId());

		model.addAttribute("UploadedFile", file);
		return "redirect:/";

	}

	@GetMapping
	public String grg(Model model) {
		Iterable<User_group> mes = groupRepo.findAll();
		model.addAttribute("mes", mes);
		return "home";
	}

	// @PostMapping
	@RequestMapping("/gg")
	public String group(@RequestParam String usergroup, Model model) {
		System.out.println("usergroup" + usergroup);
		User_group group = new User_group(usergroup);
		groupRepo.save(group);
		Iterable<User_group> mes = groupRepo.findAll();
		model.addAttribute("mes", usergroup);

		return "home";
	}

	// Загрузка файлов с гугл драйва с отображением их названия в браузере
	@RequestMapping(value = "/1")
	public void SingleUpload(Model model, boolean useDirectDownload, HttpServletResponse response) throws Exception {
		final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
		Drive drive = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, auth(HTTP_TRANSPORT)).setApplicationName("dra6")
				.build();

		String home = System.getProperty("user.home");

		String fileId = "1Y3HQhbOkTVOFfHagGGJZXe_zuhkDLHkJ";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		// java.io.File file = new java.io.File(home+"/Downloads/12367.jpg");

		java.io.File file = new java.io.File("12367910.jpg");
		// try {
		drive.files().get(fileId).executeMediaAndDownloadTo(baos);
		FileOutputStream fop;
		fop = new FileOutputStream(file);
		fop.write(baos.toByteArray());
		fop.flush();
		fop.close();
		file.deleteOnExit();

		// }catch (Exception e) {
		// e.printStackTrace();
		// }

		// java.io.File file = new java.io.File("1228.jpg");
		response.setContentType("application/octet-stream");
		response.setContentLength((int) file.length());
		response.setHeader("Content-Disposition", String.format("attachment; filename= 123697.jpg"));
		OutputStream out = response.getOutputStream();
		FileInputStream inputStream = new FileInputStream(file);
		byte[] buffer = new byte[10234];
		int length;
		while ((length = inputStream.read(buffer)) > 0) {
			out.write(buffer, 0, length);
		}
		out.flush();
		inputStream.close();
		out.close();

	}

}
