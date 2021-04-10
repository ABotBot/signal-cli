package org.asamk.signal.manager;

import org.asamk.signal.manager.groups.GroupId;
import org.whispersystems.signalservice.api.push.SignalServiceAddress;
import org.whispersystems.signalservice.api.util.StreamDetails;

import java.io.IOException;
import java.io.OutputStream;

public interface AvatarStore {
    StreamDetails retrieveContactAvatar(SignalServiceAddress address) throws IOException;

    StreamDetails retrieveProfileAvatar(SignalServiceAddress address) throws IOException;

    StreamDetails retrieveGroupAvatar(GroupId groupId) throws IOException;

    void storeContactAvatar(SignalServiceAddress address, AvatarStorer storer) throws IOException;

    void storeProfileAvatar(SignalServiceAddress address, AvatarStorer storer) throws IOException;

    void storeGroupAvatar(GroupId groupId, AvatarStorer storer) throws IOException;

    void deleteProfileAvatar(SignalServiceAddress address) throws IOException;

    @FunctionalInterface
    public interface AvatarStorer {

        void store(OutputStream outputStream) throws IOException;
    }
}
