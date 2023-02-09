package com.sigma.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.sigma.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VigiladorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Vigilador.class);
        Vigilador vigilador1 = new Vigilador();
        vigilador1.setId(1L);
        Vigilador vigilador2 = new Vigilador();
        vigilador2.setId(vigilador1.getId());
        assertThat(vigilador1).isEqualTo(vigilador2);
        vigilador2.setId(2L);
        assertThat(vigilador1).isNotEqualTo(vigilador2);
        vigilador1.setId(null);
        assertThat(vigilador1).isNotEqualTo(vigilador2);
    }
}
