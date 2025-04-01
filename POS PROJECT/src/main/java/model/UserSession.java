package model;

/*
 * import java.net.InetAddress; import java.sql.Blob; import java.sql.Timestamp;
 * import java.time.Instant; import java.time.LocalDateTime;
 * 
 * import javax.persistence.Column; import javax.persistence.Entity; import
 * javax.persistence.GeneratedValue; import javax.persistence.GenerationType;
 * import javax.persistence.Id; import javax.persistence.ManyToOne; import
 * javax.persistence.SequenceGenerator; import javax.persistence.Table;
 * 
 * import lombok.Data;
 * 
 * @Data
 * 
 * @Entity
 * 
 * @Table(name = "sessions") public class UserSession extends BaseEntity {
 * 
 * @Id
 * 
 * @GeneratedValue(strategy = GenerationType.SEQUENCE, generator =
 * "user_session_seq_generator")
 * 
 * @SequenceGenerator(name = "user_session_seq_generator", sequenceName =
 * "user_session_sequence", allocationSize = 1) private long sessionId;
 * 
 * private String ipAddress; private Timestamp timeStamp =
 * Timestamp.from(Instant.now()); private Blob sessionData; private
 * LocalDateTime expiryDate;
 * 
 * @ManyToOne private Employee employee;
 * 
 * private static UserSession instance;
 * 
 * public UserSession() { this.expiryDate = calculateExpiryDate(); }
 * 
 * private LocalDateTime calculateExpiryDate() { return
 * LocalDateTime.now().plusHours(24); }
 * 
 * public boolean isExpired() { return LocalDateTime.now().isAfter(expiryDate);
 * }
 * 
 * public static UserSession setInstance(Employee employee) { if (instance ==
 * null) { instance = new UserSession(); instance.setEmployee(employee); }
 * return instance; }
 * 
 * public static UserSession getInstance() { return instance; }
 * 
 * 
 * 
 * public void cleanLoginSession() { instance = null; employee = null; }
 * 
 * @Override public String toString() { return "LoginSession [username=" +
 * employee.toString() + "]"; } }
 */
