package glints.test.errorController;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus
public class UserCreatedHandler extends RuntimeException {

    public UserCreatedHandler(String message) {
        super(message);
    }

}
