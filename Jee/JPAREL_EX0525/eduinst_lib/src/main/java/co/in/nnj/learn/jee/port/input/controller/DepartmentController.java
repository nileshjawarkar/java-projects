package co.in.nnj.learn.jee.port.input.controller;

import java.util.List;
import java.util.UUID;

import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.in.nnj.learn.jee.domain.service.DepartmentService;
import co.in.nnj.learn.jee.domain.service.EmployeeService;
import co.in.nnj.learn.jee.domain.valueobjects.Department;
import co.in.nnj.learn.jee.domain.valueobjects.DepartmentType;
import co.in.nnj.learn.jee.domain.valueobjects.Employee;

@Path("department")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DepartmentController {
    private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentController.class.getName());
    @Inject
    DepartmentService departmentSrv;
    @Inject
    EmployeeService employeeService;

    @Context
    UriInfo uriInfo;

    @POST
    public Response createDepartment(final JsonObject jsonInput) {
        final String name = jsonInput.containsKey("name") ? jsonInput.getString("name") : null;
        final String function = jsonInput.containsKey("function") ? jsonInput.getString("function") : null;
        if (name == null || name.isEmpty() || function == null || function.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error - name and function are mandatory attributes.")
                    .build();
        }

        try {
            final DepartmentType type = DepartmentType.valueOf(function);
            final Department created = departmentSrv.create(new Department(name, type));
            final JsonObject jsonObject = JsonMapper.departmentToJsonObj(created);

            return Response.status(Response.Status.CREATED).entity(jsonObject)
                    .location(uriInfo.getBaseUriBuilder().path(DepartmentController.class)
                            .path(DepartmentController.class, "findDepartment").build(created.id()))
                    .build();
        } catch (final IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(String.format("Error - [%s] is not valid value for function.", function))
                    .build();
        }
    }

    @GET
    @Path("{id}")
    public Response findDepartment(@PathParam("id") final String id) {
        final UUID uuid = UUID.fromString(id);
        final Department dept = departmentSrv.find(uuid);
        if (dept == null) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        return Response.ok().entity(JsonMapper.departmentToJsonObj(dept)).build();
    }

    Response listToResponse(final List<Department> depts) {
        if (depts == null || depts.isEmpty()) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }

        final JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (final Department dept : depts) {
            arrayBuilder.add(JsonMapper.departmentToJsonObj(dept));
        }
        return Response.ok().entity(arrayBuilder.build()).build();
    }

    @GET
    public Response findAll(@QueryParam("name") final String name) {
        LOGGER.info("name - " + name);
        final List<Department> depts = (name == null ? departmentSrv.findAll() : departmentSrv.findByName(name));
        return listToResponse(depts);
    }

    @POST
    @Path("{id}/employee")
    public Response addEmployee(@PathParam("id") final String id, final JsonObject jsonObject) {
        final Employee emp = JsonMapper.jsonObjToEmployee(jsonObject);
        if (emp == null || id == null || id.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .build();
        }
        final Employee newEmp = employeeService.create(emp);
        if (newEmp == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(String.format("Error - Failed to create employee [name = %s %s]", emp.fname(), emp.lname()))
                    .build();
        }
        return Response.ok().entity(JsonMapper.employeeToJsonObj(newEmp)).build();
    }
}
