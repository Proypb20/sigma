package com.sigma.myapp.service;

import com.sigma.myapp.domain.Notificacion;
import com.sigma.myapp.repository.NotificacionRepository;
import com.sigma.myapp.service.dto.NotificacionDTO;
import com.sigma.myapp.service.mapper.NotificacionMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Notificacion}.
 */
@Service
@Transactional
public class NotificacionService {

    private final Logger log = LoggerFactory.getLogger(NotificacionService.class);

    private final NotificacionRepository notificacionRepository;

    private final NotificacionMapper notificacionMapper;

    public NotificacionService(NotificacionRepository notificacionRepository, NotificacionMapper notificacionMapper) {
        this.notificacionRepository = notificacionRepository;
        this.notificacionMapper = notificacionMapper;
    }

    /**
     * Save a notificacion.
     *
     * @param notificacionDTO the entity to save.
     * @return the persisted entity.
     */
    public NotificacionDTO save(NotificacionDTO notificacionDTO) {
        log.debug("Request to save Notificacion : {}", notificacionDTO);
        Notificacion notificacion = notificacionMapper.toEntity(notificacionDTO);
        notificacion = notificacionRepository.save(notificacion);
        return notificacionMapper.toDto(notificacion);
    }

    /**
     * Update a notificacion.
     *
     * @param notificacionDTO the entity to save.
     * @return the persisted entity.
     */
    public NotificacionDTO update(NotificacionDTO notificacionDTO) {
        log.debug("Request to update Notificacion : {}", notificacionDTO);
        Notificacion notificacion = notificacionMapper.toEntity(notificacionDTO);
        notificacion = notificacionRepository.save(notificacion);
        return notificacionMapper.toDto(notificacion);
    }

    /**
     * Partially update a notificacion.
     *
     * @param notificacionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NotificacionDTO> partialUpdate(NotificacionDTO notificacionDTO) {
        log.debug("Request to partially update Notificacion : {}", notificacionDTO);

        return notificacionRepository
            .findById(notificacionDTO.getId())
            .map(existingNotificacion -> {
                notificacionMapper.partialUpdate(existingNotificacion, notificacionDTO);

                return existingNotificacion;
            })
            .map(notificacionRepository::save)
            .map(notificacionMapper::toDto);
    }

    /**
     * Get all the notificacions.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<NotificacionDTO> findAll() {
        log.debug("Request to get all Notificacions");
        return notificacionRepository.findAll().stream().map(notificacionMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one notificacion by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NotificacionDTO> findOne(Long id) {
        log.debug("Request to get Notificacion : {}", id);
        return notificacionRepository.findById(id).map(notificacionMapper::toDto);
    }

    /**
     * Delete the notificacion by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Notificacion : {}", id);
        notificacionRepository.deleteById(id);
    }
}
