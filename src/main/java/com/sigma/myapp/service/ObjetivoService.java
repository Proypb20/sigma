package com.sigma.myapp.service;

import com.sigma.myapp.domain.Objetivo;
import com.sigma.myapp.repository.ObjetivoRepository;
import com.sigma.myapp.service.dto.ObjetivoDTO;
import com.sigma.myapp.service.mapper.ObjetivoMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Objetivo}.
 */
@Service
@Transactional
public class ObjetivoService {

    private final Logger log = LoggerFactory.getLogger(ObjetivoService.class);

    private final ObjetivoRepository objetivoRepository;

    private final ObjetivoMapper objetivoMapper;

    public ObjetivoService(ObjetivoRepository objetivoRepository, ObjetivoMapper objetivoMapper) {
        this.objetivoRepository = objetivoRepository;
        this.objetivoMapper = objetivoMapper;
    }

    /**
     * Save a objetivo.
     *
     * @param objetivoDTO the entity to save.
     * @return the persisted entity.
     */
    public ObjetivoDTO save(ObjetivoDTO objetivoDTO) {
        log.debug("Request to save Objetivo : {}", objetivoDTO);
        Objetivo objetivo = objetivoMapper.toEntity(objetivoDTO);
        objetivo = objetivoRepository.save(objetivo);
        return objetivoMapper.toDto(objetivo);
    }

    /**
     * Update a objetivo.
     *
     * @param objetivoDTO the entity to save.
     * @return the persisted entity.
     */
    public ObjetivoDTO update(ObjetivoDTO objetivoDTO) {
        log.debug("Request to update Objetivo : {}", objetivoDTO);
        Objetivo objetivo = objetivoMapper.toEntity(objetivoDTO);
        objetivo = objetivoRepository.save(objetivo);
        return objetivoMapper.toDto(objetivo);
    }

    /**
     * Partially update a objetivo.
     *
     * @param objetivoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ObjetivoDTO> partialUpdate(ObjetivoDTO objetivoDTO) {
        log.debug("Request to partially update Objetivo : {}", objetivoDTO);

        return objetivoRepository
            .findById(objetivoDTO.getId())
            .map(existingObjetivo -> {
                objetivoMapper.partialUpdate(existingObjetivo, objetivoDTO);

                return existingObjetivo;
            })
            .map(objetivoRepository::save)
            .map(objetivoMapper::toDto);
    }

    /**
     * Get all the objetivos.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ObjetivoDTO> findAll() {
        log.debug("Request to get all Objetivos");
        return objetivoRepository.findAll().stream().map(objetivoMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one objetivo by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ObjetivoDTO> findOne(Long id) {
        log.debug("Request to get Objetivo : {}", id);
        return objetivoRepository.findById(id).map(objetivoMapper::toDto);
    }

    /**
     * Delete the objetivo by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Objetivo : {}", id);
        objetivoRepository.deleteById(id);
    }
}
