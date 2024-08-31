import java.util.*;

// from ChatGPT
public class JSON {
    // Parse method to parse the JSON string
    public static Object parse(String json) {
        json = json.trim();
        if (json.startsWith("{")) {
            return parseObject(json.toCharArray(), new int[]{0});
        } else if (json.startsWith("[")) {
            return parseArray(json.toCharArray(), new int[]{0});
        }
        return null;
    }

    // Parse JSON object
    private static Map<String, Object> parseObject(char[] json, int[] index) {
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
    private static List<Object> parseArray(char[] json, int[] index) {
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
    private static Object parseValue(char[] json, int[] index) {
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
    private static String parseString(char[] json, int[] index) {
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
    private static Object parsePrimitive(char[] json, int[] index) {
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
    private static void skipWhitespace(char[] json, int[] index) {
        while (index[0] < json.length && Character.isWhitespace(json[index[0]])) {
            index[0]++;
        }
    }
}