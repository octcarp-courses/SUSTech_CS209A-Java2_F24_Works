package io.github.octcarp.sustech.cs209a.practice.p7t1;

import java.util.concurrent.locks.ReentrantLock;

public abstract class Account {
    private double balance;

    /**
     * @param money the amount of money to deposit
     */
    public abstract void deposit(double money);
//    public void deposit(double money) {
//		try {
//			double newBalance = balance + money;
//			try {
//				Thread.sleep(10);   // Simulating this service takes some processing time
//			} catch (InterruptedException ex) {
//				ex.printStackTrace();
//			}
//			balance = newBalance;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

    public Account() {
        balance = 0;
    }

    void setBalance(double balance) {
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }
}

class SyncAccount extends Account {
    @Override
    public synchronized void deposit(double money) {
        try {
            double newBalance = getBalance() + money;
            try {
                Thread.sleep(10);   // Simulating this service takes some processing time
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            setBalance(newBalance);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class LockAccount extends Account {
    private final ReentrantLock lock = new ReentrantLock();

    @Override
    public void deposit(double money) {
        lock.lock();
        try {
            double newBalance = getBalance() + money;
            try {
                Thread.sleep(10);   // Simulating this service takes some processing time
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            setBalance(newBalance);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}