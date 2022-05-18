/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FunctionGrapher.MathFunction;

import java.util.Stack;

//** Operation
import FunctionGrapher.MathFunction.Operation.AddOperation;
import FunctionGrapher.MathFunction.Operation.SubOperation;
import FunctionGrapher.MathFunction.Operation.MultiplicationOperation;
import FunctionGrapher.MathFunction.Operation.DivOperation;
//** Operand
import FunctionGrapher.MathFunction.Operand.Variable;
import FunctionGrapher.MathFunction.Operand.Number;
import FunctionGrapher.MathFunction.Operand.ParenthesesOperand;
//** Function
import FunctionGrapher.MathFunction.MathFunctionOneVar;
import FunctionGrapher.Exception.MathFunctionException;

/**
 *
 * @author noname
 */
public class MathFunctionOneVar {
        private String szArithmeticExpre = "";
        private Variable objVariableX = new Variable("x");
        private Number objParsingResult = null;
        
        public MathFunctionOneVar(String _szArithmeticExpre) throws MathFunctionException{
            szArithmeticExpre = _szArithmeticExpre;
            objParsingResult = funcParsing(_szArithmeticExpre);
        }
        
        public boolean setVariableX(double _dVariableX){
            if(objVariableX == null)
                return false;
            objVariableX.setdData(_dVariableX);
            return true;
        }
        
        public double computeFunction(double _dVariableX ) throws MathFunctionException{
            objVariableX.setdData(_dVariableX);
            return computeFunction();
        }
        
        public double computeFunction() throws MathFunctionException{
            if(objParsingResult == null)
                throw new MathFunctionException("No Expression.");
            return objParsingResult.getdData();
        }
        
        public String getszArithmeticExpre(){
            return szArithmeticExpre;
        }
        
        //** Expresion Parsing
        private Number funcParsing(String _szArithmeticExpre) throws MathFunctionException{
            Number objReturn = null;
            Number objReturnTmp = null;
            
            for(int iCounter = 0 ; iCounter < _szArithmeticExpre.length() ; iCounter++){
                if( _szArithmeticExpre.charAt(iCounter) == ' ')
                    continue;
                //** Parentheses Return
                else if( _szArithmeticExpre.charAt(iCounter) == ')' ){
                    if(objReturn == null)
                        throw new MathFunctionException("Unvalid Expression : Unvalid Parentheses");
                    return objReturn;
                }
                //** Digit
                else if(isDigit(_szArithmeticExpre.charAt(iCounter))){
                    int iCounterStart = iCounter;
                    String szRealNum;
                    
                    while(iCounter < (_szArithmeticExpre.length() - 1) &&
                          ( isDigit(_szArithmeticExpre.charAt(iCounter+1)) || _szArithmeticExpre.charAt(iCounter+1) == '.' ))
                        iCounter++;
                    szRealNum = _szArithmeticExpre.substring(iCounterStart, iCounter+1);
                    szRealNum = (szRealNum.indexOf('.') == -1) ? szRealNum + '.' : szRealNum;
                    
                    try{
                        objReturnTmp = new Number( Double.parseDouble(szRealNum) );
                    }catch(Exception e){
                        objReturn = null;
                        throw new MathFunctionException("Unvalid Expression : Operand : " + szRealNum);
                    }
                    
                    //** Checking
                    if(objReturn == null)
                        objReturn = objReturnTmp;
                    //** Negatif Operand
                    else if(objReturn instanceof SubOperation && ((SubOperation)objReturn).getobjRightOperand() == null)
                        ((SubOperation)objReturn).setobjRightOperand(objReturnTmp);
                    else 
                        objReturn = new MultiplicationOperation(objReturn,objReturnTmp);
                }
                //** Variable
                else if( _szArithmeticExpre.charAt(iCounter) == 'x' ||  _szArithmeticExpre.charAt(iCounter) == 'X'){
                    if(objReturn == null)
                        objReturn = objVariableX;
                    else if(objReturn instanceof SubOperation && ((SubOperation)objReturn).getobjRightOperand() == null)
                        ((SubOperation)objReturn).setobjRightOperand(objVariableX);
                    else 
                        objReturn = new MultiplicationOperation(objReturn,objVariableX);
                }
                //** Operation
                else if( _szArithmeticExpre.charAt(iCounter) == '+' ||  _szArithmeticExpre.charAt(iCounter) == '-'){
                    if(objReturn == null){
                        if(_szArithmeticExpre.charAt(iCounter) == '+' || iCounter == _szArithmeticExpre.length()-1)
                            throw new MathFunctionException("Unvalid Expression : Add (+)");
                        objReturn = new SubOperation(new Number(0.0) , null);
                    }
                    else if(objReturn instanceof SubOperation && ((SubOperation)objReturn).getobjRightOperand() == null)
                        objReturn = null;
                    else{
                        System.out.println(_szArithmeticExpre.substring( _szArithmeticExpre.charAt(iCounter) == '-' ? iCounter : iCounter+1));
                        objReturn = new AddOperation( objReturn ,
                                    this.funcParsing(
                                    _szArithmeticExpre.substring( _szArithmeticExpre.charAt(iCounter) == '-' ? iCounter : iCounter+1)
                                    ) );
                        return objReturn;
                    }
                }
                else if( _szArithmeticExpre.charAt(iCounter) == '*' ||  _szArithmeticExpre.charAt(iCounter) == '/'){
                    boolean bIsMul = _szArithmeticExpre.charAt(iCounter) == '*' ? true : false;
                    Number objTmp = null;
                    
                    if(objReturn == null || ++iCounter == _szArithmeticExpre.length())
                        throw new MathFunctionException("Unvalid Expression : Mul (*)");
                        
                    //** Skip space
                    while(_szArithmeticExpre.charAt(iCounter) == ' ' ||
                          _szArithmeticExpre.charAt(iCounter) == '-' ||
                          _szArithmeticExpre.charAt(iCounter) == '+') {
                        
                        if(_szArithmeticExpre.charAt(iCounter) == '-'){
                            if(objReturnTmp == null)
                                objReturnTmp =  new SubOperation(new Number(0.0) , null);
                            else
                                objReturnTmp = null;
                        }
                        iCounter++;
                    }
                    if(_szArithmeticExpre.charAt(iCounter) == 'x' ||  _szArithmeticExpre.charAt(iCounter) == 'X')
                        objTmp = objVariableX;
                    else if( isDigit(_szArithmeticExpre.charAt(iCounter)) ){
                        int iCounterStart = iCounter;
                        String szRealNum;
                    
                        while(iCounter < (_szArithmeticExpre.length() - 1) &&
                            ( isDigit(_szArithmeticExpre.charAt(iCounter+1)) || _szArithmeticExpre.charAt(iCounter+1) == '.' ))
                            iCounter++;
                        szRealNum = _szArithmeticExpre.substring(iCounterStart, iCounter+1);
                        szRealNum = (szRealNum.indexOf('.') == -1) ? szRealNum + '.' : szRealNum;
                        System.out.println(szRealNum + " " + iCounter);
                        try{
                            objTmp = new Number( Double.parseDouble(szRealNum) );
                        }catch(Exception e){
                            objReturn = null;
                            throw new MathFunctionException("Unvalid Expression : Operand : " + szRealNum);
                        }
                    }
                    else if(_szArithmeticExpre.charAt(iCounter) == '('){
                        objTmp = new ParenthesesOperand( funcParsing(_szArithmeticExpre.substring(iCounter+1)) );
                        iCounter += parenthesesSkip(_szArithmeticExpre.substring(iCounter));
                    }
                    else
                        throw new MathFunctionException("Unvalid Expression : Mul (*) Or Div (/)");
                    
                    //** Checking
                    if(objReturnTmp == null)
                        objReturnTmp = objTmp;
                    else
                        ((SubOperation)objReturnTmp).setobjRightOperand(objTmp);
                    
                    //** '*' or '/'
                    if(bIsMul)
                        objReturn = new MultiplicationOperation(objReturn , objReturnTmp);
                    else
                        objReturn = new DivOperation(objReturn,objReturnTmp);
                }
                //** Parentheses
                else if( _szArithmeticExpre.charAt(iCounter) == '('){
                    objReturnTmp = new ParenthesesOperand( funcParsing(_szArithmeticExpre.substring(iCounter+1)) );
                    iCounter += parenthesesSkip(_szArithmeticExpre.substring(iCounter));
                   
                    //** Checking
                    if(objReturn == null)
                        objReturn = objReturnTmp;
                    //** Negatif Operand
                    else if(objReturn instanceof SubOperation && ((SubOperation)objReturn).getobjRightOperand() == null)
                        ((SubOperation)objReturn).setobjRightOperand(objReturnTmp);
                    else 
                        objReturn = new MultiplicationOperation(objReturn,objReturnTmp);                    //** Checking
                }
                else
                    throw new MathFunctionException("Unvalid Expression.");
                
                //** Reset objReturnTmp
                objReturnTmp = null;
            }
            return objReturn;
        }
        
        
        private int parenthesesSkip(String _szArithmeticExpre) throws MathFunctionException{
            Stack<Character> objStack = new Stack<>();
            
            for(int iCounter = 0 ; iCounter < _szArithmeticExpre.length() ; iCounter++){
                if(_szArithmeticExpre.charAt(iCounter) == ' ')
                    continue;
                else if(_szArithmeticExpre.charAt(iCounter) == '(')
                    objStack.push(_szArithmeticExpre.charAt(iCounter));
                else if(_szArithmeticExpre.charAt(iCounter) == ')'){
                    if(objStack.isEmpty())
                        return -1;
                    objStack.pop();
                    if(objStack.isEmpty())
                        return iCounter;
                }
                else if(objStack.isEmpty())
                    return -1;
            }
            throw new MathFunctionException("Unvalid Expression : Unvalid Parentheses");
        }
        
        private boolean isDigit(char _cData){
            return (_cData <= '9') && (_cData >= '0');
        }
        
        private boolean isOperationObj(Object _objObj){
            return (_objObj instanceof MultiplicationOperation);
        }
}
