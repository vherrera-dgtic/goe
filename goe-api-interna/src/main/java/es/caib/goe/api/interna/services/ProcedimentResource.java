package es.caib.goe.api.interna.services;

import es.caib.goe.service.facade.ProcedimentServiceFacade;
import es.caib.goe.service.model.Pagina;
import es.caib.goe.service.model.PaginaProcediment;
import es.caib.goe.service.model.ProcedimentDTO;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.headers.Header;
import org.eclipse.microprofile.openapi.annotations.links.Link;
import org.eclipse.microprofile.openapi.annotations.links.LinkParameter;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import javax.ejb.EJB;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

/**
 * Recurs REST per accedir a Procediments.
 *
 * Es pot establir la seguretat a nivell url-pattern/http-method a dins web.xml, però d'altra banda, aquesta
 * ja està establerta a nivell de la capa de serveis.
 *
 *  @author areus
 */
@Path("procediments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProcedimentResource {

    @EJB
    private ProcedimentServiceFacade procedimentService;

    /**
     * Retorna tots els procediments d'una unitat orgànica.
     * @param unitatId identificador de la unitat orgànica
     * @return Un codi 200 amb tots els procediments
     */
    @GET
    @Operation(operationId = "getProcedimentsByUnitat", summary = "Retorna una llista de procediments d'una unitat")
    @APIResponse(
            responseCode = "200",
            description = "Llista de procediments",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = PaginaProcediment.class)))
    public Response getByUnitat(
            @Parameter(description = "L'identificador de la unitat", required = true)
            @NotNull @QueryParam("unitatId") Long unitatId,
            @Parameter(description = "Primer resultat, per defecte 0")
            @DefaultValue("0") @QueryParam("firstResult") int firstResult,
            @Parameter(description = "Nombre màxim de resultats, per defecte 10")
            @DefaultValue("10") @QueryParam("maxResult") int maxResult) {
        Pagina<ProcedimentDTO> pagina = procedimentService.findByUnitat(firstResult, maxResult, unitatId);
        return Response.ok().entity(pagina).build();
    }

    /**
     * Obté un procediment
     *
     * @param id identificador
     * @return Resposta amb status 200 i la informació del procediment o
     * un resposta amb estatus 404 si l'identificador no existeix.
     */
    @GET
    @Path("{id}")
    @Operation(operationId = "getProcediment", summary = "Obté un procediment")
    @APIResponse(responseCode = "200",
            description = "Procediment",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProcedimentDTO.class)),
            links = @Link(name = "unitat",
                    description = "Unitat orgànica del procediment",
                    operationId = "getUnitat",
                    parameters = @LinkParameter(name = "id", expression = "$response.body#/idUnitat"))
    )
    @APIResponse(responseCode = "404", description = "Recurs no trobat")
    public Response get(@Parameter(description = "L'identificador del procediment", required = true)
                        @PathParam("id") Long id) {
        ProcedimentDTO procediment = procedimentService.findById(id)
                .orElseThrow(NotFoundException::new);
        return Response.ok(procediment).build();
    }

    /**
     * Crea un nou procediment dependent d'una unitat orgànica.
     *
     * @param procediment la nova unitat orgànica a crear.
     * @param unitatId    identificador de la unitat orgànica de la que dependrà el procediment.
     * @return Un codi 201 amb la localització del procediment creat.
     */
    @POST
    @Operation(operationId = "createProcediment", summary = "Crea un nou procediment associat a la unitat orgànica")
    @APIResponse(responseCode = "201", description = "Recurs creat",
            headers = @Header(name = "location", description = "Enllaç al nou recurs",
                    schema = @Schema(type = SchemaType.STRING)))
    public Response create(
            @RequestBody(
                    required = true,
                    description = "Procediment",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProcedimentDTO.class)))
            @NotNull @Valid ProcedimentDTO procediment,
            @Parameter(description = "L'identificador de la unitat", required = true)
            @NotNull @QueryParam("unitatId") Long unitatId) {
        Long newId = procedimentService.create(procediment, unitatId);
        return Response.created(URI.create("procediments/" + newId)).build();
    }

    /**
     * Actualitza un procediment. Carrega el procediment indicat per l'identificador i l'actualitza
     * amb els camps rebuts.
     *
     * @param procediment dades a actualitzar del procediment.
     * @param id          Identificador del procediment a actualitzar.
     * @return Resposta amb status 204 si l'operació té èxit, o 404 si el recurs amb l'id indicat no existeix.
     */
    @PUT
    @Path("{id}")
    @Operation(operationId = "updateProcediment", summary = "Actualitza un procediment")
    @APIResponse(responseCode = "204", description = "Operació realitzada correctament")
    @APIResponse(responseCode = "404", description = "Recurs no trobat")
    public Response update(
            @RequestBody(
                    required = true,
                    description = "Procediment",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProcedimentDTO.class)))
            @NotNull @Valid ProcedimentDTO procediment,
            @Parameter(description = "L'identificador del procediment", required = true)
            @PathParam("id") Long id)  {
        procediment.setId(id);
        procedimentService.update(procediment);
        return Response.noContent().build();
    }

    /**
     * Esborra un procediment
     *
     * @param id identificador
     * @return Resposta amb status 204 si l'operació té èxit, o 404 si el recurs amb l'id indicat no existeix.
     */
    @DELETE
    @Path("{id}")
    @Operation(operationId = "deleteProcediment", summary = "Esborra un procediment")
    @APIResponse(responseCode = "204", description = "Operació realitzada correctament")
    @APIResponse(responseCode = "404", description = "Recurs no trobat")
    public Response delete(@Parameter(description = "L'identificador del procediment", required = true)
                           @PathParam("id") Long id) {
        procedimentService.delete(id);
        return Response.noContent().build();
    }
}
