package auth;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * File-backed user store. Persists users to {@code users.dat} at the project root. Passwords are hashed with SHA-256
 * over a per-user random salt.
 */
public class UserStore {
    private static final Path FILE = Paths.get("users.dat");

    private final Map<String, User> users = new HashMap<>();

    public UserStore() {
        load();
    }

    /**
     * @return true if registration succeeded; false if the username is taken or input invalid.
     */
    public boolean register(String username, String password) {
        if (username == null || username.isBlank())
            return false;
        if (password == null || password.isEmpty())
            return false;
        if (users.containsKey(username))
            return false;

        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        String saltB64 = Base64.getEncoder().encodeToString(salt);
        String hash = hash(password, salt);

        users.put(username, new User(username, hash, saltB64));
        save();
        return true;
    }

    /**
     * @return true if the credentials match a registered user.
     */
    public boolean authenticate(String username, String password) {
        if (username == null || password == null)
            return false;
        User u = users.get(username);
        if (u == null)
            return false;
        byte[] salt = Base64.getDecoder().decode(u.salt());
        return hash(password, salt).equals(u.passwordHash());
    }

    private static String hash(String pw, byte[] salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] digest = md.digest(pw.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private void load() {
        if (!Files.exists(FILE))
            return;
        try {
            for (String line : Files.readAllLines(FILE, StandardCharsets.UTF_8)) {
                if (line.isBlank())
                    continue;
                String[] parts = line.split(":", 3);
                if (parts.length == 3) {
                    users.put(parts[0], new User(parts[0], parts[1], parts[2]));
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void save() {
        try (BufferedWriter w = Files.newBufferedWriter(FILE, StandardCharsets.UTF_8)) {
            for (User u : users.values()) {
                w.write(u.username() + ":" + u.passwordHash() + ":" + u.salt());
                w.newLine();
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
