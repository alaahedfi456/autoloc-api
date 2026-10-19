
package tn.esprit.autoloc.web.controller;

import java.util.List;
import org.springframework.web.bind.annotation.*;

import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.service.IEquipementService;

@RestController
@RequestMapping("/api/equipements")
public class EquipementController {

    private final IEquipementService equipementService;

    public EquipementController(IEquipementService equipementService) {
        this.equipementService = equipementService;
    }

    @PostMapping
    public Equipement create(@RequestBody Equipement equipement) {
        return equipementService.create(equipement);
    }

    @GetMapping("/{id}")
    public Equipement findById(@PathVariable Long id) {
        return equipementService.findById(id);
    }

    @GetMapping
    public List<Equipement> findAll() {
        return equipementService.findAll();
    }

    @PutMapping("/{id}")
    public Equipement update(@PathVariable Long id, @RequestBody Equipement equipement) {
        return equipementService.update(id, equipement);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        equipementService.deleteById(id);
    }
}
