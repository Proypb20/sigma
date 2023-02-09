package com.sigma.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.sigma.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NovedadDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NovedadDTO.class);
        NovedadDTO novedadDTO1 = new NovedadDTO();
        novedadDTO1.setId(1L);
        NovedadDTO novedadDTO2 = new NovedadDTO();
        assertThat(novedadDTO1).isNotEqualTo(novedadDTO2);
        novedadDTO2.setId(novedadDTO1.getId());
        assertThat(novedadDTO1).isEqualTo(novedadDTO2);
        novedadDTO2.setId(2L);
        assertThat(novedadDTO1).isNotEqualTo(novedadDTO2);
        novedadDTO1.setId(null);
        assertThat(novedadDTO1).isNotEqualTo(novedadDTO2);
    }
}
