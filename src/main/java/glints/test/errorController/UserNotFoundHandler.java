package glints.test.errorController;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus
public class UserNotFoundHandler extends RuntimeException {

    public UserNotFoundHandler(String message) {
        super(message);
    }

}
