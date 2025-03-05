package co.edu.uniandes.dse.parcialprueba.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import co.edu.uniandes.dse.parcialprueba.entities.EspecialidadEntity;
import co.edu.uniandes.dse.parcialprueba.entities.MedicoEntity;
import co.edu.uniandes.dse.parcialprueba.repositories.EspecialidadRepository;
import co.edu.uniandes.dse.parcialprueba.repositories.MedicoRepository;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@Import({MedicoEspecialidadService.class, MedicoService.class, EspecialidadService.class})
public class MedicoEspecialidadTest {
    
    @Autowired
    private MedicoEspecialidadService medicoEspecialidadService;

    @Autowired
    private MedicoService medicoService;

    @Autowired
    private EspecialidadService especialidadService;

    @Autowired
    private TestEntityManager entityManager;
    
    private PodamFactory factory = new PodamFactoryImpl();

    private List<MedicoEntity> medicosList = new ArrayList<>();
    private List<EspecialidadEntity> especialidadesList = new ArrayList<>();
    
    @BeforeEach
    void setUp(){
        clearData();
        insertData();
    }

    private void clearData(){
        entityManager.getEntityManager().createQuery("delete from MedicoEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from EspecialidadEntity").executeUpdate();
    }

    private void insertData(){
        for (int i = 0; i < 3; i++) {
            MedicoEntity medico = factory.manufacturePojo(MedicoEntity.class);
            entityManager.persist(medico);
            medicosList.add(medico);
        }
        for (int i = 0; i < 3; i++) {
            EspecialidadEntity especialidad = factory.manufacturePojo(EspecialidadEntity.class);
            entityManager.persist(especialidad);
            especialidadesList.add(especialidad);
        }
    }

    @Test
    void testAddEspecialidad() {
        MedicoEntity medico = medicosList.get(0);
        EspecialidadEntity especialidad = especialidadesList.get(0);
        MedicoEntity result = medicoEspecialidadService.addEspecialidad(medico.getId(), especialidad.getId());
        MedicoEntity entity = entityManager.find(MedicoEntity.class, result.getId());
        assertNotNull(entity);
        assertEquals(medico.getId(), entity.getId());
        assertEquals(medico.getNombre(), entity.getNombre());
        assertEquals(medico.getRegistroMedico(), entity.getRegistroMedico());
        assertEquals(medico.getEspecialidades().size(), entity.getEspecialidades().size());
        assertEquals(medico.getEspecialidades().get(0), entity.getEspecialidades().get(0));
    }
    
    @Test
    void testAddEspecialidadWithInvalidMedico() {
        MedicoEntity medico = medicosList.get(0);
        EspecialidadEntity especialidad = especialidadesList.get(0);
        try {
            medicoEspecialidadService.addEspecialidad(0L, especialidad.getId());
        } catch (Exception e) {
            assertEquals("No se encontró el médico con id: 0", e.getMessage());
        }
    }

    @Test
    void testAddEspecialidadWithInvalidEspecialidad() {
        MedicoEntity medico = medicosList.get(0);
        EspecialidadEntity especialidad = especialidadesList.get(0);
        try {
            medicoEspecialidadService.addEspecialidad(medico.getId(), 0L);
        } catch (Exception e) {
            assertEquals("No se encontró la especialidad con id: 0", e.getMessage());
        }
    }
}
