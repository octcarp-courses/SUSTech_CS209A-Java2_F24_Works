package p4t1;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

// Main example class
class Subject {
    private String state;

    private final List<Consumer<String>> observers = new ArrayList<>();

    public void subscribe(Consumer<String> observer) {
        observers.add(observer);
    }

    public void unsubscribe(Consumer<String> observer) {
        observers.remove(observer);
    }

    public void changeState(String state) {
        this.state = state;
        notifyObservers();
    }

    public void notifyObservers() {
        for (Consumer<String> observer : observers) {
            observer.accept(this.state);
        }
    }
}

public class ObserverPattern {
    public static void main(String[] args) {
        // Create the subject
        Subject subject = new Subject();

        // Define observer 1: print the state change
        Consumer<String> observer1 = s -> System.out.println("Observer 1 received state update: " + s);

        // Define observer 2: log the state change
        Consumer<String> observer2 = s -> System.out.println("Observer 2 logged: State changed to " + s);

        // Define observer 3: perform specific actions
        Consumer<String> observer3 = s -> System.out.println("Observer 3 performing " + s + " action.");

        // Subscribe the observers
        subject.subscribe(observer1);
        subject.subscribe(observer2);
        subject.subscribe(observer3);

        // Change state
        System.out.println("Changing state to 'START'.");
        subject.changeState("START");

        System.out.println("\nChanging state to 'STOP'.");
        subject.changeState("STOP");

        // Unsubscribe observer 2
        subject.unsubscribe(observer2);

        System.out.println("\nChanging state to 'RESTART', observer 2 is unsubscribed.");
        subject.changeState("RESTART");
    }
}