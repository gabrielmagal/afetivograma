package br.com.afetivograma.model.entity;

import br.com.afetivograma.view.dto.AfetivogramaDto;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
public class AfetivogramaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID id;
    private String username;
    private LocalDateTime localDateTime;
    private boolean euforia_agitacao_aceleracao_agressividade;
    private boolean irritabilidade_inquietacao_impaciencia;
    private boolean bomhumor_estabilidade;
    private boolean tristeza_fadiga_cansaco_desanimo;
    private boolean tristezaprofunda_lentidao_apatia;

    @Transient
    public AfetivogramaDto toDto() {
        AfetivogramaDto afetivogramaDto = new AfetivogramaDto();
        afetivogramaDto.setId(id);
        afetivogramaDto.setLocalDateTime(localDateTime);
        afetivogramaDto.setUsername(getUsername());
        afetivogramaDto.setEuforia_agitacao_aceleracao_agressividade(isEuforia_agitacao_aceleracao_agressividade());
        afetivogramaDto.setIrritabilidade_inquietacao_impaciencia(isIrritabilidade_inquietacao_impaciencia());
        afetivogramaDto.setBomhumor_estabilidade(isBomhumor_estabilidade());
        afetivogramaDto.setTristeza_fadiga_cansaco_desanimo(isTristeza_fadiga_cansaco_desanimo());
        afetivogramaDto.setTristezaprofunda_lentidao_apatia(isTristezaprofunda_lentidao_apatia());
        return afetivogramaDto;
    }
}
