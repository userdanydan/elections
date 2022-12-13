package com.zetres.elections.util;

import com.zetres.elections.domain.GeoCanton;
import com.zetres.elections.domain.GeoCommune;
import com.zetres.elections.domain.GeoLocalite;
import com.zetres.elections.domain.GeoPrefecture;

import java.util.*;
import java.util.stream.Collectors;

public class MiscUtils {
    public static Set<String> getUniqueCodesSet(List<GeoLocalite> geoLocalites) {
        return geoLocalites.stream()
                .map(GeoLocalite::getCode)
                .collect(Collectors.toSet());
    }
    public static Set<String> getUniqueCodesSetCanton(List<GeoCanton> geoCantons) {
        return geoCantons.stream()
                .map(GeoCanton::getCode)
                .collect(Collectors.toSet());
    }

    //https://stackoverflow.com/questions/8119366/sorting-hashmap-by-values
     public static Map<Integer, String> sortByValue(Map<Integer, String> unsortMap, final boolean order)
    {
        List<Map.Entry<Integer, String>> list = new LinkedList<>(unsortMap.entrySet());

        // Sorting the list based on values
        list.sort((o1, o2) -> order ? o1.getValue().compareTo(o2.getValue()) == 0
                ? o1.getKey().compareTo(o2.getKey())
                : o1.getValue().compareTo(o2.getValue()) : o2.getValue().compareTo(o1.getValue()) == 0
                ? o2.getKey().compareTo(o1.getKey())
                : o2.getValue().compareTo(o1.getValue()));
        return list.stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> b, LinkedHashMap::new));
    }

    public static Set<String> getUniqueCodesSetCommune(List<GeoCommune> geoCommunes) {
        return geoCommunes.stream()
                .map(GeoCommune::getCode)
                .collect(Collectors.toSet());
    }

    public static Set<String> getUniqueCodesSetPrefecture(List<GeoPrefecture> geoPrefectures) {
        return geoPrefectures.stream()
                .map(GeoPrefecture::getCode)
                .collect(Collectors.toSet());
    }
}
