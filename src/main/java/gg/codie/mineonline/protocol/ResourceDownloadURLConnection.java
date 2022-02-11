package gg.codie.mineonline.protocol;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class ResourceDownloadURLConnection extends HttpURLConnection {
    public ResourceDownloadURLConnection(URL url) {
        super(url);
        try {
            this.url = new URL(url.getProtocol(), "resourceproxy.pymcl.net", url.getPort(), url.getFile());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void disconnect() {

    }

    @Override
    public boolean usingProxy() {
        return false;
    }

    @Override
    public void connect() throws IOException {

    }
}
