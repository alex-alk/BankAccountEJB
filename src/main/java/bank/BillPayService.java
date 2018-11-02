package bank;

import javax.ejb.Remote;
import javax.ejb.Singleton;
import java.util.*;
import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

@Singleton
@Remote
public class BillPayService implements BillPay {
    @Resource(mappedName = "java:/ConnectionFactory")
    private ConnectionFactory connectionFactory;
    @Resource(mappedName = "java:/jms/queue/DLQ")
    private javax.jms.Queue queue;
    boolean ok = false;
    
    @Override
    public List<String> getPayees() {
        List<String> l = new ArrayList<>();
        l.add("Energie");
        l.add("Apă");
        l.add("Ipotecă");
        l.add("Cablu");
        return l;
    }
    
    @Override
    public String doPay(String accountNumber, String payee, double amount){
        String confirmation = new Long(System.currentTimeMillis()).toString();
        while(!ok) {
        	this.updateBackofficeRecords(accountNumber, payee, amount, confirmation, new Date());
        }
        ok = false;   
        return confirmation;
    }
    
    private void updateBackofficeRecords(String accountNumber, String payee, double amount, String confirmation, Date d) {
        try (Connection connection = connectionFactory.createConnection()){
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer messageProducer = session.createProducer(queue);
            TextMessage message = session.createTextMessage();
            if(payee.equals("Cablu")) {
            	message.setText(accountNumber + ":" + payee + " plătit: RON" + amount + " ["+confirmation+"]");
            	
            }else {
                message.setText(accountNumber + ": " + payee + " plătită: RON" + amount + " ["+confirmation+"]"); 	
            }
            messageProducer.send(message);
            ok = true;
        } catch (JMSException e) {
            ok = false;
        }   
    }
}
