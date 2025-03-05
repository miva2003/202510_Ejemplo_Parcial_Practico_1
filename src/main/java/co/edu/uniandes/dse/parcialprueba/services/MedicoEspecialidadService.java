package co.edu.uniandes.dse.parcialprueba.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.parcialprueba.entities.EspecialidadEntity;
import co.edu.uniandes.dse.parcialprueba.entities.MedicoEntity;
import co.edu.uniandes.dse.parcialprueba.repositories.EspecialidadRepository;
import co.edu.uniandes.dse.parcialprueba.repositories.MedicoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MedicoEspecialidadService {
    
    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private EspecialidadRepository especialidadRepository;

    @Transactional
    public MedicoEntity addEspecialidad(Long medicoId, Long especialidadId){
        log.info("Agregando especialidad a médico");
        Optional<MedicoEntity> medico = medicoRepository.findById(medicoId);
        if (medico.isEmpty()){
            throw new EntityNotFoundException("No se encontró el médico con id: " + medicoId);
        }
        Optional<EspecialidadEntity> especialidad = especialidadRepository.findById(especialidadId);
        if (especialidad.isEmpty()){
            throw new EntityNotFoundException("No se encontró la especialidad con id: " + especialidadId);
        }
        medico.get().getEspecialidades().add(especialidad.get());
        log.info("Especialidad agregada a médico");
        return medicoRepository.save(medico.get());
    }

}
