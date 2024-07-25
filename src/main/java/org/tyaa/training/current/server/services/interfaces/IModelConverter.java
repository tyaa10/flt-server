package org.tyaa.training.current.server.services.interfaces;

import org.tyaa.training.current.server.entities.interfaces.IEntity;
import org.tyaa.training.current.server.models.interfaces.IModel;

/**
 * Абстракция преобразований "сущность в модель" и "модель в сущность"
 * @param <E> тип сущности
 * @param <M> тип модели
 * */
public interface IModelConverter<M extends IModel, E extends IEntity>  {
    /**
     * Преобразование "сущность в модель"
     * @param entity сущность
     * @return модель
     * */
    M entityToModel(E entity);
    /**
     * Преобразование "модель в сущность"
     * @param model модель
     * @return сущность
     * */
    E modelToEntity(M model);
}
