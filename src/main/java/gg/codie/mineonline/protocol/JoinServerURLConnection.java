package gg.codie.mineonline.protocol;

import gg.codie.minecraft.api.SessionServer;
import net.glasslauncher.wrapper.LegacyWrapper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class JoinServerURLConnection extends HttpURLConnection {
    public JoinServerURLConnection(URL url) {
        super(url);
    }

    @Override
    public void disconnect() {

    }

    @Override
    public boolean usingProxy() {
        return false;
    }

    private String response = "bad login";

    @Override
    public void connect() throws IOException {

    }

    @Override
    public InputStream getInputStream() throws IOException {
        String serverId = this.url.toString().substring(this.url.toString().indexOf("&serverId=") + 10);

        boolean validJoin = SessionServer.joinGame(
                LegacyWrapper.TOKEN,
                LegacyWrapper.UUID,
                serverId
        );

        if (validJoin) {
            response = "ok";
        }

        return new ByteArrayInputStream(response.getBytes());
    }

    @Override
    public int getResponseCode() {
        return 200;
    }

    @Override
    public String getResponseMessage() {
        return "ok";
    }
}
