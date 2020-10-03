package glints.test.errorController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import glints.test.model.User;
import glints.test.service.CommonService;
import glints.test.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.UUID;

@ControllerAdvice
public class ErrorHandler {

    @Autowired
    UserService userService;

    @Autowired
    CommonService commonService;

    @ExceptionHandler(UserNotFoundHandler.class)
    public ResponseEntity<JsonNode> customHandleNotFound(Exception ex, WebRequest request) {
        JsonNode errNode = new ObjectMapper().createObjectNode();
        if (ex.getMessage().equals("GetByID")) {
            return new ResponseEntity<>(errNode, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(errNode, HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @ExceptionHandler(UserCreatedHandler.class)
    public ResponseEntity<JsonNode> createUser(Exception ex, WebRequest request) {
        JsonNode responseNode = new ObjectMapper().createObjectNode();
        Integer userId = Integer.valueOf(ex.getMessage());
        User user = userService.findById(userId);

        Date createdDate = user.getCreatedAt();
        String createdDateString = createdDate.toInstant().toString();
        System.out.println(createdDateString);
        if (user != null) {
            responseNode = new ObjectMapper().convertValue(user, JsonNode.class);
            ((ObjectNode) responseNode).put("createdAt", createdDateString);
            ((ObjectNode) responseNode).remove("updatedAt");

        }
        return new ResponseEntity<>(responseNode, HttpStatus.CREATED);
    }

    @ExceptionHandler(UserDeletedHandler.class)
    public ResponseEntity<JsonNode> deleteUser(Exception ex, WebRequest request) {
        JsonNode responseNode = null;
        Integer userId = Integer.valueOf(ex.getMessage());
        if (userService.delete(userId)) {
            responseNode = new ObjectMapper().createObjectNode();
            return new ResponseEntity<>(responseNode, HttpStatus.NO_CONTENT);
        } else return new ResponseEntity<>(responseNode, HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(LoginRegisterHandler.class)
    public ResponseEntity<JsonNode> register(Exception ex, WebRequest request) {
        JsonNode responseNode = new ObjectMapper().createObjectNode();
        if (Boolean.valueOf(ex.getMessage())) {
            ((ObjectNode) responseNode).put("id", commonService.generateIneteger(2));
            ((ObjectNode) responseNode).put("token", UUID.randomUUID().toString().replace("-", ""));
            return new ResponseEntity<>(responseNode, HttpStatus.OK);
        } else {
            String getMessage = ex.getMessage().equals("false")?"you :D ":ex.getMessage();
            ((ObjectNode) responseNode).put("error", "Missing "+getMessage);
            return new ResponseEntity<>(responseNode, HttpStatus.BAD_REQUEST);
        }

    }


}
