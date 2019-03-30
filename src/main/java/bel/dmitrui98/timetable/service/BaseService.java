package bel.dmitrui98.timetable.service;

import bel.dmitrui98.timetable.util.exception.AppsException;

import java.util.List;

public interface BaseService<T, T_ID> {
    boolean save(T entity) throws AppsException;
    boolean delete(T_ID id) throws AppsException;
    List<T> findAll();
}
