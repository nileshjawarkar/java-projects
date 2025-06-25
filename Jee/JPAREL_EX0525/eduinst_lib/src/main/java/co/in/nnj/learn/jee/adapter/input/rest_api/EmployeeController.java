package co.in.nnj.learn.jee.adapter.input.rest_api;

import java.util.List;
import java.util.UUID;

import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import co.in.nnj.learn.jee.domain.service.EmployeeService;
import co.in.nnj.learn.jee.domain.valueobjects.Employee;

@Path("employee")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EmployeeController {
    @Inject
    EmployeeService employeeService;

    @GET
    public Response getAll(@QueryParam("fname") final String fname, @QueryParam("lname") final String lname) {
        final List<Employee> emps = (fname == null ? employeeService.findAll() : employeeService.findByName(fname, lname));
        if(emps == null || emps.isEmpty()) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        final JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (final Employee employee : emps) {
           arrayBuilder.add(JsonMapper.employeeToJsonObj(employee));
        }
        return Response.ok().entity(arrayBuilder.build()).build();
    }

    @GET
    @Path("{id}")
    public Response getEmployee(@PathParam("id") final String id) {
        final Employee employee = employeeService.find(UUID.fromString(id));
        if(employee == null) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        return Response.ok().entity(JsonMapper.employeeToJsonObj(employee)).build();
    }
}
