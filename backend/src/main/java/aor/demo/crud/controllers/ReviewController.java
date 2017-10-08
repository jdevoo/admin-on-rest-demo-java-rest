package aor.demo.crud.controllers;


import aor.demo.crud.entities.Review;
import aor.demo.crud.repos.AORSpecifications;
import aor.demo.crud.repos.ReviewRepository;
import aor.demo.crud.utils.ApiUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
public class ReviewController {
    @Autowired
    private ReviewRepository repo;

    @Autowired
    private AORSpecifications<Review> specification;

    @Autowired
    private ApiUtils utils;

    @RequestMapping(value = "reviews", method = RequestMethod.POST)
    public Review create(@RequestBody Review review) {
        return repo.save(review);
    }

    @RequestMapping(value = "reviews/{id}", method = RequestMethod.PUT)
    public Review update(@RequestBody Review review, @PathVariable int id) {
        review.id = id;
        return repo.save(review);
    }

    @RequestMapping(value = "reviews/{id}/published/{value}", method = RequestMethod.POST)
    public Review publishedUpdate(@PathVariable int id, @PathVariable boolean value) {
        Review review = repo.findOne(id);
        review.published = value;
        return repo.save(review);
    }


    @RequestMapping(value = "reviews/{id}", method = RequestMethod.GET)
    public Review getById(@PathVariable int id) {
        return repo.findOne(id);
    }

    @RequestMapping(value = "reviews", method = RequestMethod.GET)
    public Iterable<Review> filterBy(
            @RequestParam(required = false, name = "filter") String filterStr,
            @RequestParam(required = false, name = "range") String rangeStr, @RequestParam(required = false, name="sort") String sortStr) {
        return utils.filterByHelper(repo, specification, filterStr, rangeStr, sortStr);
    }
}
