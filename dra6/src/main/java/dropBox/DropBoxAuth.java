package dropBox;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import com.dropbox.core.*;

public class DropBoxAuth {
	private static String APP_KEY;
    private static String APP_SECRET;

    public static void main(String[] args) throws DropboxException, MalformedURLException, IOException, URISyntaxException {
        setupAppKeyPair();
        AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
        WebAuthSession session = new WebAuthSession(appKeys, AccessType.DROPBOX);
        WebAuthInfo authInfo = session.getAuthInfo();

        RequestTokenPair pair = authInfo.requestTokenPair;
        String url = authInfo.url;

        Desktop.getDesktop().browse(new URL(url).toURI());
        JOptionPane.showMessageDialog(null, "Press ok to continue once you have authenticated.");
        try {
            session.retrieveWebAccessToken(pair);
        } catch (Exception e) {
            System.out.println("authentication fail with exception:" + e);
        }

        AccessTokenPair tokens = session.getAccessTokenPair();
        System.out.println("Use this token pair in future so you don't have to re-authenticate each time:");
        System.out.println("accessKey: " + tokens.key);
        System.out.println("accessSecret: " + tokens.secret);
        System.exit(0);
    }

}
