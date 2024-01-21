package br.com.afetivograma.model.repository;

import br.com.afetivograma.model.entity.AfetivogramaEntity;
import br.com.afetivograma.model.interfaces.AfetivogramaRepositoryImpl;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

@Transactional
@ApplicationScoped
public class AfetivogramaRepository implements AfetivogramaRepositoryImpl, PanacheRepository<AfetivogramaEntity> {
    @Override
    public void createAfetivograma(AfetivogramaEntity afetivogramaEntity) {
        persistAndFlush(afetivogramaEntity);
    }

    @Override
    public AfetivogramaEntity getAfetivogramaEntity(UUID uuid) {
        return find("id", uuid).firstResult();
    }

    @Override
    public List<AfetivogramaEntity> listAfetivogramaEntity(String username) {
        return find("username", username).list();
    }

    @Override
    public List<AfetivogramaEntity> listAfetivogramaEntity(String query, Parameters params) {
        return find(query, params).list();
    }

    @Override
    public void deleteUser(AfetivogramaEntity afetivogramaEntity) {
        delete(afetivogramaEntity);
    }
}