package com.sigma.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NotificacionMapperTest {

    private NotificacionMapper notificacionMapper;

    @BeforeEach
    public void setUp() {
        notificacionMapper = new NotificacionMapperImpl();
    }
}
