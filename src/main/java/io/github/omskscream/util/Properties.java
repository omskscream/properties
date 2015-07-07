package io.github.omskscream.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.nio.charset.Charset;
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

    public void load(InputStream inStream) throws IOException {
        this.load(new InputStreamReader(inStream));
    }

    public void load(Reader reader) throws IOException {
        try(BufferedReader r = new BufferedReader(reader)) {
            r.lines().parallel().forEach(line -> {
                if (isKeyValueLine(line)) {
                    String[] keyVal = line.split("[:=]");
                    if (2 != keyVal.length) {
                        String[] kv = new String[2];
                        kv[0] = keyVal[0];
                        StringBuilder builder = new StringBuilder(keyVal.length - 1);
                        for (int i = 1; i < keyVal.length; ++i) {
                            builder.append(keyVal[i]);
                        }
                        kv[1] = builder.toString();
                        keyVal = kv;
                    }
                    this.map.put(keyVal[0].trim(), keyVal[1].trim());
                }
            });
        }
    }

    public void store(OutputStream out, String comments) throws IOException {
        this.store(new OutputStreamWriter(out, Charset.forName("UTF-8")), comments);
    }

    public void store(Writer writer, String comments) throws IOException {
        try(BufferedWriter w = new BufferedWriter(writer)) {
            for (Map.Entry<String, String> e : this.map.entrySet()) {
                w.write(e.getKey()
                        .replace(" ", "\\ ")
                        + " = " +
                        e.getValue());
                w.newLine();
            }
            w.flush();
        }
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

    private boolean isKeyValueLine(String line) {
        return line.matches("^[^#!].+[:=].+");
    }
}
