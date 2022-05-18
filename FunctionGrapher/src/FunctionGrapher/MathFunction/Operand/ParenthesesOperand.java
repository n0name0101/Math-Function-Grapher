/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FunctionGrapher.MathFunction.Operand;

import FunctionGrapher.MathFunction.Operand.Number;

/**
 *
 * @author noname
 */
public class ParenthesesOperand extends Number {
    Number objBody;
    
    public ParenthesesOperand(Number _objBody){
        super(0);
        objBody = _objBody;
    }
    
    @Override
    public double getdData(){
        if(objBody == null)
            return 0.0;
        return objBody.getdData();
    }
}
