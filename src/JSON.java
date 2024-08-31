import java.util.*;

public class JSON {

    // Main method for testing
//    public static void main(String[] args) {
//        String jsonString = "[{\"name\":\"John\",\"age\":30,\"email\":\"john@example.com\",\"isStudent\":false,\"scores\":[85, 90, 92]},"
//                + "{\"name\":\"Jane\",\"age\":25,\"email\":\"jane@example.com\",\"isStudent\":true,\"scores\":[88, 91, 95]}]";
//
//        JSON parser = new JSON();
//        Object result = parser.parse(jsonString);
//
//        System.out.println(result);
//    }

    // Parse method to parse the JSON string
    public Object parse(String json) {
        json = json.trim();
        if (json.startsWith("{")) {
            return parseObject(json.toCharArray(), new int[]{0});
        } else if (json.startsWith("[")) {
            return parseArray(json.toCharArray(), new int[]{0});
        }
        return null;
    }

    // Parse JSON object
    private Map<String, Object> parseObject(char[] json, int[] index) {
        Map<String, Object> jsonObject = new HashMap<>();
        String key = null;
        while (index[0] < json.length) {
            char currentChar = json[index[0]];
            switch (currentChar) {
                case '{':
                    index[0]++;
                    continue;
                case '}':
                    index[0]++;
                    return jsonObject;
                case ':':
                    index[0]++;
                    jsonObject.put(key, parseValue(json, index));
                    break;
                case ',':
                    index[0]++;
                    continue;
                case '"':
                    key = parseString(json, index);
                    break;
                default:
                    index[0]++;
            }
        }
        return jsonObject;
    }

    // Parse JSON array
    private List<Object> parseArray(char[] json, int[] index) {
        List<Object> jsonArray = new ArrayList<>();
        while (index[0] < json.length) {
            char currentChar = json[index[0]];
            switch (currentChar) {
                case '[':
                    index[0]++;
                    continue;
                case ']':
                    index[0]++;
                    return jsonArray;
                case ',':
                    index[0]++;
                    continue;
                default:
                    jsonArray.add(parseValue(json, index));
            }
        }
        return jsonArray;
    }

    // Parse value (string, number, boolean, or null)
    private Object parseValue(char[] json, int[] index) {
        skipWhitespace(json, index);
        char currentChar = json[index[0]];
        if (currentChar == '"') {
            return parseString(json, index);
        } else if (currentChar == '{') {
            return parseObject(json, index);
        } else if (currentChar == '[') {
            return parseArray(json, index);
        } else {
            return parsePrimitive(json, index);
        }
    }

    // Parse JSON string
    private String parseString(char[] json, int[] index) {
        StringBuilder sb = new StringBuilder();
        index[0]++;  // Skip opening quote
        while (index[0] < json.length && json[index[0]] != '"') {
            sb.append(json[index[0]]);
            index[0]++;
        }
        index[0]++;  // Skip closing quote
        return sb.toString();
    }

    // Parse primitive (number, boolean, null)
    private Object parsePrimitive(char[] json, int[] index) {
        StringBuilder sb = new StringBuilder();
        while (index[0] < json.length && !Character.isWhitespace(json[index[0]]) && json[index[0]] != ',' && json[index[0]] != ']' && json[index[0]] != '}') {
            sb.append(json[index[0]]);
            index[0]++;
        }
        String value = sb.toString();
        if (value.equals("true") || value.equals("false")) {
            return Boolean.parseBoolean(value);
        } else if (value.equals("null")) {
            return null;
        } else if (value.contains(".")) {
            return Double.parseDouble(value);
        } else {
            return Integer.parseInt(value);
        }
    }

    // Skip whitespace
    private void skipWhitespace(char[] json, int[] index) {
        while (index[0] < json.length && Character.isWhitespace(json[index[0]])) {
            index[0]++;
        }
    }
}