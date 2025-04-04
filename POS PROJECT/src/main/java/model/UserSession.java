package model;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;



public class UserSession extends BaseEntity {

    private long sessionId; // ID của phiên làm việc, có thể tự động sinh trong CSDL
    private String sessionToken;  // Mã thông báo phiên làm việc (Token)
    private Timestamp timestamp;  // Thời gian đăng nhập
    private Timestamp expiryTime; // Thời gian hết hạn phiên
    private String employeeNumber;  // Mã nhân viên thay vì employeeId
    private Employee employee; // Đối tượng Employee nếu cần tham chiếu
    private String sessionData; // Dữ liệu phiên làm việc (tùy chọn)

    public UserSession(Employee employee) {
        this.employee = employee;
        this.employeeNumber = employee.getEmployeeNumber(); // Lấy employeeNumber từ đối tượng Employee
        this.timestamp = Timestamp.from(Instant.now());
        this.sessionToken = generateSessionToken();
        this.expiryTime = calculateExpiryDate();  // Phiên hết hạn sau 8 giờ
    }

    // Tạo mã thông báo phiên (token) duy nhất
    private String generateSessionToken() {
        return UUID.randomUUID().toString(); // Mã thông báo phiên duy nhất
    }

    // Tính toán thời gian hết hạn của phiên (8 giờ sau)
    private Timestamp calculateExpiryDate() {
        Instant now = Instant.now();
        Instant expiryInstant = now.plus(8, ChronoUnit.HOURS);  // Cộng thêm 8 giờ
        return Timestamp.from(expiryInstant);
    }

    // Kiểm tra xem phiên làm việc có hết hạn không
    public boolean isExpired() {
        return Instant.now().isAfter(expiryTime.toInstant());
    }

    // Getter và Setter
    public long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public Timestamp getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(Timestamp expiryTime) {
        this.expiryTime = expiryTime;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getSessionData() {
        return sessionData;
    }

    public void setSessionData(String sessionData) {
        this.sessionData = sessionData;
    }
}


