package server.framework.custom;

import java.io.Serial;
import java.util.LinkedHashMap;

public class CustomMap extends LinkedHashMap<String, Object> {

    @Serial
    private static final long serialVersionUID = 2540786928617721678L;

    @Override
    public Object put(String key, Object value) {
        key = key.trim();
        if (value instanceof String) {
            value = ((String) value).trim();
        }
        return super.put(key, value);
    }
}
