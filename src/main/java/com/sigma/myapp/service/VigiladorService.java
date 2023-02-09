package com.sigma.myapp.service;

import com.sigma.myapp.domain.Vigilador;
import com.sigma.myapp.repository.UserRepository;
import com.sigma.myapp.repository.VigiladorRepository;
import com.sigma.myapp.service.dto.VigiladorDTO;
import com.sigma.myapp.service.mapper.VigiladorMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Vigilador}.
 */
@Service
@Transactional
public class VigiladorService {

    private final Logger log = LoggerFactory.getLogger(VigiladorService.class);

    private final VigiladorRepository vigiladorRepository;

    private final VigiladorMapper vigiladorMapper;

    private final UserRepository userRepository;

    public VigiladorService(VigiladorRepository vigiladorRepository, VigiladorMapper vigiladorMapper, UserRepository userRepository) {
        this.vigiladorRepository = vigiladorRepository;
        this.vigiladorMapper = vigiladorMapper;
        this.userRepository = userRepository;
    }

    /**
     * Save a vigilador.
     *
     * @param vigiladorDTO the entity to save.
     * @return the persisted entity.
     */
    public VigiladorDTO save(VigiladorDTO vigiladorDTO) {
        log.debug("Request to save Vigilador : {}", vigiladorDTO);
        Vigilador vigilador = vigiladorMapper.toEntity(vigiladorDTO);
        Long userId = vigiladorDTO.getUser().getId();
        userRepository.findById(userId).ifPresent(vigilador::user);
        vigilador = vigiladorRepository.save(vigilador);
        return vigiladorMapper.toDto(vigilador);
    }

    /**
     * Update a vigilador.
     *
     * @param vigiladorDTO the entity to save.
     * @return the persisted entity.
     */
    public VigiladorDTO update(VigiladorDTO vigiladorDTO) {
        log.debug("Request to update Vigilador : {}", vigiladorDTO);
        Vigilador vigilador = vigiladorMapper.toEntity(vigiladorDTO);
        Long userId = vigiladorDTO.getUser().getId();
        userRepository.findById(userId).ifPresent(vigilador::user);
        vigilador = vigiladorRepository.save(vigilador);
        return vigiladorMapper.toDto(vigilador);
    }

    /**
     * Partially update a vigilador.
     *
     * @param vigiladorDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<VigiladorDTO> partialUpdate(VigiladorDTO vigiladorDTO) {
        log.debug("Request to partially update Vigilador : {}", vigiladorDTO);

        return vigiladorRepository
            .findById(vigiladorDTO.getId())
            .map(existingVigilador -> {
                vigiladorMapper.partialUpdate(existingVigilador, vigiladorDTO);

                return existingVigilador;
            })
            .map(vigiladorRepository::save)
            .map(vigiladorMapper::toDto);
    }

    /**
     * Get all the vigiladors.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<VigiladorDTO> findAll() {
        log.debug("Request to get all Vigiladors");
        return vigiladorRepository.findAll().stream().map(vigiladorMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the vigiladors with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<VigiladorDTO> findAllWithEagerRelationships(Pageable pageable) {
        return vigiladorRepository.findAllWithEagerRelationships(pageable).map(vigiladorMapper::toDto);
    }

    /**
     * Get one vigilador by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<VigiladorDTO> findOne(Long id) {
        log.debug("Request to get Vigilador : {}", id);
        return vigiladorRepository.findOneWithEagerRelationships(id).map(vigiladorMapper::toDto);
    }

    /**
     * Delete the vigilador by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Vigilador : {}", id);
        vigiladorRepository.deleteById(id);
    }
}
