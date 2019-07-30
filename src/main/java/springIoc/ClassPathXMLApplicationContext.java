package springIoc;




import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;



import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class ClassPathXMLApplicationContext implements  applicationContext {
    private HashMap<String,Object> ioc=new HashMap<String, Object>();

    public ClassPathXMLApplicationContext(String config)
    {

        try {
            creatBean(config);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void creatBean(String config) throws Exception {
        SAXReader reader = new SAXReader();
        Document doc = (Document) reader.read(this.getClass().getClassLoader().getResourceAsStream(config));

        Element rootElement = doc.getRootElement();
        List<Element> list =rootElement.elements();
        Iterator i = list.iterator();
        while (i.hasNext())
        {
            Element bean= (Element) i.next();
            String id=bean.attributeValue("id");

            String clas=bean.attributeValue("class");

            Object obj= Class.forName(clas).newInstance();
            Method[] methods=obj.getClass().getDeclaredMethods();
            List<Element> elements=bean.elements();
            for (Element e: elements )
            {
                for(int j=0;j<methods.length;j++)
                {
                    String temp=null;
                    if(methods[j].getName().startsWith("set"))
                    {
                        temp=(methods[j].getName()).substring(3,methods[j].getName().length()).toLowerCase();

                        if(e.attributeValue("name")!=null)
                        {
                            if(e.attributeValue("name").equals(temp))
                            {

                                methods[j].invoke(obj,e.attributeValue("value"));
                            }
                        }
                        else
                            {
                            methods[j].invoke(obj,ioc.get(e.attributeValue("ref")));
                        }
                    }
                }


                }
            ioc.put(id,obj);

        }

    }
    public Object getBean(String id)
    {
       Object object =ioc.get(id);
       return object;
    }


}
