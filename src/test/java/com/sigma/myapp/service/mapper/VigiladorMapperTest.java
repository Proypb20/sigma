package com.sigma.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VigiladorMapperTest {

    private VigiladorMapper vigiladorMapper;

    @BeforeEach
    public void setUp() {
        vigiladorMapper = new VigiladorMapperImpl();
    }
}
