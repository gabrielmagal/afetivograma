package br.com.afetivograma.controller.interfaces;

import br.com.afetivograma.controller.enums.PeriodoAfetivogramaEnum;
import br.com.afetivograma.view.dto.AfetivogramaDto;
import br.com.afetivograma.view.dto.AfetivogramaPercentDto;

import java.time.LocalDateTime;
import java.util.List;

public interface AfetivogramaControllerImpl {
    void createAfetivograma(AfetivogramaDto afetivogramaDto);
    List<AfetivogramaDto> getAfetivogramaByUsername();
    AfetivogramaPercentDto getAfetivogramaByUsernamePercent();
    List<AfetivogramaDto> getAfetivogramaPeriod(PeriodoAfetivogramaEnum periodoAfetivogramaEnum);
    AfetivogramaPercentDto getAfetivogramaPeriodPercent(PeriodoAfetivogramaEnum periodoAfetivogramaEnum);
    List<AfetivogramaDto> getAfetivogramaPeriod(LocalDateTime localDateTimeEntre, LocalDateTime localDateTimeAte);
    AfetivogramaPercentDto getAfetivogramaPeriodPercent(LocalDateTime localDateTimeEntre, LocalDateTime localDateTimeAte);
}
