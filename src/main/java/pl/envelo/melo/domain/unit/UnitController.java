package pl.envelo.melo.domain.unit;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.envelo.melo.authorization.employee.Employee;
import pl.envelo.melo.domain.unit.dto.UnitDetailsDto;
import pl.envelo.melo.domain.unit.dto.UnitNewDto;
import pl.envelo.melo.domain.unit.dto.UnitToDisplayOnListDto;

import java.security.Principal;
import java.util.List;

@RequestMapping("/v1/units")
@RestController
@Tag(name = "Unit Controller")
@AllArgsConstructor
@CrossOrigin(origins = "${melo.cors-origin}")
public class UnitController {
    private final UnitService unitService;

    @PreAuthorize("hasAnyAuthority(@securityConfiguration.getAdminRole(), @securityConfiguration.getEmployeeRole())")
    @GetMapping("/{id}")
    @Operation(summary = "Retrieve concrete unit by id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Retrieve details of single unit with given id", content =
                    @Content(mediaType = "application/json", schema = @Schema(
                            description = "Unit details",
                            implementation = UnitDetailsDto.class
                    ))
                    ),
                    @ApiResponse(responseCode = "404", description = "Error when unit with given ID is missing")
            })
    public ResponseEntity<?> getUnit(@PathVariable("id") int id) {
        return unitService.getUnit(id);
    }

    @PreAuthorize("hasAnyAuthority(@securityConfiguration.getAdminRole(), @securityConfiguration.getEmployeeRole())")
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
    public ResponseEntity<?> getUnits(@RequestParam(required = false, name = "search") String text) {
        return unitService.getUnits(text);
    }


    public ResponseEntity<List<Employee>> getUnitEmployees() {
        return unitService.getUnitEmployees();
    }

    @PreAuthorize("hasAnyAuthority( @securityConfiguration.getEmployeeRole(), @securityConfiguration.getAdminRole())")
    @PatchMapping("/{unitId}/owner")
    @Operation(summary = "Change unit owner from current to another employee")
    public ResponseEntity<?> changeOwnership(@PathVariable("unitId") int unitId,
                                             @RequestParam("newEmployeeId") int newEmployeeId,
                                             Principal principal) {
        return unitService.changeOwnership(newEmployeeId, unitId, principal);
    }

    @PreAuthorize("hasAuthority( @securityConfiguration.getEmployeeRole())")
    @GetMapping("/{unitId}/join")
    @Operation(summary = "Add employee to unit members",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Employee added to unit"),
                    @ApiResponse(responseCode = "400", description = "Employee already in unit"),
                    @ApiResponse(responseCode = "404", description = "Unit or employee do not exist")
            })
    public ResponseEntity<?> addEmployee(@PathVariable("unitId") int unitId, Principal principal) {
        return unitService.addEmployee(unitId, principal);
    }

    @PreAuthorize("hasAuthority(@securityConfiguration.getEmployeeRole())")
    @Transactional
    @PatchMapping("/{unitId}/members")
    @Operation(summary = "Remove employee from unit members",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Employee was removed"),
                    @ApiResponse(responseCode = "404"),
            })
    public ResponseEntity<?> quitUnit(@PathVariable("unitId") int unitId, Principal principal) {
        return unitService.quitUnit(unitId, principal);
    }

    @PreAuthorize("hasAuthority(@securityConfiguration.getEmployeeRole())")
    @PostMapping("")
    @Operation(summary = "Add new unit",
            responses = {
                    @ApiResponse(responseCode = "200", description = "New unit is created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UnitToDisplayOnListDto.class))),
                    @ApiResponse(responseCode = "404"),
                    @ApiResponse(responseCode = "400", description = "Wrong data")
            })
    public ResponseEntity<?> addNewUnit(@RequestBody @Valid UnitNewDto unitDto, Principal principal) {
        return unitService.insertNewUnit(unitDto, principal);
    }

    @PreAuthorize("hasAuthority(@securityConfiguration.getEmployeeRole())")
    @PatchMapping("/{unit-id}")
    @Operation(summary = "Edit unit", responses = {
            @ApiResponse(responseCode = "200", description = "Unit edited successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UnitToDisplayOnListDto.class))),
            @ApiResponse(responseCode = "404", description = "Unit with given ID is not present in database."),
            @ApiResponse(responseCode = "400", description = "Name and description that you provided are the same as in database. <br />" +
                    "Name must be between 2 and 255 characters. <br />" +
                    "Description must not exceed 4000 characters.<br />")
    })
    public ResponseEntity<?> updateUnit(@PathVariable("unit-id") int id, @RequestBody @Valid UnitNewDto unitNewDto, Principal principal) {
        return unitService.updateUnit(id, unitNewDto, principal);
    }


}
