package p7t2;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MyBlockingQueue<E> {
    private Queue<E> queue;
    private int capacity;

    private ReentrantLock lock = new ReentrantLock();
    private Condition notEmpty = lock.newCondition();
    private Condition notFull = lock.newCondition();
	
    public MyBlockingQueue(int capacity){
        //TODO: Constructor with input capacity
    }

    public void put(E e){
        //TODO: When the queue is full,  wait until the consumer takes data and the queue has some empty buffer
 
    }

    public E take() {
        //TODO: When queue empty,   wait until the producer puts new data into the queue

    }

    public static void main(String[] args) throws InterruptedException {

        int CAPACITY = 200;
        int PRODUCER_WORK = 20;
        int PRODUCER_CNT = 10;
        int PRODUCER_OFF = 10;
        int CONSUMER_WORK = 20;
        int CONSUMER_CNT = 10;
        int CONSUMER_OFF = 10;

        MyBlockingQueue<Integer> queue = new MyBlockingQueue<>(CAPACITY);

        Runnable producer = () -> {
            for(int i=0; i<PRODUCER_WORK; i++){
                queue.put(i);
                try {
                    Thread.sleep(PRODUCER_OFF);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Runnable consumer = () -> {
            for(int i=0; i<CONSUMER_WORK; i++){
                queue.take();
                try {
                    Thread.sleep(CONSUMER_OFF);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        for (int i = 0; i < PRODUCER_CNT; i++) {
            new Thread(producer).start();
        }
        for (int i = 0; i < CONSUMER_CNT; i++) {
            new Thread(consumer).start();
        }

    }

}
