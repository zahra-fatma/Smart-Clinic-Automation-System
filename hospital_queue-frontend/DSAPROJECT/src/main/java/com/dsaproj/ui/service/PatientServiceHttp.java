package com.dsaproj.ui.service;

import com.dsaproj.ui.model.PatientFx;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class PatientServiceHttp {
    private static final String BASE_URL = "http://localhost:8088/patients";
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    // 🔹 Get all patients
    public List<PatientFx> getPatients() {
        List<PatientFx> list = new ArrayList<>();
        try {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL))
                    .GET()
                    .build();

            HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
            JsonNode root = mapper.readTree(resp.body());

            for (JsonNode p : root) {
                PatientFx fx = new PatientFx(
                        p.path("id").asLong(),
                        p.path("name").asText(),
                        p.path("age").asInt(),
                        p.path("priorityLevel").asText(),
                        p.path("status").asText(),
                        p.path("createdAt").asText()
                );
                list.add(fx);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 🔹 Attend the next patient
    public PatientFx attendNextPatient() {
        try {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/attend"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());

            if (resp.statusCode() == 404 || resp.body().contains("No patients")) {
                return null;
            }

            JsonNode p = mapper.readTree(resp.body());
            return new PatientFx(
                    p.path("id").asLong(),
                    p.path("name").asText(),
                    p.path("age").asInt(),
                    p.path("priorityLevel").asText(),
                    p.path("status").asText(),
                    p.path("createdAt").asText()
            );

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 🔹 Add a new patient
    public PatientFx addPatient(String name, int age, String priorityLevel) {
        try {
            String json = String.format(
                    "{\"name\":\"%s\",\"age\":%d,\"status\":\"WAITING\",\"priorityLevel\":\"%s\"}",
                    name, age, priorityLevel
            );

            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
            JsonNode p = mapper.readTree(resp.body());

            return new PatientFx(
                    p.path("id").asLong(),
                    p.path("name").asText(),
                    p.path("age").asInt(),
                    p.path("priorityLevel").asText(),
                    p.path("status").asText(),
                    p.path("createdAt").asText()
            );

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}