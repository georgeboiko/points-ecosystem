package itmo.programming.api_gateway_service.websocket.NotificationHandlers;

import itmo.programming.api_gateway_service.security.JwtUtils;
import itmo.programming.api_gateway_service.websocket.WebSocketSessionRegistry;
import org.springframework.stereotype.Component;

@Component
public class PointsDeletedWebSocketHandler extends AbstractNotificationWebSocketHandler {

    public PointsDeletedWebSocketHandler(JwtUtils jwtUtils) {
        super(jwtUtils, new WebSocketSessionRegistry());
    }
}
