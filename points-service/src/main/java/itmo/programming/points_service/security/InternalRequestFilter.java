package itmo.programming.points_service.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.web.filter.OncePerRequestFilter;

public class InternalRequestFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String internalHeader = request.getHeader("X-Internal-Request");
        String userIdHeader = request.getHeader("X-User-Id");

        try {
            if (!internalHeader.equalsIgnoreCase("true") ||
                Integer.parseInt(userIdHeader) <= 0) {
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
