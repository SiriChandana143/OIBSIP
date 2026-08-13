package service;

import model.User;

public class AuthenticationService {
    
    private User currentUser;

    public AuthenticationService() {
        // Pre-configure the test user
        this.currentUser = new User("student", "student123", "Student");
    }

    public boolean login(String username, String password) {
        if (currentUser.getUsername().equals(username) && currentUser.getPassword().equals(password)) {
            return true;
        }
        return false;
    }

    public void updateProfile(String displayName, String newPassword) {
        if (displayName != null && !displayName.trim().isEmpty()) {
            currentUser.setDisplayName(displayName.trim());
        }
        if (newPassword != null && !newPassword.trim().isEmpty()) {
            currentUser.setPassword(newPassword);
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }
    
    public void logout() {
        // User remains configured for next login, just simulating logout from session
    }
}
