package util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    // The session factory that will be used to create Hibernate sessions
    private static SessionFactory sessionFactory;
    
    // Logger for logging information, warnings, and errors
    private static final Logger Log = LogManager.getLogger(HibernateUtil.class);
    
    // Synchronized method to build the SessionFactory to ensure it's created only once in a thread-safe manner
    private static synchronized SessionFactory buildSessionFactory() {
        // Check if the sessionFactory is already created, to prevent redundant creation
        if (sessionFactory == null) {
            try {
                // Create a Hibernate Configuration object
                Configuration configuration = new Configuration();
                // Configure Hibernate with settings from the hibernate.cfg.xml file
                configuration.configure("hibernate.cfg.xml");

                // Check if update mode is enabled from the app config (e.g., for automatic schema updates)
                if (AppConfig.USE_UPDATE_MODE) {
                    // If in update mode, Hibernate will automatically update the database schema
                    configuration.setProperty("hibernate.hbm2ddl.auto", "update");
                    Log.info("Hibernate Schema Mode: UPDATE");
                } else {
                    // If not in update mode, no automatic schema changes will be made
                    configuration.setProperty("hibernate.hbm2ddl.auto", "none");
                    Log.info("Hibernate Schema Mode: NONE");
                }

                // Build the SessionFactory using the configuration
                sessionFactory = configuration.buildSessionFactory();
            } catch (Throwable ex) {
                // Log any errors that occur during the SessionFactory creation
                Log.error("Initial SessionFactory creation failed", ex);
                ex.printStackTrace();
                throw new ExceptionInInitializerError(ex); // Throw an error if initialization fails
            }
        }
        return sessionFactory; // Return the created SessionFactory
    }

    // Method to get the SessionFactory (creates it if it doesn't exist)
    public static SessionFactory getSessionFactory() {
        // If the SessionFactory doesn't exist, create it
        if (sessionFactory == null) {
            buildSessionFactory();
        }
        return sessionFactory; // Return the existing or newly created SessionFactory
    }

    // Method to shut down the SessionFactory and close any open sessions
    public static void shutdown() {
        // Check if the sessionFactory is not null and not already closed
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            // Close the sessionFactory to release resources
            sessionFactory.close();
        }
    }
}
