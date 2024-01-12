package br.com.afetivograma.view;

import br.com.afetivograma.controller.enums.PeriodoAfetivogramaEnum;
import br.com.afetivograma.controller.interfaces.AfetivogramaControllerImpl;
import br.com.afetivograma.view.dto.AfetivogramaDto;
import br.com.afetivograma.view.dto.AfetivogramaPercentDto;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import java.time.LocalDateTime;
import java.util.List;

@Authenticated
@Path("afetivograma")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AfetivogramaResource {
    @Inject
    AfetivogramaControllerImpl afetivogramaControllerImpl;

    @POST
    public void createUser(@RequestBody AfetivogramaDto afetivogramaDto) {
        afetivogramaControllerImpl.createAfetivograma(afetivogramaDto);
    }

    @GET
    @Path("/by-username")
    public List<AfetivogramaDto> getUserByUsername() {
        return afetivogramaControllerImpl.getAfetivogramaByUsername();
    }

    @GET
    @Path("/by-username/percent")
    public AfetivogramaPercentDto getUserByUsernamePercent() {
        return afetivogramaControllerImpl.getAfetivogramaByUsernamePercent();
    }

    @GET
    @Path("/by-period")
    public List<AfetivogramaDto> getUserByUsername(@QueryParam("periodo") PeriodoAfetivogramaEnum periodoAfetivogramaEnum) {
        return afetivogramaControllerImpl.getAfetivogramaPeriod(periodoAfetivogramaEnum);
    }

    @GET
    @Path("/by-period/percent")
    public AfetivogramaPercentDto getUserByUsernamePercent(@QueryParam("periodo") PeriodoAfetivogramaEnum periodoAfetivogramaEnum) {
        return afetivogramaControllerImpl.getAfetivogramaPeriodPercent(periodoAfetivogramaEnum);
    }

    @GET
    @Path("/by-period-custom")
    public List<AfetivogramaDto> getUserByUsername(@QueryParam("entre") LocalDateTime localDateTimeEntre, @QueryParam("ate") LocalDateTime localDateTimeAte) {
        return afetivogramaControllerImpl.getAfetivogramaPeriod(localDateTimeEntre, localDateTimeAte);
    }

    @GET
    @Path("/by-period-custom/percent")
    public AfetivogramaPercentDto getUserByUsernamePercent(@QueryParam("entre") LocalDateTime localDateTimeEntre, @QueryParam("ate") LocalDateTime localDateTimeAte) {
        return afetivogramaControllerImpl.getAfetivogramaPeriodPercent(localDateTimeEntre, localDateTimeAte);
    }
}