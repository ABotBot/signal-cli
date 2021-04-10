package org.asamk.signal.manager.storage.protocol;

import org.whispersystems.signalservice.api.push.SignalServiceAddress;

public interface RecipientStore {
    SignalServiceAddress resolveServiceAddress(SignalServiceAddress serviceAddress);
}
