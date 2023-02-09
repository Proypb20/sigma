package com.sigma.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ObjetivoMapperTest {

    private ObjetivoMapper objetivoMapper;

    @BeforeEach
    public void setUp() {
        objetivoMapper = new ObjetivoMapperImpl();
    }
}
