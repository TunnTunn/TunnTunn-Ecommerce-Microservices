package com.example.order_events;

/**
 * Constants for Order Service Events
 * Defines Exchange, Queue and Routing Key names for event-driven communication
 */
public final class OrderEventConstants {

    // Exchange names
    public static final String ORDER_EXCHANGE = "order.exchange";

    // Queue names
    public static final String ORDER_CREATED_QUEUE = "order.created.queue";
    public static final String ORDER_UPDATED_QUEUE = "order.updated.queue";
    public static final String ORDER_CANCELLED_QUEUE = "order.cancelled.queue";

    // Routing keys
    public static final String ORDER_CREATED_ROUTING_KEY = "order.created";
    public static final String ORDER_UPDATED_ROUTING_KEY = "order.updated";
    public static final String ORDER_CANCELLED_ROUTING_KEY = "order.cancelled";

    private OrderEventConstants() {
        // Utility class - prevent instantiation
    }
}
