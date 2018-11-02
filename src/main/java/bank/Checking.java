

package bank;

import javax.ejb.*;

@Remote
public interface Checking {

    public double getBalance();
    public void doDeduct(double amount);
    public String getAccountNumber();
    public boolean isEmpty();
}
