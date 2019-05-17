package sg.com.wego.util;

import java.util.Comparator;
import java.util.function.Function;

public class SortingUtil {

    public static enum Nulls {FIRST, LAST}
    public static enum Order {ASCENDING, DESCENDING}

    public static <T, R extends Comparable> Comparator<T> comparatorOf(Function<T, R> function, Order order, Nulls nulls) {

        Comparator<R> rComparator = Comparator.naturalOrder();

        if (order == Order.DESCENDING) {
            rComparator = rComparator.reversed();
        }
        rComparator = (nulls == Nulls.FIRST) ? Comparator.nullsFirst(rComparator) : Comparator.nullsLast(rComparator);

        Comparator<T> tComparator = Comparator.comparing(function, rComparator);

        return tComparator;
    }

}
