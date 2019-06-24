package pl.tscript3r.photogram2.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.tscript3r.photogram2.domains.EmailConfirmation;
import pl.tscript3r.photogram2.domains.User;

import java.util.Optional;
import java.util.UUID;

public interface EmailConfirmationRepository extends CrudRepository<EmailConfirmation, Long> {

    Optional<EmailConfirmation> findByUser(User user);

    Boolean existsByToken(UUID token);

}
