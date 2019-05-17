package sg.com.wego.service.sortable;

import sg.com.wego.model.FareFlightSearchParam;

import java.util.function.Function;

@FunctionalInterface
public interface FareFlightComparator<T, R extends Comparable> {
    Function<T, R> getValue(FareFlightSearchParam fareFlightSearchParam);
}
