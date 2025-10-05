package com.assecor.assessment.dto;

import com.assecor.assessment.model.Color;
import com.assecor.assessment.model.Person;
import org.springframework.stereotype.Component;

@Component
public class PersonMapper {
    
    public PersonDto toDto(Person person) {
        if (person == null) {
            return null;
        }
        
        return new PersonDto(
                person.getId(),
                person.getName(),
                person.getLastname(),
                person.getZipcode(),
                person.getCity(),
                person.getColor() != null ? person.getColor().getName() : null
        );
    }
    
    public Person toEntity(PersonDto dto) {
        if (dto == null) {
            return null;
        }
        
        Person person = new Person();
        person.setId(dto.getId());
        person.setName(dto.getName());
        person.setLastname(dto.getLastname());
        person.setZipcode(dto.getZipcode());
        person.setCity(dto.getCity());
        
        // Create a Color entity with just the name - the service will handle finding the actual Color
        if (dto.getColor() != null) {
            Color color = new Color();
            color.setName(dto.getColor());
            person.setColor(color);
        }
        
        return person;
    }
    
    public Person toEntity(PersonInputDto dto) {
        if (dto == null) {
            return null;
        }
        
        Person person = new Person();
        person.setId(dto.getId());
        person.setName(dto.getName());
        person.setLastname(dto.getLastname());
        person.setZipcode(dto.getZipcode());
        person.setCity(dto.getCity());
        
        // Create a Color entity with just the name - the service will handle finding the actual Color
        if (dto.getColor() != null) {
            Color color = new Color();
            color.setName(dto.getColor());
            person.setColor(color);
        }
        
        return person;
    }
}
