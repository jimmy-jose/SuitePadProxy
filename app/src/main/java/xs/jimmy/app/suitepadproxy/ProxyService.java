package xs.jimmy.app.suitepadproxy;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.webkit.WebResourceResponse;

import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;

public class ProxyService extends Service {

    private WebServer server;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        server = new WebServer();
        try {
            server.start();
            Log.d("Httpd", "The server started.");
        } catch(IOException ioe) {
            Log.d("Httpd", "The server could not start.");
        }
    }


    @Override
    public void onDestroy() {
        if (server != null) {
            server.stop();
            Log.d("Httpd", "The server stopped.");
        }
        super.onDestroy();
    }
    private WebResourceResponse getRss() {
        return getUtf8EncodedCssWebResourceResponse(getResources().openRawResource(R.raw.sample));
    }

    private WebResourceResponse getUtf8EncodedCssWebResourceResponse(InputStream data) {
        return new WebResourceResponse("text/plain", "UTF-8", data);
    }

    private class WebServer extends NanoHTTPD {

        WebServer()
        {
            super(8080);
        }

        @Override
        public Response serve(IHTTPSession session) {
            return super.serve(session);
        }

        @Override
        public NanoHTTPD.Response serve(String uri, Method method,
                                        Map<String, String> header,
                                        Map<String, String> parameters,
                                        Map<String, String> files) {
            return new NanoHTTPD.Response(inputStreamToString(getResources().openRawResource(R.raw.sample)));
        }
    }

    public String inputStreamToString(InputStream inputStream)  {
        try(ByteArrayOutputStream result = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            return result.toString("UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
