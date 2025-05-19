Tôi là một Kiến trúc sư Phần mềm Cấp cao (Senior Software Architect) với nhiều năm kinh nghiệm và kỹ năng viết tài liệu kỹ thuật chuyên sâu về các hệ thống phân tán và truyền phát đa phương tiện.  
**TÀI LIỆU ĐẶC TẢ YÊU CẦU PHẦN MỀM (SRS)** **HỆ THỐNG UPLOAD VÀ LIVESTREAM VIDEO**  
**Mục lục:**

1. Giới thiệu  
   * 1.1. Mục đích tài liệu  
   * 1.2. Tổng quan hệ thống  
   * 1.3. Định nghĩa, Từ viết tắt và Thuật ngữ  
2. Mô tả Tổng quan  
   * 2.1. Bối cảnh Sản phẩm  
   * 2.2. Tóm tắt Chức năng Sản phẩm  
   * 2.3. Đặc điểm Người dùng  
   * 2.4. Các Ràng buộc Chung  
   * 2.5. Giả định và Phụ thuộc  
3. Tính năng Hệ thống và Yêu cầu Chức năng Chi tiết  
   * 3.1. Chức năng Upload Video (File MP4)  
     * 3.1.1. UC1: Người dùng tải lên file MP4  
     * 3.1.2. UC2: Hệ thống xử lý và chuyển đổi video sang HLS ABR  
     * 3.1.3. UC3: Người dùng xem lại video đã upload  
   * 3.2. Chức năng Livestream Trực tiếp  
     * 3.2.1. UC4: Người dùng (Streamer) khởi tạo và gửi luồng video (RTMP/WebRTC)  
     * 3.2.2. UC5: Hệ thống nhận và chuyển đổi luồng livestream sang HLS/DASH ABR  
     * 3.2.3. UC6: Người xem truy cập và xem livestream  
     * 3.2.4. UC7: (Tùy chọn) Hệ thống lưu trữ lại luồng livestream thành file VOD  
   * 3.3. Quản lý Video và Livestream (Chung cho cả hai luồng)  
     * 3.3.1. UC8: Xem danh sách video đã upload  
     * 3.3.2. UC9: Xem thông tin chi tiết video  
     * 3.3.3. UC10: Xem danh sách livestream đang/đã diễn ra  
     * 3.3.4. UC11: Xem thông tin chi tiết phiên livestream  
   * 3.4. Chức năng Quản trị Hệ thống  
     * 3.4.1. UC12: Quản trị viên giám sát hệ thống  
     * 3.4.2. UC13: Quản trị viên quản lý video/livestream  
4. Yêu cầu Giao diện Ngoại vi  
   * 4.1. Giao diện Người dùng (UI)  
   * 4.2. Đặc tả API Chi tiết  
     * 4.2.1. API Lưu trữ (spring-video)  
     * 4.2.2. API Quản lý Video (spring-video)  
     * 4.2.3. API Quản lý Phiên Livestream (spring-video)  
     * 4.2.4. Endpoint HLS/DASH cho Livestream  
   * 4.3. Giao diện Phần cứng  
   * 4.4. Giao diện Phần mềm  
5. Yêu cầu Phi Chức năng  
   * 5.1. Yêu cầu về Hiệu năng  
   * 5.2. Yêu cầu về Khả năng Mở rộng (Scalability)  
   * 5.3. Yêu cầu về Độ tin cậy và Tính sẵn sàng (Reliability & Availability)  
   * 5.4. Yêu cầu về Bảo mật (Security)  
   * 5.5. Yêu cầu về Khả năng Bảo trì (Maintainability)  
   * 5.6. Yêu cầu về Tính dễ sử dụng (Usability)  
6. Đặc tả Dữ liệu  
   * 6.1. Mô hình Dữ liệu Logic  
   * 6.2. Chi tiết các Bảng Dữ liệu  
     * 6.2.1. Bảng videos  
     * 6.2.2. Bảng livestreams  
     * 6.2.3. (Mở rộng) Bảng users  
     * 6.2.4. (Mở rộng) Bảng video\_processing\_jobs  
     * 6.2.5. (Mở rộng) Bảng watch\_history  
     * 6.2.6. (Mở rộng) Bảng engagement\_metrics  
   * 6.3. Cấu trúc Sự kiện Kafka  
     * 6.3.1. Sự kiện NewVideoUploadedEvent  
     * 6.3.2. Sự kiện VideoProcessingCompletedEvent  
     * 6.3.3. Sự kiện LivestreamStatusEvent  
7. Kiến trúc Hệ thống Chi tiết  
   * 7.1. Sơ đồ Kiến trúc Tổng thể  
   * 7.2. Mô tả các Thành phần  
     * 7.2.1. Frontend  
     * 7.2.2. Backend API (spring-video)  
     * 7.2.3. Video Processing Worker (spring-worker)  
     * 7.2.4. RTMP/WebRTC Media Server  
     * 7.2.5. MinIO Object Storage  
     * 7.2.6. Apache Kafka & Debezium  
     * 7.2.7. PostgreSQL Database  
   * 7.3. Luồng Dữ liệu Chi tiết  
     * 7.3.1. Luồng Upload Video và Transcoding  
     * 7.3.2. Luồng Phát lại Video (VOD)  
     * 7.3.3. Luồng Livestream (RTMP Ingest)  
     * 7.3.4. Luồng Livestream (WebRTC Ingest \- WHIP)  
     * 7.3.5. Luồng Xem Livestream (HLS/DASH)  
     * 7.3.6. Luồng Lưu trữ Livestream thành VOD  
8. Các Vấn đề Cần Lưu ý khi Triển khai  
   * 8.1. Cấu hình FFmpeg  
   * 8.2. Cấu hình Media Server  
   * 8.3. Bảo mật và Quản lý Khóa  
   * 8.4. Giám sát và Logging  
   * 8.5. Chiến lược Mở rộng và Tối ưu Chi phí  
9. Phụ lục (Nếu có)  
10. Tổng kết

**1\. Giới thiệu**  
Phần này giới thiệu tổng quan về tài liệu Đặc tả Yêu cầu Phần mềm (SRS), mục đích của nó, phạm vi của hệ thống được mô tả, cùng với các định nghĩa, từ viết tắt và thuật ngữ sẽ được sử dụng xuyên suốt tài liệu. Một tài liệu SRS được cấu trúc tốt là nền tảng cho sự thành công của dự án, đảm bảo mọi thành viên trong đội ngũ và các bên liên quan có chung một hiểu biết về những gì cần xây dựng.

* **1.1. Mục đích tài liệu**  
  * Tài liệu này nhằm mục đích đặc tả chi tiết các yêu cầu phần mềm cho Hệ thống Upload và Livestream Video. Nó sẽ phục vụ như một nguồn thông tin chính thức và duy nhất cho đội ngũ phát triển, kiểm thử, quản lý dự án và các bên liên quan khác trong suốt vòng đời của sản phẩm. Mục tiêu là cung cấp một mô tả đầy đủ và rõ ràng về các chức năng, hành vi, giao diện và các thuộc tính chất lượng của hệ thống.  
  * Tài liệu này tuân theo cấu trúc cơ bản của một tài liệu SRS, nhấn mạnh vào việc làm rõ các yêu cầu chức năng, phi chức năng, giao diện, dữ liệu và kiến trúc hệ thống. Việc tuân thủ một cấu trúc chuẩn, như các hướng dẫn trong IEEE Std 830-1998 (mặc dù đã được thay thế bởi ISO/IEC/IEEE 29148\) , giúp đảm bảo tính đầy đủ, nhất quán và dễ hiểu cho tất cả các bên liên quan, từ đó giảm thiểu rủi ro hiểu lầm và sai sót trong quá trình phát triển.  
* **1.2. Tổng quan hệ thống**  
  * Hệ thống được thiết kế để cung cấp một nền tảng toàn diện cho việc quản lý và phân phối nội dung video, hỗ trợ hai luồng nghiệp vụ chính:  
    * **Upload Video (File MP4):** Cho phép người dùng tải lên các tệp video định dạng MP4. Hệ thống sẽ xử lý, chuyển đổi (transcode) sang định dạng streaming (HTTP Live Streaming \- HLS) với nhiều mức chất lượng khác nhau (Adaptive Bitrate Streaming \- ABR) và lưu trữ để có thể phát lại theo yêu cầu (Video on Demand \- VOD).  
    * **Livestream Trực tiếp (Real-time Streaming):** Cho phép người dùng phát video trực tiếp từ các thiết bị của họ (bao gồm camera máy tính, camera chuyên dụng kết nối qua phần mềm như OBS, vMix, hoặc trực tiếp từ trình duyệt web sử dụng công nghệ WebRTC) lên hệ thống. Luồng trực tiếp này sẽ được xử lý, chuyển đổi sang định dạng HLS và/hoặc Dynamic Adaptive Streaming over HTTP (DASH) để phân phối đến nhiều người xem đồng thời trong thời gian thực. Hệ thống cũng cung cấp tùy chọn lưu trữ lại luồng livestream để phát lại sau dưới dạng VOD.  
  * Hệ thống sẽ được xây dựng dựa trên kiến trúc microservices, bao gồm các thành phần backend được phát triển bằng Spring Boot để xử lý logic nghiệp vụ và cung cấp API; các tiến trình worker chuyên dụng cho việc chuyển đổi video; máy chủ ingest livestream chuyên biệt (như Nginx-RTMP, SRS, hoặc MediaSoup); hệ thống lưu trữ đối tượng hiệu năng cao (MinIO) cho các tệp video và HLS/DASH; hệ thống hàng đợi thông điệp (Apache Kafka) kết hợp với Debezium (tùy chọn) để xử lý sự kiện và giao tiếp bất đồng bộ giữa các service; và cơ sở dữ liệu quan hệ (PostgreSQL) để lưu trữ metadata của video, thông tin người dùng và các dữ liệu cấu trúc khác.  
  * Mục tiêu cốt lõi là xây dựng một hệ thống linh hoạt, có khả năng mở rộng cao, đảm bảo hiệu năng ổn định và tuân thủ các tiêu chuẩn bảo mật, nhằm đáp ứng nhu cầu ngày càng tăng về sản xuất, tiêu thụ và quản lý nội dung video trong các ứng dụng hiện đại.  
* **1.3. Định nghĩa, Từ viết tắt và Thuật ngữ**  
  * **SRS:** Software Requirements Specification (Đặc tả Yêu cầu Phần mềm)  
  * **MP4:** MPEG-4 Part 14, một định dạng container đa phương tiện phổ biến cho video và audio.  
  * **HLS:** HTTP Live Streaming, một giao thức truyền phát video trực tiếp dựa trên HTTP được phát triển bởi Apple. HLS chia video thành các đoạn nhỏ (segments) và sử dụng một tệp playlist (M3U8) để mô tả thứ tự của các segment này, cho phép client thích ứng với điều kiện mạng.  
  * **DASH:** Dynamic Adaptive Streaming over HTTP, một giao thức truyền phát video thích ứng dựa trên HTTP, là một tiêu chuẩn ISO. Tương tự HLS, DASH cũng chia video thành các segment và sử dụng một tệp manifest (MPD \- Media Presentation Description) để mô tả các segment và các phiên bản chất lượng khác nhau.  
  * **RTMP:** Real-Time Messaging Protocol, một giao thức dựa trên TCP ban đầu được phát triển bởi Macromedia (sau này là Adobe) để truyền phát âm thanh, video và dữ liệu qua Internet, thường được sử dụng cho việc ingest luồng trực tiếp từ encoder lên media server.  
  * **WebRTC:** Web Real-Time Communication, một dự án mã nguồn mở cung cấp cho các trình duyệt và ứng dụng di động khả năng giao tiếp thời gian thực (RTC) thông qua các API JavaScript đơn giản. Nó cho phép truyền phát âm thanh, video và dữ liệu ngang hàng (peer-to-peer) hoặc thông qua máy chủ trung gian.  
  * **WHIP:** WebRTC-HTTP Ingestion Protocol, một giao thức được chuẩn hóa bởi IETF (RFC 9725\) nhằm đơn giản hóa việc ingest luồng WebRTC từ client (ví dụ: trình duyệt, OBS) vào một media server hoặc CDN bằng cách sử dụng các yêu cầu HTTP POST đơn giản để trao đổi SDP.  
  * **MinIO:** Một hệ thống lưu trữ đối tượng mã nguồn mở, hiệu năng cao, tương thích với API Amazon S3. MinIO phù hợp cho việc lưu trữ lượng lớn dữ liệu phi cấu trúc như video, hình ảnh.  
  * **Kafka:** Apache Kafka, một nền tảng truyền phát sự kiện phân tán mã nguồn mở, được sử dụng để xây dựng các pipeline dữ liệu thời gian thực và các ứng dụng streaming. Kafka có khả năng xử lý thông lượng cao và độ trễ thấp.  
  * **Debezium:** Một nền tảng phân tán mã nguồn mở cho Change Data Capture (CDC). Debezium giám sát các thay đổi trong cơ sở dữ liệu (ví dụ: PostgreSQL) và tạo ra các sự kiện tương ứng, thường được đẩy vào Kafka.  
  * **PostgreSQL:** Một hệ quản trị cơ sở dữ liệu quan hệ đối tượng mã nguồn mở mạnh mẽ và có nhiều tính năng.  
  * **FFmpeg:** Một bộ công cụ phần mềm tự do và mã nguồn mở hàng đầu để xử lý các tệp và luồng âm thanh, video. FFmpeg được sử dụng rộng rãi cho việc chuyển mã (transcoding), thay đổi kích thước, ghép nối, và nhiều tác vụ xử lý đa phương tiện khác.  
  * **API:** Application Programming Interface (Giao diện Lập trình Ứng dụng).  
  * **VOD:** Video on Demand (Video theo yêu cầu).  
  * **ABR:** Adaptive Bitrate Streaming (Truyền phát bitrate thích ứng). Một kỹ thuật cho phép trình phát video tự động chọn luồng chất lượng tốt nhất dựa trên băng thông mạng hiện tại của người xem và khả năng xử lý của thiết bị.  
  * **CDN:** Content Delivery Network (Mạng phân phối nội dung). Một mạng lưới các máy chủ được phân bổ theo địa lý để cung cấp nội dung web (bao gồm video) nhanh chóng cho người dùng dựa trên vị trí của họ.  
  * **OBS:** Open Broadcaster Software, một phần mềm miễn phí và mã nguồn mở phổ biến cho việc quay video màn hình, trộn hình ảnh và phát trực tiếp.  
  * **Worker:** Một tiến trình hoặc service chạy ngầm, thường được thiết kế để thực hiện các tác vụ tốn thời gian hoặc tài nguyên một cách bất đồng bộ, ví dụ như chuyển đổi video.  
  * **Stream Key:** Một chuỗi ký tự bí mật, duy nhất được cung cấp cho streamer để xác thực và định danh luồng phát của họ khi gửi đến RTMP server.  
  * **Metadata:** Dữ liệu mô tả về dữ liệu khác. Trong ngữ cảnh này, metadata video bao gồm tiêu đề, mô tả, người tạo, ngày tạo, thời lượng, v.v.  
  * **Bucket:** Trong các hệ thống lưu trữ đối tượng như MinIO, bucket là một container cấp cao nhất để lưu trữ các đối tượng (tệp).  
  * **Endpoint:** Một URL mà client (ví dụ: trình duyệt, ứng dụng di động, service khác) có thể gửi request đến để tương tác với một service.  
  * **Segment:** Trong HLS và DASH, video được chia thành các đoạn nhỏ gọi là segment. Client sẽ tải và phát các segment này một cách tuần tự.  
  * **Playlist/Manifest:** Một tệp văn bản (thường là .m3u8 cho HLS hoặc .mpd cho DASH) chứa thông tin về các segment của một luồng video, bao gồm URL của chúng, thời lượng, và các thông tin về các luồng chất lượng khác nhau (trong trường hợp ABR).

Việc định nghĩa rõ ràng các thuật ngữ và từ viết tắt ngay từ đầu là cực kỳ quan trọng trong một tài liệu kỹ thuật phức tạp như SRS cho hệ thống streaming. Điều này giúp tránh hiểu lầm và đảm bảo tất cả các bên liên quan có cùng một cách hiểu về các khái niệm cốt lõi. Hệ thống streaming video sử dụng rất nhiều công nghệ và giao thức chuyên ngành, mỗi công nghệ/giao thức có thể có nhiều từ viết tắt hoặc thuật ngữ riêng. Nếu không định nghĩa rõ, người đọc từ các background khác nhau (kỹ thuật, kinh doanh, quản lý) có thể hiểu sai, dẫn đến sai lệch trong quá trình phát triển và nghiệm thu. Việc liệt kê và giải thích các thuật ngữ chính như HLS, DASH, RTMP, WebRTC, MinIO, Kafka, FFmpeg,... sẽ tạo nền tảng vững chắc cho các phần sau của tài liệu. Ví dụ, hiểu rõ sự khác biệt giữa "segment" và "playlist" là cần thiết khi mô tả luồng HLS.  
**2\. Mô tả Tổng quan**  
Phần này cung cấp một cái nhìn tổng thể về sản phẩm, chức năng chính, đối tượng người dùng, các ràng buộc và giả định liên quan đến việc phát triển hệ thống. Mục đích là để người đọc hiểu được mục tiêu và phạm vi của phần mềm trước khi đi vào các chi tiết kỹ thuật.

* **2.1. Bối cảnh Sản phẩm**  
  * Hệ thống Upload và Livestream Video được hình thành để đáp ứng nhu cầu ngày càng tăng về việc tạo, chia sẻ và tiêu thụ nội dung video trực tuyến. Nó có thể được phát triển như một giải pháp độc lập, cung cấp một nền tảng streaming chuyên dụng, hoặc được thiết kế như một module cốt lõi để tích hợp vào các hệ sinh thái ứng dụng lớn hơn. Ví dụ, hệ thống này có thể phục vụ như một thành phần backend cho một nền tảng mạng xã hội cho phép người dùng chia sẻ video và livestream, một hệ thống quản lý học tập (LMS) hỗ trợ bài giảng video và lớp học trực tuyến, hoặc một cổng thông tin nội bộ cho doanh nghiệp để chia sẻ các buổi đào tạo và thông báo qua video.  
  * Trong trường hợp là một phần của hệ sinh thái lớn hơn, hệ thống này sẽ cần định nghĩa rõ ràng các giao diện tương tác (API) với các dịch vụ khác trong hệ sinh thái đó. Các tương tác này có thể bao gồm:  
    * **Dịch vụ Quản lý Người dùng (User Management Service):** Để xác thực người dùng, lấy thông tin hồ sơ người dùng, và quản lý quyền truy cập vào các chức năng upload, livestream.  
    * **Dịch vụ Thanh toán (Payment Service):** Nếu hệ thống có các tính năng cao cấp hoặc gói thuê bao (ví dụ: dung lượng lưu trữ lớn hơn, chất lượng livestream cao hơn), nó cần tích hợp với dịch vụ thanh toán để xử lý giao dịch.  
    * **Dịch vụ Thông báo (Notification Service):** Để gửi thông báo cho người dùng về các sự kiện quan trọng, như video đã xử lý xong, livestream sắp bắt đầu, hoặc có bình luận mới.  
    * **Dịch vụ Analytics:** Để thu thập dữ liệu về lượt xem, tương tác người dùng, và hiệu suất hệ thống, có thể cần gửi dữ liệu đến một dịch vụ analytics tập trung. Các giao diện tương tác này sẽ được đặc tả chi tiết hơn trong Mục 4 (Yêu cầu Giao diện Ngoại vi).  
* **2.2. Tóm tắt Chức năng Sản phẩm**  
  * Như đã nêu trong Mục 1.1 của tài liệu tóm tắt từ người dùng, hệ thống tập trung vào các khả năng cốt lõi sau, nhằm cung cấp một giải pháp streaming toàn diện:  
    * **Tải lên video (MP4) và Phát lại (VOD):** Người dùng có thể tải lên các tệp video định dạng MP4. Hệ thống sẽ tự động tiếp nhận, lưu trữ an toàn tệp gốc. Quan trọng hơn, hệ thống sẽ thực hiện quá trình xử lý và chuyển đổi (transcoding) tệp MP4 này sang định dạng HTTP Live Streaming (HLS) với nhiều mức chất lượng khác nhau (Adaptive Bitrate Streaming \- ABR). Việc này đảm bảo video có thể được phát lại mượt mà trên nhiều loại thiết bị (máy tính, điện thoại, tablet) và thích ứng với các điều kiện mạng khác nhau của người xem. Video sau khi xử lý thành công sẽ sẵn sàng để xem lại bất cứ lúc nào thông qua trình phát video hỗ trợ HLS.  
    * **Livestream Trực tiếp và Xem trực tiếp (Real-time):** Hệ thống cho phép người dùng (streamer) phát video trực tiếp từ thiết bị của họ. Các nguồn phát có thể đa dạng, từ camera máy tính, camera chuyên dụng kết nối qua phần mềm streaming phổ biến như OBS (Open Broadcaster Software), vMix, cho đến việc phát trực tiếp từ trình duyệt web sử dụng công nghệ WebRTC. Luồng trực tiếp này sẽ được ingest vào hệ thống, xử lý (có thể bao gồm transcoding sang HLS/DASH ABR) và phân phối đến nhiều người xem đồng thời trong thời gian thực.  
    * **Lưu trữ Video An toàn và Hiệu quả:** Cả video gốc (tệp MP4 được người dùng tải lên) và các phiên bản video đã được chuyển đổi (các segment và playlist của HLS/DASH) sẽ được lưu trữ một cách an toàn và có tổ chức trên hệ thống lưu trữ đối tượng MinIO. MinIO được chọn vì khả năng tương thích S3, hiệu năng cao và khả năng mở rộng tốt cho dữ liệu phi cấu trúc.  
    * **Cung cấp API Phát lại Video (HLS/DASH):** Để các ứng dụng client (giao diện web, ứng dụng di động) có thể hiển thị và phát video, hệ thống sẽ cung cấp một bộ API mạnh mẽ. Các API này sẽ cho phép client lấy thông tin video, URL của playlist HLS (hoặc manifest DASH) cho cả video đã upload (VOD) và các luồng livestream đang diễn ra. Điều này tạo điều kiện cho việc tích hợp dễ dàng với các trình phát video hiện đại.  
* **2.3. Đặc điểm Người dùng**  
  * Dựa trên Mục 1.2 của tài liệu tóm tắt, có hai nhóm đối tượng người dùng chính, mỗi nhóm có những nhu cầu và cách tương tác khác nhau với hệ thống:  
    * **Người dùng cuối (End User):** Đây là nhóm người dùng trực tiếp tạo ra và tiêu thụ nội dung video.  
      * **Người tải lên (Uploader):** Là các cá nhân hoặc tổ chức muốn chia sẻ nội dung video của họ dưới dạng VOD. Họ sẽ tương tác với hệ thống thông qua giao diện upload để tải lên các tệp MP4, cung cấp các thông tin mô tả (metadata) cho video như tiêu đề, mô tả. Họ cũng cần các công cụ để quản lý các video đã tải lên, ví dụ như xem lại thông tin, theo dõi trạng thái xử lý, và có thể là xóa video.  
      * **Người phát trực tiếp (Streamer):** Là các cá nhân hoặc tổ chức muốn phát sóng nội dung của họ trực tiếp đến khán giả. Họ sẽ sử dụng phần mềm streaming chuyên dụng (OBS, vMix), thiết bị encoder phần cứng, hoặc trình duyệt web (qua WebRTC) để gửi luồng video đến các điểm ingest của hệ thống. Họ cần khả năng quản lý các phiên livestream của mình, bao gồm việc lấy thông tin cấu hình (RTMP URL, stream key, thông tin signaling WebRTC), bắt đầu và kết thúc một phiên phát.  
      * **Người xem (Viewer):** Là đối tượng tiêu thụ nội dung video, bao gồm cả VOD và livestream. Họ sẽ tương tác chủ yếu với trình phát video được nhúng trên các trang web hoặc trong ứng dụng di động để xem nội dung, chọn chất lượng video (nếu trình phát hỗ trợ), và có thể có các tương tác khác như bình luận, thích (nếu hệ thống hỗ trợ).  
    * **Quản trị viên (Administrator):** Đây là nhóm người dùng có vai trò kỹ thuật, chịu trách nhiệm vận hành và duy trì hệ thống.  
      * Họ cần các công cụ và giao diện để quản lý tổng thể hệ thống, bao gồm giám sát tình trạng hoạt động của các thành phần (API server, worker, media server, database, storage), theo dõi tài nguyên hệ thống (CPU, RAM, disk, network).  
      * Quản lý tài khoản người dùng (nếu hệ thống có cơ chế đăng ký/đăng nhập riêng), phân quyền.  
      * Cấu hình các tham số hệ thống, ví dụ như cấu hình transcoding profiles, giới hạn kích thước upload.  
      * Xem log hệ thống để phục vụ việc điều tra và xử lý sự cố.  
      * Có thể cần các công cụ để quản lý nội dung ở mức độ cao hơn, ví dụ như gỡ bỏ nội dung vi phạm.  
* **2.4. Các Ràng buộc Chung**  
  * **Công nghệ:** Hệ thống sẽ được phát triển dựa trên các công nghệ đã được đề xuất trong bản tóm tắt và được chấp thuận. Cụ thể:  
    * Backend API và Worker Services: Spring Boot (Java).  
    * Cơ sở dữ liệu Metadata: PostgreSQL.  
    * Lưu trữ đối tượng (Video gốc, HLS/DASH segments): MinIO.  
    * Hàng đợi thông điệp và Xử lý sự kiện: Apache Kafka. Debezium có thể được xem xét cho các kịch bản CDC phức tạp hơn, nhưng luồng upload ban đầu sẽ do service tự publish event.  
    * Ingest và Xử lý Livestream:  
      * RTMP Ingest: Nginx-RTMP module hoặc SRS (Simple Realtime Server).  
      * WebRTC Ingest: MediaSoup hoặc SRS (nếu hỗ trợ WebRTC ingest tốt).  
      * Transcoding (cho cả VOD và Livestream): FFmpeg.  
    * Giao thức Streaming đầu ra: HLS là chủ đạo, DASH là tùy chọn. Việc lựa chọn phiên bản cụ thể của các công nghệ này, cũng như các thư viện phụ trợ, sẽ được quyết định trong giai đoạn thiết kế chi tiết, nhưng phải đảm bảo tính tương thích, hiệu suất, bảo mật và khả năng bảo trì lâu dài.  
  * **Ngôn ngữ:**  
    * Giao diện người dùng (Frontend) và các tài liệu hướng dẫn người dùng cuối sẽ sử dụng Tiếng Việt.  
    * Mã nguồn, tài liệu kỹ thuật nội bộ, comment trong code sẽ sử dụng Tiếng Anh để thuận tiện cho việc tham khảo tài liệu quốc tế và cộng tác (nếu có).  
  * **Môi trường triển khai:** Cần xác định rõ môi trường mục tiêu cho việc triển khai hệ thống (ví dụ: on-premise, public cloud như AWS/Azure/GCP, private cloud). Quyết định này sẽ ảnh hưởng đến kiến trúc mạng, lựa chọn dịch vụ quản lý (managed services), chiến lược scaling và chi phí vận hành. Ví dụ, nếu triển khai trên cloud, có thể tận dụng các dịch vụ như Kubernetes (EKS, GKE, AKS) để quản lý container, hoặc các dịch vụ media chuyên dụng của cloud provider.  
  * **Tuân thủ pháp lý và bản quyền:** Hệ thống phải được thiết kế để có khả năng tuân thủ các quy định pháp lý hiện hành liên quan đến bản quyền nội dung video, bảo vệ dữ liệu cá nhân của người dùng (ví dụ: GDPR nếu có người dùng từ Liên minh Châu Âu, hoặc các quy định tương tự của Việt Nam). Điều này có thể bao gồm các tính năng như quản lý quyền sở hữu nội dung, cơ chế báo cáo vi phạm, và mã hóa dữ liệu nhạy cảm.  
* **2.5. Giả định và Phụ thuộc**  
  * **Giả định:** Các giả định này là những điều kiện được cho là đúng mà không cần chứng minh trong phạm vi của dự án này. Nếu các giả định này không còn đúng, nó có thể ảnh hưởng đến thiết kế hoặc hoạt động của hệ thống.  
    * Người dùng (cả uploader, streamer và viewer) có kết nối internet với băng thông đủ và độ ổn định cần thiết để thực hiện các thao tác của họ (tải lên file lớn, phát luồng chất lượng cao, xem video không giật).  
    * Streamer sở hữu hoặc có quyền truy cập vào các thiết bị (camera, microphone) và phần mềm (OBS, vMix, trình duyệt hỗ trợ WebRTC) tương thích với các giao thức ingest được hệ thống hỗ trợ (RTMP, WebRTC/WHIP).  
    * Hạ tầng mạng của nơi triển khai hệ thống (data center hoặc cloud) có đủ băng thông đầu ra (egress bandwidth) để phục vụ đồng thời nhiều người xem video và livestream.  
    * Có sẵn tài nguyên máy chủ (CPU, RAM, dung lượng lưu trữ tốc độ cao cho transcoding tạm thời, disk I/O) đủ để triển khai và vận hành các thành phần của hệ thống, đặc biệt là các worker xử lý video và media server.  
    * Các API của bên thứ ba (nếu có, ví dụ: dịch vụ CDN, dịch vụ xác thực tập trung) sẽ hoạt động ổn định và đúng như tài liệu mô tả.  
  * **Phụ thuộc:** Hệ thống phụ thuộc vào các yếu tố bên ngoài sau:  
    * Sự ổn định, hiệu năng và các giới hạn (nếu có) của các dịch vụ bên thứ ba được tích hợp, ví dụ như dịch vụ CDN dùng để phân phối HLS/DASH, dịch vụ email để gửi thông báo, hoặc hệ thống xác thực OAuth2/OpenID Connect.  
    * Sự sẵn có, tính tương thích và các bản vá lỗi của các thư viện mã nguồn mở được sử dụng trong quá trình phát triển (ví dụ: FFmpeg, các thư viện Kafka client, MinIO client, Spring Boot starters, thư viện HLS/DASH player). Bất kỳ thay đổi không tương thích hoặc lỗi nghiêm trọng nào trong các thư viện này đều có thể ảnh hưởng đến hệ thống.  
    * Việc cấu hình chính xác và tối ưu các thành phần hạ tầng cốt lõi như RTMP/Media Server (Nginx-RTMP, SRS, MediaSoup), MinIO, Kafka cluster, và PostgreSQL database. Sai sót trong cấu hình có thể dẫn đến hiệu năng kém, mất ổn định hoặc lỗ hổng bảo mật.  
    * Nếu sử dụng Debezium, hệ thống sẽ phụ thuộc vào khả năng của Debezium connector trong việc đọc và diễn giải Write-Ahead Log (WAL) của PostgreSQL một cách chính xác và kịp thời.

Việc làm rõ các ràng buộc và giả định giúp quản lý kỳ vọng của các bên liên quan và xác định các yếu tố rủi ro tiềm ẩn ngay từ giai đoạn đầu của dự án. Ví dụ, giả định về "kết nối internet ổn định của người dùng" là một yếu tố quan trọng, vì chất lượng dịch vụ streaming phụ thuộc rất nhiều vào điều này. Nếu giả định này không được đáp ứng đối với phần lớn người dùng mục tiêu, trải nghiệm người dùng sẽ bị ảnh hưởng nghiêm trọng, và có thể cần xem xét các giải pháp tối ưu hóa cho mạng yếu. Tương tự, việc không làm rõ các ràng buộc về công nghệ có thể dẫn đến lựa chọn thiết kế không phù hợp hoặc vượt quá khả năng của đội ngũ phát triển hoặc ngân sách dự án. Nếu có ràng buộc về việc chỉ sử dụng các giải pháp mã nguồn mở, thì không thể đề xuất các dịch vụ media thương mại đắt tiền, ngay cả khi chúng có nhiều tính năng ưu việt. Các giả định chưa được kiểm chứng có thể trở thành rủi ro lớn; ví dụ, nếu giả định "người dùng có thiết bị/phần mềm tương thích để livestream" là sai, tỷ lệ người dùng thực sự sử dụng tính năng livestream có thể rất thấp. Liệt kê các phụ thuộc, như sự ổn định của thư viện mã nguồn mở hoặc dịch vụ của bên thứ ba, giúp đội ngũ dự án nhận diện các yếu tố bên ngoài có thể ảnh hưởng đến tiến độ hoặc chất lượng sản phẩm, từ đó có kế hoạch dự phòng hoặc giảm thiểu rủi ro. Trong bối cảnh của một hệ thống streaming video, các ràng buộc về băng thông mạng, khả năng xử lý của máy chủ, và sự tương thích của các thiết bị đầu cuối của người dùng là cực kỳ quan trọng và cần được phân tích và nêu rõ trong tài liệu SRS.  
**3\. Tính năng Hệ thống và Yêu cầu Chức năng Chi tiết**  
Phần này mô tả chi tiết từng chức năng của hệ thống dưới dạng các Use Case (UC). Mỗi UC sẽ bao gồm mô tả tổng quan, các điều kiện tiên quyết để thực hiện UC, luồng sự kiện chính (happy path) mô tả các bước thực hiện thành công, các luồng phụ hoặc luồng ngoại lệ (alternative/exception flows) xử lý các tình huống không mong muốn, và các API liên quan trực tiếp đến UC đó. Việc mô tả dưới dạng UC giúp làm rõ cách người dùng tương tác với hệ thống và kết quả mong đợi từ những tương tác đó.

* **3.1. Chức năng Upload Video (File MP4)**  
  * Chức năng này là một trong hai trụ cột chính của hệ thống, cho phép người dùng đóng góp nội dung video của họ dưới dạng tệp MP4. Sau khi tải lên, hệ thống sẽ tự động xử lý các tệp này để chúng sẵn sàng cho việc phát lại tối ưu trên nhiều nền tảng.  
  * **3.1.1. UC1: Người dùng tải lên file MP4**  
    * **Mô tả:** Người dùng cuối, sau khi đã được xác thực và cấp quyền, truy cập vào giao diện upload của hệ thống và chọn một hoặc nhiều tệp video định dạng MP4 từ thiết bị lưu trữ cá nhân của họ để tải lên máy chủ. Quá trình này bao gồm việc gửi tệp và cung cấp các thông tin metadata cơ bản.  
    * **Điều kiện tiên quyết:**  
      * Người dùng đã đăng nhập thành công vào hệ thống bằng tài khoản hợp lệ.  
      * Tài khoản của người dùng có quyền thực hiện chức năng tải lên video (ví dụ: không bị khóa, thuộc nhóm người dùng được phép upload).  
      * Người dùng có kết nối mạng ổn định để quá trình tải lên không bị gián đoạn.  
    * **Luồng chính (Happy Path):**  
      1. Người dùng tương tác với giao diện người dùng (UI), ví dụ, nhấp vào nút "Upload Video" hoặc một biểu tượng tương tự.  
      2. Hệ thống (Frontend) hiển thị một biểu mẫu (form) hoặc một khu vực cho phép người dùng chọn tệp từ hệ thống tệp cục bộ của họ hoặc thực hiện thao tác kéo-thả (drag-and-drop) tệp video.  
      3. Người dùng chọn một tệp video có định dạng MP4. Hệ thống có thể thực hiện kiểm tra sơ bộ định dạng tệp ở phía client (ví dụ, qua phần mở rộng.mp4 hoặc MIME type) để cung cấp phản hồi sớm.  
      4. Sau khi chọn tệp, người dùng được cung cấp các trường để nhập thông tin metadata cơ bản cho video, tối thiểu bao gồm "Tiêu đề" (title). Các trường khác như "Mô tả" (description), "Tags" có thể là tùy chọn, dựa trên thiết kế của bảng video (Mục 4.1 tài liệu tóm tắt).  
      5. Người dùng xác nhận việc tải lên bằng cách nhấp vào nút "Bắt đầu Upload", "Xác nhận" hoặc tương tự.  
      6. Frontend bắt đầu quá trình tải tệp lên. Đối với các tệp lớn, Frontend có thể chia tệp thành các phần nhỏ hơn (chunking) và tải lên tuần tự hoặc song song để cải thiện độ tin cậy và hiệu suất. Dữ liệu tệp được gửi đến Backend API thông qua endpoint POST /api/storage/upload, thường sử dụng Content-Type: multipart/form-data.  
      7. Backend API (service spring-video) nhận được yêu cầu tải lên. Nó thực hiện các bước xác thực quan trọng:  
         * Kiểm tra quyền của người dùng.  
         * Kiểm tra định dạng tệp (đảm bảo là MP4 hợp lệ, có thể kiểm tra MIME type thực tế thay vì chỉ dựa vào tên tệp).  
         * Kiểm tra kích thước tệp có vượt quá giới hạn tối đa cho phép của hệ thống hay không (ví dụ: 2GB).  
      8. Nếu các bước xác thực thành công, Backend API tiến hành lưu trữ tệp MP4 gốc vào một bucket được chỉ định trong MinIO, ví dụ bucket có tên là video. Để đảm bảo tính duy nhất và dễ quản lý, đường dẫn lưu trữ tệp trong MinIO nên được thiết kế cẩn thận, ví dụ: /{owner\_id}/{temp\_video\_id}/{original\_filename}.mp4 hoặc /{owner\_id}/{uuid}.mp4. temp\_video\_id hoặc uuid là một định danh duy nhất được tạo ra cho lần upload này.  
      9. Sau khi tệp đã được lưu trữ thành công vào MinIO và Backend API nhận được xác nhận từ MinIO, Backend API sẽ gọi một endpoint nội bộ hoặc thực hiện logic để tạo một bản ghi metadata mới cho video này trong cơ sở dữ liệu PostgreSQL (bảng videos). Yêu cầu này được thực hiện thông qua POST /api/video/. Bản ghi này sẽ chứa các thông tin như ownerId (ID của người dùng tải lên), title (tiêu đề do người dùng nhập), description (nếu có), đường dẫn đến tệp MP4 gốc trong MinIO (ví dụ: video/owner\_id/temp\_video\_id/file.mp4), và trạng thái ban đầu của video (ví dụ: UPLOADED hoặc PENDING\_PROCESSING).  
      10. Hệ thống (Backend API) trả về một phản hồi thành công (ví dụ: HTTP 201 Created hoặc 202 Accepted) cho Frontend. Phản hồi này có thể bao gồm ID của video vừa được tạo trong hệ thống, để Frontend có thể theo dõi hoặc điều hướng người dùng. Giao diện người dùng hiển thị thông báo "Upload thành công" hoặc tương tự.  
    * **Luồng phụ/Ngoại lệ:**  
      * **E1: Tệp không hợp lệ hoặc vượt quá kích thước:**  
        1. Nếu người dùng chọn một tệp không phải là định dạng MP4 được hỗ trợ, hoặc kích thước tệp vượt quá giới hạn đã cấu hình.  
        2. Hệ thống (Frontend hoặc Backend) từ chối tệp và hiển thị một thông báo lỗi rõ ràng cho người dùng, ví dụ: "Định dạng tệp không được hỗ trợ. Vui lòng chọn tệp MP4." hoặc "Kích thước tệp vượt quá giới hạn cho phép (ví dụ: 2GB)."  
      * **E2: Lỗi kết nối mạng trong quá trình upload:**  
        1. Nếu kết nối mạng của người dùng bị gián đoạn trong khi đang tải tệp lên.  
        2. Frontend nên cố gắng phát hiện lỗi này. Tùy thuộc vào thư viện upload được sử dụng, hệ thống có thể hỗ trợ khả năng tải lại (resume) từ điểm bị gián đoạn (nếu API và MinIO hỗ trợ multipart upload và resume).  
        3. Nếu không thể resume, hệ thống hiển thị thông báo lỗi "Mất kết nối. Vui lòng kiểm tra mạng và thử lại." Người dùng có thể cần phải bắt đầu lại quá trình upload.  
      * **E3: Lỗi khi lưu trữ tệp vào MinIO:**  
        1. Nếu Backend API gặp sự cố khi cố gắng lưu tệp MP4 vào MinIO (ví dụ: MinIO không khả dụng, lỗi xác thực với MinIO, hết dung lượng bucket).  
        2. Backend API ghi log chi tiết về lỗi (bao gồm mã lỗi từ MinIO SDK).  
        3. Backend API trả về mã lỗi HTTP phù hợp (ví dụ: 500 Internal Server Error hoặc 503 Service Unavailable) cho Frontend.  
        4. Frontend hiển thị thông báo lỗi chung cho người dùng: "Đã xảy ra lỗi trong quá trình lưu trữ video. Vui lòng thử lại sau."  
      * **E4: Lỗi khi tạo bản ghi metadata video trong PostgreSQL:**  
        1. Sau khi tệp đã được lưu vào MinIO, nếu Backend API gặp lỗi khi tạo bản ghi trong bảng videos của PostgreSQL (ví dụ: lỗi kết nối DB, ràng buộc dữ liệu không thỏa mãn).  
        2. Hệ thống cần có cơ chế xử lý nhất quán. Một lựa chọn là rollback logic: nếu không tạo được metadata, tệp đã lưu trên MinIO có thể được xem xét để xóa (hoặc đánh dấu là "orphan" để một tiến trình dọn dẹp xử lý sau) để tránh lãng phí dung lượng.  
        3. Backend API ghi log lỗi và trả về mã lỗi HTTP (ví dụ: 500 Internal Server Error) cho Frontend.  
        4. Frontend hiển thị thông báo lỗi cho người dùng.  
    * **Yêu cầu đặc tả API liên quan:**  
      * POST /api/storage/upload: Endpoint để Frontend gửi dữ liệu tệp MP4.  
      * POST /api/video/: Endpoint để Backend API (sau khi lưu file vào MinIO) tạo bản ghi metadata cho video.  
  * **3.1.2. UC2: Hệ thống xử lý và chuyển đổi video sang HLS ABR**  
    * **Mô tả:** Sau khi một tệp video MP4 được người dùng tải lên thành công và thông tin metadata ban đầu của nó được ghi nhận, hệ thống sẽ tự động kích hoạt một quy trình xử lý ngầm. Quy trình này có nhiệm vụ chính là chuyển đổi (transcode) tệp MP4 gốc sang định dạng HTTP Live Streaming (HLS) với nhiều mức chất lượng khác nhau (Adaptive Bitrate Streaming \- ABR). Các tệp HLS kết quả (bao gồm các tệp segment video.ts và các tệp playlist.m3u8) sẽ được lưu trữ vào MinIO để sẵn sàng cho việc phát lại.  
    * **Điều kiện tiên quyết:**  
      * Tệp video MP4 gốc đã được tải lên và lưu trữ thành công vào bucket video trên MinIO.  
      * Một bản ghi tương ứng với video này đã được tạo trong bảng videos của PostgreSQL, với trạng thái ban đầu là UPLOADED hoặc PENDING\_PROCESSING, và có chứa đường dẫn đến tệp MP4 gốc trong MinIO.  
    * **Luồng chính (Happy Path):**  
      1. Service spring-video (Backend API), ngay sau khi tạo thành công bản ghi metadata cho video mới trong PostgreSQL (như mô tả ở UC1, bước 9), sẽ sinh một sự kiện (event) mang thông tin về video mới này. Sự kiện này, ví dụ có tên là NEW\_VIDEO\_UPLOADED, sẽ chứa các dữ liệu cần thiết để worker có thể bắt đầu xử lý, bao gồm:  
         * videoId: UUID của video trong bảng videos.  
         * ownerId: UUID của người sở hữu video.  
         * sourcePathInMinIO: Đường dẫn đầy đủ đến tệp MP4 gốc trong bucket video của MinIO (ví dụ: video/owner\_id/video\_id\_temp/original.mp4).  
         * requestedTranscodingProfiles: Một danh sách các cấu hình transcoding mong muốn. Ví dụ: \`\`. Các profile này có thể được định nghĩa trước trong cấu hình hệ thống.  
         * originalFileName: Tên tệp gốc do người dùng tải lên.  
         * uploadTimestamp: Thời điểm video được tải lên.  
         * **Cấu trúc sự kiện Kafka :**  
           `{`  
             `"eventId": "generate_uuid_here",`  
             `"eventType": "NEW_VIDEO_UPLOADED",`  
             `"eventTimestamp": "current_iso_datetime",`  
             `"payload": {`  
               `"videoId": "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx",`  
               `"ownerId": "yyyyyyyy-yyyy-yyyy-yyyy-yyyyyyyyyyyy",`  
               `"sourcePathInMinIO": "video/owner_id/video_id_original/my_awesome_video.mp4",`  
               `"requestedTranscodingProfiles":,`  
               `"originalFileName": "my_awesome_video.mp4",`  
               `"uploadTimestamp": "2023-10-27T10:30:00Z"`  
             `}`  
           `}`  
           Việc sử dụng một cấu trúc sự kiện rõ ràng, có định danh (eventId, eventType, eventTimestamp) và payload chứa dữ liệu cụ thể là rất quan trọng trong kiến trúc hướng sự kiện. Nó giúp cho việc theo dõi, gỡ lỗi và mở rộng hệ thống trở nên dễ dàng hơn. Các thông tin như videoId, sourcePathInMinIO, và requestedTranscodingProfiles là tối cần thiết để worker có thể thực hiện nhiệm vụ của mình.  
      2. Sự kiện NEW\_VIDEO\_UPLOADED này được spring-video publish lên một topic cụ thể trong Apache Kafka, ví dụ: video\_processing\_trigger. Việc sử dụng Kafka ở đây mang lại nhiều lợi ích: tách rời (decoupling) service spring-video khỏi service xử lý video (spring-worker), cho phép xử lý bất đồng bộ, tăng khả năng chịu lỗi (nếu worker lỗi, message vẫn còn trong Kafka) và khả năng mở rộng (có thể tăng số lượng worker để xử lý nhiều message hơn).  
      3. Service spring-worker, được thiết kế như một Kafka consumer, lắng nghe (subscribe) topic video\_processing\_trigger. Khi có message mới, spring-worker sẽ nhận và xử lý sự kiện này.  
      4. Ngay khi nhận được sự kiện, spring-worker nên cập nhật trạng thái của video tương ứng trong bảng videos của PostgreSQL thành PROCESSING. Điều này giúp người dùng và hệ thống biết rằng video đang được xử lý.  
      5. spring-worker sử dụng thông tin sourcePathInMinIO từ sự kiện để tải (download) tệp MP4 gốc từ bucket video trong MinIO về môi trường làm việc cục bộ của worker.  
      6. spring-worker gọi và thực thi tiến trình FFmpeg để thực hiện việc chuyển đổi video từ tệp MP4 gốc sang các luồng HLS ABR theo các requestedTranscodingProfiles. Quá trình này bao gồm:  
         * Tạo ra nhiều phiên bản (renditions) của video với các độ phân giải (ví dụ: 1080p, 720p, 480p) và bitrate khác nhau.  
         * Tạo ra một hoặc nhiều luồng audio riêng biệt (ví dụ: AAC 128kbps).  
         * Chia nhỏ từng rendition video/audio thành các tệp segment nhỏ (thường có đuôi .ts).  
         * Tạo các tệp media playlist (ví dụ: 1080p.m3u8, 720p.m3u8, audio.m3u8) mô tả các segment của từng rendition.  
         * Tạo một tệp master playlist (ví dụ: index.m3u8) tham chiếu đến tất cả các media playlist của các rendition khác nhau, kèm theo thông tin về băng thông và độ phân giải của chúng.  
         * **Ví dụ lệnh FFmpeg chi tiết để chuyển đổi MP4 sang HLS ABR :**  
           `ffmpeg -i /path/to/worker/temp/input.mp4 \`  
           `-map 0:v:0 -map 0:a:0 -map 0:v:0 -map 0:a:0 -map 0:v:0 -map 0:a:0 -map 0:a:0 \`  
           `-c:v libx264 -crf 22 -preset medium -sc_threshold 0 -g 48 \`  
           `-c:a aac -ar 44100 \`  
           `-filter:v:0 scale=w=1920:h=1080 -b:v:0 5000k -maxrate:v:0 5500k -bufsize:v:0 10000k -metadata:s:v:0 title="1080p" \`  
           `-filter:v:1 scale=w=1280:h=720 -b:v:1 2800k -maxrate:v:1 3000k -bufsize:v:1 5600k -metadata:s:v:1 title="720p" \`  
           `-filter:v:2 scale=w=854:h=480 -b:v:2 1000k -maxrate:v:2 1200k -bufsize:v:2 2000k -metadata:s:v:2 title="480p" \`  
           `-b:a:0 128k -metadata:s:a:0 title="audio_128k" \`  
           `-var_stream_map "v:0,a:0,name:1080p v:1,a:0,name:720p v:2,a:0,name:480p a:0,name:audio_128k" \`  
           `-master_pl_name index.m3u8 \`  
           `-hls_time 6 \`  
           `-hls_list_size 0 \`  
           `-hls_segment_filename "/path/to/worker/output/{video_id}/%v/segment%05d.ts" \`  
           `-f hls "/path/to/worker/output/{video_id}/dummy_for_master.m3u8"`  
           Trong đó:  
           * /path/to/worker/temp/input.mp4: Đường dẫn đến file MP4 gốc worker đã tải về.  
           * /path/to/worker/output/{video\_id}/: Thư mục output tạm thời của worker, với {video\_id} là ID của video.  
           * Các tùy chọn \-map, \-filter:v, \-b:v, \-maxrate:v, \-bufsize:v được lặp lại cho mỗi rendition video (1080p, 720p, 480p).  
           * \-b:a:0 128k: Định nghĩa một rendition audio riêng.  
           * \-var\_stream\_map: Ánh xạ các luồng video và audio đã xử lý vào các tên rendition trong master playlist.  
           * \-master\_pl\_name index.m3u8: Tên của master playlist sẽ được tạo trong thư mục output chính.  
           * \-hls\_segment\_filename: Mẫu đặt tên cho các tệp segment, bao gồm cả đường dẫn tương đối %v (tên rendition) để FFmpeg tự tạo thư mục con cho mỗi rendition.  
           * \-f hls "/path/to/worker/output/{video\_id}/dummy\_for\_master.m3u8": FFmpeg yêu cầu một output file, nhưng master playlist và media playlist sẽ được tạo dựa trên \-master\_pl\_name và cấu trúc thư mục từ \-hls\_segment\_filename. Việc sử dụng FFmpeg là một lựa chọn mạnh mẽ và linh hoạt, cho phép tùy chỉnh sâu các thông số transcoding để đạt được chất lượng và hiệu suất mong muốn. Tạo ra các luồng ABR là một yêu cầu thiết yếu để đảm bảo trải nghiệm xem video tốt nhất trên đa dạng thiết bị và tốc độ kết nối mạng của người dùng.  
      7. Sau khi FFmpeg hoàn tất quá trình chuyển đổi và tạo ra tất cả các tệp HLS (bao gồm master playlist index.m3u8, các media playlist cho từng rendition, và tất cả các tệp segment .ts) trong thư mục output tạm thời của worker, spring-worker sẽ tiến hành tải (upload) toàn bộ cấu trúc thư mục này lên một bucket được chỉ định trong MinIO, ví dụ bucket có tên là stream. Cấu trúc lưu trữ trong bucket stream nên phản ánh cấu trúc đã tạo, ví dụ: /{video\_id}/index.m3u8, /{video\_id}/1080p/1080p.m3u8, /{video\_id}/1080p/segment00001.ts, v.v.  
      8. Khi tất cả các tệp HLS đã được tải lên MinIO thành công, spring-worker cập nhật trạng thái của video trong bảng videos của PostgreSQL thành COMPLETED hoặc AVAILABLE. Đồng thời, worker sẽ lưu đường dẫn (hoặc một phần đường dẫn tương đối, tùy theo cách API phát lại được thiết kế) đến tệp master playlist HLS (ví dụ: /{video\_id}/index.m3u8) vào trường path của bản ghi video.  
      9. (Tùy chọn) spring-worker có thể publish một sự kiện Kafka khác, ví dụ VIDEO\_PROCESSING\_COMPLETED, lên một topic khác (ví dụ: video\_notifications). Sự kiện này có thể chứa videoId và trạng thái xử lý. Các service khác, chẳng hạn như một notification service, có thể lắng nghe topic này để thông báo cho người dùng rằng video của họ đã sẵn sàng để xem.  
    * **Luồng phụ/Ngoại lệ:**  
      * **E1: Lỗi tải video gốc từ MinIO:**  
        1. Nếu spring-worker không thể tải tệp MP4 gốc từ MinIO do lỗi mạng, tệp không tồn tại, hoặc lỗi phân quyền.  
        2. Worker ghi log chi tiết về lỗi.  
        3. Worker cập nhật trạng thái video trong PostgreSQL thành PROCESSING\_FAILED với một mã lỗi hoặc thông điệp mô tả nguyên nhân.  
        4. Worker có thể gửi một message vào một dead-letter queue (DLQ) của Kafka để phân tích sau, hoặc gửi thông báo cho quản trị viên.  
      * **E2: Lỗi trong quá trình thực thi FFmpeg:**  
        1. Nếu tiến trình FFmpeg gặp lỗi trong quá trình chuyển đổi (ví dụ: tệp MP4 gốc bị hỏng, thiếu codec cần thiết trên worker, lỗi cú pháp lệnh FFmpeg, hết tài nguyên trên worker).  
        2. Worker cần bắt được mã thoát (exit code) và output (stdout/stderr) của FFmpeg để ghi log chi tiết.  
        3. Worker cập nhật trạng thái video trong PostgreSQL thành PROCESSING\_FAILED với thông tin lỗi từ FFmpeg.  
        4. Xử lý tương tự E1 về DLQ hoặc thông báo.  
      * **E3: Lỗi upload các tệp HLS lên MinIO:**  
        1. Nếu spring-worker gặp lỗi khi tải một hoặc nhiều tệp segment .ts hoặc playlist .m3u8 lên MinIO bucket stream.  
        2. Worker ghi log lỗi.  
        3. Worker cập nhật trạng thái video trong PostgreSQL thành PROCESSING\_FAILED.  
        4. Cần có cơ chế dọn dẹp (cleanup) các tệp đã được tải lên một phần vào MinIO để tránh lãng phí dung lượng và dữ liệu không nhất quán. Điều này có thể thực hiện bằng cách xóa các tệp đã tải lên cho videoId đó trong bucket stream.  
      * **E4: Worker bị lỗi hoặc dừng đột ngột giữa chừng:**  
        1. Nếu instance của spring-worker đang xử lý một video bị lỗi và dừng đột ngột (ví dụ: do lỗi phần cứng, lỗi phần mềm không bắt được, hoặc bị deploy phiên bản mới).  
        2. Kafka với cơ chế consumer group và offset commit sẽ đảm bảo rằng message NEW\_VIDEO\_UPLOADED đó sẽ được một instance worker khác (hoặc instance đó sau khi khởi động lại) nhận lại và xử lý lại từ đầu (nếu offset chưa được commit).  
        3. Để tránh xử lý trùng lặp gây ra vấn đề (ví dụ: tạo nhiều bộ HLS cho cùng một video), logic xử lý của worker cần có tính idempotent (xử lý nhiều lần cùng một message vẫn cho ra kết quả như xử lý một lần). Điều này có thể đạt được bằng cách kiểm tra trạng thái video trước khi bắt đầu xử lý, hoặc sử dụng một cơ chế khóa (distributed lock) nếu cần.  
        4. Nếu một message liên tục gây lỗi cho worker, nó có thể được chuyển vào một dead-letter queue (DLQ) sau một số lần thử lại nhất định để tránh làm tắc nghẽn hàng đợi chính.  
    * **Công nghệ liên quan:** Apache Kafka (cho hàng đợi thông điệp và xử lý sự kiện) , Debezium (nếu được sử dụng để capture thay đổi từ DB và trigger Kafka event, mặc dù trong kịch bản này, spring-video service tự publish sự kiện lên Kafka sau khi ghi vào DB), FFmpeg (công cụ chính cho việc chuyển đổi video) , MinIO (để lưu trữ tệp MP4 gốc và các tệp HLS đã xử lý).  
    * Kiến trúc sử dụng hàng đợi thông điệp (Kafka) và một service worker riêng biệt (spring-worker) cho quá trình transcoding là một thiết kế phổ biến và hiệu quả trong các hệ thống xử lý video. Nó giúp hệ thống có khả năng chịu lỗi cao hơn: nếu một worker gặp sự cố, thông điệp yêu cầu transcoding vẫn còn trong Kafka và có thể được xử lý bởi một worker khác hoặc bởi chính worker đó sau khi khởi động lại. Thiết kế này cũng tăng khả năng mở rộng: khi lượng video cần xử lý tăng lên, chỉ cần tăng số lượng instance của spring-worker mà không ảnh hưởng đến các thành phần khác của hệ thống. Quan trọng nhất, nó không làm block luồng request chính của người dùng khi họ tải video lên, vì việc transcoding là một tác vụ nặng và tốn thời gian, nên được thực hiện bất đồng bộ. FFmpeg là công cụ cực kỳ mạnh mẽ và linh hoạt cho việc transcoding, hỗ trợ vô số định dạng, codec và các tùy chọn cấu hình, cho phép tạo ra các luồng HLS ABR chất lượng cao, đáp ứng yêu cầu phát lại trên nhiều thiết bị và điều kiện mạng khác nhau.  
  * **3.1.3. UC3: Người dùng xem lại video đã upload (VOD Playback)**  
    * **Mô tả:** Người dùng cuối, thông qua một giao diện web hoặc ứng dụng di động có tích hợp trình phát video HLS, chọn một video đã được hệ thống xử lý thành công (tức là đã chuyển đổi sang HLS ABR và sẵn sàng) và bắt đầu xem nội dung video đó.  
    * **Điều kiện tiên quyết:**  
      * Video đã hoàn tất quá trình xử lý (UC2) và có trạng thái là COMPLETED hoặc AVAILABLE trong cơ sở dữ liệu PostgreSQL.  
      * Đường dẫn đến tệp master playlist HLS (ví dụ: index.m3u8) của video đó đã được lưu trữ chính xác trong trường path của bảng videos.  
      * Người dùng có quyền truy cập để xem video đó (có thể có các kiểm tra phân quyền dựa trên trạng thái public/private của video hoặc quyền sở hữu).  
    * **Luồng chính (Happy Path):**  
      1. Người dùng tương tác với giao diện (ví dụ: nhấp vào một thumbnail video trong danh sách video) để yêu cầu xem một video cụ thể.  
      2. Frontend (ứng dụng web hoặc di động) gửi một yêu cầu đến Backend API (service spring-video) để lấy thông tin chi tiết của video đó, bao gồm cả đường dẫn đến luồng HLS. Ví dụ: GET /api/video/{id}/profile, trong đó {id} là UUID của video.  
      3. Backend API (spring-video) nhận yêu cầu, xác thực người dùng và kiểm tra quyền truy cập video (nếu cần). Sau đó, nó truy vấn cơ sở dữ liệu PostgreSQL (bảng videos) để lấy thông tin của video với {id} tương ứng. Thông tin quan trọng cần lấy là trường path, nơi lưu trữ đường dẫn đến master playlist HLS của video (ví dụ: /{video\_id}/index.m3u8).  
      4. Backend API trả về thông tin chi tiết của video (bao gồm tiêu đề, mô tả, thời lượng, và URL đầy đủ hoặc tương đối để truy cập master playlist HLS) cho Frontend. Ví dụ, URL có thể là https://your-api-domain.com/api/video/{id}/index.m3u8.  
      5. Frontend nhận được thông tin và khởi tạo một trình phát video HLS (ví dụ: sử dụng thư viện HLS.js, Video.js, Shaka Player, hoặc trình phát gốc của hệ điều hành nếu hỗ trợ). Trình phát được cấu hình với URL của master playlist HLS đã nhận được.  
      6. Trình phát HLS (client-side) gửi một yêu cầu HTTP GET đến URL của master playlist, ví dụ: GET https://your-api-domain.com/api/video/{id}/index.m3u8.  
      7. Backend API (spring-video) nhận yêu cầu này. Nó sẽ thực hiện các bước sau:  
         * Xác thực yêu cầu (ví dụ: kiểm tra token, session của người dùng).  
         * Xác định đường dẫn thực tế của tệp index.m3u8 trong MinIO bucket stream dựa trên {id} (ví dụ: stream/{video\_id}/index.m3u8).  
         * Lấy nội dung của tệp index.m3u8 từ MinIO. Điều này có thể được thực hiện bằng cách Backend API tự fetch từ MinIO hoặc, nếu cấu trúc cho phép, tạo một presigned URL có thời gian sống ngắn để client tự fetch (tuy nhiên, việc proxy qua backend giúp kiểm soát tốt hơn). Trong trường hợp này, giả định Backend API sẽ proxy request.  
         * Trả về nội dung của tệp index.m3u8 với Content-Type: application/vnd.apple.mpegurl (hoặc audio/mpegurl) cho trình phát HLS.  
      8. Trình phát HLS phân tích nội dung của tệp master playlist (index.m3u8). Tệp này chứa thông tin về các luồng con (variant streams) với các mức chất lượng (bitrate, độ phân giải) khác nhau và URL đến các tệp media playlist tương ứng của chúng (ví dụ: 1080p.m3u8, 720p.m3u8).  
      9. Dựa trên điều kiện mạng hiện tại và khả năng của thiết bị, trình phát HLS chọn một luồng con phù hợp ban đầu và gửi yêu cầu HTTP GET để tải tệp media playlist của luồng đó (ví dụ: GET https://your-api-domain.com/api/video/{id}/1080p.m3u8). Backend API sẽ xử lý yêu cầu này tương tự như bước 7, lấy tệp media playlist từ MinIO và trả về.  
      10. Trình phát HLS phân tích tệp media playlist. Tệp này chứa danh sách các URL đến các tệp segment video/audio (thường là .ts) của luồng đó, cùng với thời lượng của mỗi segment.  
      11. Trình phát HLS bắt đầu gửi các yêu cầu HTTP GET tuần tự để tải các tệp segment .ts (ví dụ: GET https://your-api-domain.com/api/video/{id}/1080p/segment001.ts, segment002.ts,...). Backend API xử lý các yêu cầu này, lấy tệp segment từ MinIO bucket stream và trả về nội dung (với Content-Type: video/MP2T) cho trình phát.  
      12. Khi nhận đủ dữ liệu segment, trình phát HLS bắt đầu giải mã và hiển thị video cho người dùng.  
      13. Trong quá trình phát, trình phát HLS liên tục theo dõi băng thông mạng và hiệu suất phát. Nếu điều kiện mạng thay đổi, nó sẽ tự động chuyển sang một luồng con (variant stream) khác có chất lượng phù hợp hơn (cao hơn hoặc thấp hơn) bằng cách yêu cầu media playlist và các segment tương ứng của luồng mới đó. Đây chính là cơ chế Adaptive Bitrate Streaming (ABR).  
    * **Luồng phụ/Ngoại lệ:**  
      * **E1: Video chưa được xử lý xong hoặc xử lý lỗi:**  
        1. Nếu Frontend yêu cầu thông tin video (bước 2\) và Backend API trả về trạng thái là PROCESSING hoặc PENDING\_PROCESSING.  
        2. Frontend hiển thị thông báo cho người dùng, ví dụ: "Video đang được xử lý, vui lòng quay lại sau."  
        3. Nếu trạng thái là PROCESSING\_FAILED.  
        4. Frontend hiển thị thông báo lỗi, ví dụ: "Không thể phát video này do lỗi xử lý."  
      * **E2: Lỗi truy cập tệp HLS từ MinIO:**  
        1. Nếu Backend API (spring-video) gặp lỗi khi cố gắng truy xuất tệp master playlist, media playlist, hoặc segment từ MinIO (ví dụ: tệp không tồn tại, lỗi kết nối MinIO, lỗi phân quyền của service account mà spring-video sử dụng để truy cập MinIO).  
        2. Backend API ghi log lỗi và trả về mã lỗi HTTP phù hợp (ví dụ: 404 Not Found nếu tệp không có, 500 Internal Server Error hoặc 503 Service Unavailable cho các lỗi khác) cho trình phát HLS.  
        3. Trình phát HLS sẽ hiển thị lỗi cho người dùng.  
      * **E3: URL HLS không hợp lệ hoặc tệp manifest bị lỗi:**  
        1. Nếu URL đến master playlist mà Frontend cung cấp cho trình phát HLS không hợp lệ, hoặc nội dung của tệp manifest (master hoặc media) bị lỗi cú pháp.  
        2. Trình phát HLS không thể tải hoặc phân tích playlist và sẽ báo lỗi.  
      * **E4: Vấn đề về mạng của người xem:**  
        1. Nếu kết nối mạng của người xem không ổn định hoặc băng thông quá thấp.  
        2. Trình phát HLS sẽ cố gắng chuyển xuống các luồng chất lượng thấp hơn. Nếu vẫn không đủ băng thông, video có thể bị dừng hình (buffering) hoặc chất lượng rất kém.  
    * **Yêu cầu đặc tả API liên quan:**  
      * GET /api/video/{id}/profile: Lấy thông tin chi tiết video, bao gồm URL master HLS.  
      * GET /api/video/{id}/index.m3u8: Phục vụ tệp master HLS playlist.  
      * GET /api/video/{id}/{rendition\_name}.m3u8: Phục vụ tệp media HLS playlist cho một rendition cụ thể.  
      * GET /api/video/{id}/{rendition\_name}/{segment\_filename}.ts: Phục vụ tệp HLS segment.  
    * Việc Backend API (spring-video) đóng vai trò là một proxy cho các yêu cầu tệp HLS (master playlist, media playlists, và các segment.ts) từ trình phát của client đến MinIO mang lại một số lợi ích quan trọng. Thứ nhất, nó cho phép hệ thống thực thi các logic kiểm soát truy cập tập trung tại backend trước khi phục vụ bất kỳ nội dung nào; ví dụ, kiểm tra xem người dùng có quyền xem video này không, video có phải là public hay private. Thứ hai, nó giúp che giấu hoàn toàn cấu trúc lưu trữ nội bộ của MinIO (tên bucket, đường dẫn cụ thể) khỏi phía client, tăng cường bảo mật. Thứ ba, backend có thể thực hiện các logic bổ sung như ghi log chi tiết về lượt xem từng segment, phục vụ cho mục đích phân tích hành vi người dùng hoặc gỡ lỗi. Mặc dù cách tiếp cận này có thể làm tăng tải cho Backend API (do phải xử lý tất cả các request segment), lợi ích về kiểm soát và bảo mật thường được ưu tiên. Để giảm tải, có thể xem xét các cơ chế caching ở tầng API hoặc sử dụng một Content Delivery Network (CDN) đặt phía trước các endpoint API này. Một giải pháp thay thế là Backend API tạo ra các presigned URL có thời gian sống ngắn cho từng tệp HLS trên MinIO và trả về cho client, client sẽ dùng các URL này để truy cập trực tiếp MinIO. Tuy nhiên, việc quản lý và làm mới presigned URL cho hàng trăm segment trong một video dài hoặc một luồng live có thể phức tạp. Yêu cầu của người dùng là "Cung cấp API phát lại video (HLS)", điều này ngụ ý rằng Backend API nên có vai trò tích cực trong quá trình phát lại, không chỉ đơn thuần là trả về một đường dẫn tĩnh.  
* **3.2. Chức năng Livestream Trực tiếp**  
  * Chức năng này cho phép người dùng (Streamer) phát video và âm thanh trực tiếp từ thiết bị của họ lên hệ thống. Hệ thống sẽ nhận luồng đầu vào này, xử lý (bao gồm chuyển mã sang các định dạng streaming thích ứng như HLS và/hoặc DASH) để nhiều người xem có thể truy cập đồng thời, và tùy chọn cung cấp khả năng lưu trữ lại toàn bộ luồng phát để xem lại sau dưới dạng Video on Demand (VOD).  
  * **3.2.1. UC4: Người dùng (Streamer) khởi tạo và gửi luồng video (RTMP/WebRTC)**  
    * **Mô tả:** Streamer, sau khi được xác thực và cấp quyền, sử dụng phần mềm streaming chuyên dụng (như OBS, vMix), thiết bị encoder phần cứng, hoặc trực tiếp từ trình duyệt web (thông qua WebRTC) để thiết lập kết nối và gửi luồng video/âm thanh trực tiếp đến một điểm ingest (ingest point) được chỉ định của hệ thống.  
    * **Điều kiện tiên quyết:**  
      * Streamer đã đăng nhập thành công vào hệ thống bằng tài khoản hợp lệ.  
      * Tài khoản của Streamer có quyền thực hiện chức năng livestream.  
      * Streamer đã được cung cấp các thông tin cần thiết để kết nối đến điểm ingest của hệ thống. Đối với RTMP, đó là RTMP URL và Stream Key. Đối với WebRTC, đó là các thông tin signaling (ví dụ: WHIP endpoint URL).  
      * Streamer có kết nối mạng ổn định với băng thông upload đủ lớn cho chất lượng luồng mong muốn.  
    * **Luồng chính (Happy Path):**  
      1. Streamer, thông qua giao diện người dùng (Frontend), yêu cầu bắt đầu một phiên livestream mới. Hành động này kích hoạt một request đến Backend API, ví dụ: POST /api/live/start. Request này có thể tùy chọn chứa các thông tin ban đầu cho phiên livestream như "Tiêu đề" (title).  
      2. Backend API (service spring-video) nhận yêu cầu, xác thực Streamer và quyền của họ. Nếu hợp lệ, Backend API sẽ:  
         * Tạo một bản ghi mới trong bảng livestreams của PostgreSQL. Bản ghi này sẽ có một id (UUID) duy nhất, ownerId (ID của Streamer), title (nếu có), và trạng thái ban đầu là INITIALIZING hoặc WAITING\_FOR\_STREAM.  
         * Sinh ra một streamKey ngẫu nhiên, duy nhất và khó đoán. streamKey này rất quan trọng để xác định và bảo vệ luồng phát của Streamer.  
         * Liên kết streamKey này với bản ghi livestream vừa tạo.  
      3. Backend API trả về một phản hồi thành công cho Frontend. Phản hồi này chứa streamKey vừa sinh và thông tin điểm ingest:  
         * Đối với RTMP: RTMP URL đầy đủ, ví dụ: rtmp://your-rtmp-server-domain.com/live/{stream-key}. Phần live là tên application trên RTMP server, và {stream-key} là khóa bí mật.  
         * Đối với WebRTC (sử dụng WHIP ): WHIP endpoint URL, ví dụ: https://your-media-server-domain.com/whip/endpoint/{stream-key} hoặc một URL mà client có thể POST SDP offer đến. Stream key có thể là một phần của URL hoặc được truyền qua một cơ chế khác như Bearer token trong header.  
      4. **Luồng xử lý cho RTMP Ingest:**  
         * Streamer nhận được RTMP URL và streamKey từ Frontend.  
         * Streamer cấu hình phần mềm streaming của họ (ví dụ: OBS Studio, vMix, XSplit) với các thông tin này. Cụ thể, họ sẽ nhập RTMP URL vào mục "Server" hoặc "URL", và streamKey vào mục "Stream Key" hoặc "Play Path".  
         * Streamer bắt đầu phát (Start Streaming) từ phần mềm của họ. Phần mềm sẽ mã hóa video/âm thanh từ camera/microphone và gửi luồng dữ liệu RTMP đến RTMP Server được chỉ định (ví dụ: Nginx-RTMP module, SRS \- Simple Realtime Server).  
      5. **Luồng xử lý cho WebRTC Ingest (sử dụng WHIP làm ví dụ):**  
         * Frontend của Streamer (chạy trong trình duyệt) sử dụng thông tin WHIP endpoint URL (và streamKey nếu cần như một Bearer token) nhận được từ Backend API.  
         * Trình duyệt thu video/âm thanh từ camera/microphone của Streamer, tạo một SDP offer mô tả luồng media.  
         * Trình duyệt gửi một HTTP POST request đến WHIP endpoint URL của Media Server (ví dụ: MediaSoup, Janus, hoặc SRS nếu có hỗ trợ WHIP). Request này có Content-Type: application/sdp và body chứa SDP offer. Header Authorization: Bearer {stream-key} có thể được sử dụng để xác thực.  
         * Media Server nhận SDP offer, xác thực (nếu có streamKey), và nếu hợp lệ, tạo một SDP answer.  
         * Media Server trả về HTTP 201 Created response với Content-Type: application/sdp, body chứa SDP answer, và một header Location trỏ đến URL của session WHIP vừa tạo. Client có thể dùng URL này để xóa session sau này bằng HTTP DELETE.  
         * Trình duyệt nhận SDP answer, hoàn tất quá trình thiết lập kết nối WebRTC (ICE, DTLS). Luồng media (RTP packets) bắt đầu được gửi từ trình duyệt đến Media Server.  
      6. **Xác thực và bắt đầu luồng trên Media Server:**  
         * **Đối với RTMP Server (Nginx-RTMP, SRS):** Khi nhận được kết nối RTMP mới trên application live với một streamKey, RTMP server cần xác thực streamKey này. Điều này thường được thực hiện thông qua một cơ chế callback gọi là on\_publish. RTMP server sẽ gửi một HTTP request (thường là POST) đến một endpoint được cấu hình trên Backend API (spring-video), mang theo streamKey và các thông tin khác của luồng. Backend API sẽ kiểm tra xem streamKey có hợp lệ và có đang ở trạng thái INITIALIZING hay không. Nếu hợp lệ, Backend API trả về HTTP 2xx (ví dụ 200 OK), và RTMP server cho phép luồng được publish. Nếu không hợp lệ, Backend API trả về HTTP 4xx (ví dụ 403 Forbidden), và RTMP server từ chối kết nối.  
         * **Đối với Media Server (WebRTC/WHIP):** Việc xác thực streamKey có thể đã được thực hiện khi Media Server nhận HTTP POST request chứa SDP offer (ví dụ, kiểm tra Bearer token).  
      7. Sau khi luồng được chấp nhận và bắt đầu truyền dữ liệu, RTMP Server/Media Server có thể (tùy chọn, nhưng khuyến khích) gửi một thông báo (webhook, Kafka event) đến Backend API (spring-video) để xác nhận rằng luồng với streamKey cụ thể đã thực sự bắt đầu nhận dữ liệu.  
      8. Backend API (spring-video) nhận được thông báo này (hoặc sau khi on\_publish thành công), sẽ cập nhật trạng thái của phiên livestream trong bảng livestreams từ INITIALIZING thành STREAMING và ghi lại thời điểm bắt đầu thực tế (startAt).  
    * **Luồng phụ/Ngoại lệ:**  
      * **E1: Lỗi tạo phiên livestream ở Backend:** Nếu Backend API (spring-video) gặp lỗi khi cố gắng tạo bản ghi mới trong bảng livestreams (ví dụ: lỗi kết nối DB, streamKey không unique do lỗi logic sinh key), nó sẽ trả về mã lỗi HTTP (ví dụ: 500 Internal Server Error) cho Frontend, và Streamer không thể bắt đầu phiên.  
      * **E2: Stream Key không hợp lệ hoặc đã được sử dụng:**  
        1. Nếu Streamer cố gắng sử dụng một streamKey không tồn tại, đã hết hạn, hoặc đang được sử dụng bởi một luồng khác.  
        2. Cơ chế on\_publish của RTMP server (hoặc logic xác thực của WHIP endpoint) sẽ gọi đến Backend API. Backend API kiểm tra và phát hiện streamKey không hợp lệ.  
        3. Backend API trả về mã lỗi (ví dụ: 403 Forbidden) cho RTMP server/Media Server.  
        4. RTMP server/Media Server từ chối kết nối từ phần mềm streaming của Streamer, và phần mềm đó sẽ hiển thị lỗi.  
      * **E3: Lỗi thiết lập kết nối WebRTC:** Trong trường hợp WebRTC ingest, nếu có vấn đề với việc trao đổi SDP, thiết lập ICE (ví dụ: do firewall, NAT traversal không thành công), hoặc DTLS handshake, kết nối WebRTC sẽ không được thiết lập. Trình duyệt của Streamer nên hiển thị thông báo lỗi.  
      * **E4: Mất kết nối từ Streamer trong quá trình phát:**  
        1. Nếu kết nối mạng của Streamer bị gián đoạn, hoặc Streamer chủ động dừng phát từ phần mềm/trình duyệt mà không gọi API /api/live/stop.  
        2. RTMP Server/Media Server sẽ phát hiện việc mất luồng dữ liệu đầu vào (ví dụ: RTMP connection closed, WebRTC transport disconnected).  
        3. RTMP Server/Media Server có thể được cấu hình để gọi một callback on\_publish\_done hoặc on\_disconnect đến Backend API.  
        4. Backend API nhận được thông báo này, sẽ cập nhật trạng thái của phiên livestream trong bảng livestreams thành INTERRUPTED hoặc ENDED (tùy theo logic nghiệp vụ, có thể có một khoảng thời gian chờ để Streamer kết nối lại). Nếu không có callback, Backend API có thể cần một cơ chế timeout để tự động chuyển trạng thái nếu không nhận được dữ liệu sau một khoảng thời gian nhất định.  
    * **Yêu cầu đặc tả API liên quan:**  
      * POST /api/live/start: Endpoint để Streamer khởi tạo phiên và nhận thông tin ingest.  
      * (Nội bộ) Endpoint cho on\_publish (ví dụ: POST /api/internal/live/auth\_publish): Được RTMP server gọi để xác thực streamKey.  
      * (Nội bộ) Endpoint cho on\_publish\_done (ví dụ: POST /api/internal/live/publish\_done): Được RTMP server gọi khi luồng kết thúc.  
    * **Công nghệ liên quan:** Giao thức RTMP , Giao thức WebRTC , Giao thức WHIP. Các Media Server như Nginx-RTMP module , SRS , MediaSoup.  
    * Việc sử dụng streamKey là một phương pháp phổ biến và hiệu quả để bảo vệ các điểm ingest RTMP. Callback on\_publish của Nginx-RTMP là một cơ chế linh hoạt cho phép tích hợp logic xác thực tùy chỉnh từ backend. Đối với WebRTC, WHIP đang nổi lên như một tiêu chuẩn giúp đơn giản hóa quá trình ingest, và việc xác thực có thể được tích hợp vào HTTP request của WHIP.  
  * **3.2.2. UC5: Hệ thống nhận và chuyển đổi luồng livestream sang HLS/DASH ABR**  
    * **Mô tả:** Sau khi RTMP Server hoặc Media Server (ví dụ: MediaSoup) nhận được thành công luồng video/âm thanh trực tiếp từ Streamer (qua RTMP hoặc WebRTC), hệ thống sẽ tiến hành xử lý và chuyển đổi (transcode) luồng này thành các định dạng streaming thích ứng như HTTP Live Streaming (HLS) và/hoặc Dynamic Adaptive Streaming over HTTP (DASH), với nhiều mức chất lượng khác nhau (Adaptive Bitrate Streaming \- ABR). Các segment và manifest của HLS/DASH sẽ được tạo ra liên tục để phục vụ người xem.  
    * **Điều kiện tiên quyết:**  
      * Luồng RTMP hoặc WebRTC từ Streamer đang được gửi ổn định đến điểm ingest của RTMP Server/Media Server.  
      * Phiên livestream đã được xác thực thành công (ví dụ, thông qua streamKey và callback on\_publish) và được Backend API đánh dấu là đang ở trạng thái STREAMING.  
      * Cấu hình transcoding cho ABR (các độ phân giải, bitrate mong muốn) đã được định nghĩa cho hệ thống.  
    * **Luồng chính (Happy Path):**  
      1. **Trường hợp sử dụng RTMP Server (ví dụ: Nginx-RTMP hoặc SRS):**  
         * RTMP Server nhận luồng RTMP đầu vào từ Streamer.  
         * Dựa trên cấu hình, RTMP Server sẽ sử dụng một trong hai cách để tạo HLS/DASH ABR:  
           * **Sử dụng module tích hợp (nếu có và hỗ trợ ABR mạnh mẽ):** Một số phiên bản hoặc module của Nginx (ví dụ: ngx\_http\_hls\_module ) hoặc SRS (HLS/DASH support ) có thể có khả năng tự động tạo HLS/DASH từ một luồng RTMP đầu vào. Tuy nhiên, việc tạo ABR (nhiều bitrate) thường phức tạp hơn và có thể không được hỗ trợ đầy đủ bởi các module cơ bản. SRS có thể hỗ trợ cấu hình transcoding sử dụng FFmpeg được gọi nội bộ (exec) để tạo ra nhiều luồng RTMP với các bitrate khác nhau, sau đó mỗi luồng RTMP này có thể được SRS chuyển thành HLS/DASH riêng lẻ.  
           * **Thực thi tiến trình FFmpeg bên ngoài (phổ biến và linh hoạt hơn cho ABR):** RTMP Server (Nginx-RTMP hoặc SRS) được cấu hình để khi nhận một luồng RTMP (ví dụ, rtmp://localhost/live/{stream-key}), nó sẽ thực thi một hoặc nhiều lệnh FFmpeg. Lệnh FFmpeg này sẽ:  
             * Lấy luồng RTMP đầu vào.  
             * Transcode luồng đó thành nhiều video rendition (ví dụ: 720p@2.5Mbps, 480p@1Mbps, 360p@500kbps) và một audio rendition (ví dụ: AAC 128kbps).  
             * Xuất ra các segment và manifest HLS ABR. FFmpeg có khả năng tạo master playlist HLS (.m3u8) và các media playlist cho từng rendition, cùng với các tệp segment (.ts).  
             * Tương tự, FFmpeg có thể xuất ra các segment và manifest DASH ABR (.mpd và .m4s hoặc .ts).  
             * **Ví dụ lệnh FFmpeg :**  
               `ffmpeg -i rtmp://localhost/live/{stream-key} \`  
               `-map 0:v:0 -map 0:a:0 -map 0:v:0 -map 0:a:0 -map 0:v:0 -map 0:a:0 \`  
               `-c:v libx264 -crf 23 -preset veryfast -g 48 -sc_threshold 0 \`  
               `-c:a aac -b:a 128k -ar 44100 \`  
               `-filter:v:0 scale=w=1280:h=720 -b:v:0 2500k -maxrate:v:0 2800k -bufsize:v:0 5000k \`  
               `-filter:v:1 scale=w=854:h=480 -b:v:1 1000k -maxrate:v:1 1200k -bufsize:v:1 2000k \`  
               `-filter:v:2 scale=w=640:h=360 -b:v:2 500k -maxrate:v:2 600k -bufsize:v:2 1000k \`  
               `-var_stream_map "v:0,a:0,name:720p v:1,a:0,name:480p v:2,a:0,name:360p" \`  
               `-hls_time 4 -hls_list_size 6 -hls_flags delete_segments+independent_segments \`  
               `-hls_segment_filename "/mnt/hls_output/{stream-key}/%v/segment%03d.ts" \`  
               `-master_pl_name index.m3u8 \`  
               `-f hls "/mnt/hls_output/{stream-key}/index.m3u8"`   
               *(Lưu ý: Đường dẫn output /mnt/hls\_output/{stream-key}/ là nơi FFmpeg ghi các tệp HLS. Các tệp này sau đó cần được phục vụ bởi một web server hoặc upload lên MinIO nếu muốn lưu trữ lâu dài hoặc phân phối qua CDN).*  
             * **Ví dụ lệnh FFmpeg :**  
               `ffmpeg -re -i rtmp://localhost/live/{stream-key} \`  
               `-map 0:v:0 -map 0:a:0 -map 0:v:0 -map 0:a:0 -map 0:v:0 -map 0:a:0 \`  
               `-c:v libx264 -preset veryfast -g 48 -sc_threshold 0 \`  
               `-c:a aac -b:a 128k -ar 44100 \`  
               `-b:v:0 2500k -s:v:0 1280x720 \`  
               `-b:v:1 1000k -s:v:1 854x480 \`  
               `-b:v:2 500k -s:v:2 640x360 \`  
               `-adaptation_sets "id=0,streams=v id=1,streams=a" \`  
               `-use_timeline 1 -use_template 1 -window_size 5 -streaming 1 -remove_at_exit 1 \`  
               `-f dash "/mnt/dash_output/{stream-key}/index.mpd"`  
               *(Lưu ý: Tương tự HLS, các tệp DASH được FFmpeg ghi vào /mnt/dash\_output/{stream-key}/ và cần được phục vụ bởi web server hoặc upload lên MinIO).*  
         * Các tệp segment HLS/DASH (.ts, .m4s, hoặc các định dạng segment khác) và các tệp manifest/playlist (.m3u8, .mpd) được tạo ra một cách liên tục và lưu trữ tạm thời trên hệ thống tệp của RTMP server (nơi FFmpeg chạy) hoặc có thể được cấu hình để FFmpeg ghi trực tiếp vào một vị trí có thể truy cập qua HTTP, hoặc thậm chí là upload trực tiếp lên MinIO (nếu FFmpeg được cấu hình với các tùy chọn output lên S3-compatible storage).  
      2. **Trường hợp sử dụng Media Server cho WebRTC (ví dụ: MediaSoup):**  
         * MediaSoup nhận luồng WebRTC từ Streamer. Bản thân MediaSoup là một Selective Forwarding Unit (SFU) và không thực hiện transcoding.  
         * Để tạo HLS/DASH ABR từ luồng WebRTC này, các track audio và video (dưới dạng các luồng RTP) từ MediaSoup cần được chuyển tiếp đến một hoặc nhiều tiến trình FFmpeg bên ngoài.  
         * MediaSoup sử dụng một PlainTransport để gửi các luồng RTP này đến các cổng UDP mà FFmpeg đang lắng nghe.  
         * Mỗi tiến trình FFmpeg sẽ nhận một hoặc nhiều luồng RTP, thực hiện transcoding thành một hoặc nhiều rendition ABR, và tạo ra các tệp HLS/DASH tương tự như mô tả ở bước 1\. Việc này đòi hỏi cấu hình SDP hoặc các tham số dòng lệnh phức tạp cho FFmpeg để nó có thể nhận và xử lý đúng các luồng RTP từ MediaSoup.  
      3. RTMP Server/Media Server (hoặc tiến trình FFmpeg) liên tục cập nhật các tệp manifest/playlist HLS/DASH khi có các segment mới được tạo ra. Đối với livestream, các manifest này sẽ được làm mới định kỳ để trỏ đến các segment mới nhất và loại bỏ các segment cũ (theo cấu hình hls\_list\_size hoặc window\_size).  
      4. Đường dẫn đến master manifest HLS (ví dụ: /live/{stream-key}/index.m3u8) và/hoặc DASH (ví dụ: /live/{stream-key}/index.mpd) cần được Backend API (spring-video) biết để có thể cung cấp cho người xem. Thông tin này có thể được lưu trong trường path của bảng livestreams trong PostgreSQL. Việc cập nhật đường dẫn này có thể xảyRra khi Backend API nhận được xác nhận rằng luồng đã bắt đầu được xử lý và các tệp manifest đầu tiên đã được tạo.  
    * **Luồng phụ/Ngoại lệ:**  
      * **E1: Lỗi FFmpeg/Transcoding:**  
        1. Nếu tiến trình FFmpeg (dù được gọi bởi RTMP Server hay nhận RTP từ MediaSoup) gặp lỗi trong quá trình transcoding (ví dụ: lỗi codec, hết tài nguyên, luồng đầu vào không hợp lệ).  
        2. RTMP Server/Media Server (hoặc service quản lý FFmpeg) cần phát hiện lỗi này (ví dụ, qua exit code của FFmpeg, log lỗi).  
        3. Hệ thống nên cố gắng khởi động lại tiến trình FFmpeg một số lần nhất định.  
        4. Nếu vẫn lỗi, hệ thống nên ngừng cung cấp HLS/DASH cho luồng đó, ghi log chi tiết, và có thể thông báo cho Backend API hoặc quản trị viên về sự cố. Trạng thái của phiên livestream trong DB có thể được cập nhật thành TRANSCODING\_FAILED.  
      * **E2: Lỗi lưu trữ segment/manifest:**  
        1. Nếu FFmpeg hoặc Media Server không thể ghi các tệp segment/manifest vào vị trí đã định (ví dụ: hết dung lượng đĩa, lỗi phân quyền ghi, lỗi kết nối MinIO nếu ghi trực tiếp).  
        2. Hệ thống ghi log lỗi và xử lý tương tự E1.  
      * **E3: Chất lượng luồng đầu vào (RTMP/WebRTC) kém hoặc không ổn định:**  
        1. Nếu luồng video/âm thanh từ Streamer bị gián đoạn, có bitrate quá thấp, hoặc chứa các lỗi nghiêm trọng.  
        2. Chất lượng của các luồng HLS/DASH đầu ra cũng sẽ bị ảnh hưởng tiêu cực (ví dụ: video bị đứng hình, vỡ hình, mất tiếng). FFmpeg có thể gặp lỗi hoặc tạo ra các segment không phát được.  
        3. Hệ thống nên có cơ chế giám sát chất lượng luồng đầu vào (nếu có thể) và ghi log các vấn đề này.  
    * **Công nghệ liên quan:** Nginx-RTMP module (đặc biệt là directive exec để gọi FFmpeg) , SRS (với khả năng exec ffmpeg hoặc các module HLS/DASH tích hợp) , MediaSoup (cho WebRTC ingest và chuyển tiếp RTP đến FFmpeg) , và công cụ FFmpeg (là trái tim của quá trình transcoding ABR).  
    * Việc lựa chọn giữa việc sử dụng module HLS/DASH tích hợp sẵn của media server (nếu có và đủ mạnh) và việc gọi FFmpeg bên ngoài cho quá trình transcoding ABR là một quyết định kiến trúc quan trọng. Module tích hợp có thể đơn giản hóa việc cấu hình ban đầu nhưng thường kém linh hoạt hơn trong việc tùy chỉnh các tham số transcoding chi tiết hoặc hỗ trợ các codec mới nhất so với việc sử dụng FFmpeg trực tiếp. FFmpeg, mặc dù mạnh mẽ, đòi hỏi việc quản lý các tiến trình FFmpeg (khởi tạo, theo dõi, khởi động lại khi lỗi) phức tạp hơn và có thể gây ra overhead về tài nguyên nếu không được tối ưu hóa. Tài liệu tóm tắt của người dùng đề cập đến việc "RTMP server... chuyển đổi sang HLS/DASH bằng FFmpeg hoặc module tích hợp", cho thấy sự nhận biết về hai phương án này. Quyết định cuối cùng sẽ phụ thuộc vào yêu cầu cụ thể về chất lượng, độ linh hoạt, khả năng mở rộng và độ phức tạp vận hành chấp nhận được. Nếu sử dụng FFmpeg bên ngoài, sự tương tác giữa Media Server, tiến trình FFmpeg, và Backend API (để cập nhật đường dẫn HLS/DASH) cần được thiết kế cẩn thận.  
  * **3.2.3. UC6: Người xem truy cập và xem livestream**  
    * **Mô tả:** Người xem, thông qua một trình duyệt web trên máy tính hoặc một ứng dụng di động có tích hợp trình phát video hỗ trợ HLS và/hoặc DASH, truy cập và xem một luồng livestream đang được phát trực tiếp trên hệ thống.  
    * **Điều kiện tiên quyết:**  
      * Phiên livestream tương ứng đang ở trạng thái STREAMING trong cơ sở dữ liệu.  
      * RTMP Server/Media Server (hoặc tiến trình FFmpeg) đang hoạt động ổn định, liên tục nhận luồng từ Streamer và tạo ra các tệp segment HLS/DASH cùng với việc cập nhật các tệp manifest/playlist.  
      * Người xem có được URL chính xác để truy cập vào tệp master manifest/playlist của luồng livestream (ví dụ: https://your-cdn-or-media-server.com/live/{stream-key}/index.m3u8 hoặc .../index.mpd).  
    * **Luồng chính (Happy Path):**  
      1. Người xem, thông qua giao diện người dùng (ví dụ: một trang danh sách các livestream đang hoạt động, hoặc một trang sự kiện cụ thể), chọn một luồng livestream để xem.  
      2. Frontend (ứng dụng web hoặc di động) gửi một yêu cầu đến Backend API (spring-video) để lấy thông tin chi tiết về phiên livestream đó, bao gồm URL để phát lại. Ví dụ: GET /api/live/{stream-key}/profile.  
      3. Backend API (spring-video) nhận yêu cầu, xác thực người dùng (nếu luồng không phải public) và truy vấn bảng livestreams trong PostgreSQL để lấy thông tin của phiên livestream với streamKey tương ứng. Thông tin quan trọng cần lấy là trường path (hoặc một trường tương tự) nơi lưu trữ URL công khai để truy cập master playlist HLS/DASH. URL này thường trỏ đến Media Server, một web server tĩnh phục vụ các file HLS/DASH, hoặc một CDN. Ví dụ: https://media.example.com/live/{stream-key}/index.m3u8.  
      4. Backend API trả về thông tin chi tiết của phiên livestream (bao gồm tiêu đề, mô tả, thời gian bắt đầu, và quan trọng nhất là hlsPlaybackUrl và/hoặc dashPlaybackUrl) cho Frontend.  
      5. Frontend nhận được thông tin và khởi tạo một trình phát video tương thích (ví dụ: HLS.js, Video.js, Shaka Player cho DASH, hoặc trình phát gốc của thiết bị). Trình phát được cấu hình với hlsPlaybackUrl (hoặc dashPlaybackUrl) đã nhận được.  
      6. Trình phát video (client-side) gửi một yêu cầu HTTP GET đến URL của master manifest/playlist (ví dụ: GET https://media.example.com/live/{stream-key}/index.m3u8).  
      7. Media Server (ví dụ: Nginx phục vụ các tệp HLS do module RTMP hoặc FFmpeg tạo ra, hoặc SRS phục vụ HLS/DASH) hoặc Web Server/CDN nhận yêu cầu này và trả về nội dung của tệp master manifest/playlist. Tệp này được cập nhật liên tục bởi server khi có segment mới.  
      8. Trình phát phân tích nội dung của master manifest. Nó chứa thông tin về các luồng con (variant streams) với các mức chất lượng (bitrate, độ phân giải) khác nhau và URL đến các tệp media playlist tương ứng của chúng.  
      9. Dựa trên điều kiện mạng hiện tại và khả năng của thiết bị, trình phát chọn một luồng con phù hợp ban đầu và gửi yêu cầu HTTP GET để tải tệp media playlist của luồng đó (ví dụ: GET https://media.example.com/live/{stream-key}/720p/index.m3u8).  
      10. Media Server/Web Server/CDN trả về nội dung của tệp media playlist. Tệp này chứa danh sách các URL đến các tệp segment video/audio (.ts cho HLS, .m4s cho DASH) của luồng đó, cùng với thời lượng và thứ tự của chúng. Do là livestream, danh sách này sẽ thay đổi theo thời gian (segment mới được thêm vào, segment cũ bị loại bỏ).  
      11. Trình phát bắt đầu gửi các yêu cầu HTTP GET tuần tự để tải các tệp segment.  
      12. Media Server/Web Server/CDN phục vụ các tệp segment này.  
      13. Khi nhận đủ dữ liệu segment, trình phát bắt đầu giải mã và hiển thị video trực tiếp cho người xem.  
      14. Trong quá trình phát, trình phát định kỳ làm mới (tải lại) các tệp media playlist (và có thể cả master playlist) để nhận thông tin về các segment mới nhất. Nó cũng liên tục theo dõi băng thông mạng và hiệu suất phát để tự động điều chỉnh chất lượng bằng cách chuyển sang một luồng con khác (ABR) nếu cần thiết, nhằm đảm bảo trải nghiệm xem liền mạch nhất có thể.  
    * **Luồng phụ/Ngoại lệ:**  
      * **E1: Phiên livestream đã kết thúc hoặc không tồn tại:**  
        1. Nếu Frontend yêu cầu thông tin phiên livestream (bước 2\) và Backend API trả về thông báo rằng phiên đó đã ENDED hoặc không tìm thấy streamKey.  
        2. Frontend hiển thị thông báo phù hợp cho người xem, ví dụ: "Buổi phát trực tiếp này đã kết thúc" hoặc "Không tìm thấy buổi phát trực tiếp."  
      * **E2: Lỗi tải tệp manifest hoặc segment:**  
        1. Nếu trình phát video không thể tải được tệp master manifest, media playlist, hoặc bất kỳ tệp segment nào từ Media Server/Web Server/CDN (ví dụ: do lỗi mạng, server không phản hồi, tệp không tồn tại, lỗi phân quyền truy cập từ CDN).  
        2. Trình phát video nên hiển thị một thông báo lỗi cho người xem (ví dụ: "Không thể tải video. Vui lòng thử lại.") và có thể cố gắng tải lại một vài lần.  
      * **E3: Độ trễ (Latency) cao bất thường:**  
        1. Nếu người xem gặp phải độ trễ rất lớn giữa hành động thực tế của Streamer và những gì họ thấy trên màn hình (vượt quá mức độ trễ dự kiến của HLS/DASH).  
        2. Nguyên nhân có thể do nhiều yếu tố: mạng của Streamer chậm, quá trình transcoding trên server bị chậm, mạng của Viewer chậm, hoặc cấu hình CDN không tối ưu.  
        3. Hệ thống nên được thiết kế để giảm thiểu độ trễ ở mọi khâu, nhưng người xem vẫn có thể gặp phải nếu có vấn đề ở phía họ hoặc trên đường truyền.  
      * **E4: Lỗi phía trình phát (Client-side Player Error):**  
        1. Trình phát video của người xem có thể gặp lỗi nội tại (ví dụ: không hỗ trợ codec, lỗi JavaScript).  
        2. Cần có cơ chế ghi log lỗi phía client (nếu có thể) để hỗ trợ gỡ lỗi.  
    * **Yêu cầu đặc tả API liên quan:**  
      * GET /api/live/{stream-key}/profile: Lấy thông tin chi tiết phiên livestream, bao gồm URL phát lại HLS/DASH.  
    * **Endpoint HLS/DASH cho Livestream (phục vụ bởi RTMP Server/Media Server hoặc Web Server/CDN, không phải spring-video):**  
      * GET /live/{stream-key}/index.m3u8 (hoặc đường dẫn tương tự do Media Server quy định)  
      * GET /live/{stream-key}/{rendition\_name\_or\_path}/index.m3u8  
      * GET /live/{stream-key}/{rendition\_name\_or\_path}/{segment\_filename}.ts  
      * Tương tự cho DASH: GET /live/{stream-key}/index.mpd, etc.  
    * Khác với trường hợp VOD (UC3) nơi spring-video có thể đóng vai trò proxy cho tất cả các request HLS, đối với livestream, việc để Media Server (Nginx-RTMP, SRS) hoặc một Web Server/CDN chuyên dụng trực tiếp phục vụ các tệp HLS/DASH là một lựa chọn kiến trúc hợp lý hơn. Điều này giúp giảm tải đáng kể cho service spring-video, vốn không được thiết kế để xử lý hàng ngàn request segment đồng thời từ nhiều người xem. spring-video chỉ cần cung cấp URL ban đầu (master manifest) cho client. Việc bảo mật các endpoint HLS/DASH này (ví dụ, bằng token trong URL, giới hạn IP, chữ ký số) sẽ được thực hiện ở tầng Media Server hoặc CDN. Điều này cũng cho phép tận dụng các cơ chế caching hiệu quả của CDN để phục vụ lượng lớn người xem ở các vị trí địa lý khác nhau.  
  * **3.2.4. UC7: (Tùy chọn) Hệ thống lưu trữ lại luồng livestream thành file VOD**  
    * **Mô tả:** Sau khi một phiên livestream kết thúc, hệ thống có thể được cấu hình để tự động lưu trữ lại toàn bộ nội dung của luồng phát đó thành một tệp video hoàn chỉnh (thường là định dạng MP4). Tệp này sau đó có thể được xử lý tiếp để người dùng có thể xem lại dưới dạng Video on Demand (VOD), tương tự như một video được upload thông thường.  
    * **Điều kiện tiên quyết:**  
      * Streamer đã gửi yêu cầu kết thúc phiên livestream của mình thông qua Backend API (ví dụ: POST /api/live/stop với streamKey tương ứng), hoặc Media Server đã phát hiện luồng input từ streamer bị ngắt.  
      * RTMP Server/Media Server (hoặc tiến trình FFmpeg nếu được sử dụng để xử lý luồng) phải được cấu hình để hỗ trợ tính năng ghi lại (recording/DVR) luồng trực tiếp. Ví dụ:  
        * Nginx-RTMP module có directive record (ví dụ: record all; record\_path /tmp/recordings; record\_suffix.flv;) để lưu luồng thành tệp FLV hoặc MP4.  
        * SRS (Simple Realtime Server) có tính năng DVR (Digital Video Recorder) cho phép ghi lại luồng.  
        * FFmpeg, nếu được sử dụng để nhận và/hoặc transcode luồng, cũng có thể được cấu hình để đồng thời ghi output vào một tệp MP4.  
      * Tính năng "Lưu lại stream thành file" được kích hoạt cho phiên livestream đó (có thể là một cài đặt mặc định hoặc tùy chọn của người dùng khi tạo phiên).  
    * **Luồng chính (Happy Path):**  
      1. Streamer, thông qua giao diện người dùng, yêu cầu kết thúc phiên livestream. Frontend gửi request POST /api/live/stop đến Backend API, kèm theo streamKey của phiên cần dừng.  
      2. Backend API (spring-video) nhận yêu cầu, xác thực streamKey và quyền của người dùng. Nếu hợp lệ, Backend API cập nhật trạng thái của phiên livestream trong bảng livestreams (PostgreSQL) thành ENDED và ghi lại thời điểm kết thúc thực tế (endAt).  
      3. Backend API có thể cần thông báo cho RTMP Server/Media Server để chính thức dừng việc nhận luồng và hoàn tất việc ghi tệp (nếu việc ghi được điều khiển hoặc cần tín hiệu từ API). Trong nhiều trường hợp, Media Server sẽ tự động dừng ghi khi luồng input từ streamer bị ngắt (ví dụ, khi streamer dừng phát từ OBS).  
      4. **Quá trình ghi tệp trên Media Server/FFmpeg:**  
         * **Nginx-RTMP:** Nếu directive record được cấu hình, Nginx-RTMP sẽ lưu toàn bộ luồng vào một tệp (ví dụ: /tmp/recordings/{stream-key}.flv hoặc .mp4). Khi luồng kết thúc (streamer dừng phát hoặc kết nối bị ngắt), Nginx-RTMP hoàn tất việc ghi tệp. Sau đó, directive on\_record\_done có thể được cấu hình để Nginx-RTMP gửi một HTTP POST request đến một endpoint của Backend API (spring-video hoặc một service worker chuyên dụng). Request này sẽ chứa thông tin về tệp đã ghi, bao gồm đường dẫn đến tệp trên server.  
         * **SRS:** Nếu tính năng DVR được kích hoạt, SRS sẽ ghi lại luồng theo cấu hình (ví dụ: lưu thành các segment FLV/MP4). Khi luồng kết thúc, SRS có thể cung cấp thông tin về các tệp đã ghi thông qua API của nó hoặc các callback tương tự.  
         * **FFmpeg (nếu dùng để xử lý chính):** Lệnh FFmpeg xử lý luồng livestream có thể được cấu hình với một output nữa để ghi toàn bộ nội dung vào một tệp MP4 duy nhất. Khi tiến trình FFmpeg này kết thúc (do luồng input dừng), tệp MP4 sẽ hoàn chỉnh.  
      5. **Xử lý tệp đã ghi bởi Backend/Worker:**  
         * Khi Backend API (spring-video hoặc một service worker) nhận được thông báo (ví dụ, từ on\_record\_done của Nginx-RTMP, hoặc bằng cách theo dõi trạng thái ENDED của livestream và tìm tệp ghi theo quy ước đặt tên/đường dẫn), nó sẽ thực hiện các bước sau:  
           * Xác định vị trí và tên của tệp video đã được Media Server/FFmpeg ghi lại (ví dụ: /tmp/recordings/{stream-key}.mp4).  
           * Tải (upload) tệp video này (đang ở định dạng MP4 hoặc FLV) lên MinIO bucket video, tương tự như cách xử lý một tệp MP4 được upload thông thường (UC1). Đường dẫn lưu trữ trong MinIO có thể là vod\_from\_live/{livestream\_id}/{original\_filename\_from\_live}.mp4.  
           * Sau khi upload thành công lên MinIO, tạo một bản ghi mới trong bảng videos của PostgreSQL. Bản ghi này sẽ có ownerId và title được lấy từ thông tin của phiên livestream tương ứng, trạng thái ban đầu là UPLOADED hoặc PENDING\_PROCESSING, và sourcePath trỏ đến tệp MP4 vừa upload lên MinIO bucket video. (Hoặc, có thể cập nhật bản ghi livestreams hiện tại để thêm một liên kết đến videoId của VOD này).  
           * Kích hoạt quy trình xử lý video VOD tiêu chuẩn (như mô tả trong UC2): publish một sự kiện NEW\_VIDEO\_UPLOADED (hoặc một sự kiện tương tự như NEW\_VOD\_FROM\_LIVE\_READY\_FOR\_PROCESSING) lên Kafka. Sự kiện này sẽ chứa videoId mới và sourcePathInMinIO của tệp MP4 vừa được lưu từ livestream.  
      6. Service spring-worker sẽ nhận sự kiện này và tiến hành chuyển đổi tệp MP4 (từ livestream đã ghi) sang định dạng HLS ABR, rồi lưu các tệp HLS vào MinIO bucket stream, cập nhật trạng thái video thành COMPLETED hoặc AVAILABLE, và lưu đường dẫn HLS vào bảng videos (giống hệt UC2).  
      7. Sau khi quá trình này hoàn tất, người dùng có thể truy cập và xem lại nội dung của buổi livestream đã kết thúc dưới dạng một Video on Demand (VOD) thông thường, thông qua các API và luồng phát lại đã được định nghĩa (UC3).  
    * **Luồng phụ/Ngoại lệ:**  
      * **E1: Lỗi trong quá trình Media Server/FFmpeg ghi tệp:** Nếu Media Server hoặc FFmpeg gặp lỗi và không thể ghi lại toàn bộ hoặc một phần luồng livestream (ví dụ: hết dung lượng đĩa, lỗi tiến trình). Hệ thống nên ghi log lỗi này. Việc tạo VOD từ livestream này có thể thất bại.  
      * **E2: Lỗi khi Backend/Worker upload tệp đã ghi lên MinIO:** Nếu script hoặc worker được kích hoạt sau on\_record\_done (hoặc cơ chế tương tự) không thể upload tệp video đã ghi lên MinIO bucket video (ví dụ: lỗi mạng, lỗi phân quyền MinIO). Hệ thống cần ghi log, có thể thử lại một số lần, và nếu vẫn thất bại, đánh dấu phiên livestream là "RECORDING\_UPLOAD\_FAILED".  
      * **E3: Lỗi trong quá trình xử lý VOD từ livestream (transcoding sang HLS):** Nếu tệp MP4 từ livestream đã được upload lên MinIO nhưng quá trình chuyển đổi sang HLS ABR bởi spring-worker gặp lỗi (tương tự các lỗi trong UC2). Trạng thái của video VOD tương ứng sẽ là PROCESSING\_FAILED.  
      * **E4: Không nhận được callback on\_record\_done hoặc callback thất bại:** Nếu Media Server không gửi được callback, hoặc Backend API không xử lý được callback, tệp ghi có thể bị "mồ côi". Cần có cơ chế dọn dẹp hoặc theo dõi định kỳ các tệp ghi chưa được xử lý.  
    * **Yêu cầu đặc tả API liên quan:**  
      * POST /api/live/stop: Endpoint để Streamer yêu cầu kết thúc phiên livestream.  
      * (Nội bộ) Endpoint cho on\_record\_done (ví dụ: POST /api/internal/live/record\_done): Được Media Server gọi khi hoàn tất việc ghi tệp.  
    * **Công nghệ liên quan:** Tính năng ghi hình của Nginx-RTMP (record, on\_record\_done) , tính năng DVR của SRS , khả năng ghi file của FFmpeg, MinIO (lưu trữ tệp MP4 gốc và HLS), Kafka (trigger worker xử lý VOD).  
    * Việc tích hợp tính năng lưu trữ livestream thành VOD đòi hỏi sự phối hợp chặt chẽ giữa Media Server (hoặc FFmpeg) và hệ thống Backend. Media Server chịu trách nhiệm chính trong việc ghi lại luồng gốc trong quá trình livestream. Sau khi livestream kết thúc và tệp ghi đã sẵn sàng, Backend cần một cơ chế đáng tin cậy (như callback on\_record\_done hoặc một tác vụ theo dõi định kỳ) để biết được điều này. Khi đó, Backend (hoặc một worker chuyên dụng) sẽ tiếp quản tệp ghi, chuyển nó vào hệ thống lưu trữ VOD (MinIO bucket video), và sau đó kích hoạt quy trình transcoding sang HLS ABR tương tự như một video được upload thông thường. Điều này đảm bảo rằng các VOD được tạo từ livestream sẽ có cùng chất lượng và định dạng phát lại như các VOD khác trong hệ thống, mang lại trải nghiệm nhất quán cho người xem.  
* **3.3. Quản lý Video và Livestream (Chung cho cả hai luồng)**  
  * Các chức năng trong mục này cung cấp cho người dùng (cả người dùng cuối và quản trị viên) khả năng xem lại thông tin, trạng thái của các video đã được tải lên và các phiên livestream đã hoặc đang diễn ra. Chúng là nền tảng cho việc xây dựng các giao diện quản lý nội dung.  
  * **3.3.1. UC8: Xem danh sách video đã upload**  
    * **Mô tả:** Người dùng cuối (chỉ xem video của mình) hoặc quản trị viên (xem tất cả video) truy cập vào một giao diện của hệ thống để xem danh sách các video đã được tải lên và đã hoàn tất quá trình xử lý (hoặc đang trong các trạng thái khác nhau).  
    * **API liên quan:** GET /api/video/list  
    * **Luồng chính:**  
      1. Người dùng (end-user hoặc admin) điều hướng đến trang/mục hiển thị danh sách video.  
      2. Frontend gửi một yêu cầu HTTP GET đến endpoint /api/video/list của Backend API (spring-video). Yêu cầu này có thể bao gồm các tham số truy vấn (query parameters) để hỗ trợ:  
         * **Phân trang:** Ví dụ, page=1\&size=20 (trang hiện tại và số lượng mục mỗi trang) để tránh tải toàn bộ danh sách video cùng một lúc, cải thiện hiệu năng.  
         * **Lọc (Filtering):**  
           * ownerId={uuid}: Nếu là người dùng cuối, chỉ lấy video của họ. Nếu là admin, có thể lọc theo ownerId cụ thể.  
           * status={status\_enum}: Lọc theo trạng thái xử lý của video (ví dụ: COMPLETED, PROCESSING, FAILED).  
           * title\_contains={search\_term}: Tìm kiếm video theo tiêu đề.  
         * **Sắp xếp (Sorting):** Ví dụ, sortBy=createAt\&order=desc (sắp xếp theo ngày tạo, mới nhất trước).  
      3. Backend API (spring-video) nhận yêu cầu, xác thực người dùng và quyền hạn. Dựa trên quyền của người dùng (admin hay end-user), API sẽ xây dựng câu truy vấn tương ứng đến bảng videos trong PostgreSQL. Ví dụ, nếu là end-user, API sẽ tự động thêm điều kiện lọc theo ownerId của người dùng đó.  
      4. Backend API thực thi truy vấn, lấy ra danh sách các bản ghi video phù hợp với các tiêu chí lọc và phân trang. Thông tin trả về cho mỗi video trong danh sách nên bao gồm các trường cơ bản như id (UUID của video), title (tiêu đề), status (trạng thái xử lý), createAt (ngày tạo), và có thể là thumbnailUrl (URL đến ảnh thumbnail của video, nếu có).  
      5. Backend API trả về một mảng JSON chứa danh sách video, cùng với thông tin phân trang (ví dụ: tổng số mục, tổng số trang) trong phần header hoặc trong body của response.  
      6. Frontend nhận dữ liệu và hiển thị danh sách video cho người dùng dưới dạng bảng hoặc lưới, kèm theo các điều khiển phân trang và lọc (nếu có).  
    * **Luồng phụ/Ngoại lệ:**  
      * **E1: Lỗi xác thực/phân quyền:** Nếu người dùng không hợp lệ hoặc không có quyền xem danh sách, API trả lỗi 401/403.  
      * **E2: Lỗi truy vấn DB:** Nếu có lỗi khi truy vấn PostgreSQL, API trả lỗi 500\.  
      * **E3: Tham số không hợp lệ:** Nếu các tham số phân trang/lọc không hợp lệ, API trả lỗi 400\.  
  * **3.3.2. UC9: Xem thông tin chi tiết video**  
    * **Mô tả:** Người dùng cuối hoặc quản trị viên chọn một video cụ thể từ danh sách (hoặc truy cập trực tiếp qua URL) để xem các thông tin chi tiết hơn về video đó.  
    * **API liên quan:** GET /api/video/{id}/profile  
    * **Luồng chính:**  
      1. Người dùng chọn một video từ danh sách hiển thị (UC8) hoặc truy cập vào trang chi tiết video.  
      2. Frontend gửi một yêu cầu HTTP GET đến endpoint /api/video/{id}/profile, trong đó {id} là UUID của video cần xem chi tiết.  
      3. Backend API (spring-video) nhận yêu cầu, xác thực người dùng và kiểm tra quyền truy cập đối với video cụ thể này (ví dụ, người dùng cuối chỉ được xem video của mình hoặc video public, admin có thể xem mọi video).  
      4. Backend API truy vấn bảng videos trong PostgreSQL để lấy đầy đủ thông tin của bản ghi video có id tương ứng.  
      5. Backend API trả về một đối tượng JSON chứa thông tin chi tiết của video, bao gồm: id, ownerId, title, description, status (trạng thái xử lý), eventResult (thông báo lỗi nếu xử lý thất bại), path (đường dẫn đến master playlist HLS nếu đã xử lý xong), createAt (ngày tạo), duration (thời lượng video), thumbnailUrl, và các metadata khác nếu có.  
      6. Frontend nhận dữ liệu và hiển thị các thông tin chi tiết này cho người dùng. Nếu video đã sẵn sàng để xem (status là COMPLETED hoặc AVAILABLE và path có giá trị), Frontend có thể hiển thị trình phát video (tham khảo UC3).  
    * **Luồng phụ/Ngoại lệ:**  
      * **E1: Video không tồn tại:** Nếu {id} không tương ứng với video nào trong DB, API trả lỗi 404 Not Found.  
      * **E2: Lỗi xác thực/phân quyền:** Tương tự UC8.  
      * **E3: Lỗi truy vấn DB:** Tương tự UC8.  
  * **3.3.3. UC10: Xem danh sách livestream đang/đã diễn ra**  
    * **Mô tả:** Người dùng cuối hoặc quản trị viên truy cập giao diện để xem danh sách các phiên livestream đang diễn ra (live now) hoặc các phiên đã kết thúc (archived/ended).  
    * **API liên quan:** GET /api/live/list  
    * **Luồng chính:**  
      1. Người dùng điều hướng đến trang/mục hiển thị danh sách livestream.  
      2. Frontend gửi một yêu cầu HTTP GET đến endpoint /api/live/list của Backend API (spring-video). Yêu cầu này có thể bao gồm các tham số truy vấn tương tự như UC8:  
         * **Phân trang:** page, size.  
         * **Lọc:**  
           * ownerId={uuid}: Lọc theo người tạo livestream.  
           * status={status\_enum}: Lọc theo trạng thái của phiên livestream (ví dụ: STREAMING, ENDED, INITIALIZING, FAILED).  
           * title\_contains={search\_term}: Tìm kiếm theo tiêu đề.  
         * **Sắp xếp:** Ví dụ, sortBy=startAt\&order=desc.  
      3. Backend API (spring-video) nhận yêu cầu, xác thực người dùng và quyền hạn.  
      4. Backend API truy vấn bảng livestreams trong PostgreSQL, áp dụng các điều kiện lọc và phân trang.  
      5. Backend API trả về một mảng JSON chứa danh sách các phiên livestream. Thông tin cho mỗi phiên có thể bao gồm: id (UUID của phiên), title, ownerId, status, startAt (thời điểm bắt đầu), endAt (thời điểm kết thúc, nếu đã kết thúc), streamKey (có thể chỉ trả về cho admin hoặc owner), thumbnailUrl (nếu có).  
      6. Frontend nhận dữ liệu và hiển thị danh sách cho người dùng, có thể phân biệt rõ các luồng đang live và các luồng đã kết thúc.  
    * **Luồng phụ/Ngoại lệ:** Tương tự UC8.  
  * **3.3.4. UC11: Xem thông tin chi tiết phiên livestream**  
    * **Mô tả:** Người dùng hoặc quản trị viên chọn một phiên livestream cụ thể từ danh sách để xem các thông tin chi tiết hơn về phiên đó.  
    * **API liên quan:** GET /api/live/{stream-key}/profile  
    * **Luồng chính:**  
      1. Người dùng chọn một phiên livestream từ danh sách (UC10).  
      2. Frontend gửi một yêu cầu HTTP GET đến endpoint /api/live/{stream-key}/profile, trong đó {stream-key} là khóa của phiên livestream.  
      3. Backend API (spring-video) nhận yêu cầu, xác thực người dùng và quyền truy cập.  
      4. Backend API truy vấn bảng livestreams trong PostgreSQL để lấy đầy đủ thông tin của phiên có streamKey tương ứng.  
      5. Backend API trả về một đối tượng JSON chứa thông tin chi tiết của phiên livestream, bao gồm: id, ownerId, streamKey, title, description (nếu có), status, hlsPlaybackUrl (URL để xem HLS nếu đang STREAMING hoặc đã được lưu thành VOD và xử lý xong), dashPlaybackUrl (tương tự cho DASH), startAt, endAt (nếu đã kết thúc), vodVideoId (ID của VOD tương ứng nếu đã được lưu và xử lý).  
      6. Frontend nhận dữ liệu và hiển thị các thông tin chi tiết. Nếu phiên đang STREAMING và có hlsPlaybackUrl, Frontend có thể hiển thị trình phát video để xem trực tiếp (tham khảo UC6). Nếu đã ENDED và có vodVideoId liên kết, có thể cung cấp tùy chọn xem lại VOD.  
    * **Luồng phụ/Ngoại lệ:**  
      * **E1: Phiên livestream không tồn tại:** Nếu {stream-key} không hợp lệ, API trả lỗi 404 Not Found.  
      * **E2: Lỗi xác thực/phân quyền:** Tương tự UC8.  
      * **E3: Lỗi truy vấn DB:** Tương tự UC8.  
* **3.4. Chức năng Quản trị Hệ thống**  
  * Các chức năng này dành riêng cho người dùng có vai trò Quản trị viên (Administrator) để quản lý và giám sát hoạt động của toàn bộ hệ thống.  
  * (Phần này sẽ được mở rộng nếu có yêu cầu chi tiết hơn từ người dùng về các tính năng quản trị cụ thể như quản lý người dùng, cấu hình hệ thống, xem log chi tiết, thống kê, v.v.)  
  * **3.4.1. UC12: Quản trị viên giám sát hệ thống**  
    * **Mô tả:** Quản trị viên truy cập vào một giao diện dashboard quản trị chuyên dụng để theo dõi trạng thái hoạt động của các thành phần chính trong hệ thống. Điều này bao gồm tình trạng của các service backend (ví dụ: spring-video, spring-worker), Media Server (Nginx-RTMP, SRS, MediaSoup), hệ thống lưu trữ MinIO, hàng đợi thông điệp Kafka, và cơ sở dữ liệu PostgreSQL. Dashboard cũng hiển thị các thông số quan trọng về tài nguyên hệ thống đang được sử dụng (CPU, RAM, disk I/O, network bandwidth), số lượng video đã upload, số lượng phiên livestream đang hoạt động, số lượng người dùng đang trực tuyến (nếu có thể theo dõi).  
    * **Yêu cầu:**  
      * Cần tích hợp hệ thống với các công cụ giám sát và thu thập metrics phổ biến như Prometheus.  
      * Các microservices (spring-video, spring-worker) cần expose các metrics cần thiết (ví dụ: số lượng request, thời gian xử lý, số lượng lỗi, độ sâu hàng đợi Kafka) ở định dạng mà Prometheus có thể scrape.  
      * Media Server, MinIO, Kafka, PostgreSQL cũng cần được cấu hình để expose metrics cho Prometheus (nếu chúng hỗ trợ, hoặc thông qua các exporter).  
      * Sử dụng công cụ trực quan hóa metrics như Grafana để xây dựng các dashboard giám sát từ dữ liệu của Prometheus.  
      * Cần có cơ chế cảnh báo (alerting) khi các chỉ số quan trọng vượt ngưỡng cho phép (ví dụ: CPU quá cao, hàng đợi Kafka đầy, service bị down).  
  * **3.4.2. UC13: Quản trị viên quản lý video/livestream**  
    * **Mô tả:** Quản trị viên có quyền truy cập và thực hiện các hành động quản lý đối với tất cả các video đã upload và các phiên livestream trên hệ thống. Các hành động này có thể bao gồm:  
      * Xem danh sách tất cả video/livestream với đầy đủ thông tin chi tiết.  
      * Tìm kiếm video/livestream theo nhiều tiêu chí (ID, tiêu đề, người tạo, trạng thái, ngày tạo).  
      * Xóa vĩnh viễn một video hoặc thông tin một phiên livestream (và các tệp liên quan trên MinIO) nếu cần thiết (ví dụ: nội dung vi phạm, yêu cầu từ người dùng).  
      * Thay đổi trạng thái của một video (ví dụ: từ COMPLETED sang HIDDEN để tạm thời ẩn khỏi người dùng cuối, hoặc FLAGGED\_FOR\_REVIEW).  
      * Đối với livestream đang diễn ra, quản trị viên có thể có quyền dừng (force stop) một phiên livestream nếu phát hiện vi phạm hoặc sự cố.  
    * **Yêu cầu API bổ sung (ví dụ):**  
      * DELETE /api/admin/video/{id}: Xóa video.  
      * PUT /api/admin/video/{id}/status: Cập nhật trạng thái video.  
      * POST /api/admin/live/{stream-key}/stop: Buộc dừng một phiên livestream.  
      * Các API này cần được bảo vệ nghiêm ngặt, chỉ cho phép truy cập bởi người dùng có vai trò admin.

Các API quản lý danh sách và chi tiết (UC8-UC11) là nền tảng cơ bản cho việc xây dựng các giao diện người dùng tương tác, cho phép người dùng theo dõi và quản lý nội dung của chính họ. Việc triển khai phân trang và các bộ lọc (filter) trong các API lấy danh sách là cực kỳ cần thiết để đảm bảo hiệu năng của hệ thống khi số lượng video và livestream tăng lên, đồng thời giúp người dùng tìm kiếm thông tin một cách nhanh chóng và hiệu quả. Thông tin trả về từ các API lấy chi tiết (profile) cần phải đầy đủ và chính xác để Frontend có thể hiển thị mọi thông tin cần thiết và hỗ trợ các hành động tiếp theo của người dùng (ví dụ, lấy đường dẫn HLS để khởi tạo trình phát video). Đối với các chức năng quản trị hệ thống (UC12, UC13), chúng thường yêu cầu quyền truy cập cao hơn và có thể được cung cấp thông qua một giao diện quản trị (admin panel) riêng biệt. Chức năng giám sát hệ thống (UC12) là tối quan trọng để đảm bảo tính ổn định và hiệu năng của toàn bộ nền tảng, cho phép quản trị viên phát hiện sớm các vấn đề và phản ứng kịp thời. Chức năng quản lý nội dung (UC13) cung cấp cho quản trị viên công cụ cần thiết để thực thi các chính sách nội dung, xử lý các trường hợp vi phạm, hoặc hỗ trợ người dùng khi có sự cố liên quan đến video hoặc livestream của họ.  
**4\. Yêu cầu Giao diện Ngoại vi**  
Phần này mô tả chi tiết các giao diện mà hệ thống sẽ tương tác, bao gồm giao diện người dùng (UI), các giao diện lập trình ứng dụng (API) với các hệ thống khác, và các giao diện với các thành phần phần cứng hoặc phần mềm liên quan. Việc định nghĩa rõ ràng các giao diện này là rất quan trọng để đảm bảo sự tương thích và tích hợp trơn tru giữa các thành phần.

* **4.1. Giao diện Người dùng (UI)**  
  * Hệ thống sẽ cung cấp một giao diện người dùng dựa trên web (Frontend) được thiết kế để phục vụ cả Người dùng cuối (Uploader, Streamer, Viewer) và Quản trị viên. Giao diện này cần đảm bảo tính trực quan, dễ sử dụng và cung cấp đầy đủ chức năng cho từng nhóm đối tượng.  
  * **Các màn hình chính dự kiến cho Người dùng cuối:**  
    * **Trang chủ/Dashboard Người dùng:** Sau khi đăng nhập, người dùng có thể thấy thông tin tóm tắt về hoạt động của mình, các video/livestream nổi bật hoặc được đề xuất, và các lối tắt đến các chức năng chính.  
    * **Màn hình Upload Video:**  
      * Cung cấp form cho phép người dùng chọn tệp MP4 từ máy tính hoặc kéo thả tệp.  
      * Hiển thị các trường để nhập metadata cho video: Tiêu đề (bắt buộc), Mô tả, Tags (tùy chọn).  
      * Hiển thị tiến trình tải lên (upload progress bar) và thông báo kết quả (thành công/thất bại).  
    * **Màn hình Quản lý Video Của Tôi (My Videos):**  
      * Hiển thị danh sách các video đã được người dùng tải lên, kèm theo thumbnail (nếu có), tiêu đề, ngày tải lên, và trạng thái xử lý (ví dụ: Đang xử lý, Hoàn thành, Lỗi).  
      * Cung cấp các tùy chọn cho mỗi video: Xem chi tiết, Sửa thông tin metadata (nếu được phép), Xóa video, Lấy link chia sẻ.  
    * **Màn hình Phát Video (Player Page \- VOD):**  
      * Hiển thị trình phát video HLS/DASH để xem nội dung video đã upload.  
      * Hiển thị thông tin chi tiết của video (tiêu đề, mô tả, người tạo).  
      * (Tùy chọn) Khu vực bình luận, nút thích/không thích, các video liên quan.  
    * **Màn hình Tạo/Quản lý Phiên Livestream (My Livestreams):**  
      * Form để người dùng nhập Tiêu đề, Mô tả cho phiên livestream.  
      * Nút "Tạo Livestream" để yêu cầu hệ thống cấp RTMP URL và Stream Key (cho RTMP ingest) hoặc thông tin WHIP endpoint (cho WebRTC ingest).  
      * Hiển thị rõ ràng RTMP URL và Stream Key (với tùy chọn sao chép dễ dàng) hoặc hướng dẫn cấu hình cho WebRTC.  
      * Hiển thị trạng thái của phiên livestream (ví dụ: Chưa bắt đầu, Đang phát, Đã kết thúc).  
      * Nút "Kết thúc Livestream" (để gọi API /api/live/stop).  
      * (Tùy chọn) Khung xem trước (preview) luồng phát nếu Media Server hỗ trợ.  
    * **Màn hình Xem Livestream (Live Player Page):**  
      * Hiển thị trình phát video HLS/DASH cho luồng trực tiếp.  
      * Hiển thị thông tin về phiên livestream (tiêu đề, người phát).  
      * (Tùy chọn) Khu vực chat trực tiếp, số

#### **Nguồn trích dẫn**

1\. That's how to write a software requirements specification (SRS document), https://www.rst.software/blog/how-to-write-srs-document 2\. Software Requirement Specification (SRS) Format \- GeeksforGeeks, https://www.geeksforgeeks.org/software-requirement-specification-srs-format/ 3\. IEEE 830-1998 \- IEEE SA, https://standards.ieee.org/ieee/830/1222/ 4\. Recommended Practice for Software Requirements Specifications (IEEE) \- Midori, https://www.midori-global.com/downloads/jpdf/jira-software-requirement-specification.pdf 5\. What is HTTP Live Streaming? | HLS streaming \- Cloudflare, https://www.cloudflare.com/learning/video/what-is-http-live-streaming/ 6\. HTTP Live Streaming (HLS) Protocol \- GetStream.io, https://getstream.io/glossary/hls-protocol/ 7\. DASH Adaptive Streaming for HTML video \- Media technologies on the web | MDN, https://developer.mozilla.org/en-US/docs/Web/Media/Guides/DASH\_Adaptive\_Streaming\_for\_HTML\_5\_Video 8\. DASH (Dynamic Adaptive Streaming over HTTP) \- Momento Docs, https://docs.momentohq.com/media-storage/performance/adaptive-bitrates/dash 9\. Real-Time Messaging Protocol (RTMP) Architecture \- PubNub, https://www.pubnub.com/blog/real-time-messaging-protocol-architecture/ 10\. What is RTMP? The Real-Time Messaging Protocol Explained \- Dacast, https://www.dacast.com/blog/rtmp-real-time-messaging-protocol/ 11\. WebRTC Video Streaming: How it Works & Applications \- VideoSDK, https://www.videosdk.live/developer-hub/webrtc/webrtc-video-streaming-app 12\. WebRTC (Web Real-Time Communication) Ultimate Guide 2025 \- Wowza, https://www.wowza.com/blog/what-is-webrtc 13\. WebRTC ingest and transcoding to HLS/DASH \- Gcore, https://gcore.com/docs/streaming-platform/live-streaming/webrtc-to-hls-transcoding 14\. WHIP (WebRTC-HTTP Ingestion Protocol) \- Ant Media Server, https://antmedia.io/docs/guides/publish-live-stream/whip/ 15\. www.rfc-editor.org, https://www.rfc-editor.org/rfc/rfc9725.pdf 16\. MinIO: a high performance, distributed object storage system | DSRS, https://dsrs.illinois.edu/blog/minio 17\. Object Storage as Primary Storage: The MinIO Story \- DEV Community, https://dev.to/ashokan/object-storage-as-primary-storage-the-minio-story-3g39 18\. Debezium Management Platform: Simplifying Change Data Capture, https://debezium.io/blog/2025/04/04/debezium-platform/ 19\. Debezium Management Platform, https://debezium.io/documentation/reference/stable/operations/debezium-platform.html 20\. Event Processor \- Confluent Developer, https://developer.confluent.io/patterns/event-processing/event-processor/ 21\. Simplifying Kafka Event Streaming: Easy Steps Explained (with code) \- Hevo Data, https://hevodata.com/learn/kafka-event/ 22\. Understanding How Debezium Captures Changes from PostgreSQL and delivers them to Kafka \[Technical Overview\] : r/apachekafka \- Reddit, https://www.reddit.com/r/apachekafka/comments/1jr1she/understanding\_how\_debezium\_captures\_changes\_from/ 23\. Postgres CDC with Debezium: Complete tutorial \- Sequin Blog, https://dev.to/sequin/streaming-postgres-changes-with-debezium-and-kafka-connect-a-hands-on-tutorial-2jgl 24\. How to Store Video in PostgreSQL Using BYTEA \- Timescale, https://www.timescale.com/learn/how-to-store-video-in-postgresql-using-bytea 25\. Handling Large Objects in Postgres \- Timescale, https://www.timescale.com/learn/handling-large-objects-in-postgres 26\. How to Convert MOV Video to HLS With FFmpeg | Mux, https://www.mux.com/articles/how-to-convert-mov-video-to-hls-format-with-ffmpeg-a-step-by-step-guide 27\. FFMPEG MP4 to HLS Conversion.md \- GitHub Gist, https://gist.github.com/lukebussey/4d27678c72580aeb660c19a6fb73e9ee 28\. How to convert MP4 to HLS format with ffmpeg: A step-by-step guide | Mux, https://www.mux.com/articles/how-to-convert-mp4-to-hls-format-with-ffmpeg-a-step-by-step-guide 29\. Video Streaming over HTTP using Spring Boot \- GitHub, https://github.com/saravanastar/video-streaming 30\. Video streaming by Spring (Boot, Web, JPA), Angular, MySQL \- GitHub, https://github.com/volvadvit/video-streaming-spring 31\. GitHub \- tejas2292/Nginx-RTMP-Server-with-HLS-and-Authentication, https://github.com/tejas2292/Nginx-RTMP-Server-with-HLS-and-Authentication 32\. Module ngx\_http\_hls\_module \- nginx, http://nginx.org/en/docs/http/ngx\_http\_hls\_module.html 33\. SRS (Simple Realtime Server) | SRS, https://ossrs.io/ 34\. SRS project \- Libre Self-hosted, https://libreselfhosted.com/project/srs/ 35\. Try Mediasoup: An Open Source Streaming Media Tool \- Andela, https://www.andela.com/blog-posts/try-mediasoup-an-open-source-streaming-media-tool 36\. Using Apache Kafka with Schema Registry and JSON Schema ..., https://quarkus.io/guides/kafka-schema-registry-json-schema 37\. How can I modify my ffmpeg-python script to output a .mp4 video in ..., https://community.gumlet.com/t/how-can-i-modify-my-ffmpeg-python-script-to-output-a-mp4-video-in-hls-format-with-multiple-bitrates-and-create-a-master-playlist/1919 38\. How to generate multiple resolutions HLS using FFmpeg for live streaming \- Stack Overflow, https://stackoverflow.com/questions/71913543/how-to-generate-multiple-resolutions-hls-using-ffmpeg-for-live-streaming 39\. How to convert MP4 to HLS format with ffmpeg: A step-by-step guide \- YouTube, https://www.youtube.com/watch?v=hmu7sFZcsCo\&pp=0gcJCdgAo7VqN5tD 40\. FFmpeg and HLS multiple audio renditions \- Super User, https://superuser.com/questions/1099442/ffmpeg-and-hls-multiple-audio-renditions 41\. How To Generate A Presigned MinIO URL Using .Net \- YouTube, https://www.youtube.com/watch?v=vHE7n2NUuO0 42\. Upload Files Using Pre-signed URLs — MinIO Object Storage for Linux, https://min.io/docs/minio/linux/integrations/presigned-put-upload-via-browser.html 43\. Which approach should I choose for HLS video streaming on S3 bucket? : r/aws \- Reddit, https://www.reddit.com/r/aws/comments/1cy4x1r/which\_approach\_should\_i\_choose\_for\_hls\_video/ 44\. minio behind proxy and presigned urls · Issue \#6853 \- GitHub, https://github.com/minio/minio/issues/6853 45\. Implementing Stream Keys with nginx-rtmp and Django \- Ben Wilber, https://benwilber.github.io/streamboat.tv/nginx/rtmp/streaming/2016/10/22/implementing-stream-keys-with-nginx-rtmp-and-django.html 46\. HLS | SRS, https://ossrs.net/lts/en-us/docs/v6/doc/hls 47\. v1\_EN\_DeliveryHLS · ossrs/srs Wiki · GitHub \- Delivery HLS, https://github.com/ossrs/srs/wiki/v1\_EN\_DeliveryHLS 48\. Transcode Deploy \- SRS, https://ossrs.net/lts/en-us/docs/v4/doc/sample-ffmpeg 49\. FFMPEG \- SRS, https://ossrs.net/lts/en-us/docs/v4/doc/ffmpeg 50\. FFMPEG | SRS, https://ossrs.io/lts/en-us/docs/v6/doc/ffmpeg\#transcode-rulers 51\. How can we transcode live rtmp stream to live hls stream using ffmpeg? \- Stack Overflow, https://stackoverflow.com/questions/19658216/how-can-we-transcode-live-rtmp-stream-to-live-hls-stream-using-ffmpeg 52\. How to build an adaptive bitrate streaming nginx \- BytePlus, https://www.byteplus.com/en/topic/40855 53\. How to create multi bit rate dash content using ffmpeg dash muxer \- Stack Overflow, https://stackoverflow.com/questions/48256686/how-to-create-multi-bit-rate-dash-content-using-ffmpeg-dash-muxer 54\. HLS in Depth \- Dyte.io, https://dyte.io/blog/hls-in-depth/ 55\. Using FFmpeg as a HLS streaming server (Part 3\) – Multiple Bitrates | Martin Riedl, https://www.martin-riedl.de/2018/08/25/using-ffmpeg-as-a-hls-streaming-server-part-3/ 56\. DASH Adaptive Streaming for HTML video \- Web APIs | MDN, https://developer.mozilla.org/en-US/docs/Web/API/Media\_Source\_Extensions\_API/DASH\_Adaptive\_Streaming 57\. Use FFmpeg to live stream with Wowza Streaming Engine, https://www.wowza.com/docs/how-to-live-stream-using-ffmpeg-with-wowza-streaming-engine 58\. Communication Between Client and Server \- mediasoup, https://mediasoup.org/documentation/v3/communication-between-client-and-server/ 59\. mediasoup-demos/mediasoup-recording/README.md at master · Kurento/mediasoup-demos \- GitHub, https://github.com/Kurento/mediasoup-demos/blob/master/mediasoup-recording/README.md 60\. iakuf/simple-rtmp-server: SRS is industrial-strength live streaming cluster, for the best conceptual integrity and the simplest implementation. \- GitHub, https://github.com/iakuf/simple-rtmp-server 61\. NGINX RTMP: Introducing asynchronous HTTP callbacks \- rarut \- WordPress.com, https://rarut.wordpress.com/2012/03/31/nginx-rtmp-introducing-asynchronous-http-callbacks/ 62\. HLS using Nginx RTMP Module not working \- Stack Overflow, https://stackoverflow.com/questions/27876056/hls-using-nginx-rtmp-module-not-working