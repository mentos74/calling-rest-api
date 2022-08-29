package com.getopenapi.model;

import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;


public class RestApiTutorial {

    public static void main(String[] args) throws Exception {

        Transcript transcript = new Transcript();
        transcript.setAudio_url("https://raw.githubusercontent.com/johnmarty3/JavaAPITutorial/main/Thirsty.mp4");
        Gson gson = new Gson();
        String transcribedAudio = gson.toJson(transcript);


        HttpRequest postRequest = HttpRequest.newBuilder().uri(new URI("https://api.assemblyai.com/v2/transcript")).
                header("Authorization", "65632e32424445ccbf5812961eedfe79").
                POST(BodyPublishers.ofString(transcribedAudio)).build();

        HttpClient httpClient = HttpClient.newHttpClient();

        HttpResponse<String> postResponse = httpClient.send(postRequest, BodyHandlers.ofString());

        System.out.println(postResponse.body());

        transcript = gson.fromJson(postResponse.body(), Transcript.class);

        System.out.println(transcript.getId());

        HttpRequest getRequests = HttpRequest.newBuilder().uri(new URI("https://api.assemblyai.com/v2/transcript/" + transcript.getId())).
                header("Authorization", "65632e32424445ccbf5812961eedfe79").
                GET().build();

        while (true) {
            HttpResponse<String> getResponse = httpClient.send(getRequests, BodyHandlers.ofString());
            transcript = gson.fromJson(getResponse.body(), Transcript.class);
            System.out.println(transcript.getStatus());
            if ("completed".equals(transcript.getStatus()) || "error".equals(transcript.getStatus())){
                break;
            }
            Thread.sleep(1000);
        }
        System.out.println("Transcription Completed");
        System.out.println(transcript.getText());

    }
}
