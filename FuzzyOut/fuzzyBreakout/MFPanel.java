package fuzzyBreakout;


/**
 * 
 * 
 * 
 * THIS CLASS IS UNUSED
 * 
 * 
 * 
 */

import java.awt.Color;
import java.awt.Dimension;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JPanel;

public class MFPanel extends JPanel  {

    private static final long serialVersionUID = 7197048996128727637L;
	private Vector<int[]> mfunctions;
    private final Color colors[] = {
            Color.red, Color.blue, Color.green, Color.orange,
            Color.cyan, Color.magenta, Color.darkGray, Color.yellow};
	private int scale;
	private int zero;
	
    /**
     * 
     * @param mfunctions
     * @param scale Represents a factor points will be multiplied for
     * @param zero Represents the zero X axis
     */
	public MFPanel(Vector<int[]> mfunctions,int scale,int zero) 
	{
		super();
		this.scale = scale;
		this.zero = zero;
		this.mfunctions = mfunctions;
		this.setPreferredSize(new Dimension(680,100));
	}
	
	
	public void setPoints(Vector<int[]> points) {
		this.mfunctions = points;
	}
    
    public void paintComponent(java.awt.Graphics g) {
          super.paintComponent( g ); // call superclass's paintComponent
          int i= 1;
          
          //Painitng axes
          g.setColor(Color.BLACK);
          g.drawLine( 80,80, 600,80 ) ;
          g.drawLine( zero,80, zero,2 ) ;
          
          Iterator <int[]> variter = this.mfunctions.iterator();
          while(variter.hasNext()) {
        	  int[]points = variter.next();
	           	g.setColor(colors[i%colors.length]);
		         //Priting First Part	               
	           g.drawLine( zero+points[0]*scale,80, zero+points[1]*scale,10 ) ;
	  	        //Printing Top Part
	          g.drawLine( zero+points[1]*scale, 10, zero+points[2]*scale,10  );
	              //Printing descending part
	           g.drawLine( zero+points[2]*scale, 10, zero+points[3]*scale,80 ) ;
	           i++;
	    	  
          }
	                /*
	           Iterator<Vector<int[]>> variter = this.mfunctions.iterator();
	           int i = 1;
	           int lingvar = 0;
	           //Part1 : cycle over linguistic variables
	           while(variter.hasNext()) 
	          {
	        	   Iterator<int[]> funciter = variter.next().iterator();
	        	   while(funciter.hasNext())
	        	   {
	           int[]points = funciter.next();
	           	g.setColor(colors[i%colors.length]);
		         //Priting First Part	               
	           g.drawLine( 350+points[0],100+(100*lingvar), 350+points[1],10+(100*lingvar) ) ;
	  	      
	          g.drawLine( 350+points[1], 10+(100*lingvar), 350+points[2],10+(100*lingvar)  );
	              //Printing descending part
	           g.drawLine(  350+points[2], 10+(100*lingvar), 350+points[3],100+(100*lingvar) ) ;
	           i++;
	 	          }//end funciter iteration
	        	   lingvar++;
	          }//end variter iteration
*/
	          } // end method paintComponent


}
