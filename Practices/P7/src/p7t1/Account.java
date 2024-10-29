package p7t1;

public class Account {
    private double balance;

    /**
     *
     * @param money
     */
    public void deposit(double money) {
        try {
			double newBalance = balance + money;
			try {
			    Thread.sleep(10);   // Simulating this service takes some processing time
			}
			catch(InterruptedException ex) {
			    ex.printStackTrace();
			}
			balance = newBalance;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }


    public double getBalance() {
        return balance;
    }
}