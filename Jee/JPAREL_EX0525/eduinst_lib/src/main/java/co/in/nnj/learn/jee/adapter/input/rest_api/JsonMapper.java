package co.in.nnj.learn.jee.adapter.input.rest_api;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import jakarta.json.Json;
import jakarta.json.JsonObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.in.nnj.learn.jee.domain.valueobjects.Address;
import co.in.nnj.learn.jee.domain.valueobjects.Department;
import co.in.nnj.learn.jee.domain.valueobjects.Employee;
import co.in.nnj.learn.jee.domain.valueobjects.EmployeeType;

public final class JsonMapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonMapper.class.getName());

    private JsonMapper() {
    }

    static JsonObject departmentToJsonObj(final Department dept) {
        return Json.createObjectBuilder()
                .add("id", dept.id().toString())
                .add("name", dept.name())
                .add("function", dept.function().toString())
                .build();
    }

    static Address jsonObjToAddress(final JsonObject jsonObject) {
        if (jsonObject != null) {
            return new Address(jsonObject.containsKey("street") ? jsonObject.getString("street") : null,
                    jsonObject.containsKey("city") ? jsonObject.getString("city") : null,
                    jsonObject.containsKey("state") ? jsonObject.getString("state") : null,
                    jsonObject.containsKey("country") ? jsonObject.getString("country") : null,
                    jsonObject.containsKey("pin") ? jsonObject.getString("pin") : null,
                    jsonObject.containsKey("landscape") ? jsonObject.getString("landscape") : null);
        }
        return null;
    }

    static Employee jsonObjToEmployee(final JsonObject jsonObject, final String dept_id, final String id) {
        if (jsonObject != null) {
            final String eType = jsonObject.containsKey("type") ? jsonObject.getString("type") : null;
            final String eDob = jsonObject.containsKey("dob") ? jsonObject.getString("dob") : null;
            final String eDoj = jsonObject.containsKey("doj") ? jsonObject.getString("doj") : null;
            final JsonObject paddress = jsonObject.containsKey("paddress") ? jsonObject.getJsonObject("paddress")
                    : null;
            final JsonObject caddress = jsonObject.containsKey("caddress") ? jsonObject.getJsonObject("caddress")
                    : null;
            try {
                final SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
                final Date dob = eDob != null ? formater.parse(eDob) : null;
                final Date doj = eDoj != null ? formater.parse(eDoj) : null;
                return new Employee(
                        id != null ? UUID.fromString(id) : null,
                        jsonObject.containsKey("fname") ? jsonObject.getString("fname") : null, jsonObject.containsKey("lname") ? jsonObject.getString("lname") : null, dob, doj,
                        jsonObject.containsKey("qualification") ? jsonObject.getString("qualification") : null,
                        jsonObject.containsKey("experties") ? jsonObject.getString("experties") : null,
                        eType != null ? EmployeeType.valueOf(eType) : null,
                        dept_id != null ? UUID.fromString(dept_id) : null,
                        jsonObjToAddress(paddress),
                        jsonObjToAddress(caddress));
            } catch (final ParseException e) {
                LOGGER.error(String.format("Failed to parse DOB {%s} or DOJ {%s}", eDob, eDoj));
                LOGGER.debug(" - ", e);
            } catch(final IllegalStateException e) {
                LOGGER.error(String.format("Failed to convert input attributes to required type. %s", e.getMessage()));
                LOGGER.debug(" - ", e);
            }
        }
        return null;
    }

    static JsonObject employeeToJsonObj(final Employee emp) {
        final SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
        return Json.createObjectBuilder()
                .add("id", emp.id().toString())
                .add("department_id", emp.departmentId().toString())
                .add("fname", emp.fname())
                .add("lname", emp.lname())
                .add("experties", emp.experties())
                .add("qualification", emp.qualification())
                .add("dob", formater.format(emp.dob()))
                .add("doj", formater.format(emp.dateOfJoining()))
                .build();
    }
}
