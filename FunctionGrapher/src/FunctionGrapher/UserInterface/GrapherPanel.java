/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package FunctionGrapher.UserInterface;

import javax.swing.JOptionPane;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import FunctionGrapher.MathFunction.MathFunctionOneVar;
import FunctionGrapher.Exception.MathFunctionException;

/**
 *
 * @author noname
 */
public class GrapherPanel extends javax.swing.JPanel {
    private int iWindowWidth = 1200;
    private int iWindowHeight = 600;
    private double dZoom = 100;
    
    private double dYMove = 0;
    private double dXMove = 0;
    private Graphics2D objGraphics2D;
    private BufferedImage objImageBuffer;
    private List<MathFunctionOneVar> objFunctionList = new ArrayList<>();
    
    private Point mousePt;
    
    /**
     * Creates new form GrapherPanel
     */
    public GrapherPanel() {
        initComponents();
        
        //** Mouse Setting
        this.addMouseListener(
            new MouseAdapter(){
                @Override
                public void mousePressed(MouseEvent e){
                    mousePt = e.getPoint();
                    System.out.println(getRealX(mousePt.x) + " " + getRealY(mousePt.y));
                    repaint();
                }
            }
        );
        this.addMouseMotionListener(
            new MouseMotionAdapter(){
                @Override
                public void mouseDragged(MouseEvent e) {
                    int iDx = e.getX() - mousePt.x;
                    int iDy = e.getY() - mousePt.y;
                    //**Move X
                    if(iDx != 0)
                        dXMove += (iDx < 0) ? -1  : 1;
                    else if(iDy != 0)
                        dYMove += (iDy < 0) ? 1  : -1; 
                    mousePt = e.getPoint();
                    repaint();
                }
            }
        );
        
        //** Window Setting
        setFocusable(true);
        requestFocusInWindow();
        setPreferredSize(new Dimension(iWindowWidth+1, iWindowHeight));
	setMinimumSize(new Dimension(iWindowWidth+1, iWindowHeight));
	setMaximumSize(new Dimension(iWindowWidth+1, iWindowHeight));
        
        //** Window Buffer
        objImageBuffer = new BufferedImage(iWindowWidth+1 , iWindowHeight , BufferedImage.TYPE_INT_RGB);
        objGraphics2D = objImageBuffer.createGraphics();
    }

    @Override
    protected void paintComponent(Graphics g){
        BasicStroke objDefaultStrokeTmp = (BasicStroke)objGraphics2D.getStroke();
        BasicStroke objBoldStrokeTmp = new BasicStroke(3.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND);
        
        super.paintComponent(g);
        objGraphics2D.setColor(Color.WHITE);
        objGraphics2D.fillRect(0 , 0 , iWindowWidth+1 , iWindowHeight);
        
        synchronized(this){
            int iAxisX = getScreenX(0.0);
            int iAxisY = getScreenY(0.0);
            
            iAxisX = (iAxisX < 0) || (iAxisX > iWindowWidth)  ?  -50 : iAxisX;
            iAxisY = (iAxisY < 0) || (iAxisY > iWindowHeight) ? (iAxisY > iWindowHeight) ? iWindowHeight : 0 : iAxisY;
            
            //** Drawing X  Y Line
            objGraphics2D.setColor(Color.BLACK);  
            objGraphics2D.drawLine(iAxisX , 0 , iAxisX , iWindowHeight);
            objGraphics2D.drawLine(0 , iAxisY , iWindowWidth , iAxisY);
            
            objGraphics2D.setFont(new Font("arabic light", Font.ITALIC, 15));
            objGraphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            objGraphics2D.setColor(new Color(50, 50, 180));
            
            //** Setting X Number and get Y
            for(int iCounter = 0 ; iCounter <= iWindowWidth ; iCounter++){
                if((iCounter % (iWindowWidth / 4 / 5)) == 0){
                    double dRealX = getRealX(iCounter);
                    if((dRealX % ((int)dRealX)) == 0){
                        objGraphics2D.setStroke(objBoldStrokeTmp);
                        objGraphics2D.drawLine(iCounter , iAxisY-10 , iCounter , iAxisY+10);
                        objGraphics2D.setStroke(objDefaultStrokeTmp);
                    }
                    else
                        objGraphics2D.drawLine(iCounter , iAxisY-5 , iCounter , iAxisY+5);
                    objGraphics2D.drawString( String.format("%.2f", dRealX) , iCounter-20 , iAxisY+40 );
                }
            }
            
            //** Setting Y Number
            for(int iCounter = 0 ; iCounter <= iWindowHeight ; iCounter++){
                if((iCounter % (iWindowHeight / 2 / 5)) == 0){
                    double dRealY = getRealY(iCounter);
                    if((dRealY % ((int)dRealY)) == 0){
                        objGraphics2D.setStroke(objBoldStrokeTmp);
                        objGraphics2D.drawLine(iAxisX-10 , iCounter , iAxisX+10 , iCounter);
                        objGraphics2D.setStroke(objDefaultStrokeTmp);
                    }
                    else
                        objGraphics2D.drawLine(iAxisX-5 , iCounter , iAxisX+5 , iCounter);
                    if(dRealY != 0.0)
                        objGraphics2D.drawString( String.format("%.2f", dRealY) , iAxisX-55 , iCounter+5 );
                }
            }
            
            for(int iCounter = 0 ; iCounter < objFunctionList.size() ; iCounter++){
                    List<Double> objListX = new ArrayList();
                    List<Double> objListY = new ArrayList();
                    Color[] aFunctionColorArray = { Color.BLUE , Color.CYAN , Color.YELLOW , Color.GREEN , Color.RED};
                    
                    for(int iCounterB = 0 ; iCounterB < iWindowWidth ; iCounterB++){
                        int iScreenYTmp = 0;
                        try{
                            iScreenYTmp = getScreenY(objFunctionList.get(iCounter).computeFunction(getRealX(iCounterB)));
                        }catch(MathFunctionException e){
                            getToolkit().beep();
                            JOptionPane.showMessageDialog(null, "Function : " + iCounter+1
                                                          + "\nF(x)= " + objFunctionList.get(iCounter).getszArithmeticExpre() 
                                                          + "\nError : " + e.getMessage() );
                            break;
                        }
                        if( (iScreenYTmp >= 0) && (iScreenYTmp <= iWindowHeight)){
                            objListX.add((double)iCounterB);
                            objListY.add((double)iScreenYTmp);
                        }
                    }
                     
                    //** Drawing Function Line
                    int[] iXArray = new int[objListX.size()];
                    int[] iYArray = new int[objListY.size()];
                    int iPre = 0;
                    int iStart = 0;
                    
                    for (int iCounterB = 0 ; iCounterB < objListX.size() ; iCounterB++) {
                        if(iCounterB == iStart || iPre == (objListX.get(iCounterB).intValue()-1) ){
                            iXArray[iCounterB-iStart] = objListX.get(iCounterB).intValue();
                            iYArray[iCounterB-iStart] = objListY.get(iCounterB).intValue();
                        }
                        if( (iPre != (objListX.get(iCounterB).intValue()-1) && iCounterB != iStart) || iCounterB+1 == objListX.size()){
                            //** Drawing
                            objGraphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                            objGraphics2D.setColor(aFunctionColorArray[iCounter]);
                            objGraphics2D.setStroke(new BasicStroke(3.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND));
                            objGraphics2D.drawPolyline(iXArray, iYArray, 
                                                       (iCounterB != objListX.size()-1) ? (iCounterB-iStart) : (iCounterB-iStart)+1 );
                            objGraphics2D.setStroke(objDefaultStrokeTmp);
                            if(iCounterB != objListX.size()-1)
                                iStart = iCounterB--;
                        }
                        iPre = objListX.get(iCounterB).intValue();
                    }
                    objListX.clear();
                    objListY.clear();
            }
        }
        g.drawImage(objImageBuffer, 0 , 0 , null);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private double getRealX(int _iX){
        return (dZoom * ((double)_iX - (dXMove * iWindowWidth/4/5))) / (double)iWindowWidth 
                - (dZoom / (double)2);
    }
    
    private double getRealY(int _iY){
        return ((dZoom * (iWindowHeight - (dYMove * iWindowHeight/2/5) - (double)_iY )) / iWindowHeight) 
                - (double)(dZoom / (double)2);
    }
    
    private int getScreenX(double _dRealX){
        return (int)(((double)(((_dRealX + (double)(dZoom/(double)2)) / dZoom) * iWindowWidth)) 
                + (dXMove * iWindowWidth / 4 / 5));
    }
    
    private int getScreenY(double _dRealY){
        return iWindowHeight - (int)(((double)(((_dRealY + (double)(dZoom/(double)2)) / dZoom) * iWindowHeight))
               + (dYMove * iWindowHeight / 2 / 5));
    }
    
    public void addMathFunction(String _szMathFunction){
        if(objFunctionList.size() < 5)
            try{
                objFunctionList.add(new MathFunctionOneVar(_szMathFunction));
            }catch(MathFunctionException e){
                getToolkit().beep();
                JOptionPane.showMessageDialog(null, "\nF(x)= " + _szMathFunction 
                                              + "\nError : " + e.getMessage() );
            }
        else{
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "Function Full");
        }
        repaint();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
