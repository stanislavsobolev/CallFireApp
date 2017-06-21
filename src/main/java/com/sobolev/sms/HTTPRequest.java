package com.sobolev.sms;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import com.sobolev.PropertyManager;
import org.apache.commons.codec.binary.Base64;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

/**
 * HTTPRequest has main() method which runs two requests:
 * sendCallFireTextRequest() to start message sending
 * sendCallFireSubscriptionRequest() to create and subscribe on broadcast actions notification
 */
public class HTTPRequest {

    private HttpClient client;
    private String broadcastId = "";

    public static void main(String[] args) throws Exception {
        HTTPRequest http = new HTTPRequest();
        http.sendCallFireTextRequest();
        http.sendCallFireSubscriptionRequest();
    }

    //Send request to CallFire to start message sending process
    public void sendCallFireTextRequest() throws Exception {
        String sendTextUrl = "https://www.callfire.com/api/1.1/rest/text.json";

        this.client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(sendTextUrl);

        post.setHeader(HttpHeaders.AUTHORIZATION, getAuthHeader());
        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("Type", "TEXT"));
        urlParameters.add(new BasicNameValuePair("To", "14243876936"));
        urlParameters.add(new BasicNameValuePair("Message", "Test message for broadcast"));
        urlParameters.add(new BasicNameValuePair("BroadcastName", "Send"));
        urlParameters.add(new BasicNameValuePair("From", "67076"));

        post.setEntity(new UrlEncodedFormEntity(urlParameters));

        this.broadcastId = this.executeAndPrintResponse(post, sendTextUrl);
    }

    //Send request to create subscription and to register local server on it
    public void sendCallFireSubscriptionRequest() throws Exception {
        String subscriptionUrl = "https://www.callfire.com/api/1.1/rest/subscription.json";

        HttpPost post = new HttpPost(subscriptionUrl);
        post.addHeader("Content-Type", "application/x-www-form-urlencoded");
        post.setHeader(HttpHeaders.AUTHORIZATION, getAuthHeader());
        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("NotificationFormat", "JSON"));
        urlParameters.add(new BasicNameValuePair("TriggerEvent", "OUTBOUND_TEXT_FINISHED"));
        urlParameters.add(new BasicNameValuePair("Endpoint", PropertyManager.getProperty("endpoint")));
        urlParameters.add(new BasicNameValuePair("BroadcastId", broadcastId));

        System.out.println("BROADCAST ID:" + broadcastId);
        post.setEntity(new UrlEncodedFormEntity(urlParameters));

        this.executeAndPrintResponse(post, subscriptionUrl);
    }


    private static String getAuthHeader() {
        String auth = PropertyManager.getProperty("login") + ":" + PropertyManager.getProperty("pass");
        byte[] encodedAuth = Base64.encodeBase64(
                auth.getBytes(Charset.forName("ISO-8859-1")));
        return  "Basic " + new String(encodedAuth);
    }

    private String executeAndPrintResponse(HttpPost post, String url) throws Exception{
        HttpResponse response = client.execute(post);
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + post.getEntity());
        System.out.println("Response Code : " +
                response.getStatusLine().getStatusCode());

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuilder result = new StringBuilder();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

        String idSubstring = result.substring(result.indexOf("Id")+4, result.indexOf(","));
        System.out.println(result);
        return idSubstring;
    }
}
