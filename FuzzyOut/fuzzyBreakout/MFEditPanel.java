package fuzzyBreakout;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MFEditPanel extends JPanel {
	
	private static final long serialVersionUID = 1077060848088589028L;
	public JTextField[] LowDanger,MediumDanger,HighDanger;
	public JTextField[] MultLow,MultMed,MultHigh;
	public  JTextField[] Still,SlowLeft,FastLeft,SlowRight,FastRight;
	public JTextField[] CloseRight,FarLeft,CloseLeft,ZeroD,FarRight;
	//To avoid Thread safety problems, the main thread will check the change flag every cycle and, if needed, will update its values
	public boolean ChangeFlag;
	
	public MFEditPanel(int[][] points) {
		this.setBackground(Color.white);
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		this.setLayout(new GridBagLayout());
		c.gridx = 0;
		c.gridy = 0;
		c.gridheight=2;
		this.add(this.getBallDistancePanel(points),c);
		c.gridx = 1;
		c.gridheight=1;
		this.add(this.getDangerLevelPanel(points),c);
		c.gridy=1;
		this.add(this.getMultiplicatorPanel(points),c);
		c.gridy=0;
		c.gridx=2;
		c.gridheight=2;
		this.add(this.getSpeedPanel(points),c);
		c.gridy=0;
		c.gridx=3;
		c.gridheight = 1;
		JButton modifyButton = new JButton("Modify MF");
		modifyButton.addActionListener(
			    new ActionListener() {
					public void actionPerformed(ActionEvent e) {
			            ChangeFlag = true;
			        }
			    });
		this.add(modifyButton,c);
		c.gridy=1;
		//JButton defaultButton = new JButton("Default MF");
		//this.add(defaultButton,c);
	}

	private Component getSpeedPanel(int[][] points) {
		JPanel SPPanel = new JPanel();
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		SPPanel.setBackground(Color.green);
		SPPanel.setLayout(new GridBagLayout());
		//Setting the top part of the Panel
		c.insets = new Insets(1,1,1,1);
		c.weightx = 0.5;
		c.gridx = 1;
		c.gridy = 0;
		JLabel Top1 = new JLabel("Start");
		SPPanel.add(Top1, c);
		c.gridx = 2;
		JLabel Top2 = new JLabel("Top1");
		SPPanel.add(Top2, c);
		c.gridx = 3;
		JLabel Top3 = new JLabel("Top2");
		SPPanel.add(Top3, c);
		c.gridx = 4;
		JLabel Top4 = new JLabel("End");
		SPPanel.add(Top4, c);
	
	//Begin Zero
		Still = new JTextField[4];
		c.gridy = 1;
		Still[0] = new JTextField(3);
		Still[0].setText(new Integer(points[11][0]).toString());
		c.gridx = 1;
		SPPanel.add(Still[0], c);
		Still[1] = new JTextField(3);
		Still[1].setText(new Integer(points[11][1]).toString());
		c.gridx = 2;
		SPPanel.add(Still[1], c);
		Still[2] = new JTextField(3);
		Still[2].setText(new Integer(points[11][2]).toString());
		c.gridx = 3;
		SPPanel.add(Still[2], c);
		Still[3] = new JTextField(3);
		Still[3].setText(new Integer(points[11][3]).toString());
		c.gridx = 4;
		SPPanel.add(Still[3], c);
		c.gridx = 0;
		JLabel FLEditLabel = new JLabel("Zero");
		SPPanel.add(FLEditLabel, c);
		
	//Begin SlowLeft	
		SlowLeft = new JTextField[4];
		c.gridx = 0;
		c.gridy = 2;
		JLabel CLEditLabel = new JLabel("SlowLeft");
		SPPanel.add(CLEditLabel, c);
		 SlowLeft[0] = new JTextField(3);
		 SlowLeft[0].setText(new Integer(points[12][0]).toString());
		c.gridx = 1;
		SPPanel.add( SlowLeft[0], c);
		SlowLeft[1] = new JTextField(3);
		SlowLeft[1].setText(new Integer(points[12][1]).toString());
		c.gridx = 2;
		SPPanel.add(SlowLeft[1], c);
		SlowLeft[2] = new JTextField(3);
		SlowLeft[2].setText(new Integer(points[12][2]).toString());
		c.gridx = 3;
		SPPanel.add(SlowLeft[2], c);
		SlowLeft[3] = new JTextField(3);
		SlowLeft[3].setText(new Integer(points[12][3]).toString());
		c.gridx = 4;
		SPPanel.add(SlowLeft[3] , c);
	
	//Begin FasttLeft 
		c.gridy = 3;
		c.gridx = 0;
		JLabel ZEEditLabel = new JLabel("FastLeft");
		SPPanel.add(ZEEditLabel, c);
		FastLeft = new JTextField[4];
		FastLeft[0] = new JTextField(3);
		FastLeft[0].setText(new Integer(points[13][0]).toString());
		c.gridx = 1;
		SPPanel.add(FastLeft[0], c);
		FastLeft[1] = new JTextField(3);
		FastLeft[1].setText(new Integer(points[13][1]).toString());
		c.gridx = 2;
		SPPanel.add(FastLeft[1], c);
		FastLeft[2] = new JTextField(3);
		FastLeft[2].setText(new Integer(points[13][2]).toString());
		c.gridx = 3;
		SPPanel.add(FastLeft[2], c);
		FastLeft[3] = new JTextField(3);
		FastLeft[3].setText(new Integer(points[13][3]).toString());
		c.gridx = 4;
		SPPanel.add(FastLeft[3], c);

//Begin SlowRight
		SlowRight = new JTextField[4];
		JLabel CREditLabel = new JLabel("SlowRight");
		c.gridx = 0;
		c.gridy = 4;
		SPPanel.add(CREditLabel, c);
		
		SlowRight[0] = new JTextField(3);
		SlowRight[0].setText(new Integer(points[14][0]).toString());
		c.gridx = 1;
		SPPanel.add(SlowRight[0], c);
		SlowRight[1] = new JTextField(3);
		SlowRight[1].setText(new Integer(points[14][1]).toString());
		c.gridx = 2;
		SPPanel.add(SlowRight[1], c);
		SlowRight[2] = new JTextField(3);
		SlowRight[2].setText(new Integer(points[14][2]).toString());
		c.gridx = 3;
		SPPanel.add(SlowRight[2], c);
		SlowRight[3] = new JTextField(3);
		SlowRight[3].setText(new Integer(points[14][3]).toString());
		c.gridx = 4;
		SPPanel.add(SlowRight[3], c);


		//Begin FastRight
		FastRight = new JTextField[4];
		JLabel FREditLabel = new JLabel("FastRight");
		c.gridx = 0;
		c.gridy = 5;
		SPPanel.add(FREditLabel, c);
		FastRight[0]= new JTextField(3);
		FastRight[0].setText(new Integer(points[15][0]).toString());
		c.gridx = 1;
		SPPanel.add(FastRight[0], c);
		FastRight[1] = new JTextField(3);
		FastRight[1].setText(new Integer(points[15][1]).toString());
		c.gridx = 2;
		SPPanel.add(FastRight[1], c);
		FastRight[2] = new JTextField(3);
		FastRight[2].setText(new Integer(points[15][2]).toString());
		c.gridx = 3;
		SPPanel.add(FastRight[2], c);
		FastRight[3] = new JTextField(3);
		FastRight[3].setText(new Integer(points[15][3]).toString());
		c.gridx = 4;
		SPPanel.add(FastRight[3], c);
		return SPPanel;
	}


	private Component getMultiplicatorPanel(int[][] points) {
		JPanel MPanel = new JPanel();
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		MPanel.setLayout(new GridBagLayout());
		MPanel.setBackground(Color.yellow);
		
		MultLow = new JTextField[4];	
		c.insets = new Insets(1,1,1,1);
		c.gridy = 0;
		c.gridx = 0;
		JLabel FLEditLabel = new JLabel("Low");
		MPanel.add(FLEditLabel, c);
		MultLow[0] = new JTextField(3);
		MultLow[0].setText(new Integer(points[8][0]).toString());
		c.gridx = 1;
	
		MPanel.add(MultLow[0], c);
		MultLow[1] = new JTextField(3);
		MultLow[1].setText(new Integer(points[8][1]).toString());
		c.gridx = 2;
		MPanel.add(MultLow[1], c);
		
		MultLow[2] = new JTextField(3);
		MultLow[2].setText(new Integer(points[8][2]).toString());
		c.gridx = 3;
		MPanel.add(MultLow[2], c);
		
		MultLow[3] = new JTextField(3);
		MultLow[3].setText(new Integer(points[8][2]).toString());
		c.gridx = 4;
		MPanel.add(MultLow[3], c);
		
		MultMed = new JTextField[4];	
		c.insets = new Insets(1,1,1,1);
		c.gridy = 1;
		c.gridx = 0;
		JLabel FMEditLabel = new JLabel("Medium");
		MPanel.add(FMEditLabel, c);
		MultMed[0] = new JTextField(3);
		MultMed[0].setText(new Integer(points[9][0]).toString());
		c.gridx = 1;
	
		MPanel.add(MultMed[0], c);
		MultMed[1] = new JTextField(3);
		MultMed[1].setText(new Integer(points[9][1]).toString());
		c.gridx = 2;
		MPanel.add(MultMed[1], c);
		
		MultMed[2] = new JTextField(3);
		MultMed[2].setText(new Integer(points[9][2]).toString());
		c.gridx = 3;
		MPanel.add(MultMed[2], c);
		
		MultMed[3] = new JTextField(3);
		MultMed[3].setText(new Integer(points[9][2]).toString());
		c.gridx = 4;
		MPanel.add(MultMed[3], c);
		
		MultHigh = new JTextField[4];	
		c.insets = new Insets(1,1,1,1);
		c.gridy = 2;
		c.gridx = 0;
		JLabel FHEditLabel = new JLabel("High");
		MPanel.add(FHEditLabel, c);
		MultHigh[0] = new JTextField(3);
		MultHigh[0].setText(new Integer(points[10][0]).toString());
		c.gridx = 1;
		MPanel.add(MultHigh[0], c);
		MultHigh[1] = new JTextField(3);
		MultHigh[1].setText(new Integer(points[10][1]).toString());
		c.gridx = 2;
		MPanel.add(MultHigh[1], c);
		MultHigh[2] = new JTextField(3);
		MultHigh[2].setText(new Integer(points[10][2]).toString());
		c.gridx = 3;
		MPanel.add(MultHigh[2], c);
		MultHigh[3] = new JTextField(3);
		MultHigh[3].setText(new Integer(points[10][3]).toString());
		c.gridx = 4;
		MPanel.add(MultHigh[3], c);
		

		return MPanel;
	}

	private JPanel getDangerLevelPanel(int[][] points) {
		JPanel DLPanel = new JPanel();
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		DLPanel.setLayout(new GridBagLayout());
		DLPanel.setBackground(Color.CYAN);
		
		LowDanger = new JTextField[4];	
		c.insets = new Insets(1,1,1,1);
		c.weightx = 0.5;
		c.gridx = 1;
		c.gridy = 0;
		JLabel Top1 = new JLabel("Start");
		DLPanel.add(Top1, c);
		c.gridx = 2;
		JLabel Top2 = new JLabel("Top1");
		DLPanel.add(Top2, c);
		c.gridx = 3;
		JLabel Top3 = new JLabel("Top2");
		DLPanel.add(Top3, c);
		c.gridx = 4;
		JLabel Top4 = new JLabel("End");
		DLPanel.add(Top4, c);
			c.gridy = 1;
		c.gridx = 0;
		JLabel FLEditLabel = new JLabel("Low");
		DLPanel.add(FLEditLabel, c);
		LowDanger[0] = new JTextField(3);
		LowDanger[0].setText(new Integer(points[7][0]).toString());
		c.gridx = 1;
	
		DLPanel.add(LowDanger[0], c);
		
		LowDanger[1] = new JTextField(3);
		LowDanger[1].setText(new Integer(points[7][1]).toString());
		c.gridx = 2;
		DLPanel.add(LowDanger[1], c);
		
		LowDanger[2] = new JTextField(3);
		LowDanger[2].setText(new Integer(points[7][2]).toString());
		c.gridx = 3;
		DLPanel.add(LowDanger[2], c);
		
		LowDanger[3] = new JTextField(3);
		LowDanger[3].setText(new Integer(points[7][3]).toString());
		c.gridx = 4;
		DLPanel.add(LowDanger[3], c);
		
		
		//MediumDanger
		MediumDanger = new JTextField[4];
		c.gridy = 2;
		c.gridx = 0;
		JLabel MDLabel = new JLabel("Medium");
		DLPanel.add(MDLabel, c);
		MediumDanger[0] = new JTextField(3);
		MediumDanger[0].setText(new Integer(points[6][0]).toString());
		c.gridx = 1;
		DLPanel.add(MediumDanger[0], c);		
		MediumDanger[1] = new JTextField(3);
		MediumDanger[1].setText(new Integer(points[6][1]).toString());
		c.gridx = 2;
		DLPanel.add(MediumDanger[1], c);
		MediumDanger[2] = new JTextField(3);
		MediumDanger[2].setText(new Integer(points[6][2]).toString());
		c.gridx = 3;
		DLPanel.add(MediumDanger[2], c);
		MediumDanger[3] = new JTextField(3);
		MediumDanger[3].setText(new Integer(points[6][3]).toString());
		c.gridx = 4;
		DLPanel.add(MediumDanger[3], c);
	
		
		HighDanger = new JTextField[4];
		c.gridy = 3;
		c.gridx = 0;
		JLabel HighEditLabel = new JLabel("High");
		DLPanel.add(HighEditLabel, c);
		HighDanger[0] = new JTextField(3);
		HighDanger[0].setText(new Integer(points[5][0]).toString());
		c.gridx = 1;
		DLPanel.add(HighDanger[0], c);		
		HighDanger[1] = new JTextField(3);
		HighDanger[1].setText(new Integer(points[5][1]).toString());
		c.gridx = 2;
		DLPanel.add(HighDanger[1], c);
		HighDanger[2] = new JTextField(3);
		HighDanger[2].setText(new Integer(points[5][2]).toString());
		c.gridx = 3;
		DLPanel.add(HighDanger[2], c);
		HighDanger[3] = new JTextField(3);
		HighDanger[3].setText(new Integer(points[5][3]).toString());
		c.gridx = 4;
		DLPanel.add(HighDanger[3], c);
		
		return DLPanel;
	}

	private JPanel getBallDistancePanel(int[][] points) {
		JPanel BDPanel = new JPanel();
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		BDPanel.setLayout(new GridBagLayout());
		BDPanel.setBackground(Color.orange);
		
		c.insets = new Insets(1,1,1,1);
		c.weightx = 0.5;
		c.gridx = 1;
		c.gridy = 0;
		JLabel Top1 = new JLabel("Start");
		BDPanel.add(Top1, c);
		c.gridx = 2;
		JLabel Top2 = new JLabel("Top1");
		BDPanel.add(Top2, c);
		c.gridx = 3;
		JLabel Top3 = new JLabel("Top2");
		BDPanel.add(Top3, c);
		c.gridx = 4;
		JLabel Top4 = new JLabel("End");
		BDPanel.add(Top4, c);
		
		
	//Begin FarLeft
		FarLeft = new JTextField[4];
		FarLeft[0] = new JTextField(3);
		FarLeft[0] .setText(new Integer(points[0][0]).toString());
		c.gridx = 1;
		c.gridy = 1;
		BDPanel.add(FarLeft[0] , c);
		
		FarLeft[1]  = new JTextField(3);
		FarLeft[1].setText(new Integer(points[0][1]).toString());
		c.gridx = 2;
		BDPanel.add(FarLeft[1], c);
		
		FarLeft[2] = new JTextField(3);
		FarLeft[2].setText(new Integer(points[0][2]).toString());
		c.gridx = 3;
		BDPanel.add(FarLeft[2], c);
		
		FarLeft[3] = new JTextField(3);
		FarLeft[3].setText(new Integer(points[0][3]).toString());
		c.gridx = 4;
		BDPanel.add(FarLeft[3], c);
		
		
		c.gridx = 0;
		JLabel FLEditLabel = new JLabel("FL");
		BDPanel.add(FLEditLabel, c);
		
	//Begin CloseLeft	
		c.gridx = 0;
		c.gridy = 2;
		JLabel CLEditLabel = new JLabel("CL");
		CloseLeft = new JTextField[4];
		BDPanel.add(CLEditLabel, c);
		CloseLeft[0]= new JTextField(3);
		CloseLeft[0].setText(new Integer(points[1][0]).toString());
		c.gridx = 1;
		BDPanel.add(CloseLeft[0], c);
		CloseLeft[1]  = new JTextField(3);
		CloseLeft[1] .setText(new Integer(points[1][1]).toString());
		c.gridx = 2;
		BDPanel.add(CloseLeft[1] , c);
		CloseLeft[2] = new JTextField(3);
		CloseLeft[2].setText(new Integer(points[1][2]).toString());
		c.gridx = 3;
		BDPanel.add(CloseLeft[2] , c);
		CloseLeft[3]  = new JTextField(3);
		CloseLeft[3].setText(new Integer(points[1][3]).toString());
		c.gridx = 4;
		BDPanel.add(CloseLeft[3], c);
	
	//Begin Zero 
		c.gridy = 3;
		c.gridx = 0;
		JLabel ZEEditLabel = new JLabel("Zero");
		BDPanel.add(ZEEditLabel, c);
		ZeroD = new JTextField[4];
		ZeroD[0] = new JTextField(3);
		ZeroD[0].setText(new Integer(points[2][0]).toString());
		c.gridx = 1;
		BDPanel.add(ZeroD[0], c);
		
		ZeroD[1] = new JTextField(3);
		ZeroD[1].setText(new Integer(points[2][1]).toString());
		c.gridx = 2;
		BDPanel.add(ZeroD[1], c);

		ZeroD[2] = new JTextField(3);
		ZeroD[2].setText(new Integer(points[2][2]).toString());
		c.gridx = 3;
		BDPanel.add(ZeroD[2], c);
		
		ZeroD[3] = new JTextField(3);
		ZeroD[3].setText(new Integer(points[2][3]).toString());
		c.gridx = 4;
		BDPanel.add(ZeroD[3], c);

//Begin CloseRight		
		JLabel CREditLabel = new JLabel("CR");
		c.gridx = 0;
		c.gridy = 4;
		BDPanel.add(CREditLabel, c);
		CloseRight = new JTextField[4];
		CloseRight[0] = new JTextField(3);
		CloseRight[0].setText(new Integer(points[3][0]).toString());
		c.gridx = 1;
		BDPanel.add(CloseRight[0], c);
		CloseRight[1] = new JTextField(3);
		CloseRight[1].setText(new Integer(points[3][1]).toString());
		c.gridx = 2;
		BDPanel.add(CloseRight[1], c);
		CloseRight[2] = new JTextField(3);
		CloseRight[2].setText(new Integer(points[3][2]).toString());
		c.gridx = 3;
		BDPanel.add(CloseRight[2], c);
		CloseRight[3] = new JTextField(3);
		CloseRight[3].setText(new Integer(points[3][3]).toString());
		c.gridx = 4;
		BDPanel.add(CloseRight[3], c);


		//Begin FarRight
		JLabel FREditLabel = new JLabel("FR");
		c.gridx = 0;
		c.gridy = 5;
		FarRight = new JTextField[4];
		BDPanel.add(FREditLabel, c);
		FarRight[0]= new JTextField(3);
		FarRight[0].setText(new Integer(points[4][0]).toString());
		c.gridx = 1;
		BDPanel.add(FarRight[0], c);
		FarRight[1] = new JTextField(3);
		FarRight[1].setText(new Integer(points[4][1]).toString());
		c.gridx = 2;
		BDPanel.add(FarRight[1], c);
		FarRight[2] = new JTextField(3);
		FarRight[2].setText(new Integer(points[4][2]).toString());
		c.gridx = 3;
		BDPanel.add(FarRight[2], c);
		FarRight[3] = new JTextField(3);
		FarRight[3].setText(new Integer(points[4][3]).toString());
		c.gridx = 4;
		BDPanel.add(FarRight[3], c);
		
		return BDPanel;
	}	
	
}
