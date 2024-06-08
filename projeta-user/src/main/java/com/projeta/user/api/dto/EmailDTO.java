package com.projeta.user.api.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class EmailDTO {
    private String emailDe;
    private List<String> emailPara;
    private List<String> emailParaCC;
    
    private String assunto;
    private String textoMsgFromTela;
    private String textoCorpoEmail;

    private String textoAnexo;
    private List<EmailAnexoDTO> anexos;
}
