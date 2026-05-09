package dtu.example.persistence;

import dtu.example.domain.Medarbejder;
import java.util.List;

public interface IMedarbejderRepository {
    List<Medarbejder> indlaesMedarbejdere();
}