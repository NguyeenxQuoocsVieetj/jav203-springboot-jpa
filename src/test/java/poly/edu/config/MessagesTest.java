package poly.edu.config;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import org.junit.jupiter.api.Test;

class MessagesTest {

    @Test
    void vietnameseAndEnglishHaveSameKeys() throws IOException {
        assertEquals(load("messages_vi.properties").stringPropertyNames(),
                load("messages_en.properties").stringPropertyNames());
    }

    private Properties load(String name) throws IOException {
        Properties properties = new Properties();
        try (InputStreamReader reader = new InputStreamReader(
                getClass().getClassLoader().getResourceAsStream(name), StandardCharsets.UTF_8)) {
            properties.load(reader);
        }
        return properties;
    }
}
