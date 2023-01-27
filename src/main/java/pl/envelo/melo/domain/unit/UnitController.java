package pl.envelo.melo.domain.unit;

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

import java.util.List;

@RequestMapping("/v1/units")
@RestController
@Tag(name = "Unit Controller")
@AllArgsConstructor
public class UnitController {

    private final UnitService unitService;


    public ResponseEntity<UnitToDisplayOnListDto> getUnit(int id) {
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


    public ResponseEntity<?> quitUnit(Employee employee, int unitId) {
        return unitService.quitUnit(employee, unitId);
    }


    @PostMapping("")
    public ResponseEntity<?> addNewUnit(@RequestBody @Valid UnitDto unitDto) {
        return unitService.insertNewUnit(unitDto);
    }

    public ResponseEntity<Unit> updateUnit(UnitToDisplayOnListDto unitToDisplayOnListDto) {
        return unitService.updateUnit(unitToDisplayOnListDto);
    }


}
