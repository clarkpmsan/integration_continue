package tn.esprit.spring.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import tn.esprit.spring.entities.Employe;
import tn.esprit.spring.entities.Role;


@RunWith(SpringRunner.class)
@SpringBootTest
public class EmployeServiceImplTest {

	@Autowired
	IEmployeService ie ;
	
	@Test
	public void testAjouterEmploye(){
		  
		Employe emp = new Employe("Moi", "Toi", "eeeeeeeeeee",true,Role.TECHNICIEN);
		System.out.println(emp);
		ie.ajouterEmploye(emp);
	}
	
}
