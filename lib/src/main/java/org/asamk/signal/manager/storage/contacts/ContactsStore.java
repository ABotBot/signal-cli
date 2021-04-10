package org.asamk.signal.manager.storage.contacts;

import org.whispersystems.signalservice.api.push.SignalServiceAddress;

import java.util.List;

public interface ContactsStore {
    void updateContact(ContactInfo contact);

    ContactInfo getContact(SignalServiceAddress address);

    List<ContactInfo> getContacts();

    void clear();
}
