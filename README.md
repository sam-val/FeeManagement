# FeeManagement
aptech school

Phần mềm quản lí tasks và fee:

- Tổng quan: Admin (công ty) làm cầu mối giữa nhân viên và clients. Quản lý những tasks của clients. Chọn nhân viên nào
có lương đề nghị phù hợp cho từng task rồi chia công việc.

-Database: sữ dụng sqlite3. Driver và data chưá trong thư mục DatabaseResource.

- Sữ dụng library JavaFX (để chạy programme add các file ".jar" trong "DatabaseResource/javafx-sdk/lib" vào project cũa bạn)

-đăng nhập bằng admin (username:admin ; password: 123) HOẶC tự tạo tài khoản HOẶC xài tài khoản nhân viên mẫu
(username: yaiba ; password: 123).

-Admin có thể approve tài khoản mới tạo của các nhân viên để họ có thể đăng nhập, có thể thêm client và nhân viên, có thể
search, thêm hoặc xóa các loại task. Có thẻ chọn một fee proposal thích hợp của nhân viên nhật định, đồng ý cho làm. Rồi
set task đó "hoàn thành" khi admin xác nhận "OK" với client.

-Nhân viên cò thể update thông tin của mình, search tasks, đề nghị lương cho từng task. Tài khoản của họ phải được approved
bởi admin trước tiên để đăng nhập.

-Giao diện window của nhân viên được lấy và chỉnh sửa từ: https://github.com/k33ptoo/RestaurantMgtSampleUI 


