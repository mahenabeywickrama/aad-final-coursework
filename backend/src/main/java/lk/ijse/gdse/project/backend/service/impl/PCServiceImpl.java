package lk.ijse.gdse.project.backend.service.impl;

import lk.ijse.gdse.project.backend.dto.PCDTO;
import lk.ijse.gdse.project.backend.entity.PC;
import lk.ijse.gdse.project.backend.exception.ResourceAlreadyFoundException;
import lk.ijse.gdse.project.backend.exception.ResourceNotFoundException;
import lk.ijse.gdse.project.backend.repository.PCRepository;
import lk.ijse.gdse.project.backend.service.PCService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PCServiceImpl implements PCService {

    private final PCRepository pcRepository;
    private final ModelMapper modelMapper;

    public PCServiceImpl(PCRepository pcRepository, ModelMapper modelMapper) {
        this.pcRepository = pcRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void savePC(PCDTO pcdto) {
        pcRepository.save(modelMapper.map(pcdto, PC.class));
    }

    @Override
    public void updatePC(PCDTO pcdto) {
        pcRepository.save(modelMapper.map(pcdto, PC.class));
    }

    @Override
    public void deletePC(String pcId) {
        if (!pcRepository.existsById(Long.valueOf(pcId))) {
            throw new ResourceNotFoundException("PC does not exist");
        }
        pcRepository.deleteById(Long.valueOf(pcId));
    }

    @Override
    public PCDTO findPCById(String pcId) {
        if (!pcRepository.existsById(Long.valueOf(pcId))) {
            throw new ResourceNotFoundException("PC does not exist");
        }
        return modelMapper.map(pcRepository.findById(Long.valueOf(pcId)).orElse(null), PCDTO.class);
    }

    @Override
    public List<PCDTO> getPCs() {
        List<PC> pcs = pcRepository.findAll();

        if (pcs.isEmpty()) {
            throw new ResourceNotFoundException("No PC found");
        }

        return modelMapper.map(pcs, new TypeToken<List<PCDTO>>() {}.getType());
    }
}
