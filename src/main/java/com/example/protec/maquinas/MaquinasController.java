package com.example.protec.maquinas;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/maquinas")
public class MaquinasController {

    private final MaquinasService maquinasService;

    public MaquinasController(
        MaquinasService maquinasService
    ) {

        this.maquinasService = maquinasService;
    }

    // ── Getters ─────────────────────────────────────────────────────────────────

    @GetMapping
    public ResponseEntity<List<MaquinasResponse>> listAll() {
        return ResponseEntity.ok(maquinasService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaquinasResponse> getById(
            @PathVariable UUID id) {
        try {
            return ResponseEntity.ok(maquinasService.getById(id));
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<MaquinasResponse> getByNome(
            @PathVariable String nome) {

        try {
            return ResponseEntity.ok(maquinasService.getByNome(nome));
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // ── Delete ─────────────────────────────────────────────────────────────────




    // ── Delete ─────────────────────────────────────────────────────────────────

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID id) {

        try {
            maquinasService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}