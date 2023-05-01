package org.bolyuk.launcher.bolu1j.classes;

import org.bolyuk.launcher.bolu1j.Interpreter;

import java.util.ArrayList;

public class BJStatement extends BJobject{
    public Interpreter _context;
    public ArrayList<BJobject> _objects = new ArrayList<>();

    public BJStatement(Interpreter context){
        _context=context;
    }
    public void add(BJobject bjo){
        _objects.add(bjo);
    }

    public BJobject get(int i){
        return  _objects.get(i);
    }
    public int size() {return _objects.size();}

    @Override
    public BJobject copy() {
        BJStatement r = new BJStatement(_context);
        r.setFinal(isFinal);
        r.value=value;
        r.type=type;
        r._objects=_objects;
        return r;
    }
}
