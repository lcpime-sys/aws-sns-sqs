package mx.aws.sqs.core.business.input;

import software.amazon.awssdk.services.sqs.model.ListQueuesResponse;

public interface SqsService {
    String receiveMessage(String queueUrl);

    String createQueue(String queueName, Integer typeQueue);

    ListQueuesResponse listQueues();

    String getAttributesQueue(String queueUrl);
}
