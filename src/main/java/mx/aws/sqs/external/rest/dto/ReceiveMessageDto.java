package mx.aws.sqs.external.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import software.amazon.awssdk.services.sqs.model.Message;

@Getter
@Setter
@Builder
@Schema(name = "ReceiveMessage", description = "Modelo para recibir un mensaje")
public class ReceiveMessageDto {

    @JsonProperty
    private String MessageId;
    @JsonProperty
    private String ReceiptHandle;
    @JsonProperty
    private String MD5OfBody;
    @JsonProperty
    private String Body;


    public static ReceiveMessageDto fromEntity(Message snsMessage) {
        return ReceiveMessageDto.builder()
                .Body(snsMessage.body())
                .MessageId(snsMessage.messageId())
                .MD5OfBody(snsMessage.md5OfBody())
                .build();
    }
}


