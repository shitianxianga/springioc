package test;

import demo.Order;
import demo.User;
import springIoc.ClassPathXMLApplicationContext;
import springIoc.applicationContext;

public class Test {
    private static applicationContext app=new ClassPathXMLApplicationContext("application.xml");
    public static void main(String[] args) {
        User user= (User) app.getBean("user");
        Order order= (Order) app.getBean("order");
        System.out.println(order.getUser().getName());

    }
}
