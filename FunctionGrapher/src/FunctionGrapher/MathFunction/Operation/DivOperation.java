/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FunctionGrapher.MathFunction.Operation;

import FunctionGrapher.MathFunction.Operand.Number;

/**
 *
 * @author noname
 */
public class DivOperation extends Number {
    private Number objLeftOperand;
    private Number objRightOperand;
    
    public DivOperation(Number _objLeftOperand , Number _objRightOperand){
        super(0.0);
        objLeftOperand = _objLeftOperand;
        objRightOperand =_objRightOperand;
    }
    
    @Override
    public double getdData(){
        if(objLeftOperand == null || objRightOperand == null)
            return 0.0;
        return objLeftOperand.getdData() / objRightOperand.getdData();
    }
}
