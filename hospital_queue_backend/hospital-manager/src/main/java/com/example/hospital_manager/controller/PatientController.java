package com.example.hospital_manager.controller;

import com.example.hospital_manager.model.Patient;
import com.example.hospital_manager.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
@CrossOrigin(origins = "*") // allow all origins (frontend, curl, etc.)
public class PatientController {

    @Autowired
    private PatientService patientService;

    // Add a new patient
    @PostMapping
    public ResponseEntity<Patient> addPatient(@RequestBody Patient patient) {
        Patient saved = patientService.addPatient(patient);
        return ResponseEntity.ok(saved);
    }

    // Get all waiting patients
    @GetMapping
    public ResponseEntity<List<Patient>> getWaitingPatients() {
        List<Patient> waiting = patientService.getWaitingPatients();
        return ResponseEntity.ok(waiting);
    }

    // Attend next patient
    @PostMapping("/attend")
    public ResponseEntity<?> attendNextPatient() {
        Patient next = patientService.attendNextPatient();
        if (next == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("⚠️ No patients waiting!");
        }
        return ResponseEntity.ok(next);
    }
}
