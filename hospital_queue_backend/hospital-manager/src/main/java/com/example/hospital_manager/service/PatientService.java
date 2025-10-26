package com.example.hospital_manager.service;

import com.example.hospital_manager.model.Patient;
import java.util.List;

public interface PatientService {
    Patient addPatient(Patient patient);
    List<Patient> getWaitingPatients();
    Patient attendNextPatient();
}