package aor.demo.crud.controllers;


import aor.demo.crud.ExampleEntity;
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
    private AORSpecifications<ExampleEntity> specification;

    @Autowired
    private ApiUtils utils;

    @RequestMapping(value = "examples", method = RequestMethod.POST)
    public ExampleEntity create(@RequestBody ExampleEntity example) {
        return repo.save(example);
    }

    @RequestMapping(value = "examples/{id}", method = RequestMethod.PUT)
    public ExampleEntity update(@RequestBody ExampleEntity example, @PathVariable int id) {
        example.id = id;
        return repo.save(example);
    }

    @RequestMapping(value = "examples/{id}/published/{value}", method = RequestMethod.POST)
    public ExampleEntity publishedUpdate(@PathVariable int id, @PathVariable boolean value) {
        ExampleEntity example = repo.findOne(id);
        example.published = value;
        return repo.save(example);
    }


    @RequestMapping(value = "examples/{id}", method = RequestMethod.GET)
    public ExampleEntity getById(@PathVariable int id) {
        return repo.findOne(id);
    }

    @RequestMapping(value = "examples", method = RequestMethod.GET)
    public Iterable<ExampleEntity> filterBy(
            @RequestParam(required = false, name = "filter") String filterStr,
            @RequestParam(required = false, name = "range") String rangeStr, @RequestParam(required = false, name="sort") String sortStr) {
        return utils.filterByHelper(repo, specification, filterStr, rangeStr, sortStr);
    }
}
