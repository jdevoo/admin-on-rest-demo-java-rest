package reactAdmin.demo.crud.repos;

import reactAdmin.demo.crud.entities.PlatformUser;
import reactAdmin.repositories.BaseRepository;

public interface UserRepository extends BaseRepository<PlatformUser> {
    PlatformUser findOneByUsername(String username);
}
