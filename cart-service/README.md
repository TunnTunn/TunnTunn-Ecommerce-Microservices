# Cart Service - Ghi chú cải thiện

Service này chịu trách nhiệm quản lý giỏ hàng của người dùng, được lưu trữ trong Redis để tăng tốc độ truy xuất.

## Vấn đề: Dữ liệu không nhất quán (Stale Data)

Trong `CartServiceImpl.java`, khi một sản phẩm được thêm vào giỏ hàng, các thông tin như `productName` và `price` được sao chép từ `product-service` và lưu trực tiếp vào Redis.

```java
// Dữ liệu sao chép có thể trở nên lỗi thời
CartItem newItem = CartItem.builder()
        .productName(product.getName())
        .price(BigDecimal.valueOf(product.getPrice()))
        //...
        .build();
```

-   **Rủi ro:** Nếu thông tin sản phẩm (ví dụ: giá, tên) được cập nhật trong `product-service`, giỏ hàng của người dùng sẽ vẫn hiển thị thông tin cũ. Điều này gây ra trải nghiệm người dùng không tốt và có thể dẫn đến sai sót khi thanh toán (ví dụ: khách hàng thấy giá cũ nhưng khi thanh toán lại là giá mới).

### **Giải pháp đề xuất: Chỉ lưu dữ liệu cốt lõi**

1.  **Tối giản dữ liệu trong Redis:** Giỏ hàng trong Redis chỉ nên chứa những thông tin mà `cart-service` chịu trách nhiệm quản lý và không thay đổi thường xuyên, bao gồm: `userId`, `productId`, và `quantity`.

2.  **Truy xuất dữ liệu "nóng" khi cần:**
    -   Khi người dùng yêu cầu xem giỏ hàng, `cart-service` sẽ lấy danh sách `productId` và `quantity` từ Redis.
    -   Sau đó, nó sẽ thực hiện một cuộc gọi API (dạng batch) đến `product-service` để lấy thông tin chi tiết mới nhất (`name`, `price`, `image`...) của các sản phẩm đó.
    -   Cuối cùng, nó sẽ tổng hợp dữ liệu từ hai nguồn (Redis và `product-service`) để trả về response hoàn chỉnh cho client.

-   **Đánh đổi (Trade-off):** Cách tiếp cận này có thể làm tăng một chút độ trễ khi xem giỏ hàng do phải gọi thêm API, nhưng nó **đảm bảo dữ liệu hiển thị cho người dùng luôn chính xác và cập nhật**, đây là yếu tố quan trọng hơn trong một hệ thống e-commerce.
