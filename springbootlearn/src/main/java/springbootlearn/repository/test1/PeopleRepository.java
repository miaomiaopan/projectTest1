package springbootlearn.repository.test1;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import springbootlearn.entity.test1.People;

@Repository
public interface PeopleRepository extends JpaRepository<People, Long>,PeopleRepositoryCustom{

}
