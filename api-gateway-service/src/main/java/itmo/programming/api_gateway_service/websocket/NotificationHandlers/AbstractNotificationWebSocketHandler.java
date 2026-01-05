package itmo.programming.api_gateway_service.websocket.NotificationHandlers;

import io.jsonwebtoken.JwtException;
import itmo.programming.api_gateway_service.security.JwtUtils;
import itmo.programming.api_gateway_service.websocket.WebSocketSessionRegistry;
import org.springframework.http.HttpCookie;
import org.springframework.web.reactive.socket.CloseStatus;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

public abstract class AbstractNotificationWebSocketHandler implements WebSocketHandler {

    protected final JwtUtils jwtUtils;
    protected final WebSocketSessionRegistry webSocketSessionRegistry;

    protected AbstractNotificationWebSocketHandler(
            JwtUtils jwtUtils,
            WebSocketSessionRegistry webSocketSessionRegistry
    ) {
        this.jwtUtils = jwtUtils;
        this.webSocketSessionRegistry = webSocketSessionRegistry;
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        HttpCookie cookie = session.getHandshakeInfo()
                .getCookies()
                .getFirst("accessToken");

        if (cookie == null || cookie.getValue().isBlank()) {
            return session.close(CloseStatus.NOT_ACCEPTABLE);
        }

        try {
            Long userId = Long.valueOf(jwtUtils.parseToken(cookie.getValue()).getSubject());

            webSocketSessionRegistry.add(userId, session);

            return session.receive()
                    .doFinally(s -> webSocketSessionRegistry.remove(userId, session))
                    .then();

        } catch (JwtException | IllegalArgumentException e) {
            return session.close(CloseStatus.NOT_ACCEPTABLE);
        }
    }

    public WebSocketSessionRegistry getRegistry() {
        return webSocketSessionRegistry;
    }
}
