package com.spring.springProject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ConnectServer {
    private HttpClient client;
    private HttpRequest request;
    private HttpResponse<String> response;
    public ConnectServer() throws IOException, InterruptedException, URISyntaxException {
        request = HttpRequest.newBuilder(new URI("https://fakestoreapi.com/products")).build();
        client = HttpClient.newBuilder().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
    }
    public String getData(){
        return response.body();
    }

}
