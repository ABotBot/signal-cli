package org.asamk.signal.manager.storage.profiles;

import org.signal.zkgroup.profiles.ProfileKey;
import org.signal.zkgroup.profiles.ProfileKeyCredential;
import org.whispersystems.signalservice.api.push.SignalServiceAddress;

public interface ProfileStore {
    SignalProfileEntry getProfileEntry(SignalServiceAddress serviceAddress);

    ProfileKey getProfileKey(SignalServiceAddress serviceAddress);

    void updateProfile(
            SignalServiceAddress serviceAddress,
            ProfileKey profileKey,
            long now,
            SignalProfile profile,
            ProfileKeyCredential profileKeyCredential
    );

    void storeProfileKey(SignalServiceAddress serviceAddress, ProfileKey profileKey);
}
