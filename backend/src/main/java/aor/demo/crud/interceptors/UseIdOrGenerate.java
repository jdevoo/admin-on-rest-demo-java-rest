package aor.demo.crud.interceptors;


import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IncrementGenerator;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.lang.reflect.Field;


//https://stackoverflow.com/a/4046908/986160
//this is needed since all records in data.js start with 0
public class UseIdOrGenerate extends IncrementGenerator {


    @Override
    public Serializable generate(SessionImplementor session, Object object)
            throws HibernateException {
        if (object == null)
            throw new HibernateException(new NullPointerException());

        for (Field field : object.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)
                    && field.isAnnotationPresent(GeneratedValue.class)) {
                boolean isAccessible = field.isAccessible();
                try {
                    field.setAccessible(true);
                    Object obj = field.get(object);
                    field.setAccessible(isAccessible);
                    if (obj != null) {
                        if (Integer.class.isAssignableFrom(obj.getClass())) {
                            if (((Integer) obj) > 0) {
                                return (Serializable) obj;
                            }
                        }
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        return super.generate(session, object);
    }
}