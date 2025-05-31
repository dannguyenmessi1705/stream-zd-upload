package com.didan.streaming.video.entity;

public enum VideoStatus {
    UPLOADING,    // Video đang được upload
    UPLOADED,     // Video đã upload xong, chờ xử lý
    PROCESSING,   // Video đang được xử lý (chuyển đổi sang HLS)
    READY,        // Video đã sẵn sàng để phát
    ERROR         // Có lỗi xảy ra trong quá trình xử lý
}
