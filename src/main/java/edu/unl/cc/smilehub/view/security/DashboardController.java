package edu.unl.cc.smilehub.view.security;

import edu.unl.cc.smilehub.business.medical.MedicalService;
import edu.unl.cc.smilehub.domain.Cita;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class DashboardController implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private MedicalService medicalService;

    // Variables para los contadores
    private long citasHoy;
    private long pendientes;
    private long realizadas;
    private long nuevosPacientes;

    // Lista real de objetos Cita
    private List<Cita> proximasCitas;

    @PostConstruct
    public void init() {
        cargarDatos();
    }

    public void cargarDatos() {
        // Consultamos a la base de datos real
        this.citasHoy = medicalService.contarCitasHoy();
        this.pendientes = medicalService.contarPendientes();
        this.realizadas = medicalService.contarRealizadas();
        this.nuevosPacientes = medicalService.contarPacientesTotal();
        this.proximasCitas = medicalService.listarProximasCitas();
    }

    // Getters
    public long getCitasHoy() { return citasHoy; }
    public long getPendientes() { return pendientes; }
    public long getRealizadas() { return realizadas; }
    public long getNuevosPacientes() { return nuevosPacientes; }
    public List<Cita> getProximasCitas() { return proximasCitas; }
}