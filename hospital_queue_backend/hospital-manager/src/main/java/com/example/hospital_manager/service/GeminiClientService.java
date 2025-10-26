package com.example.hospital_manager.service;

import com.example.hospital_manager.model.Patient;
import com.example.hospital_manager.model.PriorityLevel;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class GeminiClientService {

    private final Random random = new Random();

    // Example stub — later you can replace this with real Gemini API call
    public Patient generatePatient() {
        String[] names = {"Sophia", "Ethan", "Olivia", "Liam", "Ava", "Noah"};

        Patient patient = new Patient();
        patient.setName(names[random.nextInt(names.length)]);
        patient.setAge(20 + random.nextInt(50));
        patient.setPriorityLevel(PriorityLevel.values()[random.nextInt(PriorityLevel.values().length)]);
        patient.setStatus("WAITING");
        patient.setCreatedAt(LocalDateTime.now());

        return patient;
    }
}