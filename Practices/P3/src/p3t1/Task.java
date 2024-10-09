package p3t1;

/**
 * An item with a description and a part number.
 */
public class Task implements Comparable<Task> {
    private String description;
    private int partNumber;

    public Task(String aDescription, int aPartNumber) {
        description = aDescription;
        partNumber = aPartNumber;
    }

    public String getDescription() {
        return description;
    }

    public int getPartNumber() {
        return partNumber;
    }

    @Override
    public String toString() {
        return "task1.Task{description='" + description + "', priority=" + partNumber + "}";
    }

    public int compareTo(Task other) {
        int diff = -Integer.compare(partNumber, other.getPartNumber());
        return diff != 0 ? diff : description.compareTo(other.getDescription());
    }
}