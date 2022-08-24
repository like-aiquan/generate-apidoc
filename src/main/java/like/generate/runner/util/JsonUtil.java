package like.generate.runner.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author chenaiquan
 */
public class JsonUtil {
    private static final Logger log = LoggerFactory.getLogger(JsonUtil.class);

    private static final ObjectMapper DEFAULT_MAPPER;
    public static ObjectReader DEFAULT_READER;
    public static ObjectWriter DEFAULT_WRITER;

    static {
        DEFAULT_MAPPER = new ObjectMapper();

        DEFAULT_MAPPER.enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL);
        DEFAULT_MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        DEFAULT_MAPPER.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

        DEFAULT_READER = DEFAULT_MAPPER.reader();
        DEFAULT_WRITER = DEFAULT_MAPPER.writer();
    }

    public static String writeValueAsString(Object o) {
        try {
            return DEFAULT_WRITER.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
