package dev.dejvokep.boostedyaml.updater;

import dev.dejvokep.boostedyaml.YamlFile;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class MergerTest {

    // Settings
    private static final UpdaterSettings UPDATER_SETTINGS = UpdaterSettings.DEFAULT;

    @Test
    void merge() {
        try {
            // File
            YamlFile file = YamlFile.create(
                    new ByteArrayInputStream("x: 1.2\ny: true\nz:\n  a: 1\n  b: 10\no: \"a: b\"\np: false".getBytes(StandardCharsets.UTF_8)),
                    new ByteArrayInputStream("x: 1.4\ny: false\nz:\n  a: 5\n  b: 10\nm: \"a: c\"".getBytes(StandardCharsets.UTF_8)),
                    GeneralSettings.DEFAULT, LoaderSettings.DEFAULT, DumperSettings.DEFAULT, UPDATER_SETTINGS);

            // Mark "p" to be kept
            file.getOptionalBlock("p").orElseThrow(NullPointerException::new).setKeep(true);
            // Merge
            Merger.merge(file, file.getDefaults(), UPDATER_SETTINGS);
            // Verify all
            assertEquals(1.2, file.get("x", null));
            assertEquals(true, file.get("y", null));
            assertEquals(1, file.get("z.a", null));
            assertEquals(10, file.get("z.b", null));
            assertEquals("a: c", file.get("m", null));
            assertEquals(false, file.get("p", null));
            assertEquals(5, file.getKeys().size());
            assertEquals(2, file.getSection("z").getKeys().size());
        } catch (
                IOException ex) {
            fail(ex);
        }
    }
}