package com.sigma.myapp.service;

import com.sigma.myapp.domain.Novedad;
import com.sigma.myapp.repository.NovedadRepository;
import com.sigma.myapp.service.dto.NovedadDTO;
import com.sigma.myapp.service.mapper.NovedadMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Novedad}.
 */
@Service
@Transactional
public class NovedadService {

    private final Logger log = LoggerFactory.getLogger(NovedadService.class);

    private final NovedadRepository novedadRepository;

    private final NovedadMapper novedadMapper;

    public NovedadService(NovedadRepository novedadRepository, NovedadMapper novedadMapper) {
        this.novedadRepository = novedadRepository;
        this.novedadMapper = novedadMapper;
    }

    /**
     * Save a novedad.
     *
     * @param novedadDTO the entity to save.
     * @return the persisted entity.
     */
    public NovedadDTO save(NovedadDTO novedadDTO) {
        log.debug("Request to save Novedad : {}", novedadDTO);
        Novedad novedad = novedadMapper.toEntity(novedadDTO);
        novedad = novedadRepository.save(novedad);
        return novedadMapper.toDto(novedad);
    }

    /**
     * Update a novedad.
     *
     * @param novedadDTO the entity to save.
     * @return the persisted entity.
     */
    public NovedadDTO update(NovedadDTO novedadDTO) {
        log.debug("Request to update Novedad : {}", novedadDTO);
        Novedad novedad = novedadMapper.toEntity(novedadDTO);
        novedad = novedadRepository.save(novedad);
        return novedadMapper.toDto(novedad);
    }

    /**
     * Partially update a novedad.
     *
     * @param novedadDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NovedadDTO> partialUpdate(NovedadDTO novedadDTO) {
        log.debug("Request to partially update Novedad : {}", novedadDTO);

        return novedadRepository
            .findById(novedadDTO.getId())
            .map(existingNovedad -> {
                novedadMapper.partialUpdate(existingNovedad, novedadDTO);

                return existingNovedad;
            })
            .map(novedadRepository::save)
            .map(novedadMapper::toDto);
    }

    /**
     * Get all the novedads.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<NovedadDTO> findAll() {
        log.debug("Request to get all Novedads");
        return novedadRepository.findAll().stream().map(novedadMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one novedad by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NovedadDTO> findOne(Long id) {
        log.debug("Request to get Novedad : {}", id);
        return novedadRepository.findById(id).map(novedadMapper::toDto);
    }

    /**
     * Delete the novedad by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Novedad : {}", id);
        novedadRepository.deleteById(id);
    }
}
