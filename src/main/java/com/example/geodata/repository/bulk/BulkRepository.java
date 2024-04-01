package com.example.geodata.repository.bulk;

import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface BulkRepository<E> {

    void bulkInsert(Collection<E> entities);

}
