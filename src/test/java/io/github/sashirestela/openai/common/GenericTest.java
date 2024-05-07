package io.github.sashirestela.openai.common;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class GenericTest {

    @Test
    void testCreateNewObject() {
        var generic = new Generic<String>("object", 123456789L, Arrays.asList("one", "two"),
                "nextStartingAfter", false);
        System.out.println(generic);
        assertNotNull(generic);
    }

}
