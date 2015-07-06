package io.github.omskscream.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Properties implements Serializable, Cloneable {

    private static final long serialVersionUID = 66613136661313L;

    private final Map<String, String> map;
    private final Properties defaults;

    public Properties() {
        this(null);
    }

    public Properties(Properties defaults) {
        this.defaults = defaults;
        this.map = new ConcurrentHashMap<>();
    }

    public String getProperty(String key) {
        return map.get(key);
    }

    public String getProperty(String key, String defaultValue) {
        return map.getOrDefault(key, defaultValue);
    }

    public String setProperty(String key, String value) {
        return map.put(key, value);
    }

    public void load(InputStream inStream) {
        //TODO
    }

    public void load(Reader reader) {
        //TODO
    }

    public void store(OutputStream out, String comments) {
        //TODO
    }

    public void store(Writer writer, String comments) {
        //TODO
    }

    public Set<String> propertyNames() {
        Set<String> result = new HashSet<>(map.keySet());
        if (defaults != null) {
            result.addAll(defaults.map.keySet());
        }
        return result;
    }

    public void list(PrintStream out) {
        Map<String, String> view = new HashMap<>(this.map);
        view.forEach((key,val) -> {
            if (val.length() > 80) {
                val = val.substring(0, 77) + "...";
            }
            out.println(key + " = " + val);
        });
    }

    public void list(PrintWriter out) {
        Map<String, String> view = new HashMap<>(this.map);
        view.forEach((key,val) -> {
            if (val.length() > 80) {
                val = val.substring(0, 77) + "...";
            }
            out.println(key + " = " + val);
        });
    }

    public int size() {
        return map.size();
    }
}
