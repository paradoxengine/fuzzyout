/**
 * Fuzzy Out - Fuzzy Logic IA Breakout Game
 * Claudio Criscione - claudio@criscio.net - Summer 2006
 * Based on game code by Brian Postma - b.postma@hetnet.nl
 * Using Fuzzy Engine by Edward S. Sazonov
 * 
     Copyright 2006 Claudio Criscione

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */

import java.applet.Applet;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import fuzzy.EvaluationException;
import fuzzy.FuzzyBlockOfRules;
import fuzzy.FuzzyEngine;
import fuzzy.LinguisticVariable;
import fuzzy.NoRulesFiredException;
import fuzzy.RulesParsingException;
import fuzzyBreakout.GamePanel;
import fuzzyBreakout.MFEditPanel;
import fuzzyBreakout.MFPanel;

public class Breakout extends Applet implements Runnable {
  private static final long serialVersionUID = 1951465271213799512L;

  // Size of the Game Panel
  private static final int GameHeight = 290;
  private static final int gameWidth = 300;

  // Image and thread data
  Graphics goff;
  Image ii;
  Thread thethread;

  // BREAKOUT DATA
  // Ball Position
  int ballx, bally;
  // Bat Position
  int batpos;
  int batdpos = 0;
  // Ball movement
  int balldx = 0, balldy = 0;
  int dxval;
  // Counter Data
  int ballsused;
  int hits;
  int count;

  // A boolean array controlling which bricks are now existing
  boolean[] showbrick;

  // Graphic data
  int bricksperline = 0;
  final int borderwidth = 5;
  final int batwidth = 20;
  final int ballsize = 5;
  final int batheight = 5;
  final int scoreheight = 20;
  final int screendelay = 300;
  final int brickwidth = 15;
  final int brickheight = 8;
  final int brickspace = 2;
  final int backcol = 0x102041;
  final int numlines = 4;
  final int startline = 32;

  // FUZZY DATA

  // The Fuzzy Engine is the core of the fuzzy system
  private FuzzyEngine fuzzyEngine = null;

  // Linguistic variables used in the software
  private LinguisticVariable ballCloseness;
  private LinguisticVariable speed;
  private LinguisticVariable dangerLevel;
  private LinguisticVariable multiplicator;
  private LinguisticVariable ballDirection;
  private LinguisticVariable wallCloseness;

  // Helper variable, used to avoid thread dangers
  private int rulesRule;

  // Display Data
  private String[] Ruleset;
  private JTextArea rulesInput;
  private JPanel rulesPanel;
  private JPanel gamePanel;
  private MFEditPanel mfPanel;

  // Membership functions
  private int[] mfCL;
  private int[] mfFL;
  private int[] mfZE;
  private int[] mfCR;
  private int[] mfFR;

  private int[] mfDH;
  private int[] mfDM;
  private int[] mfDL;
  // mfMultPoints is a multidimensional array, a smarter way to store points
  // than using many variables
  // mf?? should be refactored, but they work like this right now.
  private int[][] mfMultPoints, mfSpeedPoints;

  // Counters
  private JLabel lostCounter;
  private JLabel hitsCounter;

  // MFgraphic panels
  MFPanel[] grafici;

  /**
   * In this method is placed the very heart of the fuzzy controller: Linguistic
   * Variables are initialised, rules are started and defuzzyfication is done
   * 
   */
  public void BatDummyMove() {

    // Init LV
    ballCloseness = new LinguisticVariable("ballCloseness");
    speed = new LinguisticVariable("speed");
    dangerLevel = new LinguisticVariable("dangerLevel");
    multiplicator = new LinguisticVariable("multiplicator");
    ballDirection = new LinguisticVariable("ballDirection");
    wallCloseness = new LinguisticVariable("wallCloseness");
    fuzzyEngine = new FuzzyEngine();

    // Update MFs values, if the user has done so
    if (mfPanel.ChangeFlag == true) {
      this.updateMFs();
      mfPanel.ChangeFlag = false;
      this.hits = 0;
      this.ballsused = 0;
      this.lostCounter.setText(new Integer(this.ballsused).toString());
      this.hitsCounter.setText(new Integer(this.hits).toString());
    }

    // Assigning MF to the Linguistic Variables
    ballCloseness.add("CL", mfCL[0], mfCL[1], mfCL[2], mfCL[3]);
    ballCloseness.add("FL", mfFL[0], mfFL[1], mfFL[2], mfFL[3]);
    ballCloseness.add("ZERO", mfZE[0], mfZE[1], mfZE[2], mfZE[3]);
    ballCloseness.add("CR", mfCR[0], mfCR[1], mfCR[2], mfCR[3]);
    ballCloseness.add("FR", mfFR[0], mfFR[1], mfFR[2], mfFR[3]);

    speed.add("Still", this.mfSpeedPoints[0][0], this.mfSpeedPoints[0][1],
        this.mfSpeedPoints[0][2], this.mfSpeedPoints[0][3]);
    speed.add("SlowLeft", this.mfSpeedPoints[1][0], this.mfSpeedPoints[1][1],
        this.mfSpeedPoints[1][2], this.mfSpeedPoints[1][3]);
    speed.add("FastLeft", this.mfSpeedPoints[2][0], this.mfSpeedPoints[2][1],
        this.mfSpeedPoints[2][2], this.mfSpeedPoints[2][3]);
    speed.add("SlowRight", this.mfSpeedPoints[3][0], this.mfSpeedPoints[3][1],
        this.mfSpeedPoints[3][2], this.mfSpeedPoints[3][3]);
    speed.add("FastRight", this.mfSpeedPoints[4][0], this.mfSpeedPoints[4][1],
        this.mfSpeedPoints[4][2], this.mfSpeedPoints[4][3]);

    dangerLevel.add("High", mfDH[0], mfDH[1], mfDH[2], mfDH[3]);
    dangerLevel.add("Medium", mfDM[0], mfDM[1], mfDM[2], mfDM[3]);
    dangerLevel.add("Low", mfDL[0], mfDL[1], mfDM[2], mfDM[3]);

    multiplicator.add("Low", this.mfMultPoints[0][0], this.mfMultPoints[0][1],
        this.mfMultPoints[0][2], this.mfMultPoints[0][3]);
    multiplicator.add("Medium", this.mfMultPoints[1][0], this.mfMultPoints[1][1],
        this.mfMultPoints[1][2], this.mfMultPoints[1][3]);
    multiplicator.add("High", this.mfMultPoints[2][0], this.mfMultPoints[2][1],
        this.mfMultPoints[2][2], this.mfMultPoints[2][3]);

    ballDirection.add("Left", -100, -100, -1, 0);
    ballDirection.add("Right", 0, 1, 100, 100);

    wallCloseness.add("CloseLeft", 5, 5, 5, 25);
    wallCloseness.add("CloseRight", Breakout.gameWidth - 30, Breakout.gameWidth - 10,
        Breakout.gameWidth - 10, Breakout.gameWidth - 10);
    wallCloseness.add("Far", 20, 25, Breakout.gameWidth - 25, Breakout.gameWidth - 25);

    // Registering Variables: only registered variables can be used
    fuzzyEngine.register(ballCloseness);
    fuzzyEngine.register(speed);
    fuzzyEngine.register(dangerLevel);
    fuzzyEngine.register(multiplicator);
    fuzzyEngine.register(ballDirection);
    fuzzyEngine.register(wallCloseness);

    // Updating ruleSets: this is done via an UpdateFlag to avoid thread
    // troubles [random freezes]
    // rulesRule is set to 1, 2 or 3 to return to these rulesets
    if (this.rulesRule == 1) {
      this.rulesRule = 0;
      this.hits = 0;
      this.ballsused = 0;
      this.lostCounter.setText(new Integer(this.ballsused).toString());
      this.hitsCounter.setText(new Integer(this.hits).toString());
      this.rulesInput.setText(this.Ruleset[0]);
    }

    if (this.rulesRule == 2) {
      this.rulesRule = 0;
      this.hits = 0;
      this.ballsused = 0;
      this.lostCounter.setText(new Integer(this.ballsused).toString());
      this.hitsCounter.setText(new Integer(this.hits).toString());
      this.rulesInput.setText(this.Ruleset[0] + this.Ruleset[1]);
    }

    if (this.rulesRule == 3) {
      this.rulesRule = 0;
      this.hits = 0;
      this.ballsused = 0;
      this.lostCounter.setText(new Integer(this.ballsused).toString());
      this.hitsCounter.setText(new Integer(this.hits).toString());
      this.rulesInput.setText(this.Ruleset[0] + this.Ruleset[1] + this.Ruleset[2]);
    }

    // Feeding rules to the engine
    FuzzyBlockOfRules b = new FuzzyBlockOfRules(this.rulesInput.getText());
    fuzzyEngine.register(b);

    // Feeding input values to the Linguistic Variables
    ballCloseness.setInputValue(this.ballx - this.batpos);
    dangerLevel.setInputValue(this.bally);
    ballDirection.setInputValue(this.balldx);
    wallCloseness.setInputValue(this.ballx);

    // Go fuzzy!
    try {
      b.parseBlock();
    } catch (RulesParsingException e) {
      e.printStackTrace();
    }

    try {
      b.evaluateBlock();
    } catch (EvaluationException e) {
      e.printStackTrace();
    }

    try {
      // Fetch Results
      java.lang.Double result = speed.defuzzify();
      java.lang.Double mult = multiplicator.defuzzify();

      // Using a multiplicator factor to speed up in critical zones
      result = mult * result;
      long res = java.lang.Math.round(result);
      batpos += res;
      // Adding a Random contribute to end Loop sates
      if (this.bally > 160) {
        batpos += (-1 + (Math.random() * 2));
      }
    } catch (NoRulesFiredException e) {
      // TODO Improve errore managing
      // e.printStackTrace();
    }
  }

  public void CheckBat() {
    batpos += batdpos;
    if (batpos < borderwidth) {
      batpos = borderwidth;
    } else if (batpos > (Breakout.gameWidth - borderwidth - batwidth)) {
      batpos = (Breakout.gameWidth - borderwidth - batwidth);
    }
    if ((bally >= (Breakout.GameHeight - scoreheight - (2 * borderwidth) - ballsize))
        && (bally < (Breakout.GameHeight - scoreheight - (2 * borderwidth)))
        && ((ballx + ballsize) >= batpos) && (ballx <= (batpos + batwidth))) {
      bally = Breakout.GameHeight - scoreheight - ballsize - (borderwidth * 2);
      balldy = -dxval;
      balldx = CheckBatBounce(balldx, ballx - batpos);
    }
  }

  public int CheckBatBounce(int dy, int delta) {
    int sign;
    int stepsize, i = -ballsize, j = 0;

    this.hits++;
    this.hitsCounter.setText(new Integer(this.hits).toString());

    stepsize = (ballsize + batwidth) / 8;
    if (dy > 0) {
      sign = 1;
    } else {
      sign = -1;
    }
    while ((i < batwidth) && (delta > i)) {
      i += stepsize;
      j++;
    }
    switch (j) {
    case 0:
    case 1:
      return -4;
    case 2:
      return -3;
    case 7:
      return 3;
    case 3:
    case 6:
      return sign * 2;
    case 4:
    case 5:
      return sign * 1;
    default:
      return 4;
    }
  }

  public void CheckBricks() {
    int i, j, x, y;
    int xspeed = balldx;
    if (xspeed < 0) {
      xspeed = -xspeed;
    }
    int ydir = balldy;

    if ((bally < (startline - ballsize))
        || (bally > (startline + (numlines * (brickspace + brickheight))))) {
      return;
    }
    for (j = 0; j < numlines; j++) {
      for (i = 0; i < bricksperline; i++) {
        if (showbrick[(j * bricksperline) + i]) {
          y = startline + (j * (brickspace + brickheight));
          x = borderwidth + (i * (brickspace + brickwidth));
          if ((bally >= (y - ballsize)) && (bally < (y + brickheight)) && (ballx >= (x - ballsize))
              && (ballx < (x + brickwidth))) {
            showbrick[(j * bricksperline) + i] = false;
            // Where did we hit the brick
            if ((ballx >= (x - ballsize)) && (ballx <= ((x - ballsize) + 3))) { // leftside
              balldx = -xspeed;
            } else if ((ballx <= ((x + brickwidth) - 1)) && (ballx >= ((x + brickwidth) - 4))) { // rightside
              balldx = xspeed;
            }
            balldy = -ydir;
          }
        }
      }
    }
  }

  /**
   * Convert to int array converts an inputARray of JTextField to an int array
   * containing the values inside the JTextFields
   * 
   * @param inputArray
   *          the array to be converted
   * @return int[]
   */
  private int[] convertToIntArray(JTextField[] inputArray) {
    int i = 0;
    int[] output = new int[inputArray.length];
    while (i < inputArray.length) {
      output[i] = new Integer(inputArray[i].getText());
      i++;
    }
    return output;
  }

  public void DrawBricks() {
    int i, j;
    boolean nobricks = true;
    int colordelta = 255 / (numlines - 1);

    for (j = 0; j < numlines; j++) {
      for (i = 0; i < bricksperline; i++) {
        if (showbrick[(j * bricksperline) + i]) {
          nobricks = false;
          goff.setColor(new Color(255, j * colordelta, 255 - (j * colordelta)));
          goff.fillRect(borderwidth + (i * (brickwidth + brickspace)), startline
              + (j * (brickheight + brickspace)), brickwidth, brickheight);
        }
      }
    }
    if (nobricks) {
      InitBricks();

    }
  }

  public void DrawPlayField() {
    // Change this.GameHeight with d.height to have fullversion
    goff.setColor(Color.white);
    goff.fillRect(0, 0, Breakout.gameWidth, borderwidth);
    goff.fillRect(0, 0, borderwidth, Breakout.GameHeight);
    goff.fillRect(Breakout.gameWidth - borderwidth, 0, borderwidth, Breakout.GameHeight);

    goff.fillRect(batpos, Breakout.GameHeight - (2 * borderwidth) - scoreheight, batwidth,
        batheight); // bat
    goff.fillRect(ballx, bally, ballsize, ballsize); // ball
  }

  /**
   * GameInit will actually start the game, placing the ball in a pseudorandom
   * position and having it start moving
   * 
   */
  public void GameInit() {
    batpos = (int) (((Breakout.gameWidth - batwidth) / 2) + (Math.random() * 5));
    ballx = (int) (((Breakout.gameWidth - ballsize) / 2) + (Math.random() * 10));
    bally = (int) ((Breakout.GameHeight - ballsize - scoreheight - (2 * borderwidth)) + (Math
        .random() * 10));
    ;

    // Init counters
    ballsused = 0;
    hits = 0;

    dxval = 2;
    if (Math.random() < 0.5) {
      balldx = dxval;
    } else {
      balldx = -dxval;
    }
    balldy = -dxval;
    count = screendelay;
    batdpos = 0;

    InitBricks();
  }

  @Override
  public String getAppletInfo() {
    return ("FuzzyOut - by Claudio Criscione. Based on code by Brian Postma");
  }

  /**
   * This method will setup the rules panel, three buttons to change Ruleset and
   * a TextArea where the rules will be put into
   * 
   * @return the Rules Panel
   */
  private JPanel getRulesPanel() {
    JPanel myPanel = new JPanel();

    myPanel.setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    c.fill = GridBagConstraints.HORIZONTAL;

    JButton ruleset1 = new JButton("Ruleset 1");
    c.weightx = 0.3;
    c.gridx = 0;
    c.gridy = 0;
    myPanel.add(ruleset1, c);
    ruleset1.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        rulesRule = 1;
      }
    });

    JButton ruleset2 = new JButton("Ruleset 2");
    c.gridx = 1;
    myPanel.add(ruleset2, c);
    ruleset2.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        rulesRule = 2;
      }
    });

    JButton ruleset3 = new JButton("Ruleset 3");
    c.gridx = 2;
    myPanel.add(ruleset3, c);
    ruleset3.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        rulesRule = 3;
      }
    });

    rulesInput = new JTextArea();
    // This Document Listener is used to reset counter at every rules change
    rulesInput.getDocument().addDocumentListener(new DocumentListener() {

      public void changedUpdate(DocumentEvent e) {
        hits = 0;
        ballsused = 0;
        lostCounter.setText(new Integer(ballsused).toString());
        hitsCounter.setText(new Integer(hits).toString());
      }

      public void insertUpdate(DocumentEvent e) {
        hits = 0;
        ballsused = 0;
        lostCounter.setText(new Integer(ballsused).toString());
        hitsCounter.setText(new Integer(hits).toString());
      }

      public void removeUpdate(DocumentEvent e) {
        hits = 0;
        ballsused = 0;
        lostCounter.setText(new Integer(ballsused).toString());
        hitsCounter.setText(new Integer(hits).toString());
      }
    });

    rulesInput.setText(Ruleset[0]);
    JScrollPane scrollPane = new JScrollPane(rulesInput);
    scrollPane.setPreferredSize(new Dimension(250, 300));
    c.gridx = 0;
    c.gridy = 1;
    c.weightx = 1;
    c.gridwidth = 3;
    myPanel.add(scrollPane, c);
    JLabel Label1 = new JLabel("Rules - Modify these rules to change Fuzzy System Behaviour");
    c.gridy = 2;
    myPanel.add(Label1, c);
    return myPanel;
  }

  /**
   * GoFuzzy will simulate the game, moving the ball and the bat and eventually
   * destroying bricks
   * 
   */
  public void GoFuzzy() {
    MoveBall();
    CheckBat();
    CheckBricks();
    BatDummyMove();
    DrawPlayField();
    DrawBricks();
  }

  /**
   * Init is the first method to be called by the applet loader
   */
  @Override
  public void init() {
    this.Ruleset = new String[3];
    this.initDefaultRules();
    grafici = new MFPanel[4];
    // Points is used in the edit Panel to change the MFs: it represents the MF
    // points
    int[][] points = new int[16][4];
    points[0] = this.mfCL;
    points[1] = this.mfFL;
    points[2] = this.mfZE;
    points[3] = this.mfCR;
    points[4] = this.mfFR;
    // DangerLevel - Init Points
    points[5] = this.mfDH;
    points[6] = this.mfDM;
    points[7] = this.mfDL;
    // Multiplicator - Init Points
    points[8] = this.mfMultPoints[0];
    points[9] = this.mfMultPoints[1];
    points[10] = this.mfMultPoints[2];
    // Speed - init Points
    points[11] = this.mfSpeedPoints[0];
    points[12] = this.mfSpeedPoints[1];
    points[13] = this.mfSpeedPoints[2];
    points[14] = this.mfSpeedPoints[3];
    points[15] = this.mfSpeedPoints[4];

    // Both have to be initialised before adding the rulesPanel to avoid null
    // pointer exception
    // in the document event handler used to put them to 0 after a change in the
    // ruleset
    lostCounter = new JLabel("0");
    hitsCounter = new JLabel("0");

    // Initialising the GUI: much Swing code down there
    rulesPanel = this.getRulesPanel();
    rulesPanel.setBorder(BorderFactory.createLineBorder(Color.black));
    gamePanel = new GamePanel();
    gamePanel.setPreferredSize(new Dimension(250, Breakout.GameHeight));
    mfPanel = new MFEditPanel(points);

    // Setting Layout
    GridBagLayout gridbag = new GridBagLayout();
    GridBagConstraints c = new GridBagConstraints();
    this.setLayout(gridbag);
    c.gridwidth = 2;
    c.ipadx = 30;
    c.gridx = 0;
    c.gridy = 0;
    this.add(gamePanel, c);
    c.gridy = 0;
    c.gridx = 2;
    c.ipadx = 0;
    this.add(rulesPanel, c);
    c.gridy = 1;
    c.gridx = 0;
    c.gridwidth = 1;

    // Adding Counter Block
    JLabel lostLabel = new JLabel("Lost Balls Counter: ");
    this.add(lostLabel, c);
    c.gridx = 1;
    c.insets = new Insets(0, 30, 0, 0);
    c.fill = GridBagConstraints.HORIZONTAL;
    this.add(lostCounter, c);
    c.fill = GridBagConstraints.NONE;
    c.insets = new Insets(0, 0, 0, 0);
    c.gridx = 2;
    JLabel hitsLabel = new JLabel("Bat Hits Counter");
    this.add(hitsLabel, c);
    c.gridx = 3;
    c.insets = new Insets(0, 30, 0, 0);
    c.fill = GridBagConstraints.HORIZONTAL;
    this.add(hitsCounter, c);

    // Adding Modify Panel Block
    c.insets = new Insets(0, 0, 0, 0);
    c.fill = GridBagConstraints.NONE;
    c.gridx = 0;
    c.gridy = 2;
    c.gridwidth = 4;
    c.insets = new Insets(5, 0, 0, 0);
    this.add(mfPanel, c);
    c.gridy = 3;

    // Adding grafici panel
    Vector<Vector<int[]>> mfVectors = this.prepareVectorForGraph(points);
    grafici[0] = new MFPanel(mfVectors.get(0), 1, 350);
    this.add(grafici[0], c);
    c.gridy = 4;
    grafici[1] = new MFPanel(mfVectors.get(1), 1, 90);
    this.add(grafici[1], c);
    c.gridy = 5;
    grafici[2] = new MFPanel(mfVectors.get(2), 10, 350);
    this.add(grafici[2], c);
    c.gridy = 6;
    grafici[3] = new MFPanel(mfVectors.get(3), 10, 350);
    this.add(grafici[3], c);

    this.setVisible(true);

    gamePanel.setBackground(new Color(backcol));

    bricksperline = (Breakout.gameWidth - (2 * borderwidth)) / (brickwidth + brickspace);
    showbrick = new boolean[bricksperline * numlines];

    // Sarts the game
    GameInit();
  }

  /**
   * initBricks will simply place the bricks on the game panel
   * 
   */
  public void InitBricks() {
    int i;
    for (i = 0; i < (numlines * bricksperline); i++) {
      showbrick[i] = true;
    }
  }

  private void initDefaultBallCloseness() {

    this.mfCL = new int[4];
    this.mfFL = new int[4];
    this.mfZE = new int[4];
    this.mfCR = new int[4];
    this.mfFR = new int[4];

    this.mfFL[0] = -200;
    this.mfFL[1] = -200;
    this.mfFL[2] = -60;
    this.mfFL[3] = -40;

    this.mfCL[0] = -60;
    this.mfCL[1] = -40;
    this.mfCL[2] = -5;
    this.mfCL[3] = -1;

    this.mfZE[0] = -5;
    this.mfZE[1] = 0;
    this.mfZE[2] = 0;
    this.mfZE[3] = 5;

    this.mfCR[0] = 1;
    this.mfCR[1] = 5;
    this.mfCR[2] = 40;
    this.mfCR[3] = 60;

    this.mfFR[0] = 40;
    this.mfFR[1] = 60;
    this.mfFR[2] = 200;
    this.mfFR[3] = 200;

  }

  private void initDefaultDangerLevel() {
    this.mfDH = new int[4];
    this.mfDH[0] = 180;
    this.mfDH[1] = 190;
    this.mfDH[2] = 290;
    this.mfDH[3] = 290;
    this.mfDM = new int[4];
    this.mfDM[0] = 80;
    this.mfDM[1] = 90;
    this.mfDM[2] = 180;
    this.mfDM[3] = 190;
    this.mfDL = new int[4];
    this.mfDL[0] = 0;
    this.mfDL[1] = 0;
    this.mfDL[2] = 90;
    this.mfDL[3] = 100;
  }

  private void initDefaultMultiplicator() {
    this.mfMultPoints = new int[3][4];

    this.mfMultPoints[0][0] = 0;
    this.mfMultPoints[0][1] = 1;
    this.mfMultPoints[0][2] = 1;
    this.mfMultPoints[0][3] = 2;

    this.mfMultPoints[1][0] = 1;
    this.mfMultPoints[1][1] = 2;
    this.mfMultPoints[1][2] = 2;
    this.mfMultPoints[1][3] = 3;

    this.mfMultPoints[2][0] = 2;
    this.mfMultPoints[2][1] = 3;
    this.mfMultPoints[2][2] = 3;
    this.mfMultPoints[2][3] = 4;
  }

  /**
   * This method initlialises all the default rulesets, adding rules to a base
   * set NOTE: This method is used to initalise all the MF default values.
   */
  private void initDefaultRules() {
    Ruleset[0] = new String();
    Ruleset[0] += "if ballCloseness is ZERO then speed is Still";
    Ruleset[0] += "\n";
    Ruleset[0] += "if ballCloseness is CL then speed is SlowLeft";
    Ruleset[0] += "\n";
    Ruleset[0] += "if ballCloseness is FL then speed is FastLeft";
    Ruleset[0] += "\n";
    Ruleset[0] += "if ballCloseness is FR then speed is FastRight";
    Ruleset[0] += "\n";
    Ruleset[0] += "if ballCloseness is CR then speed is SlowRight";
    Ruleset[0] += "\n";

    // Danger Level
    Ruleset[0] += "if dangerLevel is High then multiplicator is High";
    Ruleset[0] += "\n";
    Ruleset[0] += "if dangerLevel is Medium then multiplicator is Medium";
    Ruleset[0] += "\n";
    Ruleset[0] += "if dangerLevel is Low then multiplicator is Low";
    Ruleset[0] += "\n";

    // Avoid too movement
    Ruleset[1] = new String();
    Ruleset[1] += "if dangerLevel is very Low then speed is Still";
    Ruleset[1] += "\n";

    Ruleset[1] += "if ballDirection is Right and ballCloseness is ZERO then speed is SlowRight";
    Ruleset[1] += "\n";
    Ruleset[1] += "if ballDirection is Left and ballCloseness is ZERO then speed is SlowLeft";
    Ruleset[1] += "\n";

    // Wall bounce fix!
    Ruleset[2] = new String();
    Ruleset[2] += "if ballDirection is Right and wallCloseness is CloseRight and dangerLevel is Medium then speed is FastLeft";
    Ruleset[2] += "\n";
    Ruleset[2] += "if ballDirection is Left and wallCloseness is CloseLeft  and dangerLevel is Medium then  speed is FastRight";
    Ruleset[2] += "\n";
    Ruleset[2] += "if ballDirection is Right and wallCloseness is CloseRight and dangerLevel is High  then speed is FastLeft";
    Ruleset[2] += "\n";
    Ruleset[2] += "if ballDirection is Left and wallCloseness is CloseLeft  and dangerLevel is High then  speed is FastRight";
    Ruleset[2] += "\n";
    Ruleset[2] += "if ballDirection is Right and wallCloseness is CloseRight and dangerLevel is Low  then speed is SlowLeft";
    Ruleset[2] += "\n";
    Ruleset[2] += "if ballDirection is Left and wallCloseness is CloseLeft  and dangerLevel is Low then  speed is SlowRight";
    Ruleset[2] += "\n";

    // Initalising MF
    this.initDefaultBallCloseness();
    this.initDefaultDangerLevel();
    this.initDefaultMultiplicator();
    this.initDefaultSpeed();

  }

  /**
   * initDefaultSpeed is used to init the default MF points init
   * this.mfSpeedPoints. [0] Is Zero, [1] is SlowLeft, [2] is FastLeft, [3] is
   * SlowRight and [4] is FastRight
   */
  private void initDefaultSpeed() {

    this.mfSpeedPoints = new int[5][4];

    this.mfSpeedPoints[0][0] = -1;
    this.mfSpeedPoints[0][1] = 0;
    this.mfSpeedPoints[0][2] = 0;
    this.mfSpeedPoints[0][3] = 1;

    this.mfSpeedPoints[1][0] = -3;
    this.mfSpeedPoints[1][1] = -2;
    this.mfSpeedPoints[1][2] = -2;
    this.mfSpeedPoints[1][3] = -1;

    this.mfSpeedPoints[2][0] = -6;
    this.mfSpeedPoints[2][1] = -5;
    this.mfSpeedPoints[2][2] = -5;
    this.mfSpeedPoints[2][3] = -4;

    this.mfSpeedPoints[3][0] = 1;
    this.mfSpeedPoints[3][1] = 2;
    this.mfSpeedPoints[3][2] = 2;
    this.mfSpeedPoints[3][3] = 3;

    this.mfSpeedPoints[4][0] = 4;
    this.mfSpeedPoints[4][1] = 5;
    this.mfSpeedPoints[4][2] = 5;
    this.mfSpeedPoints[4][3] = 6;

  }

  public void MoveBall() {
    ballx += balldx;
    bally += balldy;
    if (bally <= borderwidth) {
      balldy = -balldy;
      bally = borderwidth;
    }
    if (bally >= (Breakout.GameHeight - ballsize - scoreheight)) {
      this.ballsused++;
      this.lostCounter.setText(new Integer(this.ballsused).toString());
      ballx = batpos + ((batwidth - ballsize) / 2);
      bally = startline + (numlines * (brickheight + brickspace));
      balldy = dxval;
      balldx = 0;
    }
    if (ballx >= (Breakout.gameWidth - borderwidth - ballsize)) {
      balldx = -balldx;
      ballx = Breakout.gameWidth - borderwidth - ballsize;
    }
    if (ballx <= borderwidth) {
      balldx = -balldx;
      ballx = borderwidth;
    }
  }

  @Override
  public void paint(Graphics g) {
    super.paint(g);
    try {
      if ((goff == null) && (Breakout.gameWidth > 0) && (Breakout.GameHeight > 0)) {
        ii = createImage(Breakout.gameWidth, Breakout.GameHeight);
        goff = ii.getGraphics();
      }
      if ((goff == null) || (ii == null)) {
        return;
      }

      goff.setColor(new Color(backcol));
      goff.fillRect(0, 0, Breakout.gameWidth, Breakout.GameHeight);
      GoFuzzy();
      g.drawImage(ii, this.gamePanel.getX(), this.gamePanel.getY(), this);

    } catch (NullPointerException e) {
    }

  }

  /**
   * This method will grab points and format them in a double array fashion to
   * be cycled over in the MFPanel code
   * 
   * @param points
   *          double array of points
   * @return Vector vectors of points (Vector1 contains Linguistic Variables,
   *         each vector their MFs)
   */
  private Vector<Vector<int[]>> prepareVectorForGraph(int[][] points) {
    Vector<Vector<int[]>> prepV = new Vector<Vector<int[]>>();

    // Closeness
    Vector<int[]> closeness = new Vector<int[]>();
    closeness.add(points[0]);
    closeness.add(points[1]);
    closeness.add(points[2]);
    closeness.add(points[3]);
    closeness.add(points[4]);
    Vector<int[]> danger = new Vector<int[]>();
    danger.add(points[5]);
    danger.add(points[6]);
    danger.add(points[7]);
    Vector<int[]> mult = new Vector<int[]>();
    mult.add(points[8]);
    mult.add(points[9]);
    mult.add(points[10]);
    Vector<int[]> speed = new Vector<int[]>();
    speed.add(points[11]);
    speed.add(points[12]);
    speed.add(points[13]);
    speed.add(points[14]);
    speed.add(points[15]);

    // Adding vectors to the main vector
    prepV.add(closeness);
    prepV.add(danger);
    prepV.add(mult);
    prepV.add(speed);

    return prepV;
  }

  public void run() {
    long starttime;
    Graphics g;

    Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
    g = getGraphics();

    while (true) {
      starttime = System.currentTimeMillis();
      try {
        paint(g);
        starttime += 5;
        Thread.sleep(Math.max(0, starttime - System.currentTimeMillis()));
      } catch (InterruptedException e) {
        break;
      }
    }
  }

  @Override
  public void start() {
    if (thethread == null) {
      thethread = new Thread(this);
      thethread.start();
    }
  }

  @Override
  @SuppressWarnings("deprecation")
  public void stop() {
    if (thethread != null) {
      thethread.stop();
      thethread = null;
    }
  }

  /**
   * updateMFs will fetch integers from the GUI and update the MF arrays.
   * updateMFs will operate on mf?? and mf*Points variables
   */
  private void updateMFs() {
    // Part 1 : Updating Closeness Function
    this.mfCL = this.convertToIntArray(mfPanel.CloseLeft);
    this.mfFL = this.convertToIntArray(mfPanel.FarLeft);
    this.mfZE = this.convertToIntArray(mfPanel.ZeroD);
    this.mfCR = this.convertToIntArray(mfPanel.CloseRight);
    this.mfFR = this.convertToIntArray(mfPanel.FarRight);
    // Part2 : Updating Danger Function
    this.mfDH = this.convertToIntArray(mfPanel.HighDanger);
    this.mfDM = this.convertToIntArray(mfPanel.MediumDanger);
    this.mfDL = this.convertToIntArray(mfPanel.LowDanger);
    // Part 3 : Updating Multtiplicator Function
    this.mfMultPoints[0] = this.convertToIntArray(mfPanel.MultLow);
    this.mfMultPoints[1] = this.convertToIntArray(mfPanel.MultMed);
    this.mfMultPoints[2] = this.convertToIntArray(mfPanel.MultHigh);
    // Part 4: Updating Speed Function
    this.mfSpeedPoints[0] = this.convertToIntArray(mfPanel.Still);
    this.mfSpeedPoints[1] = this.convertToIntArray(mfPanel.SlowLeft);
    this.mfSpeedPoints[2] = this.convertToIntArray(mfPanel.FastLeft);
    this.mfSpeedPoints[3] = this.convertToIntArray(mfPanel.SlowRight);
    this.mfSpeedPoints[4] = this.convertToIntArray(mfPanel.FastRight);

    int[][] points = new int[16][4];
    points[0] = this.mfCL;
    points[1] = this.mfFL;
    points[2] = this.mfZE;
    points[3] = this.mfCR;
    points[4] = this.mfFR;
    // DangerLevel - Init Points
    points[5] = this.mfDH;
    points[6] = this.mfDM;
    points[7] = this.mfDL;
    // Multiplicator - Init Points
    points[8] = this.mfMultPoints[0];
    points[9] = this.mfMultPoints[1];
    points[10] = this.mfMultPoints[2];
    // Speed - init Points
    points[11] = this.mfSpeedPoints[0];
    points[12] = this.mfSpeedPoints[1];
    points[13] = this.mfSpeedPoints[2];
    points[14] = this.mfSpeedPoints[3];
    points[15] = this.mfSpeedPoints[4];

    Vector<Vector<int[]>> mfVectors = this.prepareVectorForGraph(points);

    grafici[0].setPoints(mfVectors.get(0));
    grafici[1].setPoints(mfVectors.get(1));
    grafici[2].setPoints(mfVectors.get(2));
    grafici[3].setPoints(mfVectors.get(3));

  }
}
