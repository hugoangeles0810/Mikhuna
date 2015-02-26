package com.jasoftsolutions.mikhuna.remote;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.jasoftsolutions.mikhuna.BuildConfig;
import com.jasoftsolutions.mikhuna.util.ExceptionUtil;
import com.jasoftsolutions.mikhuna.util.IOUtil;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class RemoteHandler {

//    public static final String SERVICE_BASE_URL = "http://190.117.240.6/mikhuna/Rest";
//    public static final String SERVICE_BASE_URL = "http://mikhuna.com/Rest";
//	public static final String SERVICE_BASE_URL = "http://192.168.40.105/mikhuna/Rest";

    private static final String TAG = RemoteHandler.class.getSimpleName();

    private static final String PARAM_LAST_UPDATE = "lupd";

	static String response = null;

    private static void addVersionParameter(List<NameValuePair> params) {
        if (params != null) {
            params.add(new BasicNameValuePair("v", String.valueOf(BuildConfig.VERSION_CODE)));
        }
    }
	
    /**
     * 
     * @param relativeUrl Url del recurso a conectarse
     * @param method
     * @param params
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public InputStream getInputStream(String relativeUrl, HttpMethod method, List<NameValuePair> params) throws ClientProtocolException, IOException {
        InputStream responseStream = null;
        
        String absoluteUrl = Const.BACKEND_REST_BASE_URL + relativeUrl;

        // http client
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpEntity httpEntity = null;
        HttpResponse httpResponse = null;

        if (params == null) {
            params = new ArrayList<NameValuePair>();
        }
        addVersionParameter(params);

        // Checking http request method type
        if (method.equals(HttpMethod.POST)) {
            HttpPost httpPost = new HttpPost(absoluteUrl);
            // adding post params
            if (params != null) {
                httpPost.setEntity(new UrlEncodedFormEntity(params));
            }

            Log.e(TAG, "requesting uri ("+method.toString()+"): "+absoluteUrl);
            Log.e(TAG, "with post data: "+ params.toString());

            httpResponse = httpClient.execute(httpPost);

        } else if (method.equals(HttpMethod.GET)) {
            // appending params to uri
            if (params != null) {
                String paramString = URLEncodedUtils
                        .format(params, "utf-8");
                absoluteUrl += "?" + paramString;
            }

            HttpGet httpGet = new HttpGet(absoluteUrl);

            Log.i(TAG, "requesting uri ("+method.toString()+"): "+absoluteUrl);

            httpResponse = httpClient.execute(httpGet);
        }
        
        httpEntity = httpResponse.getEntity();
        responseStream = httpEntity.getContent();
        
        return responseStream;
    }

    public <T> T getResourceFromUrl(String url, Class<T> c) {
        return getResourceFromUrl(url, null, c);
    }

    public <T> T getResourceFromUrl(String url, Long lastUpdate, Class<T> c) {
        return getResourceFromUrl(url, null, lastUpdate, c);
    }

    public <T> T getResourceFromUrl(String url, List<NameValuePair> params, Long lastUpdate, Class<T> c) {
        return requestResourceFromUrl(url, HttpMethod.GET, params, lastUpdate, c);
    }

    public <T> T postResourceFromUrl(String url, Class<T> c) {
        return postResourceFromUrl(url, null, c);
    }

    public <T> T postResourceFromUrl(String url, List<NameValuePair> params, Class<T> c) {
        return requestResourceFromUrl(url, HttpMethod.POST, params, null, c);
    }

    public <T> T requestResourceFromUrl(String url, HttpMethod httpMethod, List<NameValuePair> params, Long lastUpdate, Class<T> c) {
        T result=null;

        String json = null;

        try {
            if (params == null) {
                params = new ArrayList<NameValuePair>();
            }
            if (lastUpdate != null && lastUpdate != 0) {
                params.add(new BasicNameValuePair(PARAM_LAST_UPDATE, lastUpdate.toString()));
            }

            InputStream is = getInputStream(url, httpMethod, params);
            Gson gson = new Gson();

            json = IOUtil.getAllTextInput(is);
//
            Log.e(TAG, json);

            result = gson.fromJson(json, c);
//            result = gson.fromJson(new InputStreamReader(is), c);
            is.close();
        } catch (JsonSyntaxException e) {
            ExceptionUtil.handleExceptionWithMessage(e, "Recibido", json);
        } catch (ClientProtocolException e) {
            ExceptionUtil.handleException(e);
        } catch (IOException e) {
            ExceptionUtil.ignoreException(e);
        }

        return result;
    }

//    public void postJsonData()
	
    
    public static RemoteHandler getInstance() {
    	return new RemoteHandler();
    }
    
}
