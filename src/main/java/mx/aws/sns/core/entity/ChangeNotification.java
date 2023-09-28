package mx.aws.sns.core.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChangeNotification {
    String id;
    String sentMessage;
}
