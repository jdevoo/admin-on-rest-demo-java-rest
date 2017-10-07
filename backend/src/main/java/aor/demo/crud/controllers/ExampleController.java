package aor.demo.crud.controllers;


import aor.demo.crud.entities.Example;
import aor.demo.crud.repos.AORSpecifications;
import aor.demo.crud.repos.ExampleRepository;
import aor.demo.crud.utils.ApiUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
public class ExampleController {
    @Autowired
    private ExampleRepository repo;

    @Autowired
    private AORSpecifications<Example> specification;

    @Autowired
    private ApiUtils utils;

    @RequestMapping(value = "examples", method = RequestMethod.POST)
    public Example create(@RequestBody Example example) {
        return repo.save(example);
    }

    @RequestMapping(value = "examples/{id}", method = RequestMethod.PUT)
    public Example update(@RequestBody Example example, @PathVariable int id) {
        example.id = id;
        return repo.save(example);
    }

    @RequestMapping(value = "examples/{id}/published/{value}", method = RequestMethod.POST)
    public Example publishedUpdate(@PathVariable int id, @PathVariable boolean value) {
        Example example = repo.findOne(id);
        example.published = value;
        return repo.save(example);
    }


    @RequestMapping(value = "examples/{id}", method = RequestMethod.GET)
    public Example getById(@PathVariable int id) {
        return repo.findOne(id);
    }

    @RequestMapping(value = "examples", method = RequestMethod.GET)
    public Iterable<Example> filterBy(
            @RequestParam(required = false, name = "filter") String filterStr,
            @RequestParam(required = false, name = "range") String rangeStr, @RequestParam(required = false, name="sort") String sortStr) {
        return utils.filterByHelper(repo, specification, filterStr, rangeStr, sortStr);
    }
}
