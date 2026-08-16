package com.example.demo.service;

import com.example.demo.entity.AcademicYear;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.AcademicYearRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AcademicYearService {

    private final AcademicYearRepository academicYearRepository;

    public List<AcademicYear> findAll() {
        return academicYearRepository.findAll();
    }

    public AcademicYear findById(UUID id) {
        return academicYearRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Année universitaire introuvable : " + id));
    }

    public AcademicYear getCurrent() {
        return academicYearRepository.findByIsCurrentTrue()
                .orElseThrow(() -> new ResourceNotFoundException("Aucune année universitaire courante définie"));
    }

    public AcademicYear create(LocalDate dateDebut, LocalDate dateFin, boolean isCurrent) {
        if (isCurrent) {
            desactiverAnneeCouranteExistante();
        }
        AcademicYear annee = AcademicYear.builder()
                .dateDebut(dateDebut)
                .dateFin(dateFin)
                .isCurrent(isCurrent)
                .build();
        return academicYearRepository.save(annee);
    }

    public AcademicYear setAsCurrent(UUID id) {
        AcademicYear nouvelleAnnee = findById(id);
        desactiverAnneeCouranteExistante();
        nouvelleAnnee.setCurrent(true);
        return academicYearRepository.save(nouvelleAnnee);
    }

    private void desactiverAnneeCouranteExistante() {
        academicYearRepository.findByIsCurrentTrue().ifPresent(ancienneAnnee -> {
            ancienneAnnee.setCurrent(false);
            academicYearRepository.save(ancienneAnnee);
        });
    }
}