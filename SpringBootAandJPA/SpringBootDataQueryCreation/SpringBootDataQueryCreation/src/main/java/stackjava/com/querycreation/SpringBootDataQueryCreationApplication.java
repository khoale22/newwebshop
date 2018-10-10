package stackjava.com.querycreation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import stackjava.com.querycreation.entities.Customer;
import stackjava.com.querycreation.repository.CustomerRepository;

@SpringBootApplication
@Component
@EnableJpaRepositories
@ComponentScan("stackjava.com")
public class SpringBootDataQueryCreationApplication implements CommandLineRunner {

	@Autowired
	private CustomerRepository customerRepository;

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(SpringBootDataQueryCreationApplication.class);
		app.run(args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("__________Reset and init data________________");
		customerRepository.deleteAll();
		customerRepository.save(new Customer("Dead pool", "Marvel"));
		customerRepository.save(new Customer("Thor", "ragnarok"));
		customerRepository.save(new Customer("Iron Man", "Marvel"));
		customerRepository.save(new Customer("Hulk", "Marvel"));
		customerRepository.save(new Customer("Hawkeye", "Marven"));
		customerRepository.save(new Customer("Thanos", "Titan"));
		customerRepository.save(new Customer("Batman", "DC"));

		System.out.println("__________Demo find all order by name desc________________");
		List<Customer> listCustomer1 = customerRepository.findAllOrderByNameDesc234();
		for (Customer customer : listCustomer1) {
			System.out.println(customer);
		}
		System.out.println("__________Demo find find by name like 'th'________________");
		List<Customer> listCustomer2 = customerRepository.findByNameLike("%th%");
		for (Customer customer : listCustomer2) {
			System.out.println(customer);
		}
		System.out.println("__________Demo find by name NOT ________________");
		List<Customer> listCustomer3 = customerRepository.findByNameNot("Thor");
		for (Customer customer : listCustomer3) {
			System.out.println(customer);
		}
	}

}
