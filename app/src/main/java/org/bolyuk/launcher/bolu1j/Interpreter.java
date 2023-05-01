package org.bolyuk.launcher.bolu1j;

import org.bolyuk.launcher.bolu1j.classes.Action;
import org.bolyuk.launcher.bolu1j.classes.BJException;
import org.bolyuk.launcher.bolu1j.classes.BJStatement;
import org.bolyuk.launcher.bolu1j.classes.BJobject;
import org.bolyuk.launcher.bolu1j.classes.fromJava.BJavaFunction;
import org.bolyuk.launcher.bolu1j.operators.AbstractOperator;

import java.util.ArrayList;
import java.util.HashMap;

public class Interpreter {
    Interpreter inner_context=null;
    ArrayList<BJStatement> bjs = new ArrayList<>();
    HashMap<String, BJobject> _values = new HashMap<>();
    public int iterator=0;

    public static Thread currentThread;

    public Interpreter(Interpreter inner_context){
        this.inner_context=inner_context;
    }

    public Interpreter context(Interpreter inner_context){
        this.inner_context=inner_context;
        return this;
    }

    public Interpreter getInner(){
        return inner_context;
    }

    public void put(BJStatement _bjs){
        bjs.add(_bjs);
    }

    public void parse(String BJCode){
        bjs = Parser.parseCode(BJCode, this);

    }
    public BJobject exec(ArrayList<BJStatement> _bjs ){
        BJobject _response = new BJobject();
        for(int i=0; i < _bjs.size();i++){
            _response = exec(_bjs.get(i), new BJobject(), 0);
            if(_response instanceof BJException)
                break;
        }
        return _response;
    }

    public void threadExec(Action l){
        currentThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        BJobject _response = new BJobject();
                        for (int i = 0; i < bjs.size(); i++) {
                            _response = exec(bjs.get(i), new BJobject(), 0);
                            if (_response instanceof BJException)
                                break;
                        }
                        l.onNotify(_response);
                       } catch (Exception err){
                        l.onNotify(new BJException("exception [ "+err+" ]"));
                    }
                }
            });
        try {

            setVariable("wait", new BJavaFunction(this).parseJava(Thread.class.getMethod("sleep", long.class)).setJContext(currentThread));
        }catch (Exception e) {
            System.out.println(e);
        }

        currentThread.start();
        }

    public BJobject exec(){
        return exec(bjs);
    }

    public BJobject exec(BJStatement bjs,BJobject prev, int i){

      BJobject cur = (i < bjs.size())? bjs.get(i): null;
      if(prev == null)
          prev = cur;

      if(cur == null)
          return prev;
      if(cur.type == "operator") {
          return exec(bjs, operatorExec(bjs, prev, i), i+2);
      }
      if(cur.type == "statement")
            return exec(bjs, exec((BJStatement)bjs.get(i), new BJobject(),0), i+1);
      return exec(bjs, bjs.get(i), i+1);
    }

    public void clearParsedCode(){
        bjs.clear();
    }

    private BJobject operatorExec(BJStatement bjs,BJobject p, int i){
        try {
        BJobject c = (i < bjs.size()) ? bjs.get(i) : null;
        BJobject n = (i < bjs.size() -1) ? bjs.get(i + 1) : null;


            for (int j = 0; j < BJC.operators.length ; j++) {
                AbstractOperator o = BJC.operators[j];
                if (o.getOperator().equals(c.value) && o.canExec(p, n))
                        return o.exec(p, n, this);
            }
            return new BJException("can not execute [ "+p.type+" "+c.value+" "+n.type+" ]");
        }catch (Exception err){
            return new BJException("can not execute [ "+err.getMessage()+" ] last value [ "+p.value+" ]");
        }
    }

    public BJobject setVariable(String name, BJobject value){
        if(_values.containsKey(name))
            removeVariable(name);
        _values.put(name,value);
        return value;
    }

    public BJobject putVariable(String name, BJobject value){
        if(_values.containsKey(name))
            return setVariable(name, value);
        else if(inner_context != null)
            return inner_context.putVariable(name, value);
        else
            return setVariable(name, value);
    }

    public BJobject getVariable(String name) {
        if (_values.containsKey(name))
            return _values.get(name);
        else if (inner_context != null)
            return inner_context.getVariable(name);
        else
            return null;
    }

    public BJobject removeVariable(String name){
        _values.remove(name);
        return new BJobject();
    }

    public static Interpreter getDefaultInterpreter(){
        Interpreter r= new Interpreter(null);
        try {
           r.setVariable("wait", new BJavaFunction(r).parseJava(Thread.class.getMethod("sleep", long.class)).setJContext(currentThread).setFinal(true));
           r.setVariable("copy", new BJavaFunction(r).parseJava(Interpreter.class.getMethod("copy", BJobject.class)).setFinal(true));
        }catch (Exception e) {
        System.out.println(e);
        }
        return r;
    }

    public static BJobject copy(BJobject o){
        return o.copy();
    }
}
