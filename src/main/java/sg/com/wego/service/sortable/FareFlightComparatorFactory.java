package sg.com.wego.service.sortable;

import sg.com.wego.cache.entity.FareFlight;
import sg.com.wego.model.FareFlightSearchParam;

import java.util.function.Function;

public class FareFlightComparatorFactory {

    public static Function<FareFlight, Comparable> getFareFlightComparator(FareFlightSearchParam fareFlightSearchParam) {
        switch (fareFlightSearchParam) {
            case PRICE:
                return FareFlight::getBasePrice;
            default:
                return FareFlight::getId;
        }
    }

}
