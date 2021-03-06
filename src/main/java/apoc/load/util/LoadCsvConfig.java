package apoc.load.util;

import apoc.load.LoadCsv;

import java.util.*;

import static apoc.util.Util.parseCharFromConfig;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

public class LoadCsvConfig {

    public static final char DEFAULT_ARRAY_SEP = ';';
    public static final char DEFAULT_SEP = ',';
    public static final char DEFAULT_QUOTE_CHAR = '"';

    private char separator;
    private char arraySep;
    private char quoteChar;
    private long skip;
    private boolean hasHeader;
    private long limit;

    private boolean failOnError;

    private EnumSet<Results> results;

    public enum Results {
        map, list, strings, stringMap
    }

    private List<String> ignore;
    private List<String> nullValues;
    private Map<String, Map<String, Object>> mapping;
    private Map<String, LoadCsv.Mapping> mappings;

    public LoadCsvConfig(Map<String, Object> config) {
        if (config == null) {
            config = Collections.emptyMap();
        }
        separator = parseCharFromConfig(config, "sep", DEFAULT_SEP);
        arraySep = parseCharFromConfig(config, "arraySep", DEFAULT_ARRAY_SEP);
        quoteChar = parseCharFromConfig(config,"quoteChar", DEFAULT_QUOTE_CHAR);
        skip = (long) config.getOrDefault("skip", 0L);
        hasHeader = (boolean) config.getOrDefault("header", true);
        limit = (long) config.getOrDefault("limit", Long.MAX_VALUE);
        failOnError = (boolean) config.getOrDefault("failOnError", true);

        results = EnumSet.noneOf(Results.class);
        List<String> resultList = (List<String>) config.getOrDefault("results", asList("map","list"));
        for (String result : resultList) {
            results.add(Results.valueOf(result));
        }

        ignore = (List<String>) config.getOrDefault("ignore", emptyList());
        nullValues = (List<String>) config.getOrDefault("nullValues", emptyList());
        mapping = (Map<String, Map<String, Object>>) config.getOrDefault("mapping", Collections.emptyMap());
        mappings = createMapping(mapping, arraySep, ignore);
    }

    private Map<String, LoadCsv.Mapping> createMapping(Map<String, Map<String, Object>> mapping, char arraySep, List<String> ignore) {
        if (mapping.isEmpty()) return Collections.emptyMap();
        HashMap<String, LoadCsv.Mapping> result = new HashMap<>(mapping.size());
        for (Map.Entry<String, Map<String, Object>> entry : mapping.entrySet()) {
            String name = entry.getKey();
            result.put(name, new LoadCsv.Mapping(name, entry.getValue(), arraySep, ignore.contains(name)));
        }
        return result;
    }

    public char getSeparator() {
        return separator;
    }

    public char getArraySep() {
        return arraySep;
    }

    public long getSkip() {
        return skip;
    }

    public boolean isHasHeader() {
        return hasHeader;
    }

    public long getLimit() {
        return limit;
    }

    public boolean isFailOnError() {
        return failOnError;
    }

    public EnumSet<Results> getResults() {
        return results;
    }

    public List<String> getIgnore() {
        return ignore;
    }

    public List<String> getNullValues() {
        return nullValues;
    }

    public Map<String, Map<String, Object>> getMapping() {
        return mapping;
    }

    public Map<String, LoadCsv.Mapping> getMappings() {
        return mappings;
    }

    public char getQuoteChar() {
        return quoteChar;
    }
}
