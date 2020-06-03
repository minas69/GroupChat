package group.chat.server.filters;

import group.chat.server.models.User;
import group.chat.server.service.JWTService;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Фильтр для аутентификации пользователя по токену.
 */
@WebFilter("/api/*")
public class Auth implements Filter {

    private static final String BEARER = "Bearer ";

    private JWTService jwtService = new JWTService();

    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws ServletException, IOException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        final String authHeader = request.getHeader("AUTHORIZATION");
        if (authHeader == null || !authHeader.startsWith(BEARER)) {
            response.sendError(403, "No token provided");
            return;
        }
        final String token = authHeader.substring(BEARER.length());

        User user = jwtService.getCredentials(token);
        if (user != null) {
            request.setAttribute("user", user);
            chain.doFilter(req, res);
        } else {
            response.sendError(403, "Forbidden");
        }

    }

    public void init(FilterConfig config) throws ServletException {

    }

}
