package br.com.afetivograma.model.interfaces;

import br.com.afetivograma.model.entity.AfetivogramaEntity;
import io.quarkus.panache.common.Parameters;

import java.util.List;
import java.util.UUID;

public interface AfetivogramaRepositoryImpl {
    void createAfetivograma(AfetivogramaEntity afetivogramaEntity);
    AfetivogramaEntity getAfetivogramaEntity(UUID uuid);
    List<AfetivogramaEntity> listAfetivogramaEntity(String username);
    List<AfetivogramaEntity> listAfetivogramaEntity(String query, Parameters params);
    void deleteUser(AfetivogramaEntity afetivogramaEntity);
}