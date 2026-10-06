package tn.esprit.autoloc.service;

import tn.esprit.autoloc.domain.Maintenance;

import java.util.List;

public interface IMaintenanceService {

    List<Maintenance> retrieveAllMaintenances();

    Maintenance addMaintenance(Maintenance maintenance);

    Maintenance updateMaintenance(Maintenance maintenance);

    Maintenance retrieveMaintenance(Long idMaintenance);

    void removeMaintenance(Long idMaintenance);

    List<Maintenance> addMaintenances(List<Maintenance> maintenances);
}