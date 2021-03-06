package bendersalavize.ex2.Crawl;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class ImageURLChecker implements URLChecker {
    @Override
    public boolean accept(String url) throws IOException {
        URL u = new URL(url);
        URLConnection connection = u.openConnection();
        connection.connect();
        String contentType = connection.getContentType();
        return contentType.contains("image");
    }

    // getKeyword() method used by URLCheckerFactory
    static String getKeyword() {
        return "image";
    }

}
