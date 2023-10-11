import java.text.DecimalFormatSymbols;
import java.util.*;
import java.text.DecimalFormat;
import java.util.function.Function;

public class Range<T extends Comparable<T>> implements Set<T> {
    private final Set<T> elements;

    public Range() {
        elements = new HashSet<>();
    }

    public static <T extends Comparable<T>> Range<T> of(T from, T to) {
        if(from.compareTo(to) > 0){
            throw new IllegalArgumentException("Starting value can not be bigger then ending value");
        }

        Range<T> range = new Range<>();
        while (from.compareTo(to) < 0) {
            range.add(from);
            from = defaultIncrement(from);
        }
        return range;
    }

    private static <T extends Comparable<T>> T defaultIncrement(T value) {

        switch (value.getClass().getSimpleName()){
            case "Integer" -> {
                return (T) Integer.valueOf(((Integer) value) + 1);
            }
            case "Long" -> {
                return (T) Long.valueOf(((Long) value) + 1);
            }
            case "Double" -> {
                return (T) Double.valueOf(((Double) value) + 0.1);
            }
            case "Float" ->{
                return (T) Float.valueOf(((Float) value) + 0.1f);
            }
            default -> throw new IllegalArgumentException("Unsupported type");
        }

    }

    public static <T extends Comparable<T>> Range<T> of(T from, T to, Function<T, T> incrementFunction) {
        Range<T> range = new Range<>();
        while (from.compareTo(to) < 0) {
            range.add(from);
            from = incrementFunction.apply(from);
        }
        return range;
    }

    @Override
    public boolean add(T element) {
        return elements.add(element);
    }

    @Override
    public boolean addAll(Collection<? extends T> collection) {
        return elements.addAll(collection);
    }

    @Override
    public boolean contains(Object o) {
        return elements.contains(o);
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return elements.containsAll(collection);
    }

    @Override
    public void clear() {
        elements.clear();
    }

    @Override
    public boolean isEmpty() {
        return elements.isEmpty();
    }

    @Override
    public int size() {
        return elements.size();
    }

    @Override
    public boolean remove(Object o) {
        return elements.remove(o);
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        return elements.removeAll(collection);
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        return elements.retainAll(collection);
    }

    @Override
    public Iterator<T> iterator() {
        return elements.iterator();
    }

    @Override
    public Object[] toArray() {
        return elements.toArray();
    }

    @Override
    public <E> E[] toArray(E[] a) {
        return elements.toArray(a);
    }

    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("0.0"); // Format for Float and Double
        df.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (T element : elements) {
            if (element instanceof Float || element instanceof Double) {
                sb.append(df.format(element));
            } else {
                sb.append(element.toString());
            }
            sb.append(", ");
        }
        if (!elements.isEmpty()) {
            sb.delete(sb.length() - 2, sb.length()); // Remove trailing comma and space
        }
        sb.append("]");
        return sb.toString();
    }

}
