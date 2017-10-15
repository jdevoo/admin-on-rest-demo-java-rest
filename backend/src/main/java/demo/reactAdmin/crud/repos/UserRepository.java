package demo.reactAdmin.crud.repos;

import demo.reactAdmin.crud.entities.PlatformUser;
import reactAdmin.repositories.BaseRepository;

public interface UserRepository extends BaseRepository<PlatformUser> {
    PlatformUser findOneByUsername(String username);
}
