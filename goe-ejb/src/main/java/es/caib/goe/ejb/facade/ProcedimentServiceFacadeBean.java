package es.caib.goe.ejb.facade;

import es.caib.goe.commons.utils.Constants;
import es.caib.goe.ejb.converter.ProcedimentConverter;
import es.caib.goe.ejb.interceptor.ExceptionTranslate;
import es.caib.goe.ejb.interceptor.Logged;
import es.caib.goe.ejb.repository.ProcedimentRepository;
import es.caib.goe.ejb.repository.UnitatOrganicaRepository;
import es.caib.goe.persistence.model.Procediment;
import es.caib.goe.service.exception.ProcedimentDuplicatException;
import es.caib.goe.service.exception.RecursNoTrobatException;
import es.caib.goe.service.facade.ProcedimentServiceFacade;
import es.caib.goe.service.model.Pagina;
import es.caib.goe.service.model.ProcedimentDTO;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

/**
 * Implementació dels casos d'ús de manteniment de Procediments.
 * És responsabilitat d'aquesta capa definir el limit de les transaccions i la seguretat.
 * Les excepcions específiques es llancen mitjançant l'{@link ExceptionTranslate} que transforma
 * els errors JPA amb les excepcions de servei com la {@link RecursNoTrobatException}
 * @author areus
 */
@Logged
@ExceptionTranslate
@Stateless @Local(ProcedimentServiceFacade.class)
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class ProcedimentServiceFacadeBean implements ProcedimentServiceFacade {

    @Inject
    private UnitatOrganicaRepository unitatRepository;

    @Inject
    private ProcedimentRepository repository;

    @Inject
    private ProcedimentConverter converter;

    @Override
    @RolesAllowed(Constants.GOE_ADMIN)
    public Long create(ProcedimentDTO dto, Long idUnitat) throws RecursNoTrobatException, ProcedimentDuplicatException {
        // Comprovam que el codiSia no existeix ja
        if (repository.findByCodiSia(dto.getCodiSia()).isPresent()) {
            throw new ProcedimentDuplicatException(dto.getCodiSia());
        }

        Procediment procediment = converter.toEntity(dto);
        procediment.setUnitatOrganica(unitatRepository.getReference(idUnitat));
        repository.create(procediment);
        return procediment.getId();
    }

    @Override
    @RolesAllowed(Constants.GOE_ADMIN)
    public void update(ProcedimentDTO dto) throws RecursNoTrobatException {
        Procediment procediment = repository.getReference(dto.getId());
        converter.updateFromDTO(procediment, dto);
    }

    @Override
    @RolesAllowed(Constants.GOE_ADMIN)
    public void delete(Long id) throws RecursNoTrobatException {
        Procediment procediment = repository.getReference(id);
        repository.delete(procediment);
    }

    @Override
    @RolesAllowed({Constants.GOE_USER, Constants.GOE_ADMIN})
    public Optional<ProcedimentDTO> findById(Long id) {
        Procediment procediment = repository.findById(id);
        ProcedimentDTO procedimentDTO = converter.toDTO(procediment);
        return Optional.ofNullable(procedimentDTO);
    }

    @Override
    @RolesAllowed({Constants.GOE_USER, Constants.GOE_ADMIN})
    public Pagina<ProcedimentDTO> findByUnitat(int firstResult, int maxResult, Long idUnitat) {

        List<ProcedimentDTO> items = repository.findPagedByUnitat(firstResult, maxResult, idUnitat);
        long total = repository.countByUnitat(idUnitat);

        return new Pagina<>(items, total);
    }
}
