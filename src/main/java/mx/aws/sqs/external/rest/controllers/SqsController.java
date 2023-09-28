package mx.aws.sqs.external.rest.controllers;


import antlr.collections.List;
import mx.aws.sqs.core.business.input.SqsService;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.stream.Collectors;

@Path("sqs")
@Tag(name = "Prueba Sqs", description = "Prueba sqs")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SqsController {

    @Inject
    SqsService sqsService;

    @GET
    @Path("/receive-sqs")
    @APIResponse(responseCode = "200", description = "ok", content = @Content(schema = @Schema(implementation = Boolean.class)))
    @Operation(operationId = "receiveNotificationSqs", summary = "Recibe una notificacion", description = "Recibe una notificacion")
    public Response receiveNotificationSqs(@QueryParam("queueUrl") String queueUrl){
        var notificaciones = sqsService.receiveMessage(queueUrl);
        return Response.ok(notificaciones).build();
    }

    @POST
    @Path("/create-queue/{typeQueue}")
    @APIResponse(responseCode = "200", description = "ok", content = @Content(schema = @Schema(implementation = Boolean.class)))
    @Operation(operationId = "createQueue", summary = "Crea una cola", description = "Crea una cola de mensajes")
    public Response createQueue(@QueryParam("nombreQueue") String nombreQueue, @PathParam("typeQueue") Integer typeQueue){
        String url = sqsService.createQueue(nombreQueue, typeQueue);
        return Response.ok(url).build();
    }

    @GET
    @Path("/list-queue")
    @APIResponse(responseCode = "200", description = "ok", content = @Content(schema = @Schema(implementation = List.class)))
    @Operation(operationId = "listQueues", summary = "Lista las colas", description = "Lista las colas disponibles")
    public Response listQueues(){
        var notificaciones = sqsService.listQueues().queueUrls().stream().collect(Collectors.toList());
        return Response.ok(notificaciones).build();
    }

    @GET
    @Path("/info-queue")
    @APIResponse(responseCode = "200", description = "ok", content = @Content(schema = @Schema(implementation = List.class)))
    @Operation(operationId = "infoQueue", summary = "Obtienes los atributos de una queue", description = "Obtiene los atributos de una queue")
    public Response infoQueue(@QueryParam("url") String urlQueue){
        var notificaciones = sqsService.getAttributesQueue(urlQueue);
        return Response.ok(notificaciones).build();
    }

}
