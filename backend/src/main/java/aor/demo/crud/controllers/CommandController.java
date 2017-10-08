package aor.demo.crud.controllers;


import aor.demo.crud.entities.Command;
import aor.demo.crud.entities.QuantifiedProduct;
import aor.demo.crud.repos.AORSpecifications;
import aor.demo.crud.repos.CommandRepository;
import aor.demo.crud.repos.ProductRepository;
import aor.demo.crud.utils.ApiUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
public class CommandController {
    @Autowired
    private CommandRepository repo;

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private AORSpecifications<Command> specification;

    @Autowired
    private ApiUtils utils;

    @RequestMapping(value = "commands", method = RequestMethod.POST)
    public Command create(@RequestBody Command command) {
        for (QuantifiedProduct qp: command.basket) {
            qp.product = productRepo.findOne(qp.productId);
        }
        return repo.save(command);
    }

    @RequestMapping(value = "commands/{id}", method = RequestMethod.PUT)
    public Command update(@RequestBody Command command, @PathVariable int id) {
        command.id = id;
        for (QuantifiedProduct qp: command.basket) {
            qp.product = productRepo.findOne(qp.productId);
        }
        return repo.save(command);
    }

    @RequestMapping(value = "commands/{id}/published/{value}", method = RequestMethod.POST)
    public Command publishedUpdate(@PathVariable int id, @PathVariable boolean value) {
        Command command = repo.findOne(id);
        command.published = value;
        return repo.save(command);
    }

    @RequestMapping(value = "commands/{id}", method = RequestMethod.GET)
    public Command getById(@PathVariable int id) {
        return repo.findOne(id);
    }

    @RequestMapping(value = "commands", method = RequestMethod.GET)
    public Iterable<Command> filterBy(
            @RequestParam(required = false, name = "filter") String filterStr,
            @RequestParam(required = false, name = "range") String rangeStr, @RequestParam(required = false, name="sort") String sortStr) {
        return utils.filterByHelper(repo, specification, filterStr, rangeStr, sortStr);
    }
}