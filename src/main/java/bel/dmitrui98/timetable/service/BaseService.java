package bel.dmitrui98.timetable.service;

import bel.dmitrui98.timetable.util.exception.AppsException;

import java.util.List;

public interface BaseService<T, T_ID> {
    void save(T entity) throws AppsException;
    void delete(T_ID id) throws AppsException;

    /**
     * Удаляет сущности одним запросом в БД по id
     */
    void deleteAll(List<T> entities) throws AppsException;

    /**
     * Удаляет сущности одним запросом в БД по id
     */
    void deleteAllByIds(List<T_ID> ids) throws AppsException;

    T findByIdThrow(T_ID id) throws AppsException;
    List<T> findAll();
}
