package com.example.protec.maquinas;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MaquinasService {

    private final MaquinasRepository maquinasRepository;

    public MaquinasService(MaquinasRepository maquinasRepository) {
        this.maquinasRepository = maquinasRepository;
    }

    public MaquinasResponse getById(UUID id) {
        Maquinas maquina = maquinasRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Maquina not found"));

        return toResponse(maquina);
    }

    public MaquinasResponse getByNome(String nome) {
        Maquinas maquina = maquinasRepository.findByNome(nome)
                .orElseThrow(() -> new IllegalArgumentException("maquina not found"));

        return toResponse(maquina);
    }

    public List<MaquinasResponse> getAll() {
        return maquinasRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public MaquinasResponse updateNome(UUID id, String newNome) {
        if (newNome == null || newNome.isBlank()) {
            throw new IllegalArgumentException("nome cannot be blank");
        }

        if (maquinasRepository.existsByNome(newNome)) {
            throw new IllegalArgumentException("nome already taken");
        }

        Maquinas maquina = maquinasRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Maquina not found"));

        maquina.setNome(newNome);
        maquinasRepository.save(maquina);

        return toResponse(maquina);
    }

    public MaquinasResponse updateRiscos(UUID id, String newRiscos) {
        if (newRiscos == null || newRiscos.isBlank()) {
            throw new IllegalArgumentException("riscos cannot be blank");
        }

        Maquinas maquina = maquinasRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("maquina not found"));

        maquina.setRiscos(newRiscos);
        maquinasRepository.save(maquina);

        return toResponse(maquina);
    }


    public void delete(UUID id) {
        if (!maquinasRepository.existsById(id)) {
            throw new IllegalArgumentException("maquina not found");
        }

        maquinasRepository.deleteById(id);
    }

    private MaquinasResponse toResponse(Maquinas maquina) {
        return new MaquinasResponse(
                maquina.getId(),
                maquina.getNome(),
                maquina.getRiscos()
        );
    }
}