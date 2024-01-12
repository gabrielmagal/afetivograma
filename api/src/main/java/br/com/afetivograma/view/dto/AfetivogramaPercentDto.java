package br.com.afetivograma.view.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Data
public class AfetivogramaPercentDto {
    private BigDecimal euforia_agitacao_aceleracao_agressividade = BigDecimal.valueOf(0);
    private BigDecimal irritabilidade_inquietacao_impaciencia = BigDecimal.valueOf(0);
    private BigDecimal bomhumor_estabilidade = BigDecimal.valueOf(0);
    private BigDecimal tristeza_fadiga_cansaco_desanimo = BigDecimal.valueOf(0);
    private BigDecimal tristezaprofunda_lentidao_apatia = BigDecimal.valueOf(0);

    public void deParaPercent() {
        BigDecimal total = euforia_agitacao_aceleracao_agressividade.add(irritabilidade_inquietacao_impaciencia)
                .add(bomhumor_estabilidade).add(tristeza_fadiga_cansaco_desanimo)
                .add(tristezaprofunda_lentidao_apatia);

        BigDecimal hundred = new BigDecimal(100);
        BigDecimal totalInt = total.setScale(0, RoundingMode.HALF_UP);

        euforia_agitacao_aceleracao_agressividade = euforia_agitacao_aceleracao_agressividade
                .multiply(hundred).divide(totalInt, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP);
        irritabilidade_inquietacao_impaciencia = irritabilidade_inquietacao_impaciencia
                .multiply(hundred).divide(totalInt, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP);
        bomhumor_estabilidade = bomhumor_estabilidade
                .multiply(hundred).divide(totalInt, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP);
        tristeza_fadiga_cansaco_desanimo = tristeza_fadiga_cansaco_desanimo
                .multiply(hundred).divide(totalInt, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP);
        tristezaprofunda_lentidao_apatia = tristezaprofunda_lentidao_apatia
                .multiply(hundred).divide(totalInt, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP);
    }
}
