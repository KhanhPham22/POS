package util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static SessionFactory sessionFactory;
    private static final Logger Log = LogManager.getLogger(HibernateUtil.class);
    
    private static synchronized SessionFactory buildSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration();
                configuration.configure("hibernate.cfg.xml");     
                // Thêm tùy chọn tùy chỉnh khác nếu cần
                if (AppConfig.USE_UPDATE_MODE) {
                    configuration.setProperty("hibernate.hbm2ddl.auto", "update");
                    Log.info("Hibernate Schema Mode: UPDATE");
                } else {
                    configuration.setProperty("hibernate.hbm2ddl.auto", "none");
                    Log.info("Hibernate Schema Mode: NONE");
                }
                sessionFactory = configuration.buildSessionFactory();
            } catch (Throwable ex) {
                Log.error("Initial SessionFactory creation failed", ex);
                ex.printStackTrace();
                throw new ExceptionInInitializerError(ex);
            }
        }
        return sessionFactory;
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            buildSessionFactory();
        }
        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
    }
}
