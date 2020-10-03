package glints.test.errorController;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus
public class LoginRegisterHandler extends RuntimeException {
    public LoginRegisterHandler(String message) {
        super(message);
    }
}
