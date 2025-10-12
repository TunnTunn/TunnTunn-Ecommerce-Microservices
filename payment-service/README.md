# Payment Service - Ghi chú cải thiện

Service này chịu trách nhiệm xử lý các giao dịch thanh toán. Logic hiện tại chỉ là giả lập và cần được hoàn thiện để phản ánh đúng quy trình trong thực tế.

## Vấn đề 1: Giả lập thanh toán quá đơn giản

Trong `PaymentServiceImpl.java`, phương thức `processPayment` luôn trả về trạng thái `SUCCESS`.

-   **Vấn đề:** Trong thực tế, một giao dịch thanh toán cần thời gian để xử lý qua cổng thanh toán và có thể thất bại. Việc mặc định là `SUCCESS` che giấu đi luồng nghiệp vụ phức tạp này.

-   **Giải pháp đề xuất: Áp dụng luồng thanh toán bất đồng bộ**
    1.  **Thêm trạng thái `PENDING`:** Khi nhận được yêu cầu, `payment-service` nên tạo một giao dịch với trạng thái `PENDING` và lưu vào DB.
    2.  **Xử lý bất đồng bộ:** Service sẽ gọi đến một cổng thanh toán thực sự (hoặc một mock service). Quá trình này nên diễn ra trong một luồng riêng (asynchronously).
    3.  **Cập nhật trạng thái:** Sau khi nhận được kết quả từ cổng thanh toán, service sẽ cập nhật lại trạng thái của giao dịch thành `SUCCESS` hoặc `FAILED`.
    4.  **Thông báo kết quả:** `payment-service` nên publish một event (ví dụ: `PaymentCompletedEvent` hoặc `PaymentFailedEvent`) để các service khác (như `order-service`) có thể cập nhật trạng thái tương ứng.

## Vấn đề 2: Kiểu dữ liệu không nhất quán và không an toàn

-   **Vấn đề 1 (An toàn tính toán):** Trường `amount` trong model `Payment` đang sử dụng kiểu `Double`. Sử dụng `Double` cho các phép tính tài chính rất nguy hiểm vì có thể gây ra lỗi làm tròn số (floating-point precision errors).
-   **Vấn đề 2 (Nhất quán dữ liệu):** `Order.id` là kiểu `Long`, nhưng `Payment.orderId` lại là `String`. `Order.totalAmount` là `BigDecimal`, nhưng `Payment.amount` lại là `Double`. Sự thiếu nhất quán này có thể gây ra lỗi khi ép kiểu và làm giảm tính toàn vẹn của dữ liệu.

-   **Giải pháp đề xuất:**
    1.  **Sử dụng `BigDecimal`:** **Luôn luôn** sử dụng `java.math.BigDecimal` cho tất cả các trường liên quan đến tiền tệ (`amount`, `price`, `total`...) trên toàn bộ hệ thống.
    2.  **Thống nhất kiểu dữ liệu của ID:** Quyết định một kiểu dữ liệu chung cho ID đơn hàng và áp dụng nó trên tất cả các service. Nếu `Order.id` là `Long`, thì `Payment.orderId` cũng nên là `Long`. Nếu sử dụng một mã định danh nghiệp vụ (ví dụ UUID), hãy dùng `String` ở mọi nơi.
