package glints.test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import glints.test.errorController.LoginRegisterHandler;
import glints.test.errorController.UserCreatedHandler;
import glints.test.errorController.UserDeletedHandler;
import glints.test.errorController.UserNotFoundHandler;
import glints.test.model.User;
import glints.test.response.Ad;
import glints.test.response.GetId;
import glints.test.response.ListData;
import glints.test.response.MainData;
import glints.test.service.CommonService;
import glints.test.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
public class Controller {
    @Autowired
    UserService userService;

    @Autowired
    CommonService commonService;

    ObjectMapper mapper = new ObjectMapper()
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);


    @Consumes({MediaType.APPLICATION_JSON})
    @GetMapping(path = "/users")
    public JsonNode getList(@RequestParam(value = "page", required = false) Integer page,
                            @RequestParam(value = "delay", required = false) Integer delay) {
        JsonNode response = null;
        if (delay != null && delay > 0) {
            try {
                TimeUnit.SECONDS.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        else if (delay == null && (page == null || page <= 0)) {
            throw new UserNotFoundHandler("");
        }
        page = page == null || page <= 0 ? 1 : page;
        Page<User> pageUser = userService.findAll(page - 1);
        if(pageUser == null){
            throw new UserNotFoundHandler("");
        }

        Integer totalPage = pageUser.getTotalPages();
        Integer totalData = Integer.valueOf(pageUser.getTotalElements() + "");
        List<MainData> mainList = new ArrayList<>();
        Integer perPage = 6;

        Ad ad = new Ad();
        ad.setCompany("StatusCode Weekly");
        ad.setText("A weekly newsletter focusing on software development, infrastructure, the server, performance, and the stack end of things.");
        ad.setUrl("http://statuscode.org/");

        Iterator<User> it = pageUser.iterator();
        while (it.hasNext()) {
            User user = it.next();
            MainData mainData = new MainData();
            mainData.setAvatar(user.getAvatar());
            mainData.setEmail(user.getEmail());
            mainData.setFirst_name(user.getFirst_name());
            mainData.setLast_name(user.getLast_name());
            mainData.setId(user.getId());
            mainList.add(mainData);
        }
        ListData listData = new ListData();
        listData.setData(mainList);
        listData.setPage(page);
        listData.setAd(ad);
        listData.setPer_page(perPage);
        listData.setTotal(totalData);
        listData.setTotal_pages(totalPage);
        response = mapper.convertValue(listData, JsonNode.class);
        return response;
    }


    @Consumes({MediaType.APPLICATION_JSON})
    @GetMapping(path = "/users/{id}")
    public JsonNode getUser(@PathVariable("id") Integer id) {

        User user = userService.findById(id);
        if (user == null) {
            throw new UserNotFoundHandler("GetByID");
        } else {
            Ad ad = new Ad();
            ad.setCompany("StatusCode Weekly");
            ad.setText("A weekly newsletter focusing on software development, infrastructure, the server, performance, and the stack end of things.");
            ad.setUrl("http://statuscode.org/");

            MainData mainData = new MainData();
            mainData.setAvatar(user.getAvatar());
            mainData.setEmail(user.getEmail());
            mainData.setFirst_name(user.getFirst_name());
            mainData.setLast_name(user.getLast_name());
            mainData.setId(user.getId());

            GetId getId = new GetId();
            getId.setData(mainData);
            getId.setAd(ad);

            return mapper.convertValue(getId, JsonNode.class);
        }
    }

    @Consumes({MediaType.APPLICATION_JSON})
    @PostMapping(path = "/users")
    public JsonNode createUser(HttpServletRequest request) throws IOException {
        JsonNode inputNode = mapper.readTree(request.getInputStream());
        JsonNode response = null;
        String first_name = commonService.stringCheck(inputNode, "first_name");
        String last_name = commonService.stringCheck(inputNode, "last_name");
        String email = commonService.stringCheck(inputNode, "email");
        String avatar = "https://s3.amazonaws.com/uifaces/faces/twitter/russoedu/128.jpg";
        if (!first_name.isEmpty() && !last_name.isEmpty() && !email.isEmpty() && !avatar.isEmpty()) {
            User user = new User();
            user.setFirst_name(first_name);
            user.setLast_name(last_name);
            user.setEmail(email);
            user.setAvatar(avatar);
            if (userService.save(user) != null) {
                throw new UserCreatedHandler(user.getId() + "");
            }
        }
        return response;
    }

    @PutMapping(path = "/users/{id}")
    public JsonNode updateUserPut(@PathVariable("id") Integer id, @RequestBody User requestUserDetails) {
        User returnValue = userService.findById(id);
        returnValue.setAvatar(requestUserDetails.getAvatar());
        returnValue.setEmail(requestUserDetails.getEmail());
        returnValue.setFirst_name(requestUserDetails.getFirst_name());
        returnValue.setLast_name(requestUserDetails.getLast_name());

        User newUser = userService.save(returnValue);
        Date updatedDate = newUser.getUpdatedAt();
        String updatedDateString = updatedDate.toInstant().toString();
        JsonNode response = mapper.convertValue(newUser, JsonNode.class);

        ((com.fasterxml.jackson.databind.node.ObjectNode) response).remove("id");
        ((com.fasterxml.jackson.databind.node.ObjectNode) response).remove("createdAt");
        ((com.fasterxml.jackson.databind.node.ObjectNode) response).put("updatedAt", updatedDateString);
        return response;
    }

    @PatchMapping(path = "/users/{id}")
    public JsonNode updateUserPatch(@PathVariable("id") Integer id, @RequestBody User requestUserDetails) {
        User returnValue = userService.findById(id);

        String firstName = requestUserDetails.getFirst_name();
        String lastName = requestUserDetails.getLast_name();
        String email = requestUserDetails.getEmail();
        String avatar = requestUserDetails.getAvatar();

        returnValue.setFirst_name(firstName.isEmpty() ? returnValue.getFirst_name() : firstName);
        returnValue.setLast_name(lastName.isEmpty() ? returnValue.getLast_name() : lastName);
        returnValue.setEmail(email.isEmpty() ? returnValue.getEmail() : email);
        returnValue.setAvatar(avatar.isEmpty() ? returnValue.getAvatar() : avatar);

        User newUser = userService.save(returnValue);
        Date updatedDate = newUser.getUpdatedAt();
        String updatedDateString = updatedDate.toInstant().toString();
        JsonNode response = mapper.convertValue(newUser, JsonNode.class);

        ((com.fasterxml.jackson.databind.node.ObjectNode) response).remove("id");
        ((com.fasterxml.jackson.databind.node.ObjectNode) response).remove("createdAt");
        ((com.fasterxml.jackson.databind.node.ObjectNode) response).put("updatedAt", updatedDateString);
        return response;
    }

    @DeleteMapping(path = "/users/{id}")
    public JsonNode deleteUserPatch(@PathVariable("id") Integer id) {
        throw new UserDeletedHandler(id + "");
    }

    @Consumes({MediaType.APPLICATION_JSON})
    @PostMapping(path = {"/login", "/register"})
    public JsonNode register(HttpServletRequest request) throws IOException {
        JsonNode inputNode = mapper.readTree(request.getInputStream());
        String password = commonService.stringCheck(inputNode, "password");
        String email = commonService.stringCheck(inputNode, "email");
        String message = "false";
        if (!password.isEmpty() && !email.isEmpty()) {
            message = "true";
        } else if (password.isEmpty()) {
            message = "password";
        } else if (email.isEmpty()) {
            message = "email";
        }
        throw new LoginRegisterHandler(message);
    }


}
