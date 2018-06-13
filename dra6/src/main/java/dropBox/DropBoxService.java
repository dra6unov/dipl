package dropBox;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.provider.HibernateUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxSessionStore;
import com.dropbox.core.DbxStandardSessionStore;
import com.dropbox.core.DbxWebAuth;
import com.dropbox.core.DbxWebAuth.BadRequestException;
import com.dropbox.core.DbxWebAuth.BadStateException;
import com.dropbox.core.DbxWebAuth.CsrfException;
import com.dropbox.core.DbxWebAuth.NotApprovedException;
import com.dropbox.core.DbxWebAuth.ProviderException;
import com.dropbox.core.v1.DbxEntry;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.UploadErrorException;
import com.dropbox.core.v2.users.FullAccount;

import ch.qos.logback.core.net.SyslogOutputStream;
import entity.Auth;
import entity.Dropbox_file;
import entity.User_file;
import repos.AuthRepo;
import repos.Dropbox_fileRepo;
import repos.User_fileRepo;

@Service
public class DropBoxService {
	private static final Logger LOG = LoggerFactory.getLogger(DropBoxService.class);

	@Value("${dropbox.app.key}")
	private String appKey;

	@Value("${dropbox.app.secret}")
	private String appSecret;

	@Value("${dropbox.app.sessionstore.key}")
	private String sessionStoreKey;

	@Value("${app.name}")
	private String appName;

	@Value("${app.base.url}")
	private String appBaseURL;
	
	
	@Autowired
	private AuthRepo authRepo;
	
	@Autowired
	Dropbox_fileRepo dropboxRepo;
	
	@Autowired
	User_fileRepo user_fileRepo;

	private DbxWebAuth createDbxWebAuth(HttpSession session) {
		DbxAppInfo appInfo = new DbxAppInfo(appKey, appSecret);
		DbxRequestConfig config = new DbxRequestConfig(appName, Locale.getDefault().toString());
		DbxWebAuth web;

		DbxSessionStore dbxSessionStore = new DbxStandardSessionStore(session, sessionStoreKey);
		System.out.println("sessionStoreKey: " + sessionStoreKey);
		LOG.info("config: " + config);
		LOG.info("appBaseURL: " + appBaseURL);
		LOG.info("dbxsessionstore: " + dbxSessionStore.get());
		web = new DbxWebAuth(config, appInfo, appBaseURL, dbxSessionStore);
		LOG.info("web: " + web);
		return new DbxWebAuth(config, appInfo, appBaseURL + "/dropbox/check", dbxSessionStore);
		// return web;

	}

	public String generateAuthURL(HttpSession session) {
		DbxWebAuth webAuth = createDbxWebAuth(session);

		String authUrl = webAuth.start(null);
		LOG.info("url123: " + authUrl);
		return authUrl;
		// String redirectUrl = dr
	}

	public String saveAccessDetails(Map<String, String[]> parameterMap, HttpSession session) throws Exception {
		if (parameterMap.containsKey("error")) {
			throw new Exception(Arrays.toString(parameterMap.get("error_description")));
		}

		DbxWebAuth webAuth = createDbxWebAuth(session);
		DbxAuthFinish authFinish = webAuth.finish(parameterMap);

		LOG.info("Map: " + parameterMap);

		DbxSessionStore dbxSessionStore = new DbxStandardSessionStore(session, sessionStoreKey);
		dbxSessionStore.set(authFinish.getAccessToken());
		LOG.info(authFinish.getAccessToken());
		
		Long userId = (Long) session.getAttribute("user_id");
		
		LOG.info("userId from Service: " + userId);
		
		Optional<Auth> data = authRepo.findById(userId);
		if(data.get().getDropbox_token()==null) {
			Auth auth = data.get();
			auth.setDropbox_token(authFinish.getAccessToken());
			authRepo.save(auth);
		}

		return appBaseURL;
	}


	public String RedirectToConfirmData() {
		return appBaseURL + "/dropbox/allow";
	}

	public void getDropboxFile(HttpSession session) throws IllegalAccessException, DbxApiException, DbxException {
		if (session.getAttribute(sessionStoreKey) == null) {
			throw new IllegalAccessException("Сначала разрешите доступ к своим данным: " + RedirectToConfirmData());
		}

		DbxSessionStore dbxSessionStore = new DbxStandardSessionStore(session, sessionStoreKey);

		DbxRequestConfig config = new DbxRequestConfig(appName, Locale.getDefault().toString());
		
		Optional<Auth> auth = authRepo.findById((Long) session.getAttribute("user_id"));
		String accessToken = auth.get().getDropbox_token();
		LOG.info("accessToken: " + accessToken);
		DbxClientV2 client = new DbxClientV2(config, accessToken);

		System.out.println("dbxsessionstore: " + dbxSessionStore.get());

		FullAccount account = client.users().getCurrentAccount();
		System.out.println(account.getName().getDisplayName());
	}
	
	
	public void uploadDropbox ( MultipartFile file, HttpSession session) throws IOException, UploadErrorException, DbxException {
		String name = file.getOriginalFilename();
		
		DbxSessionStore dbxSessionStore = new DbxStandardSessionStore(session, sessionStoreKey);
		DbxRequestConfig config = new DbxRequestConfig(appName, Locale.getDefault().toString());
		
		Optional<Auth> data = authRepo.findById((Long) session.getAttribute("user_id"));
		
		String accessToken = data.get().getDropbox_token();
		
		LOG.info("accessToken from uploadDropbox: " + accessToken);
		
		DbxClientV2 client = new DbxClientV2(config, accessToken);
		
		InputStream inputStream = file.getInputStream();
		FileMetadata metadata = client.files().uploadBuilder("/"+name).uploadAndFinish(inputStream);
		inputStream.close();
		Long id = (Long) session.getAttribute("file_id");
		Dropbox_file dropbox_file = new Dropbox_file(id,"/"+name);
		dropboxRepo.save(dropbox_file);
		}

}
