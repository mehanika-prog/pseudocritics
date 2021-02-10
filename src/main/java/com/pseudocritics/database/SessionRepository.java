package com.pseudocritics.database;

import com.pseudocritics.domain.Session;
import org.springframework.data.repository.CrudRepository;

public interface SessionRepository extends CrudRepository<Session, Integer> {

    Session getByUuid(String uuid);

}
