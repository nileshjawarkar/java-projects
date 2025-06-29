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
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import co.in.nnj.learn.jee.domain.service.EmployeeService;
import co.in.nnj.learn.jee.domain.valueobjects.Address;
import co.in.nnj.learn.jee.domain.valueobjects.Employee;

@Path("employee")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EmployeeController {
    //-- private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeController.class.getName());
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
        Response.Status status = Response.Status.BAD_REQUEST;
        final Employee emp = JsonMapper.jsonObjToEmployee(jsonInput, null, id);
        if (emp != null
                && !(emp.lname() == null & emp.fname() == null && emp.dob() == null && emp.dateOfJoining() == null
                        && emp.qualification() == null && emp.experties() == null && emp.type() == null
                        && emp.departmentId() == null
                        && emp.paddress() == null && emp.caddress() == null)) {
            if (employeeService.update(emp)) {
                return Response.ok().build();
            }
            status = Response.Status.INTERNAL_SERVER_ERROR;
        }
        return Response.status(status).build();
    }

    @PUT
    @POST
    @Path("{id}/address/{whichone}")
    public Response updateAddress(@PathParam("id") final String id, @PathParam("whichone") final String whichone,
            final JsonObject jsonInput) {
        Response.Status status = Response.Status.BAD_REQUEST;
        final Employee emp = employeeService.find(UUID.fromString(id));
        if (emp != null) {
            final Address address = JsonMapper.jsonObjToAddress(jsonInput);
            if (address != null) {
                Address caddress = null;
                Address paddress = null;
                if ("correspondent".equals(whichone)) {
                    caddress = address;
                } else {
                    paddress = address;
                }

                if (employeeService.update(new Employee(emp.id(), paddress, caddress))) {
                    return Response.ok().build();
                }
                status = Response.Status.INTERNAL_SERVER_ERROR;
            }
        }
        return Response.status(status).build();
    }
}
