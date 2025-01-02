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
package web.ambassador.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class for parsing and serializing JSON data.
 *
 * This class provides methods to serialize Java objects into JSON strings
 * and parse JSON strings into Java objects. The supported types for serialization
 * and deserialization include {@code Map}, {@code List}, {@code String},
 * {@code Number}, {@code Boolean}, and {@code null}.
 *
 * This utility implements a simple JSON parser from scratch and adheres to
 * standard JSON specifications. The parser handles nested JSON structures,
 * arrays, strings, numbers, booleans, and null values.
 *
 * <h2>Serialization Example:</h2>
 * ```java
 * Map<String, Object> data = new HashMap&lt;&gt;();
 * data.put("name", "John");
 * data.put("age", 30);
 * data.put("isVerified", true);
 *
 * String jsonString = JsonUtil.toJson(data);
 * System.out.println(jsonString);
 * ```
 *
 * <h2>Parsing Example:</h2>
 * ```java
 * String json = "{\"name\":\"John\", \"age\":30}";
 * JsonUtil parser = new JsonUtil(json);
 *
 * Map<String, Object> result = (Map<String, Object>) parser.parse();
 * System.out.println(result.get("name"));  // Output: John
 * ```
 *
 * @author <a href="https://github.com/nthnn" target="_blank">Nathanne Isip</a> &lt;<a href="mailto:nathanneisip@gmail.com">nathanneisip@gmail.com</a>&gt;
 * @version 1.0
 * @since 1.0
 */
public class JsonUtil {
    private int position;
    private final String json;

    /**
     * Constructs a new {@code JsonUtil} instance for parsing the specified JSON string.
     *
     * @param json the JSON string to parse
     */
    public JsonUtil(String json) {
        this.json = json;
        this.position = 0;
    }

    /**
     * Serializes the given Java object into a JSON string representation.
     *
     * Supported object types include:
     * <ul>
     *     <li>{@code Map} - serialized as JSON objects</li>
     *     <li>{@code List} - serialized as JSON arrays</li>
     *     <li>{@code String} - serialized as JSON strings</li>
     *     <li>{@code Number}, {@code Boolean} - serialized as their JSON equivalents</li>
     *     <li>{@code null} - serialized as {@code null}</li>
     * </ul>
     *
     * If the object type is unsupported, it will be converted to a string and serialized.
     *
     * @param obj the object to serialize
     * @return the JSON string representation of the object
     */
    public static String toJson(Object obj) {
        if(obj == null)
            return "[null]";

        StringBuilder sb = new StringBuilder();
        JsonUtil.serializeValue(obj, sb, 0);

        return sb.toString();
    }

    private static void serializeValue(
        Object value,
        StringBuilder sb,
        int indent
    ) {
        if(value == null)
            sb.append("null");
        else if(value instanceof Map)
            JsonUtil.serializeMap((Map<?, ?>) value, sb, indent);
        else if(value instanceof List)
            JsonUtil.serializeList((List<?>) value, sb, indent);
        else if(value instanceof String)
            JsonUtil.serializeString((String) value, sb);
        else if(value instanceof Number || value instanceof Boolean)
            sb.append(value);
        else JsonUtil.serializeString(value.toString(), sb);
    }

    private static void serializeMap(
        Map<?, ?> map,
        StringBuilder sb,
        int indent
    ) {
        sb.append('{');
        boolean first = true;

        for(Map.Entry<?, ?> entry : map.entrySet()) {
            if(!first)
                sb.append(',');

            sb.append('\n')
                .append("  ".repeat(indent + 1));

            JsonUtil.serializeString(entry.getKey().toString(), sb);
            sb.append(": ");

            JsonUtil.serializeValue(entry.getValue(), sb, indent + 1);
            first = false;
        }

        if(!map.isEmpty())
            sb.append('\n').append("  ".repeat(indent));
        sb.append('}');
    }

    private static void serializeList(
        List<?> list,
        StringBuilder sb,
        int indent
    ) {
        sb.append('[');
        boolean first = true;

        for(Object value : list) {
            if(!first)
                sb.append(',');

            sb.append('\n')
                .append("  ".repeat(indent + 1));
            JsonUtil.serializeValue(value, sb, indent + 1);

            first = false;
        }

        if(!list.isEmpty())
            sb.append('\n').append("  ".repeat(indent));
        sb.append(']');
    }

    private static void serializeString(String str, StringBuilder sb) {
        sb.append('"');

        for(int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            switch(c) {
                case '"':
                    sb.append("\\\"");
                    break;

                case '\\':
                    sb.append("\\\\");
                    break;

                case '\b':
                    sb.append("\\b");
                    break;

                case '\f':
                    sb.append("\\f");
                    break;

                case '\n':
                    sb.append("\\n");
                    break;

                case '\r':
                    sb.append("\\r");
                    break;

                case '\t':
                    sb.append("\\t");
                    break;

                default:
                    if(c < ' ')
                        sb.append(String.format("\\u%04x", (int) c));
                    else sb.append(c);
            }
        }

        sb.append('"');
    }

    /**
     * Parses the JSON string and returns the resulting Java object.
     *
     * This method will parse the entire JSON string and return a Java
     * representation of the data. The return type can be {@code Map},
     * {@code List}, {@code String}, {@code Number}, {@code Boolean}, or
     * {@code null}, depending on the JSON structure.
     *
     * @return the parsed Java object
     * @throws JsonParseException if the JSON string is malformed or contains errors
     */
    public Object parse() {
        this.skipWhitespace();

        Object result = this.parseValue();
        this.skipWhitespace();

        if(position != json.length())
            throw new JsonParseException("Unexpected characters after parsed value");

        return result;
    }

    /**
     * Parses a JSON value and returns the corresponding Java representation.
     *
     * @return the parsed value (Map, List, String, Number, Boolean, or null)
     * @throws JsonParseException if the value is invalid
     */
    private Object parseValue() {
        char current = this.peek();
        return switch(current) {
            case '{' -> this.parseObject();
            case '[' -> this.parseArray();
            case '"' -> this.parseString();
            case 't' -> this.parseTrue();
            case 'f' -> this.parseFalse();
            case 'n' -> this.parseNull();
            default -> {
                if(current == '-' || Character.isDigit(current))
                    yield this.parseNumber();

                throw new JsonParseException("Unexpected character: " + current);
            }
        };
    }

    /**
     * Parses a JSON object (enclosed in curly braces).
     *
     * @return the parsed {@code Map} representing the object
     * @throws JsonParseException if the object is malformed
     */
    private Map<String, Object> parseObject() {
        Map<String, Object> map = new HashMap<>();
        this.consume('{');
        this.skipWhitespace();

        if(this.peek() == '}') {
            this.consume('}');
            return map;
        }

        while(true) {
            this.skipWhitespace();

            String key = this.parseString();
            this.skipWhitespace();
            this.consume(':');

            Object value = this.parseValue();
            map.put(key, value);
            this.skipWhitespace();

            if(this.peek() == '}') {
                this.consume('}');
                break;
            }

            this.consume(',');
        }

        return map;
    }

    /**
     * Parses a JSON array (enclosed in square brackets).
     *
     * @return the parsed {@code List} representing the array
     * @throws JsonParseException if the array is malformed
     */
    private List<Object> parseArray() {
        List<Object> list = new ArrayList<>();
        this.consume('[');
        this.skipWhitespace();

        if(this.peek() == ']') {
            this.consume(']');
            return list;
        }

        while(true) {
            Object value = this.parseValue();
            list.add(value);
            this.skipWhitespace();

            if(this.peek() == ']') {
                this.consume(']');
                break;
            }

            this.consume(',');
            this.skipWhitespace();
        }

        return list;
    }

    /**
     * Parses a JSON string (enclosed in double quotes).
     *
     * @return the parsed {@code String} object
     * @throws JsonParseException if the string is malformed
     */
    private String parseString() {
        this.consume('"');

        StringBuilder sb = new StringBuilder();
        while(this.position < this.json.length()) {
            char current = this.json.charAt(this.position);
            if(current == '"') {
                this.position++;
                return sb.toString();
            }

            if(current == '\\') {
                this.position++;
                if(this.position >= this.json.length())
                    throw new JsonParseException("Unexpected end of string");

                char escaped = this.json.charAt(this.position);
                switch(escaped) {
                    case '"':
                    case '\\':
                    case '/':
                        sb.append(escaped);
                        break;

                    case 'b':
                        sb.append('\b');
                        break;

                    case 'f':
                        sb.append('\f');
                        break;

                    case 'n':
                        sb.append('\n');
                        break;

                    case 'r':
                        sb.append('\r');
                        break;

                    case 't':
                        sb.append('\t');
                        break;

                    case 'u':
                        if(this.position + 4 >= this.json.length())
                            throw new JsonParseException("Invalid Unicode escape sequence");

                        String hex = json.substring(this.position + 1, this.position + 5);
                        sb.append((char) Integer.parseInt(hex, 16));
                        this.position += 4;

                        break;

                    default:
                        throw new JsonParseException("Invalid escape sequence: \\" + escaped);
                }
            }
            else sb.append(current);
            this.position++;
        }

        throw new JsonParseException("Unterminated string");
    }

    /**
     * Parses a JSON number.
     *
     * @return the parsed {@code Number} representing the value
     * @throws JsonParseException if the number value is malformed
     */
    private Number parseNumber() {
        int start = this.position;
        boolean isFloat = false;

        if(this.peek() == '-')
            this.position++;

        while(this.position < this.json.length() &&
            Character.isDigit(this.json.charAt(this.position)))
            this.position++;

        if(this.position < this.json.length() &&
            this.json.charAt(this.position) == '.') {
            isFloat = true;
            this.position++;

            if(!Character.isDigit(peek()))
                throw new JsonParseException("Expected digit after decimal point");

            while(this.position < this.json.length() &&
                Character.isDigit(this.json.charAt(this.position)))
                this.position++;
        }

        if(this.position < this.json.length() &&
            (this.json.charAt(this.position) == 'e' ||
                this.json.charAt(this.position) == 'E')
        ) {
            isFloat = true;
            this.position++;

            if(this.peek() == '+' || this.peek() == '-')
                this.position++;

            if(!Character.isDigit(this.peek()))
                throw new JsonParseException("Expected digit in exponent");

            while(this.position < this.json.length() &&
                Character.isDigit(this.json.charAt(this.position)))
                this.position++;
        }

        String number = this.json.substring(start, this.position);
        return isFloat ? Double.parseDouble(number) : Long.parseLong(number);
    }

    private boolean parseTrue() {
        this.consumeExpected("true");
        return true;
    }

    private boolean parseFalse() {
        this.consumeExpected("false");
        return false;
    }

    private Object parseNull() {
        this.consumeExpected("null");
        return null;
    }

    private void consumeExpected(String expected) {
        for(char c : expected.toCharArray()) {
            if(this.position >= this.json.length() ||
                this.json.charAt(this.position) != c)
                throw new JsonParseException("Expected '" + expected + "'");

            this.position++;
        }
    }

    private void consume(char expected) {
        if(this.position >= this.json.length() ||
            this.json.charAt(this.position) != expected)
            throw new JsonParseException("Expected '" + expected + "'");

        this.position++;
    }

    private char peek() {
        if(this.position >= this.json.length())
            throw new JsonParseException("Unexpected end of input");

        return this.json.charAt(this.position);
    }

    private void skipWhitespace() {
        while(this.position < this.json.length() &&
            Character.isWhitespace(this.json.charAt(this.position)))
            this.position++;
    }
}
