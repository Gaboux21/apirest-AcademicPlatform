package com.hitss.spring.apiacademicplatform.service;

import com.hitss.spring.apiacademicplatform.dto.PeriodoLectivoDTO;
import com.hitss.spring.apiacademicplatform.model.PeriodoLectivo;
import com.hitss.spring.apiacademicplatform.repository.PeriodoLectivoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PeriodoLectivoService {

    @Autowired
    private PeriodoLectivoRepository periodoLectivoRepository;

    private PeriodoLectivoDTO convertToDto(PeriodoLectivo periodo) {
        return new PeriodoLectivoDTO(
                periodo.getId(),
                periodo.getNombre(),
                periodo.getFechaInicio(),
                periodo.getFechaFin()
        );
    }

    private PeriodoLectivo convertToEntity(PeriodoLectivoDTO periodoDTO) {
        PeriodoLectivo periodo = new PeriodoLectivo();
        periodo.setId(periodoDTO.getId());
        periodo.setNombre(periodoDTO.getNombre());
        periodo.setFechaInicio(periodoDTO.getFechaInicio());
        periodo.setFechaFin(periodoDTO.getFechaFin());
        return periodo;
    }

    public List<PeriodoLectivoDTO> getAllPeriodos() {
        return periodoLectivoRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<PeriodoLectivoDTO> getPeriodoById(Long id) {
        return periodoLectivoRepository.findById(id)
                .map(this::convertToDto);
    }

    public PeriodoLectivoDTO createPeriodo(PeriodoLectivoDTO periodoDTO) {
        PeriodoLectivo periodo = convertToEntity(periodoDTO);
        return convertToDto(periodoLectivoRepository.save(periodo));
    }

    public PeriodoLectivoDTO updatePeriodo(Long id, PeriodoLectivoDTO periodoDTO) {
        return periodoLectivoRepository.findById(id)
                .map(existingPeriodo -> {
                    existingPeriodo.setNombre(periodoDTO.getNombre());
                    existingPeriodo.setFechaInicio(periodoDTO.getFechaInicio());
                    existingPeriodo.setFechaFin(periodoDTO.getFechaFin());
                    return convertToDto(periodoLectivoRepository.save(existingPeriodo));
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Periodo lectivo no encontrado con ID: " + id));
    }

    public void deletePeriodo(Long id) {
        if (!periodoLectivoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Periodo lectivo no encontrado con ID: " + id);
        }
        periodoLectivoRepository.deleteById(id);
    }
}
