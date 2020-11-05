 package tn.esprit.spring.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
		return employe.getId();
	}

	public String mettreAjourEmailByEmployeId(String email, int employeId) {
		l.info("Dans la fonction mettreAjourEmailByEmployeId");
		l.debug("Je m'apprete à recherhcer l'employe selon id");
		
		Optional<Employe> optional = employeRepository.findById(employeId);
		if(optional.isPresent()){
			Employe employe = optional.get();
			l.info("Fin de la recherche");
			l.debug("Je met à jour l'email");
			employe.setEmail(email);
			l.debug("Reenregistrement de l'employé");
			employeRepository.save(employe);
			l.info("Succcesss");
			return employe.getEmail();
		}
		
		return null; 

	}

	@Transactional	
	public void affecterEmployeADepartement(int employeId, int depId) {
		l.info("Dans la methode affecterEmployeADepartement");
		l.debug("Je cherche le departement");
		Optional<Departement> optionaldep = deptRepoistory.findById(depId);
		Optional<Employe> optionalempl = employeRepository.findById(employeId);
		if(optionaldep.isPresent() && optionalempl.isPresent()){
			
			Departement depManagedEntity = optionaldep.get();
			l.debug("Je cherche l'employe");
			Employe employeManagedEntity = optionalempl.get();
			l.info("Je vais rentrer dans le if ou le else");
			if(depManagedEntity.getEmployes() == null){
				l.info("Je suis dans le if");
				l.debug("Je crée une nouvelle liste");
				List<Employe> employes = new ArrayList<>();
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
		
	}
	@Transactional
	public void desaffecterEmployeDuDepartement(int employeId, int depId)
	{
		l.info("Dans la methode desaffecterEmployeDuDepartement");
		l.debug("Je cherche le departement");
		Optional<Departement> optionaldep = deptRepoistory.findById(depId);
		if(optionaldep.isPresent()){
		Departement dep = optionaldep.get();
		l.debug("Je compte le nombre d'etudiants");
		int employeNb = dep.getEmployes().size();
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
	}

	public int ajouterContrat(Contrat contrat) {
		contratRepoistory.save(contrat);
		return contrat.getReference();
	}

	public void affecterContratAEmploye(int contratId, int employeId) {
		
		Optional<Contrat> optcont= contratRepoistory.findById(contratId);
		Optional<Employe> optemp = employeRepository.findById(employeId);
		if(optcont.isPresent() && optemp.isPresent()){
			Contrat contratManagedEntity = optcont.get();
			Employe employeManagedEntity = optemp.get();
		contratManagedEntity.setEmploye(employeManagedEntity);
		contratRepoistory.save(contratManagedEntity);
		
		}
		
	}

	public String getEmployePrenomById(int employeId) {
		l.info("Dans la methode getEmployePrenomById");
		l.debug("Je recherhe l'employé selon son id");
		Optional <Employe> opt = employeRepository.findById(employeId);
		if(opt.isPresent()){
		Employe employeManagedEntity = opt.get();
		l.info("Emplooyé recupéré");
		return employeManagedEntity.getPrenom();}
		else{
			return null;
		}
		
	}
	public void deleteEmployeById(int employeId)
	{
		l.info("Dans la methode deleteEmployeById");
		l.debug("Je recherhe l'employé selon son id");
		
		Optional <Employe> opt = employeRepository.findById(employeId);
		if(opt.isPresent()){
		Employe employe = opt.get();
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
		l.info("Out of delet EmployeId");}
		
		else{
			
			l.info("Aucune suppression, id inexistant");
			l.error("Erreur");
		}
	}

	public void deleteContratById(int contratId) {
		Optional <Contrat> opt = contratRepoistory.findById(contratId);
		if(opt.isPresent()){
		Contrat contratManagedEntity = opt.get();
		contratRepoistory.delete(contratManagedEntity);}
		

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
