package co.edu.uniandes.dse.parcialprueba.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.parcialprueba.entities.MedicoEntity;
import co.edu.uniandes.dse.parcialprueba.repositories.MedicoRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MedicoService {
    
    @Autowired
    MedicoRepository medicoRepository;

    @Transactional
    public void createMedicos(MedicoEntity medico){
        log.info("Creando medico");
        if (medico.getRegistroMedico().substring(0, 1).equals("RM"))
            medicoRepository.save(medico);
        else
            throw new IllegalArgumentException("El registro medico debe empezar con RM");
       
    }

}
