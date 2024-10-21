package io.github.sashirestela.openai.support;

import io.github.sashirestela.openai.support.Base64Util.MediaType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Base64UtilTest {

    @Test
    void testEncodeDecode() {
        var filePath = "src/test/resources/audios_speak.mp3";
        var base64StringWithMedia = Base64Util.encode(filePath, MediaType.AUDIO);
        assertTrue(base64StringWithMedia.startsWith("data:"));
        var base64String = Base64Util.encode(filePath, null);
        assertFalse(base64String.startsWith("data:"));
        assertDoesNotThrow(() -> Base64Util.decode(base64String, filePath));
    }

    @Test
    void testEncodeException() {
        var filePath = "src/test/resources/image_not_found.png";
        assertThrows(Exception.class, () -> Base64Util.encode(filePath, MediaType.IMAGE));
    }

    @Test
    void testDecodeException() {
        var base64String = "ºªÇ";
        var filePath = "src/test/resources/image_error.png";
        assertThrows(Exception.class, () -> Base64Util.decode(base64String, filePath));
    }

}
