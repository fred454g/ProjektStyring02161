package dtu.example.persistence;

import dtu.example.domain.Medarbejder;
import dtu.example.domain.Projekt;
import java.util.List;

public interface IProjektRepository {
    void gemProjekter(List<Projekt> projekter);
    List<Projekt> indlaesProjekter(List<Medarbejder> medarbejdere);
}