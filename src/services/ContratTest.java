package tn.esprit.spring.services;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import tn.esprit.spring.entities.Contrat;
import tn.esprit.spring.entities.Employe;
import tn.esprit.spring.entities.Role;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ContratTest {
	
	@Autowired
	IEmployeService ie ;
	
	@Test
	public void testAjouterContrat(){
		Contrat c = new Contrat(new Date(), "domicilde", (float) 7.5 );
		ie.ajouterContrat(c);
	}
	
	@Test
	public void testaffecterContratAEmploye(){
		
		ie.affecterContratAEmploye(15,1);
	}
	
	@Test
	public void testdeleteContratById(){
		ie.deleteContratById(15);
	}
	
	@Test
	public void testdeleteAllContratJPQL(){
		ie.deleteAllContratJPQL();
	}
	
}
