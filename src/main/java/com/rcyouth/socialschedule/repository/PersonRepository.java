package com.rcyouth.socialschedule.repository;

import com.rcyouth.socialschedule.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
