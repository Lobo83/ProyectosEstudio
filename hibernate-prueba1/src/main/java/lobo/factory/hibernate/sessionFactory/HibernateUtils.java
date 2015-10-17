package lobo.factory.hibernate.sessionFactory;


import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


public class HibernateUtils {
	private static SessionFactory sessionFactory=null;
	private static final String CONFIG_PATH="hibernate.cfg.xml";
	
	private HibernateUtils(){
		
	}
	public static SessionFactory getInstance(){
		if(sessionFactory==null){
			sessionFactory=new Configuration().configure(CONFIG_PATH).buildSessionFactory();
		}
		return sessionFactory;
	}
	 
	//Esto servirá para ponerlo en el método destroy en Spring 
	public static void shutdown() {
		// Close caches and connection pools
		getInstance().close();
	}
}
