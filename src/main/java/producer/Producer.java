package producer;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Producer {

    public static void main(String[] args) throws NamingException, JMSException {

        InitialContext initialContext = new InitialContext();
        Queue requestQueue = (Queue) initialContext.lookup("queue/requestQueue");
/*
DUPS_OK_ACKNOWLEDGE - снимает некоторую нагрузку с сервера, ему больше не нужно думать о том, что сообщение должно быть доставлено только один раз
как это происходит в AUTO_ACKNOWLEDGE
При уточнении JMSContext.SESSION_TRANSACTED сообщения помещаются во временную очередь и по настоящему смогут дойти до получателя только после
строки context.commit
 */
        try (ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
             JMSContext context = connectionFactory.createContext(JMSContext.SESSION_TRANSACTED)) {

            JMSProducer producer = context.createProducer();
            producer.send(requestQueue, "Message 1");
            producer.send(requestQueue, "Message 2");
            producer.send(requestQueue, "Message 3");
            context.commit();
            context.rollback(); //можно отменить отправку сообщений, которые не были закомичены
        }

    }
}
