package com.davidcubesvk.yamlUpdater.core.versioning.wrapper;

import com.davidcubesvk.yamlUpdater.core.block.Section;
import com.davidcubesvk.yamlUpdater.core.versioning.Pattern;
import com.davidcubesvk.yamlUpdater.core.versioning.Version;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents manually supplied versioning information.
 */
public class ManualVersioning implements Versioning {

    //Versions
    private final Version userSectionVersion;
    private final Version defSectionVersion;

    /**
     * Creates manually-supplied versioning information, which parses the given IDs using the pattern straight away, then
     * supplies the (already) parsed versions to the implementing methods.
     *
     * @param pattern              the pattern
     * @param userSectionVersionId the version ID of the user section (file) matching the given pattern
     * @param defSectionVersionId  the version ID of the default section (file) matching the given pattern
     * @throws IllegalArgumentException if either of the given IDs do not match the given pattern (see {@link Pattern#getVersion(String)} for more information)
     */
    public ManualVersioning(@NotNull Pattern pattern, @Nullable String userSectionVersionId, @NotNull String defSectionVersionId) throws IllegalArgumentException {
        this.userSectionVersion = pattern.getVersion(userSectionVersionId);
        this.defSectionVersion = userSectionVersionId == null ? null : pattern.getVersion(defSectionVersionId);
    }

    @Override
    public Version getDefSectionVersion(Section section) {
        return defSectionVersion;
    }

    @Override
    public Version getUserSectionVersion(Section section) {
        return userSectionVersion;
    }

    @Override
    public Version getOldest() {
        return defSectionVersion.getPattern().getOldestVersion();
    }
}