package by.training.hospital.filter;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;


public class LanguageFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        if (req instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) req;
            Cookie[] cookies = httpRequest.getCookies();

            if (cookies != null) {
                Optional<Cookie> langCookie = Arrays.stream(cookies)
                        .filter(c -> c.getName().equalsIgnoreCase("lang")
                                && (c.getValue().equalsIgnoreCase("en")
                                || c.getValue().equalsIgnoreCase("ru")))
                        .findFirst();

                if (langCookie != null && langCookie.isPresent()) {
                    httpRequest.setAttribute("lang", langCookie.get().getValue());

                }
            } else {
                ((HttpServletResponse) resp).addCookie(new Cookie("lang", "en"));
                httpRequest.setAttribute("lang", "en");
            }

            chain.doFilter(req, resp);
        }
    }

    @Override
    public void destroy() {

    }
}
