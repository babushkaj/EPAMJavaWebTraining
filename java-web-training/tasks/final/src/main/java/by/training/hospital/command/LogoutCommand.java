package by.training.hospital.command;

import by.training.hospital.context.SecurityContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LogoutCommand implements Command {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long currentUserId = (Long) req.getSession().getAttribute("currentUserId");
        SecurityContext.getInstance().deleteSessionByUserId(currentUserId);

        resp.sendRedirect(req.getContextPath() + "/");
    }
}
