/*
 * Copyright (c) 2024 - Nathanne Isip
 * This file is part of Ambassador.
 *
 * Ambassador is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * Ambassador is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Ambassador. If not, see <https://www.gnu.org/licenses/>.
 */
package web.ambassador.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The {@code PathPattern} class is responsible for matching URL paths against
 * a pattern and extracting dynamic parameters from the path.
 *
 * This class allows defining path patterns with placeholders enclosed in curly braces (e.g., {@code /user/{id}/profile}).
 * It compiles the pattern into a regular expression that can match paths and extract dynamic segments.
 *
 * The extracted parameters are returned as key-value pairs, where the key is the placeholder name
 * and the value is the corresponding path segment.
 *
 * This is useful for implementing routing in web applications, where URL patterns with dynamic segments
 * need to map to handler methods or controllers.
 *
 * <p><b>Example Usage:</b></p>
 * ```java
 *     PathPattern pattern = new PathPattern("/user/{id}/profile");
 *     boolean matches = pattern.matches("/user/123/profile"); // true
 *
 *     Map<String, String> params = pattern.extractParams("/user/123/profile");
 *     System.out.println(params.get("id"));  // Outputs: 123
 * ```
 *
 * <p><b>Another Example Usage:</b></p>
 * ```java
 *     PathPattern pattern = new PathPattern("/product/{category}/{id}");
 *     boolean isMatch = pattern.matches("/product/electronics/42");  // true
 *
 *     Map<String, String> params = pattern.extractParams("/product/electronics/42");
 *     System.out.println(params.get("category"));  // "electronics"
 *     System.out.println(params.get("id"));        // "42"
 * ```
 *
 * @author <a href="https://github.com/nthnn" target="_blank">Nathanne Isip</a> &lt;<a href="mailto:nathanneisip@gmail.com">nathanneisip@gmail.com</a>&gt;
 * @version 1.0
 * @since 1.0
 */
public class PathPattern {
    private final String pattern;
    private final List<String> paramNames;
    private final Pattern regex;

    /**
     * Constructs a {@code PathPattern} instance by compiling the given path pattern.
     * The path pattern can contain placeholders in the format {@code {name}}, which
     * will match dynamic segments of a URL path.
     *
     * @param pattern The path pattern (e.g., {@code /user/{id}/profile}).
     */
    public PathPattern(String pattern) {
        this.pattern = pattern;
        this.paramNames = new ArrayList<>();
        this.regex = compilePattern();
    }

    /**
     * Compiles the provided path pattern into a regular expression.
     *
     * Each placeholder enclosed in curly braces (e.g., {@code {id}}) is replaced
     * by a capturing group {@code ([^/]+)} that matches any non-slash characters.
     *
     * @return A compiled {@code Pattern} that can match URL paths.
     */
    private Pattern compilePattern() {
        String regexPattern = this.pattern;
        Matcher matcher = Pattern.compile("\\{([^}]+)}")
            .matcher(this.pattern);

        while(matcher.find()) {
            this.paramNames.add(matcher.group(1));
            regexPattern = regexPattern.replace(
                matcher.group(),
                "([^/]+)"
            );
        }

        return Pattern.compile("^" + regexPattern + "$");
    }

    /**
     * Checks if the given path matches the compiled path pattern.
     *
     * @param path The URL path to be checked.
     * @return {@code true} if the path matches the pattern, {@code false} otherwise.
     */
    public boolean matches(String path) {
        return this.regex.matcher(path).matches();
    }

    /**
     * Extracts dynamic parameters from the given path based on the pattern.
     *
     * If the path matches the pattern, the method extracts the dynamic segments and
     * returns them as a {@code Map} with placeholder names as keys and path segments as values.
     *
     * @param path The URL path to extract parameters from.
     * @return A map containing parameter names and their corresponding values.
     *         If the path does not match the pattern, an empty map is returned.
     */
    public Map<String, String> extractParams(String path) {
        Map<String, String> params = new HashMap<>();
        Matcher matcher = this.regex.matcher(path);

        if(matcher.matches())
            for(int i = 0; i < this.paramNames.size(); i++)
                params.put(
                    this.paramNames.get(i),
                    matcher.group(i + 1)
                );

        return params;
    }
}
