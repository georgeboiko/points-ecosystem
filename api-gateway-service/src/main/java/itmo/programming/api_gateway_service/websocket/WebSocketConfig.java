package itmo.programming.api_gateway_service.websocket;

import itmo.programming.api_gateway_service.websocket.NotificationHandlers.AbstractNotificationWebSocketHandler;
import itmo.programming.api_gateway_service.websocket.NotificationHandlers.PointsAddedWebSocketHandler;
import itmo.programming.api_gateway_service.websocket.NotificationHandlers.PointsDeletedWebSocketHandler;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

@Configuration
@EnableWebFlux
public class WebSocketConfig {

    @Bean
    public HandlerMapping handlerMapping(PointsAddedWebSocketHandler addHandler,
                                         PointsDeletedWebSocketHandler deleteHandler) {
        return new SimpleUrlHandlerMapping(
                Map.of("/ws/points-added", addHandler,
                        "/ws/points-deleted", deleteHandler),
                 1
        );
    }

    @Bean
    public WebSocketHandlerAdapter handlerAdapter() {
        return new WebSocketHandlerAdapter();
    }

}
