package pl.envelo.melo.authorization.employee;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.envelo.melo.authorization.employee.dto.EmployeeDto;
import pl.envelo.melo.authorization.employee.dto.EmployeeListDto;
import pl.envelo.melo.domain.event.dto.EventToDisplayOnListDto;
import pl.envelo.melo.domain.unit.dto.UnitToDisplayOnListDto;

import java.security.Principal;
import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("v1/users")
public class EmployeeController {
    private final EmployeeService employeeService;

    @PreAuthorize("hasAnyAuthority(@securityConfiguration.getAdminRole(), @securityConfiguration.getEmployeeRole())")
    @GetMapping()
    public ResponseEntity<Set<EmployeeListDto>> getEmployees(@RequestParam(value = "search", required = false) String q) {
        return employeeService.getEmployees(q);
    }

    @PreAuthorize("hasAuthority(@securityConfiguration.getEmployeeRole())")
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getEmployee(@PathVariable("id") int id, Principal principal) {
        return employeeService.getEmployee(id, principal);
    }

    @PreAuthorize("hasAuthority(@securityConfiguration.getEmployeeRole())")
    @GetMapping("/{id}/owned-events")
    public ResponseEntity<Set<EventToDisplayOnListDto>> getOwnedEvents(@PathVariable("id") int id, Principal principal) {
        return (ResponseEntity<Set<EventToDisplayOnListDto>>) employeeService.getSetOfOwnedEvents(id, principal);
    }

    @PreAuthorize("hasAuthority(@securityConfiguration.getEmployeeRole())")
    @Operation(summary = "Retrieve list of employee joined units",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Retrieve list of employee joined units", content =
                    @Content(mediaType = "application/json", schema = @Schema(
                            description = "List of employee joined units",
                            oneOf = {UnitToDisplayOnListDto.class}
                    ))
                    )
            })
    @GetMapping("/{id}/joined-units")
    public ResponseEntity<?> getJoinedUnitList(@PathVariable("id") int id, Principal principal) {
        return employeeService.getListOfJoinedUnits(id, principal);
    }

    @PreAuthorize("hasAuthority(@securityConfiguration.getEmployeeRole())")
    @GetMapping("/{id}/owned-units")
    @Operation(summary = "Retrieve list of created units by employee",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Retrieve list of units", content =
                    @Content(mediaType = "application/json", schema = @Schema(
                            description = "List of units",
                            oneOf = {UnitToDisplayOnListDto.class}
                    ))),
                    @ApiResponse(responseCode = "404", description = "Employee does not exist")
            })
    public ResponseEntity<?> getListOfCreatedUnits(@PathVariable("id") int id, Principal principal) {
        return employeeService.getListOfCreatedUnits(id, principal);
    }
}
