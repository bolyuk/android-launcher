package org.bolyuk.launcher.bolu1j.classes.fromJava;

import org.bolyuk.launcher.bolu1j.Interpreter;
import org.bolyuk.launcher.bolu1j.classes.BJException;
import org.bolyuk.launcher.bolu1j.classes.BJStatement;
import org.bolyuk.launcher.bolu1j.classes.BJclass;
import org.bolyuk.launcher.bolu1j.classes.BJobject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class BJavaUtil {

    public static Object[] argsToObj(BJStatement args, Interpreter context, Method m){
        Object[] r = new Object[args.size()];
        for(int i=0; i <args.size(); i++) {
            BJobject c = args.get(i).isStatement() ? args.get(i).getStatement(context) : (BJobject) args.get(i);
           
            switch (m.getParameterTypes()[i].getTypeName()){
                case "boolean":
                    r[i]=c.getLogic();
                    break;
                case "int":
                case "double":
                case "java.lang.Double":
                    r[i]=c.getNumber();
                    break;
                case "long":
                case "java.lang.Long":
                    r[i]=Long.valueOf(c.getNumber());
                    break;

                default:
                    if(m.getParameterTypes()[i].getTypeName().contains("org.bolu1j")) {
                          r[i] = c;
                    }else
                    r[i]= c.value;
            }
        }
        return r;
    }

    public static <R> BJclass classToBJ(R obj, Interpreter context){
        BJclass r = new BJclass(context);
        for(Method m: obj.getClass().getMethods())
            r.in_context.setVariable(m.getName(), new BJavaFunction(r.in_context).parseJava(m).setJContext(obj));
        for(Field f: obj.getClass().getFields())
            r.in_context.setVariable(f.getName(), fieldToBJ(f, obj, context));
        return r;
    }

    public static<R>  BJobject fieldToBJ(Field f,R obj, Interpreter context) {
        BJobject r = new BJobject();
        try {
            Object buf = f.get(obj);
            switch (buf.getClass().getTypeName()) {
                case "boolean":
                case "int":
                case"java.lang.Integer":
                case "java.lang.String":
                    r.value=buf.toString();
                    break;
                default:
                    r = classToBJ(buf,context);
            }
        }catch (Exception err){
            r = new BJException(err.getMessage());
        }
        return r;
    }
}
