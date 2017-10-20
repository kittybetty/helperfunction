/**
 * Todo: Time based filtering queries; first, prev, self, next, last; stringbuilder; skip character; invalid;
 * 
 * test
 */
public class HelperFunctions {

    // GET /app/items?limit=30&page=2
    public static String getPage(String input) {
        int limitIndex = input.indexOf("limit=");
        int pageIndex = input.indexOf("page=");

        String limitString = input.substring(limitIndex + 6, pageIndex - 1);
        String pageString = input.substring(pageIndex + 5);
        int limit = Integer.parseInt(limitString);
        int page = Integer.parseInt(pageString);
        int offset = (page - 1 ) * limit;
        String sql = "SELECT * FROM [database] LIMIT " + limit + " OFFSET " +  offset;
        return sql;
    }

    // /app/items?foo=buzz&baz=quux
    public static String filterEqual(String input) {
        String newInput = input.substring(input.indexOf("?") + 1);
        String[] substrings = newInput.split("&");

        String sql = "SELECT * FROM [database] WHERE " + substrings[0];
        if (substrings.length == 1) {
            return sql;
        }
        for (int i = 1; i < substrings.length; i++) {
            sql += " AND " + substrings[i];
        }
        return sql;
    }

    // GET /app/items?foo=in:buzz,bar
    // GET /app/items?foo=nin:buzz,bar
    public static String filterIn(String input, boolean in) {
        String newInput = input.substring(input.indexOf("?") + 1);
        String[] substrings = newInput.split("=");
        String key = substrings[0];
        String values = substrings[1];
        String sql = "SELECT * FROM [database] WHERE " + key;
        if (in) {
            sql += " IN (" + values.substring(3) + ")";
        } else {
            sql += " NOT IN (" + values.substring(4) + ")";
        }
        return sql;
    }

    // GET /app/items?foo=neq:buzz
    // GET /app/items?size=gt:8
    // GET /app/items?size=gte:8
    // GET /app/items?size=lt:8
    // GET /app/items?size=lte:8
    public static String filterOperators(String input) {
        String newInput = input.substring(input.indexOf("?") + 1);
        String pair = "";
        if (newInput.contains("=neq:")) {
            pair = newInput.replace("=neq:", "!=");
        } else if (newInput.contains("=gt:")){
            pair = newInput.replace("=gt:", ">");
        } else if (newInput.contains("=gte:")){
            pair = newInput.replace("=gte:", ">=");
        } else if (newInput.contains("=lt:")){
            pair = newInput.replace("=lt:", "<");
        } else if (newInput.contains("=lte:")){
            pair = newInput.replace("=lte:", "<=");
        }
        String sql = "SELECT * FROM [database] WHERE " + pair;
        return sql;
    }

    //  GET /app/items?sort=key1:asc,key2:desc,key3:asc
    public static String sort(String input) {
        String newInput = input.substring(input.indexOf("=") + 1);
        String removedColonStr = newInput.replace(":", " ");
        String addedSpaceStr = removedColonStr.replace(",", ", ");
        String sql = "SELECT * FROM [database] ORDER BY " + addedSpaceStr;
        return sql;
    }

    public static void main(String[] args) {
        System.out.println(getPage("GET /app/items?limit=30&page=2"));

        System.out.println(filterEqual("/app/items?foo=buzz"));
        System.out.println(filterEqual("/app/items?foo=buzz&baz=quux"));

        System.out.println(filterIn("GET /app/items?foo=in:buzz,bar", true));
        System.out.println(filterIn("GET /app/items?foo=nin:buzz,bar", false));

        System.out.println(filterOperators("GET /app/items?foo=neq:buzz"));
        System.out.println(filterOperators("GET /app/items?size=gt:8"));
        System.out.println(filterOperators("GET /app/items?size=gte:8"));
        System.out.println(filterOperators("GET /app/items?size=lt:8"));
        System.out.println(filterOperators("GET /app/items?size=lte:8"));

        System.out.println(sort("GET /app/items?sort=key1:asc,key2:desc,key3:asc"));

    }
}