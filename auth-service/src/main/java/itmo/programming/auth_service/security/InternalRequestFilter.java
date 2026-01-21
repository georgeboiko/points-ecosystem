package itmo.programming.auth_service.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.filter.OncePerRequestFilter;

public class InternalRequestFilter extends OncePerRequestFilter {

    @Value("${auth-service.check-endpoint}")
    private String EXTERNAL_PATH;

    @Value("${auth-service.public-paths}")
    private List<String> PUBLIC_PATHS;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (EXTERNAL_PATH.equalsIgnoreCase(request.getRequestURI())) {
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
