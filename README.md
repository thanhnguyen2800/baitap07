# UploadImages - Ứng dụng Android

Đây là một dự án ứng dụng Android đơn giản, cho phép người dùng đăng ký, đăng nhập, xem thông tin cá nhân và tải lên ảnh đại diện.

## Tính năng chính

- **Đăng ký tài khoản:** Người dùng có thể tạo tài khoản mới với các thông tin:
  - Tên đăng nhập
  - Họ và tên
  - Email
  - Mật khẩu
  - Giới tính
- **Đăng nhập:** Xác thực người dùng bằng email và mật khẩu. Phiên đăng nhập được lưu lại bằng `SharedPreferences`.
- **Trang thông tin cá nhân (Profile):**
  - Hiển thị thông tin của người dùng đang đăng nhập.
  - Hiển thị ảnh đại diện (avatar).
  - Cho phép người dùng đăng xuất.
- **Tải lên ảnh đại diện:**
  - Người dùng có thể chọn một ảnh từ thư viện trên thiết bị.
  - Ứng dụng sẽ tải ảnh đã chọn lên máy chủ (server).
  - Ảnh đại diện của người dùng sẽ được cập nhật và hiển thị trên trang cá nhân.

## Màn hình chính

1.  **RegisterActivity:** Màn hình đăng ký tài khoản.
2.  **LoginActivity:** Màn hình đăng nhập.
3.  **ProfileActivity:** Màn hình hiển thị thông tin cá nhân sau khi đăng nhập.
4.  **MainActivity:** Màn hình cho phép người dùng chọn và tải ảnh lên.

## Công nghệ và thư viện sử dụng

- **Ngôn ngữ:** Java
- **Networking:**
  - **Retrofit:** Thư viện mạnh mẽ để thực hiện các yêu cầu mạng (API call) đến server.
  - **Gson:** Dùng để chuyển đổi giữa đối tượng Java và định dạng JSON.
- **Tải và hiển thị ảnh:**
  - **Glide:** Một thư viện phổ biến và hiệu quả để tải, cache và hiển thị ảnh từ URL.
- **Lưu trữ cục bộ:**
  - **SharedPreferences:** Dùng để lưu trữ dữ liệu đơn giản như thông tin đăng nhập của người dùng trên thiết bị.

## Cấu hình API

- **Base URL:** `http://app.iotstar.vn:8081/appfoods/`
- **Các endpoint chính:**
  - `register.php`: Xử lý việc đăng ký tài khoản mới.
  - `updateimages.php`: Xử lý việc tải lên và cập nhật ảnh đại diện.
  - 
## Hướng dẫn cài đặt và chạy dự án

1.  Sao chép (clone) repository này về máy tính của bạn.
2.  Mở dự án bằng Android Studio.
3.  Đợi cho Gradle đồng bộ hóa các thư viện cần thiết.
4.  Build và chạy ứng dụng trên máy ảo hoặc thiết bị Android thật.
