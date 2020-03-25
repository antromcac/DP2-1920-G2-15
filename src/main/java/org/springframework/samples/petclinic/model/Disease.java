
package org.springframework.samples.petclinic.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


@Entity
@Table(name = "Diseases")
public class Disease extends BaseEntity {

	
    @NotEmpty
    @Column(name = "symptoms")
	private String  symptoms;

	
    @NotEmpty
    @Pattern(regexp = "LOW|MEDIUM|HIGH")
    @Column(name = "severity")
    private String severity;
     	
    @NotEmpty
    @Column(name = "cure")
    private String  cure;

    
    @ManyToOne
    private Pet pet;


    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getCure() {
        return cure;
    }

    public void setCure(String cure) {
        this.cure = cure;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

	

}