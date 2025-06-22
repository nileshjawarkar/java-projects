package co.in.nnj.learn.jee.port.input.controller;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

import jakarta.json.Json;
import jakarta.json.JsonException;
import jakarta.json.JsonObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.in.nnj.learn.jee.domain.valueobjects.Department;
import co.in.nnj.learn.jee.domain.valueobjects.Employee;
import co.in.nnj.learn.jee.domain.valueobjects.EmployeeType;

public final class JsonMapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonMapper.class.getName());

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

    static Employee jsonObjToEmployee(final JsonObject jsonObject) {
        try {
            final String eFName = jsonObject.getString("fname");
            final String eLName = jsonObject.getString("lname");
            final String eType = jsonObject.getString("type");
            final String eExpertices = jsonObject.getString("experties");
            final String eQualification = jsonObject.getString("qualification");
            final String eDob = jsonObject.getString("dob");
            final String eDoj = jsonObject.getString("doj");
            try {
                final DateTimeFormatter dateFormater = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                final Date dob = Date.from(Instant.from(dateFormater.parse(eDob)));
                final Date doj = Date.from(Instant.from(dateFormater.parse(eDoj)));
                return new Employee(eFName, eLName, dob, doj, eQualification, eExpertices, EmployeeType.valueOf(eType));
            } catch (final DateTimeParseException e) {
                LOGGER.error(String.format("Failed to parse DOB {%s} or DOJ {%s}", eDob, eDoj));
                LOGGER.debug(" - ", e);
            }
        } catch (final JsonException e) {
            LOGGER.error("Mandetory attribute missing.");
            LOGGER.debug(" - ", e);
        }
        return null;
    }

    static JsonObject employeeToJsonObj(final Employee emp) {
        final SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
        final JsonObject jsonObject = Json.createObjectBuilder()
                .add("id", emp.id().toString())
                .add("fname", emp.fname())
                .add("lname", emp.lname())
                .add("experties", emp.experties())
                .add("qualification", emp.qualification())
                .add("dob", formater.format(emp.dob()))
                .add("doj", formater.format(emp.dateOfJoining()))
                .build();
        return jsonObject;
    }
}
