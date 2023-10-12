import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

public class Range<T extends Comparable<T>> implements Set<T>, Comparable<Range<T>> {

    private static final String UOE_DELETE_LITERAL = "You can not remove elements from the range";

    private T start;
    private T end;

    private Function<T, T> incrementFunction;

    private Range() {


        incrementFunction = t -> {
            switch (t.getClass().getSimpleName()){
                case "Integer" -> {
                    return (T) Integer.valueOf(((Integer) t) + 1);
                }
                case "Long" -> {
                    return (T) Long.valueOf(((Long) t) + 1);
                }
                case "Double" -> {
                    return (T) Double.valueOf(((Double) t) + 0.1);
                }
                case "Float" ->{
                    return (T) Float.valueOf(((Float) t) + 0.1f);
                }
                default -> throw new IllegalArgumentException("Unsupported type");
            }
        };
    }

    private Range(T start, T end){
        this();
        this.start = start;
        this.end = end;
    }


    public static <T extends Number & Comparable<T>> Range<T> of(T  from, T to) {
        if(from.compareTo(to) > 0){
            throw new IllegalArgumentException("Starting value can not be bigger then ending value");
        }

        return new Range<>(from, to);
    }

    public static <T extends Comparable<T>> Range<T> of(T from, T to, Function<T, T> incrementFunction) {
        if(from == null || to == null || incrementFunction == null){
            throw new IllegalArgumentException("Can not use null variables");
        }

        Range<T> range = new Range<>(from, to);
        range.incrementFunction = incrementFunction;

        return range;
    }

    @Override
    public boolean add(T element) {
        throw new UnsupportedOperationException("You can not add new elements to the range");
    }

    @Override
    public boolean addAll(Collection<? extends T> collection) {
        throw new UnsupportedOperationException("You can not add new elements to the range");
    }

    @Override
    public boolean contains(Object o) {
        return ((T)o).compareTo(this.start) >= 0 || ((T)o).compareTo(this.end) < 0;
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        for(Object element : collection){
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void clear() {
        //To avoid returning null
        this.start = this.end;
    }

    @Override
    public boolean isEmpty() {
        return this.end.equals(this.start);
    }

    @Override
    public int size() {
        int size = 0;
        for(T i = this.start; i.compareTo(this.end) < 0; i = this.incrementFunction.apply(i)){
            size++;
        }
        return size;
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException(Range.UOE_DELETE_LITERAL);
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        throw new UnsupportedOperationException(Range.UOE_DELETE_LITERAL);
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        throw new UnsupportedOperationException(Range.UOE_DELETE_LITERAL);
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            private T current = start;

            @Override
            public boolean hasNext() {
                return current.compareTo(end) < 0;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                T next = current;
                current = incrementFunction.apply(current);
                return next;
            }
        };
    }

    @Override
    public T[] toArray() {
        throw new UnsupportedOperationException("You can not transfer range to Array");
    }


    @Override
    public <E> E[] toArray(E[] a) {
        throw new UnsupportedOperationException("You can not transfer range to Array");
    }

    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("0.0"); // Format for Float and Double
        df.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        T temp = this.start;
        for(int i = 0; i < size(); i++){
            if (temp instanceof Float || temp instanceof Double) {
                sb.append(df.format(temp));
                temp = incrementFunction.apply(temp);
            } else {
                sb.append(temp.toString());
            }
            sb.append(", ");
        }
        if (temp.equals(end)) {
            sb.delete(sb.length() - 2, sb.length()); // Remove trailing comma and space
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public int compareTo(Range<T> other) {
        return Integer.compare(this.size(), other.size());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Range<?> range)) {
            return false;
        }
        return Objects.equals(start, range.start) &&
            Objects.equals(end, range.end) &&
            Objects.equals(incrementFunction, range.incrementFunction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end, incrementFunction);
    }
}
