package demo.reactAdmin.crud.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactAdmin.rest.controllers.BaseController;
import demo.reactAdmin.crud.entities.Customer;
import demo.reactAdmin.crud.repos.CustomerRepository;

@RestController
@RequestMapping("api/v1")
public class CustomerController extends BaseController<Customer> {
    @Autowired
    private CustomerRepository repo;

    @RequestMapping(value = "customers", method = RequestMethod.POST)
    public Customer create(@RequestBody Customer customer) {
        return repo.save(customer);
    }

    @RequestMapping(value = "customers/{id}", method = RequestMethod.PUT)
    public Customer update(@RequestBody Customer customer, @PathVariable int id) {
        customer.id = id;
        return repo.save(customer);
    }

    @RequestMapping(value = "customers/{id}/published/{value}", method = RequestMethod.POST)
    public Customer publishedUpdate(@PathVariable int id, @PathVariable boolean value) {
        Customer customer = repo.findOne(id);
        customer.published = value;
        return repo.save(customer);
    }


    @RequestMapping(value = "customers/{id}", method = RequestMethod.GET)
    public Customer getById(@PathVariable int id) {
        return repo.findOne(id);
    }

    @RequestMapping(value = "customers", method = RequestMethod.GET)
    public Iterable<Customer> filterBy(
            @RequestParam(required = false, name = "filter") String filterStr,
            @RequestParam(required = false, name = "range") String rangeStr, @RequestParam(required = false, name="sort") String sortStr) {
        return super.filterBy(filterStr,rangeStr, sortStr, repo);
    }
}
