package com.sigma.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.sigma.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VigiladorDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VigiladorDTO.class);
        VigiladorDTO vigiladorDTO1 = new VigiladorDTO();
        vigiladorDTO1.setId(1L);
        VigiladorDTO vigiladorDTO2 = new VigiladorDTO();
        assertThat(vigiladorDTO1).isNotEqualTo(vigiladorDTO2);
        vigiladorDTO2.setId(vigiladorDTO1.getId());
        assertThat(vigiladorDTO1).isEqualTo(vigiladorDTO2);
        vigiladorDTO2.setId(2L);
        assertThat(vigiladorDTO1).isNotEqualTo(vigiladorDTO2);
        vigiladorDTO1.setId(null);
        assertThat(vigiladorDTO1).isNotEqualTo(vigiladorDTO2);
    }
}
