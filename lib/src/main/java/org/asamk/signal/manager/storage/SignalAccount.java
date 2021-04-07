package org.asamk.signal.manager.storage;

import org.asamk.signal.manager.storage.contacts.JsonContactsStore;
import org.asamk.signal.manager.storage.groups.JsonGroupStore;
import org.asamk.signal.manager.storage.messageCache.MessageCache;
import org.asamk.signal.manager.storage.profiles.ProfileStore;
import org.asamk.signal.manager.storage.protocol.JsonSignalProtocolStore;
import org.asamk.signal.manager.storage.protocol.RecipientStore;
import org.asamk.signal.manager.storage.protocol.SignalServiceAddressResolver;
import org.asamk.signal.manager.storage.stickers.StickerStore;
import org.asamk.signal.manager.util.KeyUtils;
import org.signal.zkgroup.InvalidInputException;
import org.signal.zkgroup.profiles.ProfileKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.whispersystems.libsignal.state.PreKeyRecord;
import org.whispersystems.libsignal.state.SignedPreKeyRecord;
import org.whispersystems.libsignal.util.Medium;
import org.whispersystems.signalservice.api.crypto.UnidentifiedAccess;
import org.whispersystems.signalservice.api.kbs.MasterKey;
import org.whispersystems.signalservice.api.push.SignalServiceAddress;
import org.whispersystems.signalservice.api.storage.StorageKey;

import java.io.Closeable;
import java.io.IOException;
import java.util.Base64;
import java.util.Collection;
import java.util.UUID;

public class SignalAccount implements Closeable {

    private final static Logger logger = LoggerFactory.getLogger(SignalAccount.class);
    private final AccountStorage accountStorage;

    public SignalAccount(AccountStorage accountStorage) {
        this.accountStorage = accountStorage;
        migrateLegacyConfigs();
    }

    public static String getStorageDescription() {
        // TODO remove static calls or figure something out (static INSTANCE or similar?) used to be:
        // SignalAccount.getFileName(pathConfig.getDataPath(), username));
        return "JsonFile";
    }

    public void migrateLegacyConfigs() {
        if (getProfileKey() == null && isRegistered()) {
            // Old config file, creating new profile key
            setProfileKey(KeyUtils.createProfileKey());
            save();
        }
        // Store profile keys only in profile store
        for (var contact : getContactStore().getContacts()) {
            var profileKeyString = contact.profileKey;
            if (profileKeyString == null) {
                continue;
            }
            final ProfileKey profileKey;
            try {
                profileKey = new ProfileKey(Base64.getDecoder().decode(profileKeyString));
            } catch (InvalidInputException ignored) {
                continue;
            }
            contact.profileKey = null;
            getProfileStore().storeProfileKey(contact.getAddress(), profileKey);
        }
        // Ensure our profile key is stored in profile store
        getProfileStore().storeProfileKey(getSelfAddress(), getProfileKey());
    }

    public void save() {
        accountStorage.save();
    }

    public void setResolver(final SignalServiceAddressResolver resolver) {
        getSignalProtocolStore().setResolver(resolver);
    }

    public void addPreKeys(Collection<PreKeyRecord> records) {
        for (var record : records) {
            getSignalProtocolStore().storePreKey(record.getId(), record);
        }
        accountStorage.setPreKeyIdOffset((getPreKeyIdOffset() + records.size()) % Medium.MAX_VALUE);
    }

    public void addSignedPreKey(SignedPreKeyRecord record) {
        getSignalProtocolStore().storeSignedPreKey(record.getId(), record);
        accountStorage.setNextSignedPreKeyId((getNextSignedPreKeyId() + 1) % Medium.MAX_VALUE);
    }

    public JsonSignalProtocolStore getSignalProtocolStore() {
        return accountStorage.getSignalProtocolStore();
    }

    public JsonGroupStore getGroupStore() {
        return accountStorage.getGroupStore();
    }

    public JsonContactsStore getContactStore() {
        return accountStorage.getContactStore();
    }

    public RecipientStore getRecipientStore() {
        return accountStorage.getRecipientStore();
    }

    public ProfileStore getProfileStore() {
        return accountStorage.getProfileStore();
    }

    public StickerStore getStickerStore() {
        return accountStorage.getStickerStore();
    }

    public MessageCache getMessageCache() {
        return accountStorage.getMessageCache();
    }

    public String getUsername() {
        return accountStorage.getUsername();
    }

    public UUID getUuid() {
        return accountStorage.getUuid();
    }

    public void setUuid(final UUID uuid) {
        accountStorage.setUuid(uuid);
    }

    public SignalServiceAddress getSelfAddress() {
        return accountStorage.getSelfAddress();
    }

    public int getDeviceId() {
        return accountStorage.getDeviceId();
    }

    public void setDeviceId(final int deviceId) {
        accountStorage.setDeviceId(deviceId);
    }

    public boolean isMasterDevice() {
        return getDeviceId() == SignalServiceAddress.DEFAULT_DEVICE_ID;
    }

    public String getPassword() {
        return accountStorage.getPassword();
    }

    public void setPassword(final String password) {
        accountStorage.setPassword(password);
    }

    public String getRegistrationLockPin() {
        return accountStorage.getRegistrationLockPin();
    }

    public void setRegistrationLockPin(final String registrationLockPin) {
        accountStorage.setRegistrationLockPin(registrationLockPin);
    }

    public MasterKey getPinMasterKey() {
        return accountStorage.getPinMasterKey();
    }

    public void setPinMasterKey(final MasterKey pinMasterKey) {
        accountStorage.setPinMasterKey(pinMasterKey);
    }

    public StorageKey getStorageKey() {
        if (getPinMasterKey() != null) {
            return getPinMasterKey().deriveStorageServiceKey();
        }
        return getStorageKey();
    }

    public void setStorageKey(final StorageKey storageKey) {
        accountStorage.setStorageKey(storageKey);
    }

    public ProfileKey getProfileKey() {
        return accountStorage.getProfileKey();
    }

    public void setProfileKey(final ProfileKey profileKey) {
        accountStorage.setProfileKey(profileKey);
    }

    public byte[] getSelfUnidentifiedAccessKey() {
        return UnidentifiedAccess.deriveAccessKeyFrom(getProfileKey());
    }

    public int getPreKeyIdOffset() {
        return accountStorage.getPreKeyIdOffset();
    }

    public int getNextSignedPreKeyId() {
        return accountStorage.getNextSignedPreKeyId();
    }

    public boolean isRegistered() {
        return accountStorage.isRegistered();
    }

    public void setRegistered(final boolean registered) {
        accountStorage.setRegistered(registered);
    }

    public boolean isMultiDevice() {
        return accountStorage.isMultiDevice();
    }

    public void setMultiDevice(final boolean multiDevice) {
        accountStorage.setMultiDevice(multiDevice);
    }

    public boolean isUnrestrictedUnidentifiedAccess() {
        // TODO make configurable
        return false;
    }

    public boolean isDiscoverableByPhoneNumber() {
        // TODO make configurable
        return true;
    }

    @Override
    public void close() throws IOException {
        accountStorage.close();
    }
}
