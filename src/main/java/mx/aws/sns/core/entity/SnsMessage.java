package mx.aws.sns.core.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class SnsMessage {
    private String Type;
    private String Message;
    private String MessageId;
    private String TopicArn;

}
