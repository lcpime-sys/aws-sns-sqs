package mx.aws.sns.core.business.implementation;

import lombok.extern.slf4j.Slf4j;
import mx.aws.sns.core.business.input.SnsService;
import mx.aws.sns.core.entity.ChangeNotification;
import mx.aws.sns.core.entity.NotificationRequest;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.*;
import software.amazon.awssdk.services.sqs.SqsClient;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@ApplicationScoped
public class SnsBs implements SnsService {


    @ConfigProperty(name = "aws.region")
    String awsRegion;
    @ConfigProperty(name="sns.topic.arn")
    String snsArn;

    private SnsClient snsClient;

    @PostConstruct
    void init() {
        snsClient = SnsClient.builder()
                .region(Region.of(awsRegion))
                .build();
    }


    /**
    * Envia una notificacion un arn sns
    * */
    @Override
    public ChangeNotification sendNotification(NotificationRequest notificationRequest){
        ChangeNotification changeNotification = ChangeNotification.builder()
                .sentMessage(notificationRequest.getMessage()).build();
        try{
//            init();
            PublishRequest request = PublishRequest.builder()
                    .message(String.format("{\"message\": \"%s\"}", notificationRequest.getMessage()))
                    .topicArn(snsArn)
                    .messageGroupId("1")
                    .build();
            PublishResponse response = snsClient.publish(request);
            log.info("{} Message sent. Status was {}",response.messageId(), response.sdkHttpResponse().statusCode());
            changeNotification.setId(response.messageId());
        }catch (SnsException exception){
            log.error(exception.awsErrorDetails().errorMessage());
        }
        return changeNotification;
    }


    /**
     *
     * Crea un tema sns estandar
     * */
    @Override
    public String createSNSTopic(String topicName) {
        CreateTopicResponse result = null;
        init();
        try {
            CreateTopicRequest request = CreateTopicRequest.builder()
                    .name(topicName)
                    .build();
            result = snsClient.createTopic(request);
            return result.topicArn();
        } catch (SnsException e) {
            log.error(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return result.topicArn();
    }


    /**
     *
     * Lista los temas de un sns
     * */
    @Override
    public List<Topic> listSNSTopics() {
        List<Topic> result = null;
        try {
            init();
            ListTopicsRequest request = ListTopicsRequest.builder()
                    .build();
            result = snsClient.listTopics(request).topics();
        } catch (SnsException e) {
            log.error(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return result;
    }




    /**
     *
     * Subscribe un sqs estandar a un tema estandar
     * */
    @Override
    public String subscribeSqsToTopic(String topicArn, String sqsUrl) {
        SubscribeResponse result = null;
        try {
            init();
            SubscribeRequest request = SubscribeRequest.builder()
                    .protocol("sqs")
                    .endpoint(sqsUrl)
                    .returnSubscriptionArn(true)
                    .topicArn(topicArn)
                    .build();

            result = snsClient.subscribe(request);
            log.info("Subscription ARN: {} \n\n Status is {}", result.subscriptionArn(), result.sdkHttpResponse().statusCode());
        } catch (SnsException e) {
            log.error(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return result.subscriptionArn();
    }


    /**
     *
     * Envia un mensaje a un tema, que se replica a todas las subscripciones que tenga ese tema
     * TODO: Hacer pruebas con policys
     * */
    @Override
    public String pubTopic(String message, String topicArn) {
        return null;
    }

    /**
     *
     * Envia un mensaje a un tema, que se replica a todas las subscripciones que tenga ese tema
     * TODO: Hacer pruebas con policys
     * */
    @Override
    public String pubMessageTopicSnsFifoLocal(String message, String topicArn, String nombreEvento){
        return "";
    }

    /**
     * Crea un sns de tipo fifo
     * **/
    @Override
    public String createFIFOTopic(String fifoTopicName){
        String topicArn = null;
        try {
            init();
            Map<String, String> topicAttributes = new HashMap<>();
            topicAttributes.put("FifoTopic", "true");
            topicAttributes.put("ContentBasedDeduplication", "false");

            CreateTopicRequest topicRequest = CreateTopicRequest.builder()
                    .name(fifoTopicName+".fifo")
                    .attributes(topicAttributes)
                    .build();

            CreateTopicResponse response = snsClient.createTopic(topicRequest);
            topicArn = response.topicArn();
            log.info("The topic ARN is {}", topicArn);
        } catch (SnsException e) {
            log.error(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return topicArn;
    }

    /**
     * Crea una subscripcion de un topic sns a una cola sqs
     * **/
    @Override
    public String subscribeSnsSqsFifo(String snsTopicFifoArn, String sqsArn){
        SubscribeResponse result = null;
        try {
            init();
            SubscribeRequest subscribeRequest = SubscribeRequest.builder()
                    .topicArn(snsTopicFifoArn)
                    .endpoint(sqsArn)
                    .protocol("sqs")
                    .build();
            result = snsClient.subscribe(subscribeRequest);
            log.info("The topic is subscribed to the queue.");
        } catch (SnsException e) {
            log.error(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return result.subscriptionArn();
    }


}
