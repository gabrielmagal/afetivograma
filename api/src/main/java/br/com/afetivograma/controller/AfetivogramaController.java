package br.com.afetivograma.controller;

import br.com.afetivograma.controller.enums.PeriodoAfetivogramaEnum;
import br.com.afetivograma.model.entity.AfetivogramaEntity;
import br.com.afetivograma.view.dto.AfetivogramaDto;
import br.com.afetivograma.controller.interfaces.AfetivogramaControllerImpl;
import br.com.afetivograma.model.interfaces.AfetivogramaRepositoryImpl;
import br.com.afetivograma.view.dto.AfetivogramaPercentDto;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@ApplicationScoped
public class AfetivogramaController implements AfetivogramaControllerImpl {
    @Inject
    AfetivogramaRepositoryImpl afetivogramaRepositoryImpl;

    @Inject
    JsonWebToken jsonWebToken;

    public void createAfetivograma(AfetivogramaDto afetivogramaDto) {
        AfetivogramaEntity afetivogramaEntity = afetivogramaDto.toEntity();
        afetivogramaEntity.setLocalDateTime(LocalDateTime.now());
        afetivogramaEntity.setUsername(jsonWebToken.getName());
        afetivogramaEntity.setId(null);
        afetivogramaRepositoryImpl.createAfetivograma(afetivogramaEntity);
    }

    public List<AfetivogramaDto> getAfetivogramaByUsername() {
        return afetivogramaRepositoryImpl.listAfetivogramaEntity(jsonWebToken.getName()).stream().map(AfetivogramaEntity::toDto).toList();
    }

    public AfetivogramaPercentDto getAfetivogramaByUsernamePercent() {
        return convertListAfetivogramaDtoParaAfetivogramaPercentDto(getAfetivogramaByUsername());
    }

    public List<AfetivogramaDto> getAfetivogramaPeriod(PeriodoAfetivogramaEnum periodoAfetivogramaEnum) {
        int days = switch (periodoAfetivogramaEnum) {
            case DIARIO -> 1;
            case SEMANAL -> 7;
            case MENSAL -> 30;
            case ANUAL -> 360;
        };

        List<AfetivogramaEntity> afetivogramaEntityList = afetivogramaRepositoryImpl.listAfetivogramaEntity(
                "username = :username and localDateTime >= :dataLimite and localDateTime <= :dataAtual",
                Parameters.with("dataLimite", LocalDateTime.now().minusDays(days))
                        .and("dataAtual", LocalDateTime.now())
                        .and("username", jsonWebToken.getName())
        );

        if(afetivogramaEntityList.isEmpty()) {
            return new ArrayList<>();
        }
        return afetivogramaEntityList.stream().map(AfetivogramaEntity::toDto).toList();
    }

    public AfetivogramaPercentDto getAfetivogramaPeriodPercent(PeriodoAfetivogramaEnum periodoAfetivogramaEnum) {
        return convertListAfetivogramaDtoParaAfetivogramaPercentDto(getAfetivogramaPeriod(periodoAfetivogramaEnum));
    }

    public List<AfetivogramaDto> getAfetivogramaPeriod(LocalDateTime localDateTimeEntre, LocalDateTime localDateTimeAte) {
        List<AfetivogramaEntity> afetivogramaEntityList = afetivogramaRepositoryImpl.listAfetivogramaEntity(
                "username = :username and localDateTime >= :dataEntre and localDateTime <= :dataAte",
                Parameters.with("dataEntre", localDateTimeEntre)
                        .and("dataAte", localDateTimeAte)
                        .and("username", jsonWebToken.getName())
        );

        if(afetivogramaEntityList.isEmpty()) {
            return new ArrayList<>();
        }
        return afetivogramaEntityList.stream().map(AfetivogramaEntity::toDto).toList();
    }

    public AfetivogramaPercentDto getAfetivogramaPeriodPercent(LocalDateTime localDateTimeEntre, LocalDateTime localDateTimeAte) {
        return convertListAfetivogramaDtoParaAfetivogramaPercentDto(getAfetivogramaPeriod(localDateTimeEntre, localDateTimeAte));
    }

    @Override
    public void deleteUser(UUID uuid) {
        AfetivogramaEntity afetivogramaEntity = afetivogramaRepositoryImpl.getAfetivogramaEntity(uuid);
        if (Objects.nonNull(afetivogramaEntity) && jsonWebToken.getName().equals(afetivogramaEntity.getUsername())) {
            afetivogramaRepositoryImpl.deleteUser(afetivogramaEntity);
        }
    }

    private AfetivogramaPercentDto convertListAfetivogramaDtoParaAfetivogramaPercentDto(List<AfetivogramaDto> afetivogramaDtoList) {
        AfetivogramaPercentDto afetivogramaPercentDto = new AfetivogramaPercentDto();
        afetivogramaDtoList.forEach(afetivogramaDto -> {
            converterAfetivogramaDtoParaAfetivogramaPercentDto(afetivogramaDto, afetivogramaPercentDto);
        });
        afetivogramaPercentDto.deParaPercent();
        return afetivogramaPercentDto;
    }

    private void converterAfetivogramaDtoParaAfetivogramaPercentDto(AfetivogramaDto afetivogramaDto, AfetivogramaPercentDto afetivogramaPercentDto) {
        if(afetivogramaDto.isBomhumor_estabilidade())
            afetivogramaPercentDto.setBomhumor_estabilidade(afetivogramaPercentDto.getBomhumor_estabilidade().add(new BigDecimal(100)));
        if(afetivogramaDto.isIrritabilidade_inquietacao_impaciencia())
            afetivogramaPercentDto.setIrritabilidade_inquietacao_impaciencia(afetivogramaPercentDto.getIrritabilidade_inquietacao_impaciencia().add(new BigDecimal(100)));
        if(afetivogramaDto.isEuforia_agitacao_aceleracao_agressividade())
            afetivogramaPercentDto.setEuforia_agitacao_aceleracao_agressividade(afetivogramaPercentDto.getEuforia_agitacao_aceleracao_agressividade().add(new BigDecimal(100)));
        if(afetivogramaDto.isTristeza_fadiga_cansaco_desanimo())
            afetivogramaPercentDto.setTristeza_fadiga_cansaco_desanimo(afetivogramaPercentDto.getTristeza_fadiga_cansaco_desanimo().add(new BigDecimal(100)));
        if(afetivogramaDto.isTristezaprofunda_lentidao_apatia())
            afetivogramaPercentDto.setTristezaprofunda_lentidao_apatia(afetivogramaPercentDto.getTristezaprofunda_lentidao_apatia().add(new BigDecimal(100)));
    }
}
