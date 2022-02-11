package net.glasslauncher.wrapper;

import gg.codie.mineonline.protocol.BasicResponseURLConnection;
import gg.codie.mineonline.protocol.CapeURLConnection;
import gg.codie.mineonline.protocol.JoinServerURLConnection;
import gg.codie.mineonline.protocol.SkinURLConnection;
import sun.net.www.protocol.http.HttpURLConnection;

import java.io.*;
import java.net.*;

public class WrapperProtocolHandler extends URLStreamHandler {
    @Override
    protected URLConnection openConnection(URL url) throws IOException {
        // Online-Mode fix
        if (url.toString().contains("/game/joinserver.jsp"))
            return new JoinServerURLConnection(url);
        // Old anti-piracy endpoints return positive responses.
        else if (url.toString().contains("/login/session.jsp"))
            return new BasicResponseURLConnection(url, 200, "ok");
        else if (url.toString().contains("/game/?n="))
            return new BasicResponseURLConnection(url, 200, "42069");
        else if (url.toString().contains("/haspaid.jsp"))
            return new BasicResponseURLConnection(url, 200, "true");
        // Skins are pulled from the new endpoint and converted to the legacy format as required.
        else if (url.toString().contains("/MinecraftSkins/") || url.toString().contains("/skin/"))
            return new SkinURLConnection(url);
        // Capes are pulled from the new endpoint.
        else if ((url.toString().contains("/MinecraftCloaks/") && url.toString().contains(".png")) || url.toString().contains("/cloak/get.jsp?user="))
            return new CapeURLConnection(url);
        else
            return new HttpURLConnection(url, null);
    }
}
