package by.training.hospital.filter;

import by.training.hospital.command.ApplicationCommandConstants;
import by.training.hospital.context.SecurityContext;
import by.training.hospital.entity.Role;
import by.training.hospital.util.RequestUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

public class CommandSecurityFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {

        String command = req.getParameter("command");
        HttpServletRequest httpReq = (HttpServletRequest) req;
        Role currentUserRole = (Role) httpReq.getSession().getAttribute("currentUserRole");

        if (command == null || command.isEmpty() || command.equalsIgnoreCase(ApplicationCommandConstants.ERR_CMD)) {
            chain.doFilter(req, resp);
        } else {
            Set<Role> rolesForCommand = SecurityContext.getInstance().getRolesForCommand(command.toUpperCase());

            if (rolesForCommand != null && rolesForCommand.contains(currentUserRole)) {
                chain.doFilter(req, resp);
            } else {
                RequestUtil.forwardToJsp(httpReq, (HttpServletResponse) resp, "error", "error.no_permission");
            }
        }
    }

    @Override
    public void destroy() {

    }
}
