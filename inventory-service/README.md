# Inventory Service - Ghi chú cải thiện

Service này chịu trách nhiệm quản lý tồn kho và xử lý các event liên quan. Cơ chế xử lý event hiện tại cần được củng cố để đảm bảo hệ thống hoạt động ổn định và nhất quán.

## Vấn đề: Xử lý Event chưa đủ mạnh mẽ

Trong `OrderEventListener.java`, phương thức `handleOrderCreatedEvent` có các điểm yếu sau:

1.  **Thiếu Giao dịch bù trừ (Compensating Transaction):** Khi kiểm tra thấy số lượng tồn kho không đủ (`newQuantity < 0`), service chỉ ghi log và `return`. Điều này khiến hệ thống rơi vào trạng thái không nhất quán: đơn hàng đã được tạo ở `order-service` nhưng kho lại không được trừ.
2.  **Thiếu tính Idempotent:** Nếu RabbitMQ vì một lý do nào đó (lỗi mạng, consumer restart) gửi lại cùng một message `OrderCreatedEvent`, service sẽ xử lý lại message đó và trừ kho hai lần cho cùng một đơn hàng.
3.  **Xử lý lỗi quá chung chung:** Việc `catch (Exception e)` có thể che giấu các lỗi tạm thời (ví dụ: `transient database connection error`) và khiến message bị "mất" thay vì được xử lý lại.

### **Giải pháp đề xuất: Nâng cấp Event Consumer**

1.  **Hoàn thiện Saga - Giao dịch bù trừ:**

    -   Khi không đủ hàng, thay vì chỉ ghi log, `inventory-service` phải publish một event ngược lại (ví dụ: `InventoryUpdateFailedEvent`) chứa `orderId` và lý do.
    -   `order-service` sẽ lắng nghe event này để hủy đơn hàng tương ứng, đảm bảo dữ liệu toàn hệ thống nhất quán.

2.  **Đảm bảo Idempotency:**

    -   Trước khi xử lý một message, cần kiểm tra xem message ID (hoặc `orderId`) đã được xử lý trước đó hay chưa.
    -   Có thể sử dụng một bảng trong DB (ví dụ: `processed_messages`) hoặc một key trong Redis để lưu lại ID của các message đã xử lý thành công. Nếu ID đã tồn tại, bỏ qua message.

3.  **Cấu hình Retry và Dead-Letter Queue (DLQ):**
    -   Cấu hình RabbitMQ để tự động thử lại (retry) các message bị lỗi xử lý (do các lỗi có thể khắc phục được như DB tạm thời mất kết nối).
    -   Sau một vài lần retry không thành công, message nên được tự động chuyển vào một hàng đợi khác (DLQ). Điều này giúp tránh làm tắc nghẽn hàng đợi chính và cho phép lập trình viên phân tích, xử lý các lỗi nghiêm trọng một cách thủ công.
