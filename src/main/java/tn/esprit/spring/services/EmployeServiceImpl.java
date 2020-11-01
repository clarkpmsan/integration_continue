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
		Departement dep = deptRepoistory.findById(depId).get();

		int employeNb = dep.getEmployes().size();
		for(int index = 0; index < employeNb; index++){
			if(dep.getEmployes().get(index).getId() == employeId){
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
		Employe employeManagedEntity = employeRepository.findById(employeId).get();
		return employeManagedEntity.getPrenom();
	}
	public void deleteEmployeById(int employeId)
	{
		Employe employe = employeRepository.findById(employeId).get();

		//Desaffecter l'employe de tous les departements
		//c'est le bout master qui permet de mettre a jour
		//la table d'association
		for(Departement dep : employe.getDepartements()){
			dep.getEmployes().remove(employe);
		}

		employeRepository.delete(employe);
	}

	public void deleteContratById(int contratId) {
		Contrat contratManagedEntity = contratRepoistory.findById(contratId).get();
		contratRepoistory.delete(contratManagedEntity);

	}

	public int getNombreEmployeJPQL() {
		return employeRepository.countemp();
	}
	
	public List<String> getAllEmployeNamesJPQL() {
		return employeRepository.employeNames();

	}
	
	public List<Employe> getAllEmployeByEntreprise(Entreprise entreprise) {
		return employeRepository.getAllEmployeByEntreprisec(entreprise);
	}

	public void mettreAjourEmailByEmployeIdJPQL(String email, int employeId) {
		employeRepository.mettreAjourEmailByEmployeIdJPQL(email, employeId);

	}
	public void deleteAllContratJPQL() {
         employeRepository.deleteAllContratJPQL();
	}
	
	public float getSalaireByEmployeIdJPQL(int employeId) {
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
				return (List<Employe>) employeRepository.findAll();
	}

}
