package demo.reactAdmin.crud.controllers;

import demo.reactAdmin.crud.entities.PlatformUser;
import demo.reactAdmin.crud.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import reactAdmin.rest.entities.FilterWrapper;
import reactAdmin.rest.services.FilterService;

@RestController
@RequestMapping("api/v1")
public class UserController {

    @Autowired
    private FilterService<PlatformUser> filterService;

    @Autowired
    private UserRepository repo;

    @RequestMapping(value = "current-user", method = RequestMethod.GET)
    public PlatformUser getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); //get logged in username
        return repo.findOneByUsername(username);
    }

    @RequestMapping(value = "users/{id}", method = RequestMethod.GET)
    public PlatformUser getById(@PathVariable int id) {
        return repo.findOne(id);
    }


    @RequestMapping(value = "users/{id}/published/{value}", method = RequestMethod.POST)
    public void publishedUpdate(@PathVariable int id, @PathVariable boolean value) {
        PlatformUser user = repo.findOne(id);
        user.published = value;
        repo.save(user);
    }

    @RequestMapping(value = "users", method = RequestMethod.GET)
    public Iterable<PlatformUser> filterBy(
            @RequestParam(required = false, name = "filter") String filterStr,
            @RequestParam(required = false, name = "range") String rangeStr, @RequestParam(required = false, name="sort") String sortStr) {
        FilterWrapper wrapper = filterService.extractFilterWrapper(filterStr, rangeStr, sortStr);
        return filterService.filterBy(wrapper, repo);
    }
}
