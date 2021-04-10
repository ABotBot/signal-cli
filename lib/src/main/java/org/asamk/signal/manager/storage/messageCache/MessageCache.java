package org.asamk.signal.manager.storage.messageCache;

import org.whispersystems.signalservice.api.messages.SignalServiceEnvelope;

public interface MessageCache {
    Iterable<CachedMessage> getCachedMessages();

    CachedMessage cacheMessage(SignalServiceEnvelope envelope);
}
