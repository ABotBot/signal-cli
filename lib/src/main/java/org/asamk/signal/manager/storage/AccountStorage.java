package org.asamk.signal.manager.storage;

import org.asamk.signal.manager.storage.contacts.ContactsStore;
import org.asamk.signal.manager.storage.groups.GroupStore;
import org.asamk.signal.manager.storage.messageCache.MessageCache;
import org.asamk.signal.manager.storage.profiles.ProfileStore;
import org.asamk.signal.manager.storage.protocol.RecipientStore;
import org.asamk.signal.manager.storage.protocol.SignalCliProtocolStore;
import org.asamk.signal.manager.storage.stickers.StickerStore;
import org.signal.zkgroup.profiles.ProfileKey;
import org.whispersystems.signalservice.api.crypto.UnidentifiedAccess;
import org.whispersystems.signalservice.api.kbs.MasterKey;
import org.whispersystems.signalservice.api.push.SignalServiceAddress;
import org.whispersystems.signalservice.api.storage.StorageKey;

import java.io.IOException;
import java.util.UUID;

public interface AccountStorage {
    void save();

    void close() throws IOException;

    default SignalServiceAddress getSelfAddress() {
        return new SignalServiceAddress(getUuid(), getUsername());
    }

    boolean isRegistered();

    void setRegistered(boolean registered);

    boolean isMultiDevice();

    void setMultiDevice(boolean multiDevice);

    SignalCliProtocolStore getSignalProtocolStore();

    GroupStore getGroupStore();

    ContactsStore getContactStore();

    RecipientStore getRecipientStore();

    ProfileStore getProfileStore();

    StickerStore getStickerStore();

    MessageCache getMessageCache();

    String getUsername();

    UUID getUuid();

    void setUuid(UUID uuid);

    int getDeviceId();

    void setDeviceId(int deviceId);

    String getPassword();

    void setPassword(String password);

    String getRegistrationLockPin();

    void setRegistrationLockPin(String registrationLockPin);

    MasterKey getPinMasterKey();

    void setPinMasterKey(MasterKey pinMasterKey);

    StorageKey getStorageKey();

    void setStorageKey(StorageKey storageKey);

    ProfileKey getProfileKey();

    void setProfileKey(ProfileKey profileKey);

    default byte[] getSelfUnidentifiedAccessKey() {
        return UnidentifiedAccess.deriveAccessKeyFrom(getProfileKey());
    }

    int getPreKeyIdOffset();

    void setPreKeyIdOffset(int preKeyIdOffset);

    int getNextSignedPreKeyId();

    void setNextSignedPreKeyId(int nextSignedPreKeyId);
}
