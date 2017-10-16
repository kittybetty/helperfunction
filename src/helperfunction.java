/**
 * Todo: in, nin, neq, gt, gte, lt, and lte; Time based filtering queries; first, prev, self, next, last
 * 
 * test
 */
public class helperfunction {

    // assume marker is id,
    public static String getFirstPage(int limit) {
        String sql = "SELECT * FROM [database] LIMIT " + limit;
//       int marker = "SELECT ID FROM (SELECT * FROM [database] LIMIT " + limit + ") ORDER BY ID DESC";
        return sql;
    }

    public static String getPage(int limit, String marker) {
        String sql = "SELECT * FROM [database] WHERE ID > " + marker + " LIMIT " + limit;
        return sql;
    }

    // GET /app/items?limit=30
    // GET /app/items?limit=30&marker=08ec231f6d9a43dda97d4b950c3393df
    public static String paginationDriver(String input) {
        int limitIndex = input.indexOf("limit=");
        String limitString = input.substring(limitIndex + 6);
        int markerIndex = input.indexOf("marker=");
        if (markerIndex < 0) {
            int limit = Integer.parseInt(limitString);
            return getFirstPage(limit);
        } else {
            limitString = input.substring(limitIndex + 6, markerIndex - 1);
            String marker = input.substring(markerIndex + 7);
            return getPage(Integer.parseInt(limitString), marker);
        }
    }

    public static String filterEqual(String input) {
        // /app/items?foo=buzz&baz=quux
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

    //  GET /app/items?sort=key1:asc,key2:desc,key3:asc
    public static String sort(String input) {
        String newInput = input.substring(input.indexOf("=") + 1);
        String removedColonStr = newInput.replace(":", " ");
        String addedSpaceStr = removedColonStr.replace(",", ", ");
        String sql = "SELECT * FROM [database] ORDER BY " + addedSpaceStr;
        return sql;
    }

    public static void main(String[] args) {
        System.out.println(paginationDriver("GET /app/items?limit=30"));
        System.out.println(paginationDriver("GET /app/items?limit=30&marker=08ec231f6d9a43dda97d4b950c3393df"));

        System.out.println(filterEqual("/app/items?foo=buzz"));
        System.out.println(filterEqual("/app/items?foo=buzz&baz=quux"));

        System.out.println(filterIn("GET /app/items?foo=in:buzz,bar", true));
        System.out.println(filterIn("GET /app/items?foo=nin:buzz,bar", false));

        System.out.println(sort("GET /app/items?sort=key1:asc,key2:desc,key3:asc"));
    }
}