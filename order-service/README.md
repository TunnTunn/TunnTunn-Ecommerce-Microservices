# Order Service - Ghi chú cải thiện

Service này chịu trách nhiệm xử lý logic đặt hàng. Tuy nhiên, luồng xử lý hiện tại có một số vấn đề nghiêm trọng về tính nhất quán dữ liệu trong môi trường microservices.

## Vấn đề 1: Race Condition và Rủi ro Overselling

Trong `OrderServiceImpl.java`, logic kiểm tra số lượng tồn kho (`stock`) được thực hiện trước khi tạo đơn hàng.

```java
// Vấn đề nằm ở đây
if (product.getStock() < item.getQuantity())
    throw new IllegalArgumentException("Not enough stock for: " + product.getName());
```

-   **Rủi ro:** Luồng xử lý này không an toàn trong môi trường có nhiều giao dịch đồng thời (high concurrency). Nhiều request có thể cùng lúc đọc số lượng tồn kho cũ và cùng lúc vượt qua bước kiểm tra này, dẫn đến tình trạng bán lố số lượng sản phẩm thực có (overselling).
-   **Nguồn dữ liệu sai:** `order-service` đang dựa vào thông tin `stock` từ `product-service`, trong khi `inventory-service` mới là nguồn dữ liệu chính xác (source of truth) cho việc quản lý kho.

### **Giải pháp đề xuất: Hoàn thiện Saga Pattern**

Cần tái cấu trúc lại luồng tạo đơn hàng để đảm bảo tính nhất quán.

1.  **Thay đổi trạng thái đơn hàng:** Khi `order-service` nhận yêu cầu, nó chỉ nên tạo một đơn hàng với trạng thái ban đầu là `PENDING`.
2.  **Giao tiếp qua Event/Command:** Thay vì kiểm tra kho trực tiếp, `order-service` sẽ publish một command/event (ví dụ: `ReserveInventoryCommand`) đến RabbitMQ.
3.  **Chờ phản hồi:** `order-service` sẽ lắng nghe các event phản hồi từ `inventory-service` (ví dụ: `InventoryReserved` hoặc `InventoryReservationFailed`) để cập nhật trạng thái cuối cùng cho đơn hàng (`CONFIRMED` hoặc `CANCELLED`).

Luồng này đảm bảo rằng nghiệp vụ tồn kho được xử lý một cách an toàn và tập trung tại `inventory-service`.

## Vấn đề 2: Trùng lặp DTO

Các class DTO (`OrderResponse`, `OrderItemResponse`, `OrderStatus`) đang bị định nghĩa trùng lặp ở cả hai module `order-client` và `order-events`.

-   **Rủi ro:** Vi phạm nguyên tắc DRY (Don't Repeat Yourself). Khi cần thay đổi cấu trúc DTO, lập trình viên phải sửa ở nhiều nơi, dễ gây ra lỗi và thiếu nhất quán.

### **Giải pháp đề xuất: Tái cấu trúc Dependency**

1.  Xóa các DTO bị trùng lặp trong module `order-events`.
2.  Thêm dependency của `order-client` vào `pom.xml` của `order-events` để tái sử dụng lại các DTO đã có.

```xml
<dependency>
    <groupId>com.example</groupId>
    <artifactId>order-client</artifactId>
    <version>${project.version}</version>
</dependency>
```
