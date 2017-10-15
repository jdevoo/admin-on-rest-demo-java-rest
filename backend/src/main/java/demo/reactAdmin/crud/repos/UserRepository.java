package demo.reactAdmin.crud.repos;

import demo.reactAdmin.crud.entities.PlatformUser;
import reactAdmin.rest.repositories.BaseRepository;

public interface UserRepository extends BaseRepository<PlatformUser> {
    PlatformUser findOneByUsername(String username);
}
