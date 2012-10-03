
package com.netomarin.tablemountain.webservice;

import static com.netomarin.tablemountain.webservice.WSConstants.WS_DEFAULT_SCHEME;
import static com.netomarin.tablemountain.webservice.WSConstants.WS_ENCODING;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class HTTPOperations {

    private HTTPOperations() {
    }

    public static HttpResponse doGet(String host, int port, String path,
            ArrayList<NameValuePair> params) {

        HttpResponse response = null;

        try {
            URI uri = URIUtils.createURI(WS_DEFAULT_SCHEME, host, port, path,
                    params == null ? null : URLEncodedUtils.format(params, WS_ENCODING), null);
            HttpClient client = new DefaultHttpClient();
            HttpGet get = new HttpGet(uri);
            response = client.execute(get);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }
}
