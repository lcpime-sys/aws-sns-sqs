package mx.aws.sns.core.business.input;

import mx.aws.sns.core.entity.ChangeNotification;
import mx.aws.sns.core.entity.NotificationRequest;
import software.amazon.awssdk.services.sns.model.Topic;

import java.util.List;

public interface SnsService {
    ChangeNotification sendNotification(NotificationRequest notificationRequest);

    String createSNSTopic(String topicName);

    List<Topic> listSNSTopics();

    String subscribeSqsToTopic(String topicArn, String sqsUrl);

    String pubTopic(String message, String topicArn);

    String pubMessageTopicSnsFifoLocal(String message, String topicArn, String nombreEvento);

    String createFIFOTopic(String fifoTopicName);

    String subscribeSnsSqsFifo(String snsTopicFifoArn, String sqsArn);
}
