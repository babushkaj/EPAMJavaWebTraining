package by.training.hospital.filter;

import by.training.hospital.util.RequestUtil;
import by.training.hospital.validator.ValidationUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

public class XSSFilter implements Filter {

    private static final String XSS_REGEX = "(<script>)|(<\\/script>)|(data:)|(about:)|(vbscript:)|(onclick)|(onload)|" +
            "(onunload)|(onabort)|(onerror)|(onblur)|(onchange)|(onfocus)|(onreset)|" +
            "(onsubmit)|(ondblclick)|(onkeydown)|(onkeypress)|(onkeyup)|(onmousedown)|" +
            "(onmouseup)|(onmouseover)|(onmouseout)|(onselect)|(javascript)|(javascript)";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        Enumeration<String> enumeration = req.getParameterNames();
        while (enumeration.hasMoreElements()) {
            String parameterName = enumeration.nextElement();
            String parameter = req.getParameter(parameterName);
            if (ValidationUtil.thisStringMatchesRegex(parameter.toLowerCase(), XSS_REGEX)) {
                HttpServletRequest httpReq = (HttpServletRequest) req;
                HttpServletResponse httpResp = (HttpServletResponse) resp;
                RequestUtil.forwardToJsp(httpReq, httpResp, "error", "error.xss_warning");
                return;
            }
        }
        chain.doFilter(req, resp);
    }

    @Override
    public void destroy() {

    }
}
