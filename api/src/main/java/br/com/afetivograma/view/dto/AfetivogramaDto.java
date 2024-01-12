package br.com.afetivograma.view.dto;

import br.com.afetivograma.model.entity.AfetivogramaEntity;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AfetivogramaDto {
    private String username;
    private LocalDateTime localDateTime;
    private boolean euforia_agitacao_aceleracao_agressividade;
    private boolean irritabilidade_inquietacao_impaciencia;
    private boolean bomhumor_estabilidade;
    private boolean tristeza_fadiga_cansaco_desanimo;
    private boolean tristezaprofunda_lentidao_apatia;

    public AfetivogramaEntity toEntity() {
        AfetivogramaEntity afetivogramaEntity = new AfetivogramaEntity();
        afetivogramaEntity.setLocalDateTime(localDateTime);
        afetivogramaEntity.setUsername(getUsername());
        afetivogramaEntity.setEuforia_agitacao_aceleracao_agressividade(isEuforia_agitacao_aceleracao_agressividade());
        afetivogramaEntity.setIrritabilidade_inquietacao_impaciencia(isIrritabilidade_inquietacao_impaciencia());
        afetivogramaEntity.setBomhumor_estabilidade(isBomhumor_estabilidade());
        afetivogramaEntity.setTristeza_fadiga_cansaco_desanimo(isTristeza_fadiga_cansaco_desanimo());
        afetivogramaEntity.setTristezaprofunda_lentidao_apatia(isTristezaprofunda_lentidao_apatia());
        return afetivogramaEntity;
    }
}
