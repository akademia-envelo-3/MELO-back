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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.envelo.melo.authorization.AuthorizationService;
import pl.envelo.melo.authorization.employee.Employee;
import pl.envelo.melo.domain.event.Event;
import pl.envelo.melo.domain.unit.dto.UnitToDisplayOnListDto;
import pl.envelo.melo.domain.unit.dto.UnitNewDto;

import java.security.Principal;
import java.util.List;

@RequestMapping("/v1/units")
@RestController
@Tag(name = "Unit Controller")
@AllArgsConstructor
public class UnitController {
    private final UnitService unitService;
    private final AuthorizationService authorizationService;
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
    public ResponseEntity<?> getUnit(@PathVariable("id") int id, Principal principal) {
        System.out.println(authorizationService.inflateUser(principal));
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

    @Transactional
    @PatchMapping("/{unitId}/owner{oldOwnerId}/")
    @Operation(summary = "Change unit owner from current to another employee")
    public ResponseEntity<?> changeOwnership(@PathVariable("unitId") int unitId,
                                                @PathVariable("oldOwnerId") int currentTokenId,
                                                @RequestBody int newEmployeeId) {
        return unitService.changeOwnership(newEmployeeId,currentTokenId,unitId);
    }

    @PatchMapping("{id}/owner")
    public ResponseEntity<?> changeOwnershipByAdmin(@PathVariable("id") int unitId, @RequestParam("new-owner") int newOwner) {
        return unitService.changeOwnershipByAdmin(unitId, newOwner);
    }
     
     
    @GetMapping("/{unitId}/join/{id}")
    @Operation(summary = "Add employee to unit members",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Employee added to unit"),
                    @ApiResponse(responseCode = "400", description = "Employee already in unit"),
                    @ApiResponse(responseCode = "404", description = "Unit or employee do not exist")
            })
    public ResponseEntity<?> addEmployee(@PathVariable("id") int employeeId, @PathVariable("unitId") int unitId) {
        return unitService.addEmployee(employeeId, unitId);
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

    @PatchMapping("/{unit-id}")
    @Operation(summary = "Edit unit",responses = {
            @ApiResponse(responseCode = "200", description = "Unit edited successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UnitToDisplayOnListDto.class))),
            @ApiResponse(responseCode = "404", description = "Unit with given ID is not present in database."),
            @ApiResponse(responseCode = "400", description = "Name and description that you provided are the same as in database.")
    })
    public ResponseEntity<?> updateUnit(@RequestParam("unit-id") int id, @RequestBody @Valid UnitNewDto unitNewDto) {
        return unitService.updateUnit(id, unitNewDto);
    }


}
