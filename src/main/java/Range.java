import java.text.DecimalFormatSymbols;
import java.util.*;
import java.text.DecimalFormat;

public class Range<T extends Number & Comparable<T>> implements Set<T> {
    private final SortedSet<T> elements;

    public Range() {
        elements = new TreeSet<>();
    }

    public static <T extends Number & Comparable<T>> Range<T> of(T from, T to) {
        if(from.compareTo(to) > 0){
            throw new IllegalArgumentException("Starting value can not be bigger then ending value");
        }

        Range<T> range = new Range<>();
        String type = String.valueOf(from.getClass());

        //in case of necessity can be extended for all classes that extend Number
        switch (type){
            case "class java.lang.Integer" ->{
                int startInt = from.intValue();
                int endInt = to.intValue();
                for (int i = startInt; i < endInt; i++) {
                    range.add((T) Integer.valueOf(i));
                }
            }
            case "class java.lang.Float" ->{
                float startFloat = from.floatValue();
                float endFloat = to.floatValue();
                float step = 0.1f;
                for (float i = startFloat; i < endFloat; i += step) {
                    range.add((T) Float.valueOf(i));
                }
            }
            case "class java.lang.Double" ->{
                double startDouble = from.doubleValue();
                double endDouble = to.doubleValue();
                double step = 0.1;
                for (double i = startDouble; i < endDouble; i += step) {
                    range.add((T) Double.valueOf(i));
                }
            }
            case "class java.lang.Long" ->{
                long startLong = from.longValue();
                long endLong = to.longValue();
                for (long i = startLong; i < endLong; i++) {
                    range.add((T) Long.valueOf(i));
                }
            }
            default -> throw new IllegalArgumentException("Unsupported type");
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
