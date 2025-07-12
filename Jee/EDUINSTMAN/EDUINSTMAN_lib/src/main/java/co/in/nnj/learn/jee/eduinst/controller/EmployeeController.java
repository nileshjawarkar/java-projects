package co.in.nnj.learn.jee.eduinst.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import jakarta.inject.Inject;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.in.nnj.learn.jee.eduinst.boundry.EmployeeService;
import co.in.nnj.learn.jee.eduinst.entity.Address;
import co.in.nnj.learn.jee.eduinst.entity.Employee;
import co.in.nnj.learn.jee.eduinst.entity.EmployeeType;
import co.in.nnj.learn.jee.eduinst.entity.SupportStaff;
import co.in.nnj.learn.jee.eduinst.entity.TeachingStaff;

@Path("employee")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EmployeeController {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeController.class.getSimpleName()); 
    // -- private static final Logger LOGGER =
    // LoggerFactory.getLogger(EmployeeController.class.getName());
    @Inject
    EmployeeService employeeService;
                                                                                                    
    @GET
    public Response getAll(@QueryParam("fname") final String fname, @QueryParam("lname") final String lname) {
        final List<UUID> emps;
        if (fname != null && lname != null) {
            emps = employeeService.findByName(fname, lname);
        } else {
            emps = employeeService.findAll();
        }
        if (!emps.isEmpty()) {
            return Response.ok(emps).build();
        }
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @GET
    @Path("{id}")
    public Response getEmployee(@PathParam("id") final String id) {
        return employeeService.find(UUID.fromString(id)).map(Response::ok)
                .orElseGet(Response::noContent).build();
    }

    @DELETE
    @Path("{id}")
    public Response deleteEmployee(@PathParam("id") final String id) {
        if (employeeService.delete(UUID.fromString(id))) {
            return Response.ok().build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

    Optional<Employee> jsonObjToEmployee(final JsonObject jsonObject, final String deptId, final String id) {
        if (jsonObject == null) {
            return Optional.empty();
        }
        final EmployeeType eType = jsonObject.containsKey("type") ? EmployeeType.valueOf(jsonObject.getString("type")) : EmployeeType.UNKNOWN;
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

            Employee emp = null;
            if(eType == EmployeeType.TEACHING) {
                final TeachingStaff ts = new TeachingStaff();

                emp = ts;
            } else if(eType == EmployeeType.SUPPORT) {
                final SupportStaff ss = new SupportStaff();

                emp = ss;
            }
            return Optional.ofNullable(emp);
        } catch (final ParseException e) {
            LOGGER.error(String.format("Failed to parse DOB {%s} or DOJ {%s}", eDob, eDoj));
            LOGGER.debug(" - ", e);
        } catch (final IllegalStateException e) {
            LOGGER.error(String.format("Failed to convert input attributes to required type. %s", e.getMessage()));
            LOGGER.debug(" - ", e);
        }
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
        final Optional<Employee> empOp = employeeService.find(UUID.fromString(id));
        if (empOp.isPresent()) {
            final Employee emp = empOp.get();
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
