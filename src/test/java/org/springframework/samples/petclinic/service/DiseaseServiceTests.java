
package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import java.util.Collection;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Disease;
import org.springframework.samples.petclinic.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class DiseaseServiceTests{


	public Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}
	
	@Autowired
	protected DiseaseService diseaseService;

	
	@Test
	void shouldFindDiseaseById() {
		assertThat(this.diseaseService.findDiseaseById(1).getId()).isEqualTo(1);

	}
	@ParameterizedTest
	@ValueSource(ints = {-10,-40} )
	void shouldFindDiseaseByIdNegative(int argument){
		Disease disease = this.diseaseService.findDiseaseById(argument);
		Assertions.assertThrows(NullPointerException.class,()->{disease.getClass();} );
	}

	@Test
	void shouldFindDiseaseAll() {

		assertThat(EntityUtils.getById(this.diseaseService.findAll(), Disease.class, 1).getCure()).isEqualTo("malisimo de la muerte");
		assertThat(EntityUtils.getById(this.diseaseService.findAll(), Disease.class, 4).getSeverity()).isEqualTo("MEDIUM");


	}
	
	public Disease Insert() {
		final Disease disease = new Disease();
		final Pet pet = new Pet();
		pet.setId(1);
		disease.setSeverity("LOW");
		disease.setCure("cure");
		disease.setSymptoms("esta mal");
		disease.setPet(pet);
		return disease;
	}
	
	@Test
	void shouldInsertDisease() {
		final Disease disease = Insert();
		this.diseaseService.saveDisease(disease);
		assertThat(disease.getId().longValue()).isNotEqualTo(0);
		assertThat(this.diseaseService.findDiseaseById(1).getId().longValue()).isNotEqualTo(this.diseaseService.findAll().size() + 1);
	}
	@Test
	void shouldInsertNegativeDisease(){
		
		final Disease d = Insert();
		d.setSeverity("");
		Validator validator = createValidator();
		Set<ConstraintViolation<Disease>> constraintViolations =  validator.validate(d); 
		assertThat(constraintViolations.size()).isEqualTo(1); 
		ConstraintViolation<Disease> violation =   constraintViolations.iterator().next();
		assertThat(violation.getPropertyPath().toString()).isEqualTo("severity"); 
		assertThat(violation.getMessage()).isEqualTo("must match LOW|MEDIUM|HIGH") ; 
	}

	@Test
	@Transactional
	void shouldUpdateDisease() {
		String oldSymptoms = this.diseaseService.findDiseaseById(1).getSymptoms();
		String newSymptoms = oldSymptoms + "XXXXXXXX";

		this.diseaseService.findDiseaseById(1).setSymptoms(newSymptoms);
		this.diseaseService.saveDisease(this.diseaseService.findDiseaseById(1));
		assertThat(this.diseaseService.findDiseaseById(1).getSymptoms()).isEqualTo(newSymptoms);
		
	}

	@ParameterizedTest
	@ValueSource(ints = {1,4})
	void shoulThrowExceptionUpdateDisease(int id){
		Disease disease = this.diseaseService.findDiseaseById(id);
		String newSymptoms = null;
		disease.setSymptoms(newSymptoms);
		this.diseaseService.saveDisease(disease);
		Assertions.assertThrows(NullPointerException.class, () -> {disease.getSymptoms().isEmpty();});
		
	}

	@Test
	void shouldDeletetDisease() {
		Collection<Disease> found1 = this.diseaseService.findAll();
		this.diseaseService.delete(this.diseaseService.findDiseaseById(1));
		Collection<Disease> found2 = this.diseaseService.findAll();
		
		assertTrue(found2.size() < found1.size());


	}

	@ParameterizedTest
	@ValueSource(ints = {-16, -30})
	void shouldThrowExceptionDeleteDisease(int id){
		Disease disease = this.diseaseService.findDiseaseById(id);
		Assertions.assertThrows(InvalidDataAccessApiUsageException.class, () -> {this.diseaseService.delete(disease);});
	}
}
