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
public class Variable extends Number{
    String szVariableName = "";
    
    public Variable(String _szVariableName){
        super(0);
        szVariableName = _szVariableName;
    }
    
}
