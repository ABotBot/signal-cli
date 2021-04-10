package org.asamk.signal.manager.storage;

import org.asamk.signal.manager.AvatarStore;
import org.asamk.signal.manager.groups.GroupId;
import org.asamk.signal.manager.util.IOUtils;
import org.asamk.signal.manager.util.Utils;
import org.whispersystems.signalservice.api.push.SignalServiceAddress;
import org.whispersystems.signalservice.api.util.StreamDetails;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

public class FileAvatarStore implements AvatarStore {

    private final File avatarsPath;

    public FileAvatarStore(final File avatarsPath) {
        this.avatarsPath = avatarsPath;
    }

    @Override public StreamDetails retrieveContactAvatar(SignalServiceAddress address) throws IOException {
        return retrieveAvatar(getContactAvatarFile(address));
    }

    @Override public StreamDetails retrieveProfileAvatar(SignalServiceAddress address) throws IOException {
        return retrieveAvatar(getProfileAvatarFile(address));
    }

    @Override public StreamDetails retrieveGroupAvatar(GroupId groupId) throws IOException {
        final var groupAvatarFile = getGroupAvatarFile(groupId);
        return retrieveAvatar(groupAvatarFile);
    }

    @Override public void storeContactAvatar(SignalServiceAddress address, AvatarStorer storer) throws IOException {
        storeAvatar(getContactAvatarFile(address), storer);
    }

    @Override public void storeProfileAvatar(SignalServiceAddress address, AvatarStorer storer) throws IOException {
        storeAvatar(getProfileAvatarFile(address), storer);
    }

    @Override public void storeGroupAvatar(GroupId groupId, AvatarStorer storer) throws IOException {
        storeAvatar(getGroupAvatarFile(groupId), storer);
    }

    @Override public void deleteProfileAvatar(SignalServiceAddress address) throws IOException {
        deleteAvatar(getProfileAvatarFile(address));
    }

    private StreamDetails retrieveAvatar(final File avatarFile) throws IOException {
        if (!avatarFile.exists()) {
            return null;
        }
        return Utils.createStreamDetailsFromFile(avatarFile);
    }

    private void storeAvatar(final File avatarFile, final AvatarStorer storer) throws IOException {
        createAvatarsDir();
        try (OutputStream output = new FileOutputStream(avatarFile)) {
            storer.store(output);
        }
    }

    private void deleteAvatar(final File avatarFile) throws IOException {
        if (avatarFile.exists()) {
            Files.delete(avatarFile.toPath());
        }
    }

    private File getGroupAvatarFile(GroupId groupId) {
        return new File(avatarsPath, "group-" + groupId.toBase64().replace("/", "_"));
    }

    private File getContactAvatarFile(SignalServiceAddress address) {
        return new File(avatarsPath, "contact-" + address.getLegacyIdentifier());
    }

    private File getProfileAvatarFile(SignalServiceAddress address) {
        return new File(avatarsPath, "profile-" + address.getLegacyIdentifier());
    }

    private void createAvatarsDir() throws IOException {
        IOUtils.createPrivateDirectories(avatarsPath);
    }

    }
