package com.sigma.myapp.service;

import com.sigma.myapp.domain.Servicio;
import com.sigma.myapp.repository.ServicioRepository;
import com.sigma.myapp.service.dto.ServicioDTO;
import com.sigma.myapp.service.mapper.ServicioMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Servicio}.
 */
@Service
@Transactional
public class ServicioService {

    private final Logger log = LoggerFactory.getLogger(ServicioService.class);

    private final ServicioRepository servicioRepository;

    private final ServicioMapper servicioMapper;

    public ServicioService(ServicioRepository servicioRepository, ServicioMapper servicioMapper) {
        this.servicioRepository = servicioRepository;
        this.servicioMapper = servicioMapper;
    }

    /**
     * Save a servicio.
     *
     * @param servicioDTO the entity to save.
     * @return the persisted entity.
     */
    public ServicioDTO save(ServicioDTO servicioDTO) {
        log.debug("Request to save Servicio : {}", servicioDTO);
        Servicio servicio = servicioMapper.toEntity(servicioDTO);
        servicio = servicioRepository.save(servicio);
        return servicioMapper.toDto(servicio);
    }

    /**
     * Update a servicio.
     *
     * @param servicioDTO the entity to save.
     * @return the persisted entity.
     */
    public ServicioDTO update(ServicioDTO servicioDTO) {
        log.debug("Request to update Servicio : {}", servicioDTO);
        Servicio servicio = servicioMapper.toEntity(servicioDTO);
        servicio = servicioRepository.save(servicio);
        return servicioMapper.toDto(servicio);
    }

    /**
     * Partially update a servicio.
     *
     * @param servicioDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ServicioDTO> partialUpdate(ServicioDTO servicioDTO) {
        log.debug("Request to partially update Servicio : {}", servicioDTO);

        return servicioRepository
            .findById(servicioDTO.getId())
            .map(existingServicio -> {
                servicioMapper.partialUpdate(existingServicio, servicioDTO);

                return existingServicio;
            })
            .map(servicioRepository::save)
            .map(servicioMapper::toDto);
    }

    /**
     * Get all the servicios.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ServicioDTO> findAll() {
        log.debug("Request to get all Servicios");
        return servicioRepository.findAll().stream().map(servicioMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one servicio by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ServicioDTO> findOne(Long id) {
        log.debug("Request to get Servicio : {}", id);
        return servicioRepository.findById(id).map(servicioMapper::toDto);
    }

    /**
     * Delete the servicio by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Servicio : {}", id);
        servicioRepository.deleteById(id);
    }
}
