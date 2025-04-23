package com.findpath;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface PathRepository extends MongoRepository<Path, String> {
}
