package reactAdmin.demo.crud.repos;

import reactAdmin.demo.crud.entities.PlatformUser;

public interface UserRepository extends GenericRepository<PlatformUser> {
    PlatformUser findOneByUsername(String username);
}
