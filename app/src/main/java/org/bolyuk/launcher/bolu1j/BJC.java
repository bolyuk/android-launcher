package org.bolyuk.launcher.bolu1j;

import org.bolyuk.launcher.bolu1j.operators.AbstractOperator;
import org.bolyuk.launcher.bolu1j.operators.AndOperator;
import org.bolyuk.launcher.bolu1j.operators.AssignmentOperator;
import org.bolyuk.launcher.bolu1j.operators.ClassOperator;
import org.bolyuk.launcher.bolu1j.operators.ConditionOperator;
import org.bolyuk.launcher.bolu1j.operators.DivisionOperator;
import org.bolyuk.launcher.bolu1j.operators.FileReadOperator;
import org.bolyuk.launcher.bolu1j.operators.FunctionOperator;
import org.bolyuk.launcher.bolu1j.operators.GrabeOperator;
import org.bolyuk.launcher.bolu1j.operators.InversionOperator;
import org.bolyuk.launcher.bolu1j.operators.LessOperator;
import org.bolyuk.launcher.bolu1j.operators.MinusOperator;
import org.bolyuk.launcher.bolu1j.operators.MoreOperator;
import org.bolyuk.launcher.bolu1j.operators.MultiplicationOperator;
import org.bolyuk.launcher.bolu1j.operators.OrOperator;
import org.bolyuk.launcher.bolu1j.operators.PlusOperator;
import org.bolyuk.launcher.bolu1j.operators.StatementExecOperator;
import org.bolyuk.launcher.bolu1j.operators.StatementSaveOperator;
import org.bolyuk.launcher.bolu1j.operators.UpperContextOperator;

public class BJC {
    public static int VERSION=2;

    public static char[]numbers = {'1','2','3','4','5','6','7','8','9','0'};
    public static char[]letters = {'q','w','e','r','t','y','u','i','o','p','a','s','d','f','g','h','j','k','l','z','x','c','v','b','n','m'};
    public static char[]symbols = {'+','-','=',';'};

    public static AbstractOperator[] operators=
             {
                     new PlusOperator(),
                     new MinusOperator(),
                     new MultiplicationOperator(),
                     new DivisionOperator(),
                     new AssignmentOperator(),
                     new GrabeOperator(),
                     new MoreOperator(),
                     new LessOperator(),
                     new ConditionOperator(),
                     new StatementExecOperator(),
                     new StatementSaveOperator(),
                     new InversionOperator(),
                     new OrOperator(),
                     new AndOperator(),
                     new FunctionOperator(),
                     new FileReadOperator(),
                     new ClassOperator(),
                     new UpperContextOperator()
             };

}
