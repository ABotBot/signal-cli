package org.asamk.signal.manager.storage.contacts;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.whispersystems.signalservice.api.push.SignalServiceAddress;

import java.util.ArrayList;
import java.util.List;

public class JsonContactsStore implements ContactsStore {

    @JsonProperty("contacts")
    private List<ContactInfo> contacts = new ArrayList<>();

    @Override
    public void updateContact(ContactInfo contact) {
        final var contactAddress = contact.getAddress();
        for (var i = 0; i < contacts.size(); i++) {
            if (contacts.get(i).getAddress().matches(contactAddress)) {
                contacts.set(i, contact);
                return;
            }
        }

        contacts.add(contact);
    }

    @Override
    public ContactInfo getContact(SignalServiceAddress address) {
        for (var contact : contacts) {
            if (contact.getAddress().matches(address)) {
                if (contact.uuid == null) {
                    contact.uuid = address.getUuid().orNull();
                } else if (contact.number == null) {
                    contact.number = address.getNumber().orNull();
                }

                return contact;
            }
        }
        return null;
    }

    @Override
    public List<ContactInfo> getContacts() {
        return new ArrayList<>(contacts);
    }

    /**
     * Remove all contacts from the store
     */
    @Override
    public void clear() {
        contacts.clear();
    }
}
