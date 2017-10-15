package reactAdmin.demo.crud.controllers;


import reactAdmin.demo.crud.entities.Product;
import reactAdmin.demo.crud.repos.AORSpecifications;
import reactAdmin.demo.crud.repos.ProductRepository;
import reactAdmin.demo.crud.utils.ApiUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
public class ProductController {
    @Autowired
    private ProductRepository repo;

    @Autowired
    private AORSpecifications<Product> specification;

    @Autowired
    private ApiUtils utils;

    @RequestMapping(value = "products", method = RequestMethod.POST)
    public Product create(@RequestBody Product product) {
        return repo.save(product);
    }

    @RequestMapping(value = "products/{id}", method = RequestMethod.PUT)
    public Product update(@RequestBody Product product, @PathVariable int id) {
        product.id = id;
        return repo.save(product);
    }

    @RequestMapping(value = "products/{id}/published/{value}", method = RequestMethod.POST)
    public Product publishedUpdate(@PathVariable int id, @PathVariable boolean value) {
        Product product = repo.findOne(id);
        product.published = value;
        return repo.save(product);
    }


    @RequestMapping(value = "products/{id}", method = RequestMethod.GET)
    public Product getById(@PathVariable int id) {
        return repo.findOne(id);
    }

    @RequestMapping(value = "products", method = RequestMethod.GET)
    public Iterable<Product> filterBy(
            @RequestParam(required = false, name = "filter") String filterStr,
            @RequestParam(required = false, name = "range") String rangeStr, @RequestParam(required = false, name="sort") String sortStr) {
        return utils.filterByHelper(repo, specification, filterStr, rangeStr, sortStr);
    }
}
