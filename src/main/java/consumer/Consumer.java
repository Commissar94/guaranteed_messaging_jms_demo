package consumer;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Consumer {

    public static void main(String[] args) throws NamingException, JMSException {

        InitialContext initialContext = new InitialContext();
        Queue requestQueue = (Queue) initialContext.lookup("queue/requestQueue");
/*
CLIENT_ACKNOWLEDGE - показывает серверу, что читающий сообщение берет на себя ответственность сообщить о его прочтении
 */
        try (ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
             JMSContext context = connectionFactory.createContext(JMSContext.SESSION_TRANSACTED)) {

            JMSConsumer consumer = context.createConsumer(requestQueue);
            TextMessage msg = (TextMessage) consumer.receive();
            System.out.println(msg.getText());
            context.commit(); //при SESSION_TRANSACTED коммит обязателен для подтверждения прочтения и удаления сообщения из очереди
            
            // msg.acknowledge(); // информируем сервер о том что сообщение прочитано и его можно удалить, без этого,
            // сообщение можно читать сколько угодно раз (при CLIENT_ACKNOWLEDGE)
        }

    }
}
