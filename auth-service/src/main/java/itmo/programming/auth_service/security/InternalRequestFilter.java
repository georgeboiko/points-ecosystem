package itmo.programming.auth_service.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.web.filter.OncePerRequestFilter;

public class InternalRequestFilter extends OncePerRequestFilter {

    private static final String PUBLIC_PATH = "/api/v1/auth/internal/token/validate";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestURI().equals(PUBLIC_PATH)) {
            filterChain.doFilter(request, response);
            return;
        }

        String internalHeader = request.getHeader("X-Internal-Request");
        String userIdHeader = request.getHeader("X-User-Id");

        try {
            if (internalHeader == null ||
                !internalHeader.equalsIgnoreCase("true") ||
                Integer.parseInt(userIdHeader) <= 0
            ) {
                abortWithUnauthorized(response);
                return;
            }

            filterChain.doFilter(request, response);
        } catch (NumberFormatException exception) {
            abortWithUnauthorized(response);
        }

    }

    private void abortWithUnauthorized(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("Unauthorized: request must come through API Gateway");
    }
}
