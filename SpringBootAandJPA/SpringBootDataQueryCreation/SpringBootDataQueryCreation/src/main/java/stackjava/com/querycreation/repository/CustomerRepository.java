package stackjava.com.querycreation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import stackjava.com.querycreation.entities.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    @Query("SELECT e FROM Customer e ORDER BY e.name DESC")
    List<Customer> findAllOrderByNameDesc234();
   /* List<Customer> findAllByOrderByNameDesc();*/
    
    List<Customer> findByName(String name);
    
    List<Customer> findByNameAndAddress(String name, String address);
    
    List<Customer> findByNameLike(String name);
    
    List<Customer> findByIdIn(List<Integer> ids);
    
    List<Customer> findByIdLessThan(int index);

    List<Customer> findByNameNot(String name);
}
