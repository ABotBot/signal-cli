package org.asamk.signal.manager.storage;

import org.asamk.signal.manager.AttachmentStore;
import org.asamk.signal.manager.util.IOUtils;
import org.whispersystems.signalservice.api.messages.SignalServiceAttachmentRemoteId;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class FileAttachmentStore implements AttachmentStore {

    private final File attachmentsPath;

    public FileAttachmentStore(final File attachmentsPath) {
        this.attachmentsPath = attachmentsPath;
    }

    @Override public void storeAttachmentPreview(
            final SignalServiceAttachmentRemoteId attachmentId, final AttachmentStorer storer
    ) throws IOException {
        storeAttachment(getAttachmentPreviewFile(attachmentId), storer);
    }

    @Override public void storeAttachment(
            final SignalServiceAttachmentRemoteId attachmentId, final AttachmentStorer storer
    ) throws IOException {
        storeAttachment(getAttachmentFile(attachmentId), storer);
    }

    private void storeAttachment(final File attachmentFile, final AttachmentStorer storer) throws IOException {
        createAttachmentsDir();
        try (OutputStream output = new FileOutputStream(attachmentFile)) {
            storer.store(output);
        }
    }

    private File getAttachmentPreviewFile(SignalServiceAttachmentRemoteId attachmentId) {
        return new File(attachmentsPath, attachmentId.toString() + ".preview");
    }

    @Override public File getAttachmentFile(SignalServiceAttachmentRemoteId attachmentId) {
        return new File(attachmentsPath, attachmentId.toString());
    }

    private void createAttachmentsDir() throws IOException {
        IOUtils.createPrivateDirectories(attachmentsPath);
    }

    }
