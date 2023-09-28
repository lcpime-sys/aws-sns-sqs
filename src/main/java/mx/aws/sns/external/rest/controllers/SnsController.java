package mx.aws.sns.external.rest.controllers;

import mx.aws.sns.core.business.input.SnsService;
import mx.aws.sns.core.entity.ChangeNotification;
import mx.aws.sns.core.entity.NotificationRequest;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import software.amazon.awssdk.services.sns.model.Topic;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.stream.Collectors;

@Path("sns")
@Tag(name = "Prueba Sns", description = "Prueba envio sns")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SnsController {


    @Inject
    SnsService snsExamplesService;

    @POST
    @Path("/send")
    @APIResponse(responseCode = "200", description = "ok", content = @Content(schema = @Schema(implementation = Integer.class)))
    @Operation(operationId = "sendNotification", summary = "Envia una notificacion", description = "Envia una notificacion")
    public Response sendNotificationSns(@RequestBody NotificationRequest notificationRequest) {
        ChangeNotification sent = snsExamplesService.sendNotification(notificationRequest);
        return Response.ok(sent).build();
    }


    @POST
    @Path("/create-topic-sns")
    @APIResponse(responseCode = "200", description = "ok", content = @Content(schema = @Schema(implementation = Integer.class)))
    @Operation(operationId = "createTopicSns", summary = "Crea un tema sns", description = "Crea un tema sns")
    public Response createTopicSns(@QueryParam("nombre") String nombre) {
        String snsTopic = snsExamplesService.createSNSTopic(nombre);
        return Response.ok(snsTopic).build();
    }

    @POST
    @Path("/create-fifo-topic-sns")
    @APIResponse(responseCode = "200", description = "ok", content = @Content(schema = @Schema(implementation = Integer.class)))
    @Operation(operationId = "createFifoTopicSns", summary = "Crea un tema fifo sns", description = "Crea un tema fifo sns")
    public Response createFifoTopicSns(@QueryParam("nombre") String nombre) {
        String snsFifoTopic = snsExamplesService.createFIFOTopic(nombre);
        return Response.ok(snsFifoTopic).build();
    }

    @POST
    @Path("/subscribe-sqsFifo-snsFifo")
    @APIResponse(responseCode = "200", description = "ok", content = @Content(schema = @Schema(implementation = Integer.class)))
    @Operation(operationId = "subscribeSqsSnsFifo", summary = "Subscribe un sns a un sqs", description = "Subscribe un sns a un sqs")
    public Response subscribeSqsSnsFifo(@QueryParam("topicArn") String topicArn, @QueryParam("sqsUrl") String sqsUrl) {
        String snsTopic = snsExamplesService.subscribeSnsSqsFifo(topicArn, sqsUrl);
        return Response.ok(snsTopic).build();
    }


    @POST
    @Path("/subscribe-sqs-sns")
    @APIResponse(responseCode = "200", description = "ok", content = @Content(schema = @Schema(implementation = Integer.class)))
    @Operation(operationId = "subscribeSqsSns", summary = "Subscribe un sns a un sqs", description = "Subscribe un sns a un sqs")
    public Response subscribeSqsSns(@QueryParam("topicArn") String topicArn, @QueryParam("sqsUrl") String sqsUrl) {
        String snsTopic = snsExamplesService.subscribeSqsToTopic(topicArn, sqsUrl);
        return Response.ok(snsTopic).build();
    }

    @POST
    @Path("/pub-message-topic")
    @APIResponse(responseCode = "200", description = "ok", content = @Content(schema = @Schema(implementation = Integer.class)))
    @Operation(operationId = "pubMessageTopic", summary = "Envia un mensaje a un tema sns", description = "Envia un mensaje a un tema sns")
    public Response pubMessageTopic(@QueryParam("message") String message, @QueryParam("topicArn") String topicArn) {
        String snsTopic = snsExamplesService.pubTopic(message, topicArn);
        return Response.ok(snsTopic).build();
    }

    @POST
    @Path("/pub-message-topic-fifo")
    @APIResponse(responseCode = "200", description = "ok", content = @Content(schema = @Schema(implementation = Integer.class)))
    @Operation(operationId = "pubMessageTopicFifo", summary = "Envia un mensaje a un tema sns fifo", description = "Envia un mensaje a un tema sns fifo")
    public Response pubMessageTopicFifo(@QueryParam("message") String message, @QueryParam("topicArn") String topicArn, @QueryParam("nombreEvento") String nombreEvento) {
        String snsTopic = snsExamplesService.pubMessageTopicSnsFifoLocal(message, topicArn, nombreEvento);
        return Response.ok(snsTopic).build();
    }

    @GET
    @Path("/list-sns-topics")
    @APIResponse(responseCode = "200", description = "ok", content = @Content(schema = @Schema(implementation = Boolean.class)))
    @Operation(operationId = "listSnsTopics", summary = "Lista los temas sns", description = "Lista los temas sns")
    public Response listSnsTopics() {
        var list = snsExamplesService.listSNSTopics().stream().map(Topic::topicArn).collect(Collectors.toList());
        return Response.ok(list).build();
    }


}
