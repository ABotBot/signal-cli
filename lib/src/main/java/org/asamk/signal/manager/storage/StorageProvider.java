package org.asamk.signal.manager.storage;

import org.asamk.signal.manager.AttachmentStore;
import org.asamk.signal.manager.AvatarStore;
import org.signal.zkgroup.profiles.ProfileKey;
import org.whispersystems.libsignal.IdentityKeyPair;

import java.io.IOException;
import java.util.UUID;

public interface StorageProvider {

    Boolean accountExists(String username);

    AvatarStore getAvatarStore();

    AttachmentStore getAttachmentStore();

    AccountStorage loadAccount(String username) throws IOException;

    AccountStorage createAccount(
            String username, IdentityKeyPair identityKey, int registrationId, ProfileKey profileKey
    ) throws IOException;

    AccountStorage createLinkedAccount(
            String username,
            UUID uuid,
            String password,
            int deviceId,
            IdentityKeyPair identityKey,
            int registrationId,
            ProfileKey profileKey
    ) throws IOException;
}
