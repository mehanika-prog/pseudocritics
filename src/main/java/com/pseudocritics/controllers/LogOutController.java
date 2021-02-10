package com.pseudocritics.controllers;

import com.pseudocritics.database.SessionRepository;
import com.pseudocritics.domain.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Pattern;

@Controller
@CrossOrigin
@RequestMapping(path="/")
public class LogOutController {

    @Autowired
    private SessionRepository sessionRepository;

    @DeleteMapping(path = "/logout")
    public @ResponseBody ResponseEntity<String> logout(
            @RequestHeader(value = "uuid", required = false) String sessionUuid) {

        if (sessionUuid == null) return new ResponseEntity<String>("Uuid header dose not exist!", HttpStatus.BAD_REQUEST);

        boolean uuidIsValid = Pattern.matches(
                "^(([a-z0-9]{8})-([a-z0-9]{4})-([a-z0-9]{4})-([a-z0-9]{4})-([a-z0-9]{12}))$",
                sessionUuid
        );

        if (!uuidIsValid) return new ResponseEntity<String>("Wrong uuid format!", HttpStatus.BAD_REQUEST);

        Session dbSession = sessionRepository.getByUuid(sessionUuid);

        if (dbSession == null) return new ResponseEntity<String>("Session dose not exist!", HttpStatus.BAD_REQUEST);

        sessionRepository.deleteById(dbSession.getId());

        return new ResponseEntity<String>(HttpStatus.OK);

    }

}
