package cc.dreamcode.platform;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public interface DreamLogger {

    void info(String text);

    void debug(String text);

    void warning(String text);

    void error(String text);

    void error(String text, Throwable throwable);

    // Logging builder from okaeri-platform with features
    class Builder {

        private String type;
        private String name;
        private Long took;

        private final Map<String, Object> meta = new TreeMap<>();
        private final List<String> footer = new ArrayList<>();

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder meta(String name, Object value) {
            this.meta.put(name, value);
            return this;
        }

        public Builder meta(Map<String, Object> metas) {
            this.meta.putAll(metas);
            return this;
        }

        public Builder took(long took) {
            this.took = took;
            return this;
        }

        public String build() {

            if (this.type == null) {
                throw new IllegalArgumentException("type cannot be null");
            }

            StringBuilder metaBuilder = new StringBuilder();
            metaBuilder.append(this.type);

            if (this.name == null) {
                throw new IllegalArgumentException("name cannot be null");
            }

            metaBuilder.append(": ");
            metaBuilder.append(this.name);

            if (!this.meta.isEmpty()) {
                metaBuilder.append(" { ");
                metaBuilder.append(this.meta.entrySet().stream()
                        .map(entry -> {
                            Object rendered = entry.getValue();
                            if (rendered instanceof String) {
                                rendered = "'" + rendered + "'";
                            }
                            return entry.getKey() + " = " + rendered;
                        })
                        .collect(Collectors.joining(", ")));
                metaBuilder.append(" }");
            }

            if (this.took != null) {
                metaBuilder.append(" [");
                metaBuilder.append(this.took);
                metaBuilder.append(" ms]");
            }

            if (!this.footer.isEmpty()) {
                for (String line : this.footer) {
                    metaBuilder.append("\n");
                    metaBuilder.append(line);
                }
            }

            return metaBuilder.toString();
        }

        public Builder footer(String line) {
            this.footer.add(line);
            return this;
        }
    }
}
