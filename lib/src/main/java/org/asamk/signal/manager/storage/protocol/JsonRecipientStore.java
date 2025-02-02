package org.asamk.signal.manager.storage.protocol;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.whispersystems.signalservice.api.push.SignalServiceAddress;
import org.whispersystems.signalservice.api.util.UuidUtil;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class JsonRecipientStore implements RecipientStore {

    @JsonProperty("recipientStore")
    @JsonDeserialize(using = RecipientStoreDeserializer.class)
    @JsonSerialize(using = RecipientStoreSerializer.class)
    private final Set<SignalServiceAddress> addresses = new HashSet<>();

    @Override
    public SignalServiceAddress resolveServiceAddress(SignalServiceAddress serviceAddress) {
        if (addresses.contains(serviceAddress)) {
            // If the Set already contains the exact address with UUID and Number,
            // we can just return it here.
            return serviceAddress;
        }

        for (var address : addresses) {
            if (address.matches(serviceAddress)) {
                return address;
            }
        }

        if (serviceAddress.getNumber().isPresent() && serviceAddress.getUuid().isPresent()) {
            addresses.add(serviceAddress);
        }

        return serviceAddress;
    }

    public static class RecipientStoreDeserializer extends JsonDeserializer<Set<SignalServiceAddress>> {

        @Override
        public Set<SignalServiceAddress> deserialize(
                JsonParser jsonParser, DeserializationContext deserializationContext
        ) throws IOException {
            JsonNode node = jsonParser.getCodec().readTree(jsonParser);

            var addresses = new HashSet<SignalServiceAddress>();

            if (node.isArray()) {
                for (var recipient : node) {
                    var recipientName = recipient.get("name").asText();
                    var uuid = UuidUtil.parseOrThrow(recipient.get("uuid").asText());
                    final var serviceAddress = new SignalServiceAddress(uuid, recipientName);
                    addresses.add(serviceAddress);
                }
            }

            return addresses;
        }
    }

    public static class RecipientStoreSerializer extends JsonSerializer<Set<SignalServiceAddress>> {

        @Override
        public void serialize(
                Set<SignalServiceAddress> addresses, JsonGenerator json, SerializerProvider serializerProvider
        ) throws IOException {
            json.writeStartArray();
            for (var address : addresses) {
                json.writeStartObject();
                json.writeStringField("name", address.getNumber().get());
                json.writeStringField("uuid", address.getUuid().get().toString());
                json.writeEndObject();
            }
            json.writeEndArray();
        }
    }
}
