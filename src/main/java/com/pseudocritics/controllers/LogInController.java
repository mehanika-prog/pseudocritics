package com.pseudocritics.controllers;

import com.pseudocritics.BCrypt;
import com.pseudocritics.database.SessionRepository;
import com.pseudocritics.database.UserRepository;
import com.pseudocritics.domain.Session;
import com.pseudocritics.domain.User;
import com.pseudocritics.domain.dto.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.regex.Pattern;

@Controller
@CrossOrigin
@RequestMapping(path="/")
public class LogInController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @PostMapping(path = "/login")
    public @ResponseBody ResponseEntity<String> login(
            @RequestBody(required = false) LoginRequest user,
            @RequestHeader(value = "uuid", required = false) String sessionUuid) {

        if (sessionUuid != null) {

            boolean uuidIsValid = Pattern.matches(
                    "^(([a-z0-9]{8})-([a-z0-9]{4})-([a-z0-9]{4})-([a-z0-9]{4})-([a-z0-9]{12}))$",
                    sessionUuid
            );

            if (uuidIsValid) {

                Session dbSession = sessionRepository.getByUuid(sessionUuid);

                if (dbSession != null) {

                    OffsetDateTime dbSessionOffsetDateTime = dbSession.getOffsetDateTime();

                    if (dbSessionOffsetDateTime.isAfter(OffsetDateTime.now())) {

                        return new ResponseEntity<String>(sessionUuid, HttpStatus.OK);

                    } else {

                        sessionRepository.delete(dbSession);

                    }

                }

            }

        }

        if (user == null) { return new ResponseEntity<String>("Wrong request format!", HttpStatus.BAD_REQUEST); }

        String emailOrUserName = user.getEmailOrUserName();
        String password = user.getPassword();
        boolean rememberMe = user.isRememberMe();
        boolean isMobile = user.isMobile();

        if (emailOrUserName == null || password == null) {

            return new ResponseEntity<String>("Wrong request format!", HttpStatus.BAD_REQUEST);

        }

        User dbUser = userRepository.getUserByEmailOrUserName(emailOrUserName);

        if (dbUser == null) return new ResponseEntity<String>("User not found!", HttpStatus.BAD_REQUEST);

        boolean passwordMatches = BCrypt.checkpw(password, dbUser.getPassword());

        if (!passwordMatches) return new ResponseEntity<String>("Bad login credentials!", HttpStatus.BAD_REQUEST);

        Session newSession = new Session();

        newSession.setUser(dbUser);
        newSession.setUuid(UUID.randomUUID().toString());

        OffsetDateTime dateTime = OffsetDateTime.now();

        if (isMobile) dateTime = dateTime.plusYears(1);
        else if (rememberMe) dateTime = dateTime.plusWeeks(1);
        else dateTime = dateTime.plusHours(1);

        newSession.setOffsetDateTime(dateTime);

        sessionRepository.save(newSession);

        return new ResponseEntity<String>(newSession.getUuid(), HttpStatus.OK);

    }

}
