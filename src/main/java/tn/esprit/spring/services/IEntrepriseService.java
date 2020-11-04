package tn.esprit.spring.services;

import java.util.List;

import tn.esprit.spring.entities.Departement;
import tn.esprit.spring.entities.Entreprise;

public interface IEntrepriseService {
	
	public Entreprise ajouterEntreprise(Entreprise entreprise);
	public 	List<Entreprise> retreiveAllEntreprises();
	public Entreprise getEntrepriseById(int entrepriseId);
	public Entreprise updateEntreprise(Entreprise entreprise);
	public void deleteEntrepriseById(int entrepriseId);
	
	/********************************************Departement****************************************************************/
	
	public int ajouterDepartement(Departement dep);
	void affecterDepartementAEntreprise(int depId, int entrepriseId);
	List<String> getAllDepartementsNamesByEntreprise(int entrepriseId);
	public void deleteDepartementById(int depId);
}
