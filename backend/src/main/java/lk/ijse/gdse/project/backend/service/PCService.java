package lk.ijse.gdse.project.backend.service;

import lk.ijse.gdse.project.backend.dto.PCDTO;
import lk.ijse.gdse.project.backend.entity.PC;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PCService {
    void savePC(PCDTO pcdto);
    void updatePC(PCDTO pcdto);
    void deletePC(String pcId);
    PCDTO findPCById(String pcId);
    List<PCDTO> getPCs();
    public Page<PC> getAllPCs(Pageable pageable);
    public Page<PC> searchPCs(String searchValue, Pageable pageable);
}
