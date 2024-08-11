package priyesh.Crowdsense2.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import priyesh.Crowdsense2.Models.Person;

@Repository
public interface IndividualRepository extends JpaRepository<Person, Integer> {

}
