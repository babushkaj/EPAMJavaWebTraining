package by.training.hospital.util;

import by.training.hospital.validator.ValidationResult;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class RequestUtil {
    public static void forwardToJsp(HttpServletRequest req, HttpServletResponse resp, String includedPage, String errorMessage) throws ServletException, IOException {
        req.setAttribute("message", errorMessage);
        req.setAttribute("includedPage", includedPage);
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("jsp/index.jsp");
        requestDispatcher.forward(req, resp);
    }

    public static void forwardToJsp(HttpServletRequest req, HttpServletResponse resp, String includedPage) throws ServletException, IOException {
        req.setAttribute("includedPage", includedPage);
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("jsp/index.jsp");
        requestDispatcher.forward(req, resp);
    }

    public static void forwardToCommand(HttpServletRequest req, HttpServletResponse resp, String command) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/?command=" + command);
        requestDispatcher.forward(req, resp);
    }

    public static void setRequestParametersFromValidationResult(HttpServletRequest req, ValidationResult validationResult) {
        for (Map.Entry<String, String> result : validationResult.getResult().entrySet()) {
            req.setAttribute(result.getKey(), result.getValue());
        }
    }
}
