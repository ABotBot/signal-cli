package org.asamk.signal.manager.storage.protocol;

import org.asamk.signal.manager.TrustLevel;
import org.whispersystems.libsignal.IdentityKey;
import org.whispersystems.signalservice.api.SignalServiceProtocolStore;
import org.whispersystems.signalservice.api.push.SignalServiceAddress;

import java.util.List;

public interface SignalCliProtocolStore extends SignalServiceProtocolStore {
    void setResolver(SignalServiceAddressResolver resolver);

    void saveIdentity(SignalServiceAddress serviceAddress, IdentityKey identityKey, TrustLevel trustLevel);

    void setIdentityTrustLevel(SignalServiceAddress serviceAddress, IdentityKey identityKey, TrustLevel trustLevel);

    void removeIdentity(SignalServiceAddress serviceAddress, IdentityKey identityKey);

    List<IdentityInfo> getIdentities();

    List<IdentityInfo> getIdentities(SignalServiceAddress serviceAddress);

    IdentityInfo getIdentity(SignalServiceAddress serviceAddress);

    List<SessionInfo> getSessions();

    void deleteAllSessions(SignalServiceAddress serviceAddress);

    void archiveAllSessions();
}
