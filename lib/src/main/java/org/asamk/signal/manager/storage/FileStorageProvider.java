package org.asamk.signal.manager.storage;

import org.asamk.signal.manager.AttachmentStore;
import org.asamk.signal.manager.AvatarStore;
import org.asamk.signal.manager.PathConfig;
import org.signal.zkgroup.profiles.ProfileKey;
import org.whispersystems.libsignal.IdentityKeyPair;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class FileStorageProvider implements StorageProvider {

    private final PathConfig pathConfig;

    public FileStorageProvider(PathConfig pathConfig) {
        this.pathConfig = pathConfig;
    }

    @Override
    public Boolean accountExists(String username) {
        if (username == null) {
            return false;
        }
        var f = new File(pathConfig.getDataPath(), username);
        return !(!f.exists() || f.isDirectory());
    }

    @Override
    public AvatarStore getAvatarStore() {
        return new AvatarStore(pathConfig.getAvatarsPath());
    }

    @Override
    public AttachmentStore getAttachmentStore() {
        return new AttachmentStore(pathConfig.getAttachmentsPath());
    }

    @Override
    public AccountStorage loadAccount(final String username) throws IOException {
        return JsonFileAccountStorage.load(pathConfig.getDataPath(), username);
    }

    @Override
    public AccountStorage createAccount(
            String username, IdentityKeyPair identityKey, int registrationId, ProfileKey profileKey
    ) throws IOException {
        return JsonFileAccountStorage.create(pathConfig.getDataPath(),
                username,
                identityKey,
                registrationId,
                profileKey);
    }

    @Override
    public AccountStorage createLinkedAccount(
            String username,
            UUID uuid,
            String password,
            int deviceId,
            IdentityKeyPair identityKey,
            int registrationId,
            ProfileKey profileKey
    ) throws IOException {
        return JsonFileAccountStorage.createLinkedAccount(pathConfig.getDataPath(),
                username,
                uuid,
                password,
                deviceId,
                identityKey,
                registrationId,
                profileKey);
    }
}
