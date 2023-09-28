package mx.aws.sqs.core.business.implementation;

import lombok.extern.slf4j.Slf4j;
import mx.aws.sqs.core.business.input.SqsService;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@ApplicationScoped
public class SqsBs implements SqsService {

    @ConfigProperty(name = "aws.region")
    String awsRegion;
    @ConfigProperty(name = "sqs.url")
    String sqsUrl;
    private SqsClient sqsClient;

    @PostConstruct
    void init() {
        sqsClient = SqsClient.builder()
                .region(Region.of(awsRegion))
                .build();
    }



    @Override
    public String receiveMessage(String queueUrl) {
        String message = "";
        try {
            ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .build();
            message = sqsClient.receiveMessage(receiveMessageRequest).messages().get(0).body();
            log.info("prueba  messages {}", message);
        } catch (SqsException e) {
            log.error(e.awsErrorDetails().errorMessage());
        }
        return message;
    }

    @Override
    public String createQueue(String queueName, Integer typeQueue) {
        String queueUrl = null;
        CreateQueueResponse createResult;
        try {
            if (typeQueue == 1) {
                queueName = queueName + ".fifo";
                Map<QueueAttributeName, String> attributes = new HashMap<>();
                attributes.put(QueueAttributeName.FIFO_QUEUE, Boolean.TRUE.toString());
                CreateQueueRequest request = CreateQueueRequest.builder()
                        .queueName(queueName)
                        .attributes(attributes)
                        .build();
                createResult = sqsClient.createQueue(request);
            } else {
                CreateQueueRequest request = CreateQueueRequest.builder()
                        .queueName(queueName)
                        .build();
                createResult = sqsClient.createQueue(request);
            }
            queueUrl = createResult.queueUrl();
        } catch (SqsException e) {
            log.error(e.awsErrorDetails().errorMessage());
        }
        return queueUrl;
    }


    @Override
    public ListQueuesResponse listQueues(){
        ListQueuesResponse list = null;
        try {
            list = sqsClient.listQueues();
        }catch (SqsException e){
            log.error(e.awsErrorDetails().errorMessage());
        }
        return list;
    }

    @Override
    public String getAttributesQueue(String queueUrl){
        String  sqsQueueArn = null;
        try {
            Map<QueueAttributeName, String> sqsAttrs = sqsClient.getQueueAttributes(GetQueueAttributesRequest.builder()
                    .queueUrl(queueUrl)
                    .attributeNames(QueueAttributeName.QUEUE_ARN)
                    .build()).attributes();
            sqsQueueArn = sqsAttrs.get(QueueAttributeName.QUEUE_ARN);
        }catch (SqsException e){
            log.error(e.awsErrorDetails().errorMessage());
        }
        return sqsQueueArn;
    }




}
