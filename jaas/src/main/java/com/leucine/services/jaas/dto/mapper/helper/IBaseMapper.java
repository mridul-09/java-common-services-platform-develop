package com.leucine.services.jaas.dto.mapper.helper;

import com.leucine.services.jaas.dto.helper.IBaseDto;
import com.leucine.services.jaas.dto.helper.IBaseEntity;

import java.util.List;
import java.util.Set;

public interface IBaseMapper<D extends IBaseDto, E extends IBaseEntity> {

  D toDto(E e);

  E toEntity(D d);

  List<D> toDto(List<E> list);

  List<E> toEntity(List<D> list);

  Set<D> toDto(Set<E> list);

  Set<E> toEntity(Set<D> list);

}
