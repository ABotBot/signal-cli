package org.asamk.signal.manager;

import org.whispersystems.signalservice.api.messages.SignalServiceAttachmentRemoteId;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public interface AttachmentStore {
    void storeAttachmentPreview(
            SignalServiceAttachmentRemoteId attachmentId, AttachmentStorer storer
    ) throws IOException;

    void storeAttachment(
            SignalServiceAttachmentRemoteId attachmentId, AttachmentStorer storer
    ) throws IOException;

    File getAttachmentFile(SignalServiceAttachmentRemoteId attachmentId);

    @FunctionalInterface
    public interface AttachmentStorer {

        void store(OutputStream outputStream) throws IOException;
    }
}
