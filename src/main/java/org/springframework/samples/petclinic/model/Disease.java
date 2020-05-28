
package org.springframework.samples.petclinic.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "Diseases")
public class Disease extends BaseEntity {

	
    @NotEmpty
    @Column(name = "symptoms")
	private String  symptoms;

	
    @Pattern(regexp = "LOW|MEDIUM|HIGH",message="must match LOW|MEDIUM|HIGH")
    @Column(name = "severity")
    private String severity;
     	
    @NotEmpty
    @Column(name = "cure")
    private String  cure;

    
    @ManyToOne(fetch = FetchType.LAZY)
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

    @Override
	public String toString() {
		return "Disease [symptoms=" + symptoms + ", severity=" + severity + ", cure=" + cure + ", pet=" + pet + "]";
	}

	

}