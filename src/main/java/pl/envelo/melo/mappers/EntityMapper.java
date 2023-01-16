package pl.envelo.melo.mappers;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface EntityMapper <D, E> {

    E toEntity(D dto);
    D toDto(E entity);

//    Collection<E> toEntity(Collection<D> dtoCollection);
//    Collection <D> toDto(Collection<E> entityCollection);
}
