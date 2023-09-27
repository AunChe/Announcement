package group.int221project.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class AnnounceNotFound extends RuntimeException {
    public AnnounceNotFound(Integer id) {
        super("Announcement id " + id + " does not exist");
    }
}
