package com.example.hospital_manager.service;

import com.example.hospital_manager.model.Patient;
import com.example.hospital_manager.model.PriorityLevel;
import com.example.hospital_manager.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Override
    public Patient addPatient(Patient patient) {
        if (patient.getStatus() == null) {
            patient.setStatus("WAITING"); // default status
        }
        return patientRepository.save(patient);
    }

    @Override
    public List<Patient> getWaitingPatients() {
        List<Patient> waiting = patientRepository.findByStatus("WAITING");

        // Sort: HIGH > MEDIUM > LOW, then FIFO by createdAt
        waiting.sort(
            Comparator.comparing(Patient::getPriorityLevel) // uses enum order
                      .thenComparing(Patient::getCreatedAt)
        );

        return waiting;
    }

    @Override
    public Patient attendNextPatient() {
        List<Patient> waiting = getWaitingPatients();
        if (waiting.isEmpty()) return null;

        Patient next = waiting.get(0);
        next.setStatus("ATTENDED");
        return patientRepository.save(next);
    }
}