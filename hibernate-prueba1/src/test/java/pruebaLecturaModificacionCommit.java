
import lobo.factory.hibernate.pojo.Unidad;
import lobo.factory.hibernate.sessionFactory.HibernateUtils;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class pruebaLecturaModificacionCommit {
	private SessionFactory sessionFactory;
	@Before
	public void method() {
		sessionFactory= HibernateUtils.getInstance();
	}
	@Test
	public void test() {
		try{
			Session sess =sessionFactory.openSession();//Se obtiene una nueva session
			sess=sessionFactory.getCurrentSession();//Esto realmente no es necesario pero as� se ver� que una session puede recuperarse desde diversos metodos
			Transaction tx = sess.beginTransaction();//Inicio de transacci�n
			tx=sess.getTransaction();//lo mismo que en el caso de getCurrentSession pero para la transaccion
			//Nuestra l�gica de negocio
			Unidad unit = (Unidad) sess.get(Unidad.class,new Long(9));
			//Unidad unit = (Unidad) sess.load(Unidad.class,new Long(8)); esto falla porque no encuentra la unidad con id=8. get no fallar�a pero no har�a nada.
			unit.setDescripcion("prueba hibernate");
			Unidad unitNueva = new Unidad();//Objeto transient de momento
			unitNueva.setCodUnidad("unidadHibernate");
			unitNueva.setDescripcion("Prueba de unidad");
			unitNueva.setNombre("parapa");
			
			sess.saveOrUpdate(unit);//lanza el update. Ojo, si se ha hecho modificaci�n pero el valor es el mismo que ten�a antes entonces no lanza el update
			sess.saveOrUpdate(unitNueva);//lanza el insert. Ahora el objeto es persistente
			tx.commit();
			
		}catch(HibernateException e){
			e.printStackTrace();
		}
		
		
	}
	@After
	public void destroy(){
		sessionFactory.close();
	}
}
