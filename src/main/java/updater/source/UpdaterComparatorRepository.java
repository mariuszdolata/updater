package updater.source;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import updater.structure.Company;

public class UpdaterComparatorRepository {

	/**
	 * Lista danych firmowych (lub osobowych) przeznaczona do aktualizacji bazy
	 */
	private List<Company> companiesToUpdate;
	/**
	 * Lista firm pobrana z bazy danych (tylko nip, regon, krs, nazwa, miasto, typ)
	 */
	private List<Company> companiesFromDB;
	private EntityManagerFactory entityManagerFactory;
	
	
	
	public List<Company> getCompaniesToUpdate() {
		return companiesToUpdate;
	}



	public void setCompaniesToUpdate(List<Company> companiesToUpdate) {
		this.companiesToUpdate = companiesToUpdate;
	}



	public List<Company> getCompaniesFromDB() {
		return companiesFromDB;
	}



	public void setCompaniesFromDB(List<Company> companiesFromDB) {
		this.companiesFromDB = companiesFromDB;
	}



	public EntityManagerFactory getEntityManagerFactory() {
		return entityManagerFactory;
	}



	public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
		this.entityManagerFactory = entityManagerFactory;
	}



	public UpdaterComparatorRepository(EntityManagerFactory emf, List<Company> companiesToUpdate) {
		this.setEntityManagerFactory(emf);
		this.setCompaniesToUpdate(companiesToUpdate);
	}
	
	/**
	 * Metoda pobierajaca wszystkie firmy z bazy danych (tylko nip, regon, krs, nazwe i miasto) w celach porownawczych
	 * @return
	 */
	public List<Company> getAllCompaniesFromDB(){
		List<Company> allCompanies = new ArrayList<Company>();
		EntityManager em = this.getEntityManagerFactory().createEntityManager();
		
		
		em.close();
		return allCompanies;
	}
	
	
}
