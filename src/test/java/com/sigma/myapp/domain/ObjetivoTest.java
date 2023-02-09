package com.sigma.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.sigma.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ObjetivoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Objetivo.class);
        Objetivo objetivo1 = new Objetivo();
        objetivo1.setId(1L);
        Objetivo objetivo2 = new Objetivo();
        objetivo2.setId(objetivo1.getId());
        assertThat(objetivo1).isEqualTo(objetivo2);
        objetivo2.setId(2L);
        assertThat(objetivo1).isNotEqualTo(objetivo2);
        objetivo1.setId(null);
        assertThat(objetivo1).isNotEqualTo(objetivo2);
    }
}
