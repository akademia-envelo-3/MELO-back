package pl.envelo.melo.authorization.employee;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.envelo.melo.authorization.employee.dto.EmployeeDto;
import pl.envelo.melo.domain.event.Event;
import pl.envelo.melo.domain.event.dto.EventToDisplayOnListDto;
import pl.envelo.melo.domain.unit.dto.UnitToDisplayOnListDto;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
public class EmployeeController {

    private final EmployeeService employeeService;


    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }


    public ResponseEntity<List<EmployeeDto>> getEmployees(){
        return null;
    }


    @GetMapping("employee/{id}")
    public ResponseEntity<EmployeeDto> getEmployee(@PathVariable int id){
        return employeeService.getEmployee(id);
    }


    @GetMapping("/user/{id}/owned-events")
    public ResponseEntity<Set<EventToDisplayOnListDto>> getOwnedEvents(@PathVariable int id){
        return (ResponseEntity<Set<EventToDisplayOnListDto>>) employeeService.getSetOfOwnedEvents(id);
    }

    @Operation(summary = "Retrieve list of employee joined units",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Retrieve list of employee joined units", content =
                    @Content(mediaType = "application/json", schema = @Schema(
                            description = "List of employee joined units",
                            oneOf = {UnitToDisplayOnListDto.class}
                    ))
                    )
            })
    @GetMapping("/user/{id}/joined-units")
    public ResponseEntity<?> getJoinedUnitList(@PathVariable("id") int id) {
        return employeeService.getListOfJoinedUnits(id);
    }
}
