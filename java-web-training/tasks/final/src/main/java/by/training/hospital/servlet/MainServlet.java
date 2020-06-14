package by.training.hospital.servlet;

import by.training.hospital.command.Command;
import by.training.hospital.context.ApplicationContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "mainServlet", urlPatterns = "/", loadOnStartup = 1)
public class MainServlet extends HttpServlet {

    private static final long serialVersionUID = 7271804811048068870L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String commandParameter = req.getParameter("command");
        if (commandParameter == null || commandParameter.length() == 0) {
            req.getRequestDispatcher("/jsp/index.jsp").forward(req, resp);
        } else {
            Command command = ApplicationContext.getInstance().getCommand(commandParameter.toUpperCase());
            command.execute(req, resp);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        doGet(req, resp);

    }
}
