package co.in.nnj.learn.jee.adapter.input.rest_api;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.in.nnj.learn.jee.domain.service.EmployeeService;
import co.in.nnj.learn.jee.domain.valueobjects.Employee;
import co.in.nnj.learn.jee.domain.valueobjects.EmployeeType;

@Path("employee")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EmployeeController {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeController.class.getName());
    @Inject
    EmployeeService employeeService;
    @GET
    public Response getAll(@QueryParam("fname") final String fname, @QueryParam("lname") final String lname) {
        final List<UUID> emps = (fname == null ? employeeService.findAll(null, null)
                : employeeService.findByName(fname, lname));
        if (emps == null || emps.isEmpty()) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        final JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (final UUID uuid : emps) {
            arrayBuilder.add(Json.createValue(uuid.toString()));
        }
        return Response.ok().entity(arrayBuilder.build()).build();
    }

    @GET
    @Path("{id}")
    public Response getEmployee(@PathParam("id") final String id) {
        final Employee employee = employeeService.find(UUID.fromString(id));
        if (employee == null) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        return Response.ok().entity(JsonMapper.employeeToJsonObj(employee)).build();
    }

    @DELETE
    @Path("{id}")
    public Response deleteEmployee(@PathParam("id") final String id) {
        if (employeeService.delete(UUID.fromString(id))) {
            return Response.ok().build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

    @PUT
    @Path("{id}")
    public Response updateEmployee(@PathParam("id") final String id, final JsonObject jsonInput) {
        final boolean hasFname = jsonInput.containsKey("fname");
        final boolean hasLname = jsonInput.containsKey("fname");
        final boolean hasDob = jsonInput.containsKey("dob");
        final boolean hasDoj = jsonInput.containsKey("doj");
        final boolean hasQualification = jsonInput.containsKey("qualification");
        final boolean hasExperties = jsonInput.containsKey("experties");
        final boolean hasType = jsonInput.containsKey("type");
        final boolean hasDeptId = jsonInput.containsKey("departmentId");
        Response.Status status = Response.Status.BAD_REQUEST;
        if (hasFname || hasLname || hasDob || hasDoj || hasQualification || hasExperties || hasType || hasDeptId) {
            final SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
            try {
                final Date dob = hasDob ? formater.parse(jsonInput.getString("dob")) : null;
                final Date doj = hasDoj ? formater.parse(jsonInput.getString("doj")) : null;
                final Employee emp = new Employee(UUID.fromString(id),
                        hasFname ? jsonInput.getString("fname") : null,
                        hasLname ? jsonInput.getString("lname") : null,
                        dob, doj,
                        hasQualification ? jsonInput.getString("qualification") : null,
                        hasExperties ? jsonInput.getString("experties") : null,
                        hasType ? EmployeeType.valueOf(jsonInput.getString("type")) : null,
                        hasDeptId ? UUID.fromString(jsonInput.getString("departmentId")) : null);
                if (employeeService.update(emp)) {
                    return Response.ok().build();
                }
                status = Response.Status.INTERNAL_SERVER_ERROR;
            } catch (final ParseException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
        return Response.status(status).build();
    }
}
