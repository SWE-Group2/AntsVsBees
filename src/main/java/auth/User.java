package auth;

public record User(String username, String passwordHash, String salt) {
}
