package tn.esprit.spring.services;

import org.junit.Assert;
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
		  
		Employe emp = new Employe("Employe 1", "Test", "eeeeeeeeeee",true,Role.TECHNICIEN);
		Assert.assertNotNull(ie.ajouterEmploye(emp));
	}
	
	
	@Test
	public void testGetEmployePrenomById(){
		
		ie.getEmployePrenomById(1);
	}
	
	@Test
	public void testMettreAjourEmailByEmployeId(){
		
		Assert.assertEquals("msan", ie.mettreAjourEmailByEmployeId("msan", 7) );
	}
	
	
	@Test
	public void testGetAllEmployes(){
		Assert.assertEquals(9, ie.getAllEmployes().size());
	}
	
	

	
}
