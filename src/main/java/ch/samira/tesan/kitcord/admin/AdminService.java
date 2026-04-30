package ch.samira.tesan.kitcord.admin;

import org.springframework.stereotype.Service;

@Service
public class AdminService {

    public String getAdminHealthStatus() {
        return "Admin access granted";
    }

    public String getDashboardMessage() {
        return "Welcome to the admin dashboard";
    }
}