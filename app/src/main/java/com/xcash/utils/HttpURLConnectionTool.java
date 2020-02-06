/**
 * Copyright (c) 2019 by snakeway
 * <p>
 * All rights reserved.
 */
package com.xcash.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpURLConnectionTool {

    public static String sendGet(String requestUrl) {
        if (requestUrl == null) {
            return null;
        }
        if (!requestUrl.startsWith("http")) {
            requestUrl = "http://" + requestUrl;
        }
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(false);
            connection.setDoInput(true);
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                StringBuffer buffer = new StringBuffer();
                String readLine;
                BufferedReader responseReader;
                responseReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((readLine = responseReader.readLine()) != null) {
                    buffer.append(readLine).append("\n");
                }
                responseReader.close();
                return buffer.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String sendPost(String requestUrl, String requestBody) {
        if (requestUrl == null || requestBody == null) {
            return null;
        }
        if (!requestUrl.startsWith("http")) {
            requestUrl = "http://" + requestUrl;
        }
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod("POST");
            byte[] requestStringBytes = requestBody.getBytes();
            connection.setRequestProperty("Content-length", "" + requestStringBytes.length);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Charset", "UTF-8");
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(requestStringBytes);
            outputStream.close();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                StringBuffer buffer = new StringBuffer();
                String readLine;
                BufferedReader responseReader;
                responseReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((readLine = responseReader.readLine()) != null) {
                    buffer.append(readLine).append("\n");
                }
                responseReader.close();
                return buffer.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
