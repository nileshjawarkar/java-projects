package co.in.nnj.learn.jee.adapter.input.rest_api;

import java.util.List;
import java.util.UUID;

import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
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
        Response.Status status = Response.Status.BAD_REQUEST;
        String errMessage = "Mandetory attributes are missing from input or assigned invalide values.";
        if (jsonInput.containsKey("name") && jsonInput.containsKey("function")) {
            try {
                final String name = jsonInput.getString("name");
                final Department created = departmentSrv
                        .create(new Department(name, DepartmentType.valueOf(jsonInput.getString("function"))));
                if (created != null) {
                    return Response.status(Response.Status.CREATED).entity(JsonMapper.departmentToJsonObj(created))
                            .location(uriInfo.getBaseUriBuilder().path(DepartmentController.class)
                                    .path(DepartmentController.class, "getDepartment").build(created.id()))
                            .build();
                }
                status = Response.Status.INTERNAL_SERVER_ERROR;
                errMessage = "Failed to create deparment.";
            } catch (final IllegalArgumentException e) {
            }
        }
        return Response.status(status).entity(errMessage).build();
    }

    @PUT
    @Path("{id}")
    public Response updateDepartment(@PathParam("id") final String id, final JsonObject jsonInput) {
        final boolean hasName = jsonInput.containsKey("name");
        final boolean hasDep = jsonInput.containsKey("function");
        if (hasName || hasDep) {
            final Department dept = new Department(
                    hasName ? jsonInput.getString("name") : null,
                    hasDep ? DepartmentType.valueOf(jsonInput.getString("function")) : null,
                    UUID.fromString(id));
            if (departmentSrv.update(dept)) {
                return Response.ok().build();
            }
        }
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(String.format("Error - Failed to update deparment with id[%s]", id))
                .build();
    }

    @DELETE
    @Path("{id}")
    public Response deleteDepartment(@PathParam("id") final String id) {
        if (departmentSrv.deleteDepartment(UUID.fromString(id))) {
            return Response.ok().build();
        }
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @GET
    @Path("{id}")
    public Response getDepartment(@PathParam("id") final String id) {
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

    @GET
    @Path("{id}/employee")
    public Response getEmployees(@PathParam("id") final String id) {
        final UUID uuid = (id == null || id.isEmpty()) ? null : UUID.fromString(id);
        if (uuid != null) {
            final List<Employee> emps = employeeService.findByDepartment(uuid);
            final JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
            for (final Employee emp : emps) {
                arrayBuilder.add(JsonMapper.employeeToJsonObj(emp));
            }
            return Response.ok().entity(arrayBuilder.build()).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @POST
    @Path("{id}/employee")
    public Response addEmployee(@PathParam("id") final String id, final JsonObject jsonObject) {
        final Employee emp = JsonMapper.jsonObjToEmployee(jsonObject, id);
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
