package io.github.octcarp.sustech.cs209a.practice.p7t1;

public class DepositThread implements Runnable {
    private Account account;
    private double money;

    public DepositThread(Account account, double money) {
        this.account = account;
        this.money = money;
    }

    @Override
    public void run() {
        account.deposit(money);
    }
}
