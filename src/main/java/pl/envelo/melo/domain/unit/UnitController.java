package pl.envelo.melo.domain.unit;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.envelo.melo.authorization.employee.Employee;
import pl.envelo.melo.domain.unit.dto.UnitToDisplayOnListDto;
import pl.envelo.melo.domain.unit.dto.UnitNewDto;

import java.util.List;

@RequestMapping("/v1/units")
@RestController
@Tag(name = "Unit Controller")
@AllArgsConstructor
public class UnitController {

    private final UnitService unitService;

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve list of units",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Retrieve details of single unit with given id", content =
                    @Content(mediaType = "application/json", schema = @Schema(
                            description = "",
                            oneOf = {UnitToDisplayOnListDto.class}
                    ))
                    ),
                    @ApiResponse(responseCode = "404", description = "Error when unit with given ID is missing")
            })
    public ResponseEntity<?> getUnit(@PathVariable("id") int id) {
        return unitService.getUnit(id);
    }

    @GetMapping
    @Operation(summary = "Retrieve list of units",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Retrieve list of units", content =
                    @Content(mediaType = "application/json", schema = @Schema(
                            description = "List of units",
                            oneOf = {UnitToDisplayOnListDto.class}
                    ))
                    )
            })
    public ResponseEntity<?> getUnits() {
        return unitService.getUnits();
    }


    public ResponseEntity<List<Employee>> getUnitEmployees() {
        return unitService.getUnitEmployees();
    }


    public ResponseEntity<Unit> changeOwnership(int newEmployeeId) {
        return unitService.changeOwnership(newEmployeeId);
    }


    public ResponseEntity<?> addEmployee(Employee employee, int unitId) {
        return unitService.addEmployee(employee, unitId);
    }

    @Transactional
    @PatchMapping("/{unitId}/members/{employeeIdToken}")
    @Operation(summary = "Remove employee from unit members",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Employee was removed"),
                    @ApiResponse(responseCode = "404"),
            })
    public ResponseEntity<?> quitUnit(@PathVariable("employeeIdToken") int employeeIdToken, @PathVariable("unitId") int unitId) {
        return unitService.quitUnit(employeeIdToken, unitId);
    }


    @PostMapping("")
    @Operation(summary = "Add new unit",
    responses = {
            @ApiResponse(responseCode = "200", description = "New unit is created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UnitToDisplayOnListDto.class))),
            @ApiResponse(responseCode = "404"),
            @ApiResponse(responseCode = "400", description = "Wrong data")
    })
    public ResponseEntity<?> addNewUnit(@RequestBody @Valid UnitNewDto unitDto) {
        return unitService.insertNewUnit(unitDto);
    }

    public ResponseEntity<Unit> updateUnit(UnitToDisplayOnListDto unitToDisplayOnListDto) {
        return unitService.updateUnit(unitToDisplayOnListDto);
    }


}
