package edu.unl.cc.smilehub.view.security;

import edu.unl.cc.smilehub.business.medical.MedicalService;
import edu.unl.cc.smilehub.domain.Cita;
import edu.unl.cc.smilehub.domain.Doctor;
import edu.unl.cc.smilehub.domain.Paciente;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Named
@ViewScoped
public class CitaController implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private MedicalService medicalService;

    // Inyectamos el DashboardController para refrescarlo cuando cambiemos algo
    @Inject
    private DashboardController dashboardController;

    private Cita nuevaCita;
    private Paciente nuevoPaciente;
    private Doctor nuevoDoctor;

    private List<Paciente> listaPacientes;
    private List<Doctor> listaDoctores;

    // Variable para guardar las fechas ocupadas en formato JSON simple para Javascript
    private String fechasJson;

    @PostConstruct
    public void init() {
        this.nuevaCita = new Cita();
        this.nuevaCita.setFechaHora(LocalDateTime.now());
        this.nuevoPaciente = new Paciente();
        this.nuevoDoctor = new Doctor();
        cargarListas();
        cargarFechasCalendario(); // Cargamos los puntitos
    }

    public void cargarListas() {
        this.listaPacientes = medicalService.listarPacientes();
        this.listaDoctores = medicalService.listarDoctores();
    }

    public void cargarFechasCalendario() {
        List<String> fechas = medicalService.obtenerFechasOcupadas();
        // Convertimos la lista de Java a un String tipo Array de JS: "['2026-02-10', '2026-02-15']"
        this.fechasJson = fechas.stream().collect(Collectors.joining("','", "['", "']"));
    }

    public void prepararNuevaCita() {
        this.nuevaCita = new Cita();
        this.nuevaCita.setFechaHora(LocalDateTime.now());
        cargarListas();
    }

    public void agendarCita() {
        try {
            medicalService.agendarCita(this.nuevaCita);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Cita agendada"));
            prepararNuevaCita();

            // Actualizamos los contadores y el calendario
            dashboardController.cargarDatos();
            cargarFechasCalendario();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo agendar"));
        }
    }

    // --- NUEVO: MÉTODO PARA CAMBIAR ESTADO (REALIZADA / CANCELADA) ---
    public void cambiarEstado(Cita cita, String nuevoEstado) {
        try {
            medicalService.actualizarEstadoCita(cita.getId(), nuevoEstado);

            String mensaje = nuevoEstado.equals("REALIZADA") ? "Cita completada con éxito" : "Cita cancelada";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Actualizado", mensaje));

            // Refrescamos el dashboard para que se muevan los números
            dashboardController.cargarDatos();
            cargarFechasCalendario(); // Actualizamos los puntos por si la cita ya no es pendiente
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo actualizar la cita"));
        }
    }

    public void guardarPaciente() {
        try {
            medicalService.guardarPaciente(this.nuevoPaciente);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Paciente registrado"));
            this.listaPacientes = medicalService.listarPacientes();
            this.nuevaCita.setPaciente(this.nuevoPaciente);
            this.nuevoPaciente = new Paciente();
            dashboardController.cargarDatos(); // Actualizar contador de pacientes
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error al guardar paciente"));
        }
    }

    public void guardarDoctor() {
        try {
            medicalService.guardarDoctor(this.nuevoDoctor);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Doctor registrado"));
            this.listaDoctores = medicalService.listarDoctores();
            this.nuevoDoctor = new Doctor();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error al guardar doctor"));
        }
    }

    // Getters y Setters
    public Cita getNuevaCita() { return nuevaCita; }
    public void setNuevaCita(Cita nuevaCita) { this.nuevaCita = nuevaCita; }
    public Paciente getNuevoPaciente() { return nuevoPaciente; }
    public void setNuevoPaciente(Paciente nuevoPaciente) { this.nuevoPaciente = nuevoPaciente; }
    public Doctor getNuevoDoctor() { return nuevoDoctor; }
    public void setNuevoDoctor(Doctor nuevoDoctor) { this.nuevoDoctor = nuevoDoctor; }
    public List<Paciente> getListaPacientes() { return listaPacientes; }
    public List<Doctor> getListaDoctores() { return listaDoctores; }

    // Getter para el JSON de fechas
    public String getFechasJson() { return fechasJson; }
}