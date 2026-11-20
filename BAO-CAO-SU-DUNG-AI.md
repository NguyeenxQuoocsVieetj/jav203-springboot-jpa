# Báo cáo sử dụng AI

**Môn:** Phát triển web nâng cao cùng AI (Java)
**Đề tài:** CareerPath Booking Portal, Giai đoạn 1 (Website MVC và giao diện người dùng)
**Sinh viên:** Nguyễn Quốc Viêệt **Mã sinh viên:** PS48341 **Lớp:** SD21301

---

## 1. AI tools đã sử dụng

| Công cụ | Mục đích sử dụng | Ghi chú |
|---|---|---|
| Claude Code (trợ lý lập trình Claude của Anthropic) | Phân tích đề, đề xuất kiến trúc, viết code, viết test, gỡ lỗi, viết tài liệu | Mô hình Claude Opus 5.5 chính; chuyển sang Claude Haiku 5.5 ở giữa quá trình |
| Google Ảnh (tìm kiếm thủ công qua trình duyệt) | Tìm ảnh minh họa cho 6 gói tư vấn | Không phải AI; kết quả được chọn bằng tay |

Không dùng ChatGPT, Gemini, Copilot hay Cursor trong quá trình này.

---

## 2. Prompt quan trọng

Các prompt có ảnh hưởng lớn đến kết quả. Ảnh chụp màn hình prompt cần đính kèm riêng.

| # | Nội dung prompt (tóm tắt) | Kết quả chính |
|---|---|---|
| 1 | Đưa file đề PDF, yêu cầu phân tích yêu cầu Giai đoạn 1 với vai trò kỹ sư backend | Danh sách mục tiêu MVC-01 đến MVC-07, các điểm đề bài chưa khớp giữa bảng dữ liệu và yêu cầu chức năng |
| 2 | Yêu cầu tổng hợp toàn bộ chức năng của đề | Bảng chức năng theo giai đoạn, ma trận phân quyền |
| 3 | Đọc các slide Spring Boot MVC, Controller, Thymeleaf 1, Thymeleaf 2 và đề xuất phương hướng giải | Map từng slide với từng mục tiêu, thứ tự làm |
| 4 | "Làm MVC-01 ngay" | Đổi package về `poly.edu`, thêm `spring-boot-starter-validation`, tạo cấu trúc thư mục |
| 5 | Yêu cầu thực hiện MVC-02 | Trang chủ, giới thiệu, trợ giúp, layout, service dữ liệu mẫu |
| 6 | Yêu cầu thực hiện MVC-03 | Các endpoint danh sách, chi tiết, tìm kiếm, lọc, đặt lịch, `/system/status` |
| 7 | Yêu cầu hoàn thiện MVC-04 bằng `th:switch` | Enum trạng thái lịch đặt, cột trạng thái, badge CSS |
| 8 | Yêu cầu phân tích và code MVC-05, MVC-06 từng phần để duyệt trước | Upload file, `FileStorageService`, validation, `BookingExceptionHandler`, test lỗi |
| 9 | Yêu cầu phân tích MVC-07 và code từng phần | Messages vi/en, `WebConfig`, `LanguageAdvice`, chuyển toàn bộ template sang `#{...}` |
| 10 | Yêu cầu bỏ comment trong code, chỉ giao tiếp qua cuộc trò chuyện | Code không có comment; mọi giải thích đưa vào phần trao đổi |
| 11 | Yêu cầu sửa UI cho nội dung trải rộng theo màn hình | Bỏ `max-width` cố định trong CSS |
| 12 | Yêu cầu viết báo cáo AI (tài liệu này) | Tổng hợp từ toàn bộ cuộc trò chuyện |

---

## 3. Phần code có AI hỗ trợ

| Module / file | Mức độ AI hỗ trợ | Sinh viên đã kiểm tra |
|---|---|---|
| `PS48341_ASM1Application` (đổi package) | Đề xuất chuyển package | Có |
| `pom.xml` (thêm validation) | Đề xuất dependency | Có |
| `entity/ConsultationPackage`, `Consultant`, `ConsultationCategory`, `Booking`, `Enum/BookingStatus` | AI viết phần lớn; sinh viên đã sửa vị trí enum | Có |
| `dto/BookingForm` | AI viết; bổ sung annotation validation và `MultipartFile` | Có |
| `service/ConsultationService` | AI viết dữ liệu mẫu và logic lọc, sắp xếp, thống kê; sinh viên đã gỡ comment | Có |
| `service/FileStorageService` | AI viết; sinh viên xác nhận quy tắc đổi tên UUID và kiểm tra đuôi file | Có |
| `controller/HomeController` | AI viết; sinh viên đã sửa mapping cấp class | Có |
| `controller/ConsultationController` | AI viết | Có |
| `controller/BookingController` | AI viết; có sửa qua các giai đoạn | Có |
| `controller/SystemController` | AI viết | Có |
| `controller/BookingExceptionHandler` | AI viết (`@ControllerAdvice`) | Có |
| `controller/LanguageAdvice` | AI viết | Có |
| `config/WebConfig` | AI viết | Có |
| Template `fragments/layout.html`, `fragments/menu.html` | AI viết; sinh viên xác nhận layout dùng chung | Có |
| Template `home/*`, `consultation/*`, `booking/*` | AI viết, sau đó chuyển sang `#{...}` | Có |
| `static/css/style.css` | AI viết; sinh viên yêu cầu sửa bố cục toàn màn hình | Có |
| `messages.properties`, `messages_vi.properties`, `messages_en.properties` | AI viết cả bản dịch tiếng Anh | Cần đối chiếu lại bản dịch |
| Test `BookingControllerTest`, `ConsultationControllerTest`, `HomeControllerTest`, `SystemControllerTest`, `LocaleTest`, `ConsultationServiceTest`, `MessagesTest` | AI viết | Có, đã chạy và pass |
| `THONG-BAO-GIAI-DOAN-2.md` | AI viết theo yêu cầu của sinh viên | Có |

---

## 4. Các lỗi phát hiện được từ AI-generated code

| # | Lỗi | Phát hiện bằng cách nào | Cách khắc phục |
|---|---|---|---|
| 1 | Class chạy app nằm ở `poly.edu.asm1`, không khớp package `poly.edu` của đề | Đối chiếu đề bài | Chuyển class chính và class test về `poly.edu` |
| 2 | Thiếu `spring-boot-starter-validation`, nếu thiếu thì `@Valid` không chạy | Phân tích trước khi code | Thêm dependency |
| 3 | `#httpServletRequest` không hoạt động trong SpEL, làm menu lỗi khi render | Test `HomeControllerTest` báo lỗi | Đổi sang truyền `activeMenu` từ controller |
| 4 | Cấu hình `.gitignore` và file `application.properties` chưa được Git theo dõi, có chứa mật khẩu | Sinh viên kiểm tra `git status` | Đưa mật khẩu sang biến môi trường; thêm file vào `.gitignore` |
| 5 | Test multipart: builder của multipart không cùng kiểu với builder thường | Lỗi biên dịch khi chạy Maven | Tách thành hai overload của helper |
| 6 | Cờ `uploadTooLarge` chưa có giá trị mặc định, biểu thức `or` với null lỗi trong Thymeleaf | Test báo lỗi | Thêm giá trị mặc định `false` trong `addCommonAttributes` |
| 7 | Test kiểm tra flash attribute cũ `message` sau khi đổi sang `messageKey` | Test báo lỗi | Cập nhật test |
| 8 | `CookieLocaleResolver` không có `setCookieName`, gây lỗi biên dịch | Lỗi biên dịch khi chạy Maven | Dùng constructor nhận tên cookie |
| 9 | Test lịch đặt truyền thêm `consultantId` trùng với tham số đã có, làm giá trị sai | Đọc lại test | Tách thành request riêng |
| 10 | AI ban đầu đưa comment vào code, trái yêu cầu của sinh viên | Sinh viên yêu cầu | Gỡ comment, giải thích bằng lời trong trao đổi |
| 11 | Ngày cố định `2026-11-25` trong test sẽ thành quá khứ | Đọc lại test | Dùng ngày động `LocalDate.now().plusDays(7)` |
| 12 | Khi đề xuất `@ExceptionHandler` trong controller để bắt lỗi file quá dung lượng, AI đã đổi sang `@ControllerAdvice` vì lỗi multipart xảy ra trước khi vào controller | Phân tích lại cơ chế Spring | Dùng `@ControllerAdvice(assignableTypes = ...)` |
| 13 | Đề xuất ban đầu dùng `th:field` cho ô input file, không hợp lệ với trình duyệt | Phân tích lại | Dùng `name="attachment"` trực tiếp |
| 14 | Trong quá trình trả lời, AI có ghi nhận sai số lượng test (34 test thực tế, không phải 36 hoặc 33 như đã báo) | Đếm lại kết quả surefire | Ghi lại số đúng trong báo cáo này |

---

## 5. Các phần đã refactor thủ công

Các thay đổi sau do sinh viên quyết định hoặc chỉnh sửa lại sau khi AI viết:

1. Gỡ toàn bộ comment khỏi code (yêu cầu của sinh viên).
2. Sửa mapping cấp class của `HomeController` thành `@RequestMapping({"/home", "/"})` và dùng `@RequiredArgsConstructor`.
3. Chuyển enum `BookingStatus` vào package `entity.Enum`.
4. Thêm `application.properties` vào `.gitignore` để tránh đưa mật khẩu lên GitHub.
5. Chọn ảnh minh họa cho từng gói tư vấn từ kết quả Google Ảnh (AI đưa ra trang xem trước, sinh viên chọn).
6. Quyết định giữ định dạng VNĐ và `dd/MM/yyyy` cho cả hai ngôn ngữ, chỉ dịch nhãn.
7. Quyết định phân loại các mục ngoài phạm vi: mục thuộc Giai đoạn 1 để trống, mục phụ thuộc Giai đoạn 2 có thông báo.
8. Quyết định dữ liệu Giai đoạn 1 lưu trong bộ nhớ, sẽ thay bằng database ở Giai đoạn 2.

---

## 6. Đánh giá hạn chế của AI trong project

1. **Không tự chạy được trình duyệt.** Giao diện được kiểm tra qua test MockMvc, chưa xem trực tiếp trên trình duyệt. Một số chi tiết hiển thị có thể cần chỉnh sau khi xem thật.
2. **Không có SQL Server trong môi trường AI.** Test `contextLoads` cần database đang chạy, nên không được kiểm tra trong quá trình làm việc.
3. **Có lúc đưa ra thông tin chưa kiểm chứng.** Ví dụ: AI từng khẳng định một số phương thức tồn tại mà thực tế không có (`setCookieName`), và phải sửa sau khi biên dịch lỗi.
4. **Phụ thuộc vào phiên bản thư viện.** Project dùng Spring Boot 4.1.2, một số API và package khác với tài liệu thường gặp (ví dụ `org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest`). AI cần được kiểm tra bằng biên dịch thực tế.
5. **Thiết kế dữ liệu chưa tối ưu cho database.** Dữ liệu mẫu nằm trong bộ nhớ, đếm lượt đặt bằng vòng lặp. Khi chuyển sang Giai đoạn 2, cần viết lại bằng Repository và `GROUP BY`.
6. **Ảnh minh họa lấy từ đường dẫn thumbnail của Google.** Đường dẫn này không đảm bảo ổn định lâu dài, có thể bị gỡ hoặc đổi. Cách an toàn hơn là tải ảnh về `static/images/packages/`, nhưng chưa thực hiện.
7. **Bản dịch tiếng Anh do AI viết** cần sinh viên đối chiếu lại để đảm bảo đúng nghĩa và đúng thuật ngữ.
8. **AI không thay thế được việc hiểu code.** Đề yêu cầu sinh viên phải giải thích được luồng xử lý chính, phải tự chạy được project, và phải review output của AI. Những việc này cần sinh viên tự thực hiện trước khi bảo vệ.
9. **Các quyết định kiến trúc do AI đề xuất** (cách tổ chức package, dùng DTO thay vì bind thẳng Entity, dùng `@ControllerAdvice`) cần được sinh viên hiểu và có thể giải thích lại.

---

## 7. Checklist kiểm thử (tự động)

| Nhóm | Test | Số lượng | Kết quả |
|---|---|---|---|
| Trang chủ | `HomeControllerTest` | 5 | Pass |
| Gói tư vấn | `ConsultationControllerTest` | 9 | Pass |
| Lịch đặt | `BookingControllerTest` | 9 | Pass |
| Trạng thái hệ thống | `SystemControllerTest` | 1 | Pass |
| Nghiệp vụ | `ConsultationServiceTest` | 7 | Pass |
| Đa ngôn ngữ | `LocaleTest` | 2 | Pass |
| Messages | `MessagesTest` | 1 | Pass |
| **Tổng** | | **34** | **Pass** |

Test `contextLoads` (có sẵn từ đầu) cần SQL Server đang chạy, không nằm trong danh sách trên.

## 8. Checklist kiểm thử thủ công (cần làm trên trình duyệt)

- [ ] Trang chủ hiển thị gói nổi bật, chuyên viên tiêu biểu, tổng số gói và lượt đặt
- [ ] Menu đánh dấu đúng trang đang mở
- [ ] Danh sách gói: tìm theo tên, lọc theo loại, lọc theo khoảng giá
- [ ] Chi tiết gói và các gói cùng loại
- [ ] Đặt lịch: submit rỗng, nhập sai email và số điện thoại, ngày quá khứ
- [ ] Đặt lịch với file đính kèm đúng định dạng và file sai định dạng
- [ ] File quá 5 MB hiển thị thông báo
- [ ] Refresh sau khi đặt lịch thành công không gửi lại form
- [ ] Danh sách lịch đặt hiển thị đúng trạng thái và cột tài liệu
- [ ] Đổi ngôn ngữ Việt / Anh trên trang tìm kiếm có giữ bộ lọc
- [ ] `/system/status` trả về JSON

## 9. Tài liệu đính kèm nên có

- Ảnh chụp màn hình các prompt quan trọng (mục 2)
- Ghi chú review AI cho phần giao diện và validation (mục 4)
- So sánh trước và sau khi tối ưu (ví dụ: `countBookingsOfPackage` quét toàn bộ danh sách, và phiên bản dùng `GROUP BY` ở Giai đoạn 2)
- Ảnh chụp kết quả checklist thủ công (mục 8)

---

**Cam kết:** Sinh viên đã đọc, kiểm tra và hiểu toàn bộ source code được AI hỗ trợ. Mọi phần code đã được chạy thử bằng test tự động. Sinh viên chịu trách nhiệm về nội dung nộp.

**Chữ ký sinh viên:** …………………………………… **Ngày:** ……………………
