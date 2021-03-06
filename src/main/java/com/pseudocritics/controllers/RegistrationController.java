package com.pseudocritics.controllers;

import com.pseudocritics.BCrypt;
import com.pseudocritics.domain.User;
import com.pseudocritics.database.UserRepository;
import com.pseudocritics.domain.dto.CheckEmailRequest;
import com.pseudocritics.domain.dto.CheckUserNameRequest;
import com.pseudocritics.domain.dto.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Pattern;

@Controller
@CrossOrigin
@RequestMapping(path="/")
public class RegistrationController {

    @Value("${com.pseudocritics.BCryptSalt}")
    private String salt;

    @Autowired
    private UserRepository userRepository;

    @PostMapping(path = "/register")
    public @ResponseBody ResponseEntity<String> register(@RequestBody(required = false) RegisterRequest user){

        if (user == null) { return new ResponseEntity<String>("Wrong request format!", HttpStatus.BAD_REQUEST); }

        String email = user.getEmail();
        String userName = user.getUserName();
        String password = user.getPassword();
        String password2 = user.getPassword2();

        if (email == null || userName == null || password == null || password2 == null) {

            return new ResponseEntity<String>("Wrong request format!", HttpStatus.BAD_REQUEST);

        }

        boolean emailIsValid = Pattern.matches(
                "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$",
                email
        );

        if (!emailIsValid) { return new ResponseEntity<String>("Wrong email format!", HttpStatus.BAD_REQUEST); }

        if (!password.equals(password2)) { return new ResponseEntity<String>("Passwords does not match!", HttpStatus.BAD_REQUEST); }

        boolean userExist = userRepository.existsByEmailOrUserName(email, userName);

        if (userExist) { return new ResponseEntity<String>("User with this credentials already exists!", HttpStatus.BAD_REQUEST); }

        User newUser = new User();

        newUser.setUserName(userName);
        newUser.setPassword(BCrypt.hashpw(password, salt));
        newUser.setEmail(email);

        userRepository.save(newUser);

        return new ResponseEntity<String>(HttpStatus.CREATED);

    }

    @PostMapping(path = "/register/checkUserName")
    public @ResponseBody ResponseEntity<String> checkUserName(@RequestBody(required = false) CheckUserNameRequest req) {

        if (req == null) { return new ResponseEntity<String>("Wrong request format!", HttpStatus.BAD_REQUEST); }

        boolean exists = userRepository.existsByUserName(req.getUserName());

        if (exists) return new ResponseEntity<String>("User with this username exists!", HttpStatus.BAD_REQUEST);

        return new ResponseEntity<String>("User with this username does not exists!", HttpStatus.OK);

    }



    @PostMapping(path = "/register/checkEmail")
    public @ResponseBody ResponseEntity<String> checkEmail(@RequestBody(required = false) CheckEmailRequest req) {

        if (req == null) { return new ResponseEntity<String>("Wrong request format!", HttpStatus.BAD_REQUEST); }

        boolean emailIsValid = Pattern.matches(
                "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$",
                req.getEmail()
        );

        if (!emailIsValid) { return new ResponseEntity<String>("Wrong email format!", HttpStatus.BAD_REQUEST); }

        boolean exists = userRepository.existsByEmail(req.getEmail());

        if (exists) return new ResponseEntity<String>("User with this email exists!", HttpStatus.BAD_REQUEST);

        return new ResponseEntity<String>("User with this email does not exists!", HttpStatus.OK);

    }

}
