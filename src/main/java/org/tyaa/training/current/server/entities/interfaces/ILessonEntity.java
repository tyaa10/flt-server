package org.tyaa.training.current.server.entities.interfaces;

/**
 * Абстракция сущности урока
 * */
public interface ILessonEntity extends IEntity {

    public Long getId();

    public void setId(Long id);

    public String getName();

    public void setName(String name);
}
