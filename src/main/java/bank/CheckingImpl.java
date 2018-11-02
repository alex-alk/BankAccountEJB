package bank;

import javax.ejb.Remote;
import javax.ejb.Stateful;
import javax.annotation.PostConstruct;

@Stateful
@Remote
public class CheckingImpl implements Checking {
    private double balance;
    private String accountNumber;
    private boolean empty;
    
    @PostConstruct
    private void init() {
        this.balance = 500;
        this.accountNumber = "1234-567-XXXXX";
    }

    @Override
    public double getBalance() {
        return this.balance;
    }
    
    @Override
    public String getAccountNumber() {
        return this.accountNumber;
    }
    
    @Override
	public boolean isEmpty() {
		return empty;
	}
	
    @Override
    public void doDeduct(double amount) {
        if (amount > this.balance) {
            empty = true;
        } else {
            balance = balance - amount;
        }
    }
    
}
