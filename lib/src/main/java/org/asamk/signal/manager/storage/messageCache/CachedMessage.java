package org.asamk.signal.manager.storage.messageCache;

import org.whispersystems.signalservice.api.messages.SignalServiceEnvelope;

public interface CachedMessage {
    SignalServiceEnvelope loadEnvelope();

    void delete();
}
