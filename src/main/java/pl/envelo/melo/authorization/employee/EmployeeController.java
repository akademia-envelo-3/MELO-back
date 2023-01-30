package pl.envelo.melo.authorization.employee;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.envelo.melo.authorization.employee.dto.EmployeeDto;
import pl.envelo.melo.domain.event.Event;
import pl.envelo.melo.domain.event.dto.EventToDisplayOnListDto;
import pl.envelo.melo.domain.unit.dto.UnitToDisplayOnListDto;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("v1/users/")
public class EmployeeController {

    private final EmployeeService employeeService;


    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }


    public ResponseEntity<List<EmployeeDto>> getEmployees(){
        return null;
    }


    @GetMapping("{id}")
    public ResponseEntity<EmployeeDto> getEmployee(@PathVariable int id){
        return employeeService.getEmployee(id);
    }


    @GetMapping("{id}/owned-events")
    public ResponseEntity<Set<EventToDisplayOnListDto>> getOwnedEvents(@PathVariable int id){
        return (ResponseEntity<Set<EventToDisplayOnListDto>>) employeeService.getSetOfOwnedEvents(id);
    }

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
    public ResponseEntity<?> getListOfCreatedUnits(@PathVariable("id") int id){
        return employeeService.getListOfCreatedUnits(id);
    }
}
