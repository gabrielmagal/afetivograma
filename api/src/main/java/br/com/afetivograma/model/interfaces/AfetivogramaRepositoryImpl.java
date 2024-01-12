package br.com.afetivograma.model.interfaces;

import br.com.afetivograma.model.entity.AfetivogramaEntity;
import io.quarkus.panache.common.Parameters;

import java.util.List;

public interface AfetivogramaRepositoryImpl {
    void createAfetivograma(AfetivogramaEntity afetivogramaEntity);
    List<AfetivogramaEntity> listAfetivogramaEntity(String username);
    List<AfetivogramaEntity> listAfetivogramaEntity(String query, Parameters params);
}