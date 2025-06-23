package com.hitss.spring.apiacademicplatform.service;

import com.hitss.spring.apiacademicplatform.dto.MaterialDTO;
import com.hitss.spring.apiacademicplatform.model.Asignatura;
import com.hitss.spring.apiacademicplatform.model.Material;
import com.hitss.spring.apiacademicplatform.model.Profesor;
import com.hitss.spring.apiacademicplatform.repository.AsignaturaRepository;
import com.hitss.spring.apiacademicplatform.repository.MaterialRepository;
import com.hitss.spring.apiacademicplatform.repository.ProfesorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MaterialService {

    @Autowired
    private MaterialRepository materialRepository;
    @Autowired
    private AsignaturaRepository asignaturaRepository;
    @Autowired
    private ProfesorRepository profesorRepository;

    private MaterialDTO convertToDto(Material material) {
        return new MaterialDTO(
                material.getId(),
                material.getTitulo(),
                material.getDescripcion(),
                material.getArchivoUrl(),
                material.getAsignatura().getId(),
                material.getProfesor().getId()
        );
    }

    private Material convertToEntity(MaterialDTO materialDTO) {
        Material material = new Material();
        material.setId(materialDTO.getId());
        material.setTitulo(materialDTO.getTitulo());
        material.setDescripcion(materialDTO.getDescripcion());
        material.setArchivoUrl(materialDTO.getArchivoUrl());

        Asignatura asignatura = asignaturaRepository.findById(materialDTO.getAsignaturaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Asignatura no encontrada con ID: " + materialDTO.getAsignaturaId()));
        material.setAsignatura(asignatura);

        Profesor profesor = profesorRepository.findById(materialDTO.getProfesorId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profesor no encontrado con ID: " + materialDTO.getProfesorId()));
        material.setProfesor(profesor);

        return material;
    }

    public List<MaterialDTO> getAllMateriales() {
        return materialRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<MaterialDTO> getMaterialById(Long id) {
        return materialRepository.findById(id)
                .map(this::convertToDto);
    }

    public MaterialDTO createMaterial(MaterialDTO materialDTO) {
        Material material = convertToEntity(materialDTO);
        return convertToDto(materialRepository.save(material));
    }

    public MaterialDTO updateMaterial(Long id, MaterialDTO materialDTO) {
        return materialRepository.findById(id)
                .map(existingMaterial -> {
                    existingMaterial.setTitulo(materialDTO.getTitulo());
                    existingMaterial.setDescripcion(materialDTO.getDescripcion());
                    existingMaterial.setArchivoUrl(materialDTO.getArchivoUrl());

                    if (!existingMaterial.getAsignatura().getId().equals(materialDTO.getAsignaturaId())) {
                        Asignatura nuevaAsignatura = asignaturaRepository.findById(materialDTO.getAsignaturaId())
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Asignatura no encontrada con ID: " + materialDTO.getAsignaturaId()));
                        existingMaterial.setAsignatura(nuevaAsignatura);
                    }
                    if (!existingMaterial.getProfesor().getId().equals(materialDTO.getProfesorId())) {
                        Profesor nuevoProfesor = profesorRepository.findById(materialDTO.getProfesorId())
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profesor no encontrado con ID: " + materialDTO.getProfesorId()));
                        existingMaterial.setProfesor(nuevoProfesor);
                    }
                    return convertToDto(materialRepository.save(existingMaterial));
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Material no encontrado con ID: " + id));
    }

    public void deleteMaterial(Long id) {
        if (!materialRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Material no encontrado con ID: " + id);
        }
        materialRepository.deleteById(id);
    }

    public List<MaterialDTO> getMaterialesByAsignaturaId(Long asignaturaId) {
        return materialRepository.findByAsignaturaId(asignaturaId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
}
