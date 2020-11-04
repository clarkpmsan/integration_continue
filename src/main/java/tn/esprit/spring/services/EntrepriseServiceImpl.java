package tn.esprit.spring.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tn.esprit.spring.entities.Departement;
import tn.esprit.spring.entities.Employe;
import tn.esprit.spring.entities.Entreprise;
import tn.esprit.spring.repository.DepartementRepository;
import tn.esprit.spring.repository.EntrepriseRepository;

@Service
public class EntrepriseServiceImpl implements IEntrepriseService {

	private static final Logger l = LogManager.getLogger(EntrepriseServiceImpl.class);

	@Autowired
	EntrepriseRepository entrepriseRepoistory;
	@Autowired
	DepartementRepository deptRepoistory;

	// Add Entreprise Service
	public Entreprise ajouterEntreprise(Entreprise entreprise) {
		try {
			l.info("In ajouterEntreprise() : ");
			l.debug("Ajout de l'entreprise");
			entrepriseRepoistory.save(entreprise);
			l.debug("Je viens d'ajouter l'entreprise : ",entreprise.getName());
			l.info("Out ajouterEntreprise() without errors.");
			return entreprise;
		} 
		catch (Exception e) {
			l.error("Erreur dans ajouterEntreprise() : " + e);
			return entreprise;
		}
	}

	// Get All Entreprises Service
	public 	List<Entreprise> retreiveAllEntreprises(){
		l.info("In retreiveAllEntreprises() : ");
		List<Entreprise> entreprises = (List<Entreprise>) entrepriseRepoistory.findAll();
		for (Entreprise entreprise : entreprises){
			l.debug("entreprise +++ : " + entreprise);
		}
		l.info("Out retreiveAllEntreprises() without errors.");
		return entreprises;
	}
	
	// Find Entreprise By Id Service
	public Entreprise getEntrepriseById(int entrepriseId) {
		try {
			l.info("In getEntrepriseById() : ");
			l.debug("Recherche de l'entreprise");
			Entreprise entrep = entrepriseRepoistory.findById(entrepriseId).get();
			l.debug("Je viens de trouver l'entreprise d'ID: ",entrepriseId);
			l.info("Out getEntrepriseById() without errors.");
			return entrep;
		} 
		catch (Exception e) {
			l.error("Erreur dans deleteEntrepriseById() : " + e);
			return entrepriseRepoistory.findById(entrepriseId).get();
		}
	}
	
	// Update Entreprise Service
	public Entreprise updateEntreprise(Entreprise entreprise) {
		l.info("In updateEntreprise() : ");
		Entreprise entrepriseUpdated = entrepriseRepoistory.save(entreprise);
		l.info("Out updateEntreprise() : ");
		return entrepriseUpdated;	
	}

	// Delete Entreprise By Id
	@Transactional
	public void deleteEntrepriseById(int entrepriseId) {
		try {
			l.info("In deleteEntrepriseById() : ");
			l.debug("Supression de l'entreprise");
			entrepriseRepoistory.delete(entrepriseRepoistory.findById(entrepriseId).get());	
			l.debug("Je viens de supprimer l'entreprise d'ID: ",entrepriseId);
			l.info("Out deleteEntrepriseById() without errors.");

		} 
		catch (Exception e) {
			l.error("Erreur dans deleteEntrepriseById() : " + e);
		}

	}




	/**********************************************Departement**************************************************************/

	public int ajouterDepartement(Departement dep) {
		deptRepoistory.save(dep);
		return dep.getId();
	}

	public void affecterDepartementAEntreprise(int depId, int entrepriseId) {
		//Le bout Master de cette relation N:1 est departement  
		//donc il faut rajouter l'entreprise a departement 
		// ==> c'est l'objet departement(le master) qui va mettre a jour l'association
		//Rappel : la classe qui contient mappedBy represente le bout Slave
		//Rappel : Dans une relation oneToMany le mappedBy doit etre du cote one.
		Entreprise entrepriseManagedEntity = entrepriseRepoistory.findById(entrepriseId).get();
		Departement depManagedEntity = deptRepoistory.findById(depId).get();

		depManagedEntity.setEntreprise(entrepriseManagedEntity);
		deptRepoistory.save(depManagedEntity);

	}

	public List<String> getAllDepartementsNamesByEntreprise(int entrepriseId) {
		Entreprise entrepriseManagedEntity = entrepriseRepoistory.findById(entrepriseId).get();
		List<String> depNames = new ArrayList<>();
		for(Departement dep : entrepriseManagedEntity.getDepartements()){
			depNames.add(dep.getName());
		}

		return depNames;
	}


	@Transactional
	public void deleteDepartementById(int depId) {
		deptRepoistory.delete(deptRepoistory.findById(depId).get());	
	}

}
