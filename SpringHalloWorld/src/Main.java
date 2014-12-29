import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"Beans.xml");

		HalloWorld obj = (HalloWorld) context.getBean("halloWorld");

		obj.getMessage();
	}
}
