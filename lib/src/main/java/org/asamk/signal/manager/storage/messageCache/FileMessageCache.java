package org.asamk.signal.manager.storage.messageCache;

import org.asamk.signal.manager.util.IOUtils;
import org.asamk.signal.manager.util.MessageCacheUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.whispersystems.signalservice.api.messages.SignalServiceEnvelope;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileMessageCache implements MessageCache {

    private final static Logger logger = LoggerFactory.getLogger(FileMessageCache.class);

    private final File messageCachePath;

    public FileMessageCache(final File messageCachePath) {
        this.messageCachePath = messageCachePath;
    }

    @Override
    public Iterable<CachedMessage> getCachedMessages() {
        if (!messageCachePath.exists()) {
            return Collections.emptyList();
        }

        return Arrays.stream(Objects.requireNonNull(messageCachePath.listFiles())).flatMap(dir -> {
            if (dir.isFile()) {
                return Stream.of(dir);
            }

            final var files = Objects.requireNonNull(dir.listFiles());
            if (files.length == 0) {
                try {
                    Files.delete(dir.toPath());
                } catch (IOException e) {
                    logger.warn("Failed to delete cache dir “{}”, ignoring: {}", dir, e.getMessage());
                }
                return Stream.empty();
            }
            return Arrays.stream(files).filter(File::isFile);
        }).map(FileCachedMessage::new).collect(Collectors.toList());
    }

    @Override
    public CachedMessage cacheMessage(SignalServiceEnvelope envelope) {
        final var now = new Date().getTime();
        final var source = envelope.hasSource() ? envelope.getSourceAddress().getLegacyIdentifier() : "";

        try {
            var cacheFile = getMessageCacheFile(source, now, envelope.getTimestamp());
            MessageCacheUtils.storeEnvelope(envelope, cacheFile);
            return new FileCachedMessage(cacheFile);
        } catch (IOException e) {
            logger.warn("Failed to store encrypted message in disk cache, ignoring: {}", e.getMessage());
            return null;
        }
    }

    private File getMessageCachePath(String sender) {
        if (sender == null || sender.isEmpty()) {
            return messageCachePath;
        }

        return new File(messageCachePath, sender.replace("/", "_"));
    }

    private File getMessageCacheFile(String sender, long now, long timestamp) throws IOException {
        var cachePath = getMessageCachePath(sender);
        IOUtils.createPrivateDirectories(cachePath);
        return new File(cachePath, now + "_" + timestamp);
    }
}
