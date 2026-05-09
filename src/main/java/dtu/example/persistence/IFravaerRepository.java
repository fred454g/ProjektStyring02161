package dtu.example.persistence;

import dtu.example.domain.Medarbejder;
import java.util.List;

public interface IFravaerRepository {
    void gemFravaer(List<Medarbejder> medarbejdere);
    void indlaesFravaer(List<Medarbejder> medarbejdere);
}