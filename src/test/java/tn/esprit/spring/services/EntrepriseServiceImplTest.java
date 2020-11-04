package tn.esprit.spring.services;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import tn.esprit.spring.entities.Entreprise;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EntrepriseServiceImplTest {

	@Autowired
	IEntrepriseService es;

	@Test
	public void testAjouterEntreprise() {
		Entreprise e = new Entreprise("SSII Consulting","Cite El Ghazela");
		Entreprise eAdded = es.ajouterEntreprise(e);
		assertEquals(e.getName(), eAdded.getName());
	}

	/*@Test
	public void retreiveAllEntreprises() {
		List<Entreprise> listEntreprises = es.retreiveAllEntreprises();
		//There is 1 entreprise in DB
		assertEquals(12,listEntreprises.size());
	}*/

	@Test
	public void testGetEntrepriseById() {
		Entreprise entrepriseRetrieved = es.getEntrepriseById(2);
		assertEquals(2L, entrepriseRetrieved.getId());
	}

	@Test
	public void updateEntreprise() {
		Entreprise e = new Entreprise(1, "SSII Consulting","Ariana");
		Entreprise eUpdated = es.updateEntreprise(e);
		assertEquals(e.getName(), eUpdated.getName());
	}

	@Test
	public void testDeleteEntrepriseById() {
		es.deleteEntrepriseById(1);
	}

}
