package es.caib.goe.ejb.repository;

import es.caib.goe.persistence.model.Procediment;
import es.caib.goe.service.model.ProcedimentDTO;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

/**
 * Implementació del repositori de Procediments.
 *
 * @author areus
 */
@Stateless @Local(ProcedimentRepository.class)
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class ProcedimentRepositoryBean extends AbstractCrudRepository<Procediment, Long>
        implements ProcedimentRepository {

    protected ProcedimentRepositoryBean() {
        super(Procediment.class);
    }

    @Override
    public Optional<Procediment> findByCodiSia(String codiSia) {
        TypedQuery<Procediment> query = entityManager.createNamedQuery(Procediment.FIND_BY_CODISIA, Procediment.class);
        query.setParameter("codiSia", codiSia);
        List<Procediment> result = query.getResultList();
        return Optional.ofNullable(result.isEmpty() ? null : result.get(0));
    }

    @Override
    public List<ProcedimentDTO> findPagedByUnitat(int firstResult, int maxResult, Long idUnitat) {
        TypedQuery<ProcedimentDTO> query = entityManager.createNamedQuery(
                Procediment.FIND_DTO_BY_IDUNITAT,
                ProcedimentDTO.class);
        query.setParameter("idUnitat", idUnitat);
        query.setFirstResult(firstResult);
        query.setMaxResults(maxResult);
        return query.getResultList();
    }

    @Override
    public long countByUnitat(Long idUnitat) {
        TypedQuery<Long> query = entityManager.createNamedQuery(Procediment.COUNT_BY_IDUNITAT, Long.class);
        query.setParameter("idUnitat", idUnitat);
        return query.getSingleResult();
    }
}
