package Personas.data;

import Personas.logic.*;
import jakarta.xml.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Data {


    @XmlElementWrapper(name = "medicos")
    @XmlElement(name = "medico")
    private List<Medico> medicos;

    @XmlElementWrapper(name = "pacientes")
    @XmlElement(name = "paciente")
    private List<Paciente> pacientes;

    @XmlElementWrapper(name = "farmaceutas")
    @XmlElement(name = "farmaceuta")
    private List<Farmaceuta> farmaceutas;

    @XmlElementWrapper(name = "medicamentos")
    @XmlElement(name = "medicamento")
    private List<Medicamento>  medicamentos;

    @XmlElementWrapper(name = "recetas")
    @XmlElement(name = "receta")
    private List<Receta> recetas;

   String pathMedicos = "medicos.pdf";
   String pathPacientes = "pacientes.pdf";
   String pathFarmaceutas = "farmaceutas.pdf";
   String pathMedicamentos = "medicamentos.pdf";

    public Data() {
        medicos = new ArrayList<>();
        pacientes = new ArrayList<>();
        farmaceutas = new ArrayList<>();
        medicamentos = new ArrayList<>();
        recetas = new ArrayList<>();



        medicos.add(new Medico("1", "Juan", "Admin", "Cardiologia"));
    }

    public List<Receta> getRecetas() {
        return recetas;
    }


    public List<Paciente> getPacientes() {
        return pacientes;
    }

    public List<Medico> getMedicos() {
        return medicos;
    }

    public List<Farmaceuta> getFarmaceutas() {
        return farmaceutas;
    }

    public List<Medicamento> getMedicamentos() { return medicamentos;}

    public String getPathMedicos() { return pathMedicos; }

    public String getPathPacientes() { return pathPacientes; }

    public String getPathFarmaceutas() { return pathFarmaceutas;  }

    public String getPathMedicamentos() { return pathMedicamentos;  }

}
