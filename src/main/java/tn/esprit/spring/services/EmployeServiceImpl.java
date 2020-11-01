package tn.esprit.spring.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tn.esprit.spring.entities.Contrat;
import tn.esprit.spring.entities.Departement;
import tn.esprit.spring.entities.Employe;
import tn.esprit.spring.entities.Entreprise;
import tn.esprit.spring.entities.Mission;
import tn.esprit.spring.entities.Timesheet;
import tn.esprit.spring.repository.ContratRepository;
import tn.esprit.spring.repository.DepartementRepository;
import tn.esprit.spring.repository.EmployeRepository;
import tn.esprit.spring.repository.TimesheetRepository;

@Service
public class EmployeServiceImpl implements IEmployeService {

	@Autowired
	EmployeRepository employeRepository;
	@Autowired
	DepartementRepository deptRepoistory;
	@Autowired
	ContratRepository contratRepoistory;
	@Autowired
	TimesheetRepository timesheetRepository;
	
	private static final Logger l = LogManager.getLogger(EmployeServiceImpl.class);

	public int ajouterEmploye(Employe employe) {
		l.info("Dans la fonction ajouterEmploye");
		l.debug("Je m'apprete à enregistrer l'employe");
		employeRepository.save(employe);
		l.info("Success");
		return employe.getId();
	}

	public void mettreAjourEmailByEmployeId(String email, int employeId) {
		l.info("Dans la fonction mettreAjourEmailByEmployeId");
		l.debug("Je m'apprete à recherhcer l'employe selon id");
		Employe employe = employeRepository.findById(employeId).get();
		l.info("Fin de la recherche");
		l.debug("Je met à jour l'email");
		employe.setEmail(email);
		l.info("Success");
		l.debug("Reenregistrement de l'employé");
		employeRepository.save(employe);
		l.info("Succcesss");

	}

	@Transactional	
	public void affecterEmployeADepartement(int employeId, int depId) {
		l.info("Dans la methode affecterEmployeADepartement");
		l.debug("Je cherche le departement");
		Departement depManagedEntity = deptRepoistory.findById(depId).get();
		l.info("Success !");
		l.debug("Je cherche l'employe");
		Employe employeManagedEntity = employeRepository.findById(employeId).get();
		l.info("Success !");
		l.info("Je vais rentrer dans le if ou le else");
		if(depManagedEntity.getEmployes() == null){
			l.info("Je suis dans le if");
			l.debug("Je crée une nouvelle liste");
			List<Employe> employes = new ArrayList<>();
			l.info("Success !");
			l.debug("j'ajoute l'employe");
			employes.add(employeManagedEntity);
			l.info("Success !");
			l.debug("j'ajoute la liste des employes dans le departement");
			depManagedEntity.setEmployes(employes);
			l.info("Success !");
			l.info("Jai fini, je sors donc du if et de la fonction");
		}else{
			l.info("Je suis dans le else");
			l.debug("J'ajoute l'employe");
			depManagedEntity.getEmployes().add(employeManagedEntity);
			l.info("Jai fini, je sors donc du else et de la fonction");
		}
	}
	@Transactional
	public void desaffecterEmployeDuDepartement(int employeId, int depId)
	{
		l.info("Dans la methode desaffecterEmployeDuDepartement");
		l.debug("Je cherche le departement");
		Departement dep = deptRepoistory.findById(depId).get();
		l.info("Success");
		l.debug("Je compte le nombre d'etudiants");
		int employeNb = dep.getEmployes().size();
		l.info("Sucesss");
		l.debug("Je vais rentrer dans la boucle for");
		for(int index = 0; index < employeNb; index++){
			l.info("Dans la boucle");
			if(dep.getEmployes().get(index).getId() == employeId){
				l.info("Dans le if");
				dep.getEmployes().remove(index);
				break;//a revoir
			}
		}
	}

	public int ajouterContrat(Contrat contrat) {
		contratRepoistory.save(contrat);
		return contrat.getReference();
	}

	public void affecterContratAEmploye(int contratId, int employeId) {
		Contrat contratManagedEntity = contratRepoistory.findById(contratId).get();
		Employe employeManagedEntity = employeRepository.findById(employeId).get();

		contratManagedEntity.setEmploye(employeManagedEntity);
		contratRepoistory.save(contratManagedEntity);
		
	}

	public String getEmployePrenomById(int employeId) {
		l.info("Dans la methode getEmployePrenomById");
		l.debug("Je recherhe l'employé selon son id");
		Employe employeManagedEntity = employeRepository.findById(employeId).get();
		return employeManagedEntity.getPrenom();
	}
	public void deleteEmployeById(int employeId)
	{
		l.info("Dans la methode deleteEmployeById");
		l.debug("Je recherhe l'employé selon son id");
		Employe employe = employeRepository.findById(employeId).get();
		l.info("Success");
		//Desaffecter l'employe de tous les departements
		//c'est le bout master qui permet de mettre a jour
		//la table d'association
		l.debug("Je rentre dans la boucle");
		for(Departement dep : employe.getDepartements()){
			dep.getEmployes().remove(employe);
		}
		l.info("Suis sorti de la boucle");
		l.debug("Je supprime l'employe");
		employeRepository.delete(employe);
		l.info("Successs");
	}

	public void deleteContratById(int contratId) {
		Contrat contratManagedEntity = contratRepoistory.findById(contratId).get();
		contratRepoistory.delete(contratManagedEntity);

	}

	public int getNombreEmployeJPQL() {
		l.info("Dans la methode getNombreEmployeJPQL");
		l.debug("Je lance la fonction getNombreEmployeJPQL Puis je retourne le resultat");
		return employeRepository.countemp();
	}
	
	public List<String> getAllEmployeNamesJPQL() {
		l.info("Dans la methode getAllEmployeNamesJPQL");
		l.debug("Je lance la fonction getAllEmployeNamesJPQL Puis je retourne le resultat");
		return employeRepository.employeNames();

	}
	
	public List<Employe> getAllEmployeByEntreprise(Entreprise entreprise) {
		return employeRepository.getAllEmployeByEntreprisec(entreprise);
	}

	public void mettreAjourEmailByEmployeIdJPQL(String email, int employeId) {
		l.info("Dans la methode mettreAjourEmailByEmployeIdJPQL");
		l.debug("Je lance la fonction mettreAjourEmailByEmployeIdJPQL Puis je retourne le resultat");
		employeRepository.mettreAjourEmailByEmployeIdJPQL(email, employeId);

	}
	public void deleteAllContratJPQL() {
         employeRepository.deleteAllContratJPQL();
	}
	
	public float getSalaireByEmployeIdJPQL(int employeId) {
		l.info("Dans la methode getSalaireByEmployeIdJPQL");
		l.debug("Je lance la fonction getSalaireByEmployeIdJPQL Puis je retourne le resultat");
		return employeRepository.getSalaireByEmployeIdJPQL(employeId);
	}

	public Double getSalaireMoyenByDepartementId(int departementId) {
		return employeRepository.getSalaireMoyenByDepartementId(departementId);
	}
	
	public List<Timesheet> getTimesheetsByMissionAndDate(Employe employe, Mission mission, Date dateDebut,
			Date dateFin) {
		return timesheetRepository.getTimesheetsByMissionAndDate(employe, mission, dateDebut, dateFin);
	}

	public List<Employe> getAllEmployes() {
		l.info("Dans la methode getAllEmployes()");
		l.debug("Je lance la fonction findALL Puis je retourne le resultat");

				return (List<Employe>) employeRepository.findAll();
	}

}
