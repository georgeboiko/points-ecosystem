package itmo.programming.auth_service.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.springframework.web.filter.OncePerRequestFilter;

public class InternalRequestFilter extends OncePerRequestFilter {

    private static final List<String> EXTERNAL_PATHS = List.of("/api/v1/auth/internal/token/validate");
    private static final List<String> PUBLIC_PATHS = List.of("/api/v1/auth/login", "/api/v1/auth/register", "/api/v1/auth/refresh");

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (EXTERNAL_PATHS.contains(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        String internalHeader = request.getHeader("X-Internal-Request");
        String userIdHeader = request.getHeader("X-User-Id");

        try {
            if (internalHeader == null ||
                !internalHeader.equalsIgnoreCase("true") ||
                (userIdHeader == null && !PUBLIC_PATHS.contains(request.getRequestURI()))
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
