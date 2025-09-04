package lk.ijse.gdse.project.backend.service;

import lk.ijse.gdse.project.backend.dto.PCDTO;

import java.util.List;

public interface PCService {
    void savePC(PCDTO pcdto);
    void updatePC(PCDTO pcdto);
    void deletePC(String pcId);
    PCDTO findPCById(String pcId);
    List<PCDTO> getPCs();

}
