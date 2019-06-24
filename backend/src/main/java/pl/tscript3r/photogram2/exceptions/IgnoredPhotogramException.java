package pl.tscript3r.photogram2.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONTINUE)
public class IgnoredPhotogramException extends PhotogramException {

    public IgnoredPhotogramException(String message) {
        super(message);
    }

}