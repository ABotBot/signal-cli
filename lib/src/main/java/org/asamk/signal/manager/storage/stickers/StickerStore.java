package org.asamk.signal.manager.storage.stickers;

public interface StickerStore {
    Sticker getSticker(byte[] packId);

    void updateSticker(Sticker sticker);
}
