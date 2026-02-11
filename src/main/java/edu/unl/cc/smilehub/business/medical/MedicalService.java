package edu.unl.cc.smilehub.business.medical;

// Asegúrate de que estos imports coincidan con donde tienes tus clases (medical o domain raiz)
import edu.unl.cc.smilehub.domain.Cita;
import edu.unl.cc.smilehub.domain.Doctor;
import edu.unl.cc.smilehub.domain.Paciente;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class MedicalService {

    // IMPORTANTE: Asegúrate de que "jbrewPU" sea el nombre que está en tu persistence.xml
    @PersistenceContext(unitName = "jbrewPU")
    private EntityManager em;

    // --- Lógica de Pacientes ---
    @Transactional
    public void guardarPaciente(Paciente paciente) {
        if (paciente.getId() == null) {
            em.persist(paciente);
        } else {
            em.merge(paciente);
        }
    }

    public List<Paciente> listarPacientes() {
        return em.createQuery("SELECT p FROM Paciente p ORDER BY p.apellidos", Paciente.class).getResultList();
    }

    // --- Lógica de Doctores ---
    @Transactional
    public void guardarDoctor(Doctor doctor) {
        if (doctor.getId() == null) {
            em.persist(doctor);
        } else {
            em.merge(doctor);
        }
    }

    public List<Doctor> listarDoctores() {
        return em.createQuery("SELECT d FROM Doctor d ORDER BY d.apellidos", Doctor.class).getResultList();
    }

    // --- Lógica de Citas ---
    @Transactional
    public void agendarCita(Cita cita) {
        if (cita.getId() == null) {
            em.persist(cita);
        } else {
            em.merge(cita);
        }
    }

    public List<Cita> listarCitas() {
        return em.createQuery("SELECT c FROM Cita c ORDER BY c.fechaHora DESC", Cita.class).getResultList();
    }

    // ----------------------------------------
    // --- ESTADÍSTICAS PARA EL DASHBOARD ---
    // ----------------------------------------

    public long contarCitasHoy() {
        // Cuenta citas donde la fecha coincida con hoy (Usando función DATE de Postgres)
        try {
            return em.createQuery("SELECT COUNT(c) FROM Cita c WHERE FUNCTION('DATE', c.fechaHora) = CURRENT_DATE", Long.class)
                    .getSingleResult();
        } catch (Exception e) {
            return 0;
        }
    }

    public long contarPendientes() {
        try {
            return em.createQuery("SELECT COUNT(c) FROM Cita c WHERE c.estado = 'PENDIENTE'", Long.class)
                    .getSingleResult();
        } catch (Exception e) {
            return 0;
        }
    }

    public long contarRealizadas() {
        // Cuenta tanto las REALIZADAS como las ATENDIDAS
        try {
            return em.createQuery("SELECT COUNT(c) FROM Cita c WHERE c.estado = 'REALIZADA' OR c.estado = 'ATENDIDA'", Long.class)
                    .getSingleResult();
        } catch (Exception e) {
            return 0;
        }
    }

    public long contarPacientesTotal() {
        try {
            return em.createQuery("SELECT COUNT(p) FROM Paciente p", Long.class).getSingleResult();
        } catch (Exception e) {
            return 0;
        }
    }

    public List<Cita> listarProximasCitas() {
        // Trae las citas futuras o de hoy, ordenadas por fecha
        return em.createQuery("SELECT c FROM Cita c WHERE c.fechaHora >= CURRENT_DATE ORDER BY c.fechaHora ASC", Cita.class)
                .setMaxResults(5) // Solo las 5 más próximas
                .getResultList();
    }

    // ------------------------------------------------
    // --- NUEVOS MÉTODOS PARA ESTADOS Y CALENDARIO ---
    // ------------------------------------------------

    @Transactional
    public void actualizarEstadoCita(Long idCita, String nuevoEstado) {
        Cita cita = em.find(Cita.class, idCita);
        if (cita != null) {
            cita.setEstado(nuevoEstado);
            em.merge(cita);
        }
    }

    public List<String> obtenerFechasOcupadas() {
        // Devuelve una lista de fechas en formato texto (yyyy-MM-dd) que tienen citas pendientes
        // Usamos TO_CHAR de Postgres para formatear
        return em.createQuery("SELECT DISTINCT FUNCTION('TO_CHAR', c.fechaHora, 'yyyy-MM-dd') FROM Cita c WHERE c.estado = 'PENDIENTE'", String.class)
                .getResultList();
    }
}