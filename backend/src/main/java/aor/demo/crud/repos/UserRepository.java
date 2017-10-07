package aor.demo.crud.repos;

import aor.demo.crud.PlatformUser;

public interface UserRepository extends GenericRepository<PlatformUser> {
    PlatformUser findOneByEmail(String email);
}
