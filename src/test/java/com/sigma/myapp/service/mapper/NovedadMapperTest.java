package com.sigma.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NovedadMapperTest {

    private NovedadMapper novedadMapper;

    @BeforeEach
    public void setUp() {
        novedadMapper = new NovedadMapperImpl();
    }
}
