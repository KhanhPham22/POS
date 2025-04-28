package dao;

import java.io.Serializable;
import java.util.List;

//interface use for CRUD entity
public interface GenericDao<T extends Serializable> {
    void setClass(Class< T > classToSet);

    T findById(final long id) throws Exception;

    List<T> findAll(int pageNumber, int pageSize) throws Exception ;

    boolean create(final T entity) throws Exception;

    boolean update(final T entity) throws Exception;

    boolean delete(final T entity) throws Exception;

    boolean deleteById(final long entityId) throws Exception;
}
