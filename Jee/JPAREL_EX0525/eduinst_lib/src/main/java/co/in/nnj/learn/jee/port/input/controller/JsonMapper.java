package co.in.nnj.learn.jee.port.input.controller;

import jakarta.json.Json;
import jakarta.json.JsonObject;

import co.in.nnj.learn.jee.domain.valueobjects.Department;

public final class JsonMapper {
    private JsonMapper() {
    }

    static JsonObject departmentToJsonObj(final Department dept) {
        final JsonObject jsonObject = Json.createObjectBuilder()
                .add("id", dept.id().toString())
                .add("name", dept.name())
                .add("function", dept.function().toString())
                .build();
        return jsonObject;
    }
}
