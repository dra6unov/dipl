package dropBox;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dropbox.core.*;
import com.dropbox.core.json.JsonReader;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.users.FullAccount;

@Controller
public class DropBoxAuth {
	 private static final String APP_KEY = "opx1o1t02x2oh5z";
	private static final String APP_SECRET ="y2m0y8hlz4476k9";

	public void connectToDropbox() throws DbxApiException, DbxException, IOException {

	       /* DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);

	        @SuppressWarnings("deprecation")
			DbxRequestConfig config = new DbxRequestConfig("JavaTutorial/1.0",
	                Locale.getDefault().toString());

	        @SuppressWarnings("deprecation")
			DbxWebAuthNoRedirect webAuth = new DbxWebAuthNoRedirect(config, appInfo);


	        // Have the user sign in and authorize your app.
	        String authorizeUrl = webAuth.start();
	        System.out.println("1. Go to: " + authorizeUrl);
	        System.out.println("2. Click \"Allow\" (you might have to log in first)");
	// No, I dont want to copy the authorization code. 
	        System.out.println("3. Copy the authorization code.");
	        String code = "k7baPKsv44AAAAAAAAAAChrG2gXsWyPg2xCyoeOKEFE";
	        try {
	            code = new BufferedReader(new InputStreamReader(System.in)).readLine().trim();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }*/
		String ACCESS_TOKEN = "k7baPKsv44AAAAAAAAAADj1JnZZMPBWw9ISAt0wka2j489yAzBJLoLSabwNiJW-p";
		HttpServletResponse response = null;
		
		DbxRequestConfig config = new DbxRequestConfig("dropbox/java-tutorial");
        DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);

        // Get current account info
        FullAccount account = client.users().getCurrentAccount();
        //System.out.println(account.getName().getDisplayName());
	        
	        ListFolderResult result = client.files().listFolder("");
	        FileMetadata fileMetadata = (FileMetadata) client.files().getMetadata("/пояснительная записка.docx");
	        while (true) {
	            for (Metadata metadata : result.getEntries()) {
	            	String acc = metadata.getPathLower();
	                System.out.println(metadata.getPathLower() + " ");
	                System.out.println(client.files().getMetadata(acc));
	            }

	            if (!result.getHasMore()) {
	                break;
	            }

	            result = client.files().listFolderContinue(result.getCursor());
	        }
	        
	        //рабочий вариант
	        DbxDownloader<FileMetadata> downloader = client.files().download("/пояснительная записка.docx");
	        try {
	            FileOutputStream out = new FileOutputStream("пояснительная записка.docx");
	            
	            downloader.download(out);
	            //downloader.download("id:v75ZOM7-ZjAAAAAAAAAADg");
	            out.close();
	        } catch (DbxException ex) {
	            System.out.println(ex.getMessage());
	        }
	        
	       /* File tmpFile = File.createTempFile("Droptemp", ".tmp");
	        DbxDownloader<FileMetadata> downloader = client.files().download("/пояснительная записка.docx");
	        OutputStream outputStream = new FileOutputStream(tmpFile);
	        downloader.download(outputStream);
	        String filepath = tmpFile.getAbsolutePath();
	        outputStream.close();
	        System.out.println("abs path" + filepath);
	        
	        response.setContentType("application/octet-stream");
			response.setContentLength((int) tmpFile.length());
			response.setHeader("Content-Disposition",  String.format("attachment; filename= пояснительная записка.docx"));
			OutputStream out = response.getOutputStream();
			FileInputStream inputStream = new FileInputStream(tmpFile);
			byte[] buffer = new byte[10234];
			int length;
			while((length = inputStream.read(buffer))>0) {
				out.write(buffer,0,length);
			}
			out.flush();
			inputStream.close();
			out.close();*/
	    }
	        
	        
	        @RequestMapping(value = "/db")
	        public String loginIntoDropbox() throws DbxApiException, DbxException, IOException{
	            DropBoxAuth connectDropbox = new DropBoxAuth();
	            connectDropbox.connectToDropbox();
	            
	            return "home";
	        }
}

