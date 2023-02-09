package com.sigma.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.sigma.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ObjetivoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ObjetivoDTO.class);
        ObjetivoDTO objetivoDTO1 = new ObjetivoDTO();
        objetivoDTO1.setId(1L);
        ObjetivoDTO objetivoDTO2 = new ObjetivoDTO();
        assertThat(objetivoDTO1).isNotEqualTo(objetivoDTO2);
        objetivoDTO2.setId(objetivoDTO1.getId());
        assertThat(objetivoDTO1).isEqualTo(objetivoDTO2);
        objetivoDTO2.setId(2L);
        assertThat(objetivoDTO1).isNotEqualTo(objetivoDTO2);
        objetivoDTO1.setId(null);
        assertThat(objetivoDTO1).isNotEqualTo(objetivoDTO2);
    }
}
