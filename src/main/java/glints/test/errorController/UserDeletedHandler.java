package glints.test.errorController;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus
public class UserDeletedHandler extends RuntimeException {

    public UserDeletedHandler(String message) {
        super(message);
    }

}
