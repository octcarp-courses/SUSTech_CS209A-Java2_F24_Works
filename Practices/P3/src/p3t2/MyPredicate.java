package p3t2;

@FunctionalInterface
public interface MyPredicate<T> {
    boolean test(T t);
}