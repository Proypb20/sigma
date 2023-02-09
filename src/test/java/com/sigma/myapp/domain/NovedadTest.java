package com.sigma.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.sigma.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NovedadTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Novedad.class);
        Novedad novedad1 = new Novedad();
        novedad1.setId(1L);
        Novedad novedad2 = new Novedad();
        novedad2.setId(novedad1.getId());
        assertThat(novedad1).isEqualTo(novedad2);
        novedad2.setId(2L);
        assertThat(novedad1).isNotEqualTo(novedad2);
        novedad1.setId(null);
        assertThat(novedad1).isNotEqualTo(novedad2);
    }
}
