package by.training.hospital.context;

import by.training.hospital.command.ApplicationCommandConstants;
import by.training.hospital.entity.Role;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class SecurityContext {

    private final Map<String, Set<Role>> commandsAndRoles = new HashMap<>();
    private Map<Long, HttpSession> usersSessions = new ConcurrentHashMap<>();

    private static SecurityContext instance;

    private static ReentrantLock lock = new ReentrantLock();

    private SecurityContext() {

    }

    public void init() throws ContextException {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("security.properties")) {
            Properties properties = new Properties();
            properties.load(inputStream);

            for (String command : ApplicationCommandConstants.commands) {
                String[] rolesString = properties.getProperty(command).split(",");
                if (rolesString.length == 0) {
                    throw new ContextException("No properties for this command: " + command);
                }
                Set<Role> commandRoles = new HashSet<>();
                for (String role : rolesString) {
                    if (role.trim().equals("null")) {
                        commandRoles.add(null);
                    } else {
                        commandRoles.add(Role.valueOf(role.trim().toUpperCase()));
                    }
                }
                commandsAndRoles.put(command, commandRoles);
            }

        } catch (NullPointerException e) {
            throw new ContextException("Error during creating SecurityContext. Check commands in security.properties file.", e);
        } catch (IllegalArgumentException e) {
            throw new ContextException("Error during creating SecurityContext. Check roles in security.properties file.", e);
        } catch (IOException e) {
            throw new ContextException("Error during creating SecurityContext. security.properties file couldn't be read.", e);
        }
    }

    public static SecurityContext getInstance() {
        if (instance == null) {
            try {
                lock.lock();
                if (instance == null) {
                    instance = new SecurityContext();
                }
            } finally {
                lock.unlock();
            }
        }
        return instance;
    }

    public void addNewUserSession(Long userId, HttpSession session) {
        usersSessions.put(userId, session);
    }

    public void deleteSessionByUserId(Long userId) {
        HttpSession userSession = usersSessions.get(userId);
        if (userSession != null) {
            userSession.invalidate();
            usersSessions.remove(userId);
        }
    }

    public Set<Role> getRolesForCommand(String commandName) {
        return commandsAndRoles.get(commandName);
    }
}
