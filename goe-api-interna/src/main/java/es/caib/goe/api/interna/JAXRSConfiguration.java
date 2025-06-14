package es.caib.goe.api.interna;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.servers.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * Configuració de l'aplicació JAX-RS. Es pot fer mitjançant aquesta subclasse de {@link Application} o
 * mitjançant la configuració a web.xml. Fer-ho amb una subclasse permet posar-hi anotacions de OpenApi.
 * Aquí fixam la informació general d'OpenAPI, la ubicació del servidor (o servidors) que bàsicament és el
 * contextpath, els sistemes d'autenticació admesos....
 * <p>
 * Al package services hi ha les classes amb els endpoints dels recursos.
 * Segueix les convencions més comunes de les apis REST, es pot trobar una bona descripció aquí:
 * https://restfulapi.net/http-methods/
 * Els mètodes de consulta GET, retornen un 200 amb la informació o 404 si no es troba.
 * Els mètodes de creació, POST, retornen un 201 amb la capçalera 'location' cap al nou recurs.
 * Els mètodes d'actualització/borrat, PUT/DELETE, retornen un 204 si ha anat bé (ja que no retornen cap entity),
 * o 404 si no es troba el recurs.
 *
 * @author areus
 */
@ApplicationPath("/services")
@SecurityScheme(securitySchemeName = "basicAuth", type = SecuritySchemeType.HTTP, scheme = "basic")
@SecurityScheme(securitySchemeName = "bearerAuth", type = SecuritySchemeType.HTTP, scheme = "bearer")
@OpenAPIDefinition(
        info = @Info(title = "API REST INTERNA", version = "1.0.0"),
        servers = {
                @Server(url = "/goe/api/interna")
        },
        security = {
                @SecurityRequirement(name = "basicAuth"),
                @SecurityRequirement(name = "bearerAuth")
        }
)
public class JAXRSConfiguration extends Application {

    private static final Logger LOG = LoggerFactory.getLogger(JAXRSConfiguration.class);

    /**
     * Les aplicacions JAX-RS necessiten un constructor buid.
     */
    public JAXRSConfiguration() {
    }

    /**
     * Podem introduir tasques a realitzar per la inicialització de l'API REST.
     */
    @PostConstruct
    private void init() {
        LOG.info("Iniciant API REST INTERNA");
    }
}
