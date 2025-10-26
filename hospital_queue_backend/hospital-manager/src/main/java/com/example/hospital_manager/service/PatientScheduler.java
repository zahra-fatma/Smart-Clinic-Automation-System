package com.example.hospital_manager.service;

import com.example.hospital_manager.model.Patient;
import com.example.hospital_manager.repository.PatientRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PatientScheduler {

    private final PatientRepository patientRepository;
    private final GeminiClientService geminiClientService;

    public PatientScheduler(PatientRepository patientRepository, GeminiClientService geminiClientService) {
        this.patientRepository = patientRepository;
        this.geminiClientService = geminiClientService;
    }

    @Scheduled(fixedRate = 20000) // every 15 seconds
    public void generatePatient() {
        Patient patient = geminiClientService.generatePatient();
        patientRepository.save(patient);

        System.out.println("🤖 Gemini added patient: " + patient.getName() +
                " (" + patient.getPriorityLevel() + ")");
    }
}