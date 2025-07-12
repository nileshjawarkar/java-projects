package co.in.nnj.learn.jee.persi.rel;

import java.util.UUID;

import jakarta.json.bind.adapter.JsonbAdapter;

public class JsonUUIDAdapter implements JsonbAdapter<UUID, String> {
    @Override
    public String adaptToJson(final UUID obj) throws Exception {
        if(obj == null) {
            return "";
        }
        return obj.toString();
    }

    @Override
    public UUID adaptFromJson(final String obj) throws Exception {
        return UUID.fromString(obj);
    }
}
