package tn.esprit.tpautoloc.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.tpautoloc.domain.Agence;
import tn.esprit.tpautoloc.domain.Employe;
import tn.esprit.tpautoloc.exception.BusinessRuleException;
import tn.esprit.tpautoloc.exception.ResourceNotFoundException;
import tn.esprit.tpautoloc.repository.IAgenceRrepository;
import tn.esprit.tpautoloc.repository.IEmployeRepository;
import tn.esprit.tpautoloc.service.IEmployeService;
import tn.esprit.tpautoloc.web.dto.EmployeDTO;
import tn.esprit.tpautoloc.web.mapper.EmployeMapper;

@Service
@RequiredArgsConstructor
@Transactional
public class EmployeServiceImpl implements IEmployeService {

    private final IEmployeRepository employeRepository;
    private final IAgenceRrepository agenceRepository;
    private final EmployeMapper employeMapper;

    @Override
    public EmployeDTO create(EmployeDTO dto) {
        if (dto.getId() != null) {
            throw new IllegalArgumentException(
                    "Un nouvel employé ne doit pas avoir d'identifiant");
        }
        Employe employe = employeMapper.toEntity(dto);
        employe.setAgence(chargerAgenceFacultative(dto.getAgenceId()));
        return employeMapper.toDto(employeRepository.save(employe));
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeDTO findById(Long id) {
        return employeMapper.toDto(chargerEmploye(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeDTO> findAll() {
        return employeRepository.findAll().stream()
                .map(employeMapper::toDto)
                .toList();
    }

    @Override
    public EmployeDTO update(Long id, EmployeDTO dto) {
        Employe existant = chargerEmploye(id);
        employeMapper.updateEntity(dto, existant);
        existant.setAgence(chargerAgenceFacultative(dto.getAgenceId()));
        return employeMapper.toDto(employeRepository.save(existant));
    }

    @Override
    public void deleteById(Long id) {
        if (!employeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Employé", id);
        }
        employeRepository.deleteById(id);
    }

    @Override
    public EmployeDTO affecterEmployeAAgence(Long employeId, Long agenceId) {
        Employe employe = chargerEmploye(employeId);
        Agence agence = chargerAgence(agenceId);

        // Côté propriétaire de l'association : Employe porte la clé étrangère agence_id_agence.
        employe.setAgence(agence);
        return employeMapper.toDto(employeRepository.save(employe));
    }

    @Override
    public EmployeDTO desaffecterEmployeDeAgence(Long employeId) {
        Employe employe = chargerEmploye(employeId);
        if (employe.getAgence() == null) {
            throw new BusinessRuleException(
                    "L'employé " + employeId + " n'est rattaché à aucune agence");
        }
        employe.setAgence(null);
        return employeMapper.toDto(employeRepository.save(employe));
    }

    private Employe chargerEmploye(Long id) {
        return employeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employé", id));
    }

    private Agence chargerAgence(Long id) {
        return agenceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agence", id));
    }

    private Agence chargerAgenceFacultative(Long agenceId) {
        return agenceId == null ? null : chargerAgence(agenceId);
    }
}
