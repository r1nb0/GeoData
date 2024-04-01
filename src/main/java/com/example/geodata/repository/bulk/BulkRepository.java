package com.example.geodata.repository.bulk;

import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface BulkRepository<ID, E> {

    void bulkInsert(Collection<E> entities);

    void bulkUpdate(Collection<E> entities);

    void bulkDelete(Collection<ID> ids);

}
