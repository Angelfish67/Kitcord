package ch.samira.tesan.kitcord.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Admin",
        description = "REST controller for administrator-only operations"
)
@SecurityRequirement(name = "bearerAuth")
@Validated
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Operation(
            summary = "Admin health check",
            description = "Simple endpoint to verify admin access."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Admin access granted"),
            @ApiResponse(responseCode = "401", description = "Not authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "No permission", content = @Content)
    })
    @PreAuthorize("hasAuthority('ROLE_admin')")
    @GetMapping("/health")
    public String adminHealthCheck() {
        return "Admin access granted";
    }

    @Operation(
            summary = "Admin dashboard",
            description = "Returns a simple admin dashboard response."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dashboard loaded successfully"),
            @ApiResponse(responseCode = "401", description = "Not authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "No permission", content = @Content)
    })
    @PreAuthorize("hasAuthority('ROLE_admin')")
    @GetMapping("/dashboard")
    public String dashboard() {
        return "Welcome to the admin dashboard";
    }
}