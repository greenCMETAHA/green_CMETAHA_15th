package untitled23;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.borland.jbcl.layout.*;
import javax.swing.border.*;
import java.lang.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2012</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class Frame1 extends JFrame implements Runnable{
  JPanel contentPane;
  JPanel jPanel1 = new JPanel();
  XYLayout xYLayout1 = new XYLayout();
  TitledBorder titledBorder1;
  TitledBorder titledBorder2;
  JButton jButton1 = new JButton();
  JButton jButton2 = new JButton();
  JLabel jlTime = new JLabel();
  XYLayout xYLayout2 = new XYLayout();
  JLabel lResult = new JLabel();
  private JButton Matrix[] = new JButton[15];
  private byte Game[][] = new byte[4][4];
  private boolean bCheckResult,bRepainting;
  private java.util.Random random = new java.util.Random();
  private byte Hour,Min,Sek,NumberOfClickedButton;
  private javax.swing.Timer timer;
  private Thread GameThread= new Thread();

  //Construct the frame
  public Frame1() {
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  //Component initialization
  private void jbInit() throws Exception  {
    contentPane = (JPanel) this.getContentPane();
    titledBorder1 = new TitledBorder("");
    titledBorder2 = new TitledBorder("");
    contentPane.setLayout(xYLayout1);
    this.setSize(new Dimension(407, 388));
    this.setTitle("Frame Title");
    jPanel1.setBackground(SystemColor.info);
    jPanel1.setAlignmentY((float) 0.5);
    jPanel1.setBorder(BorderFactory.createLoweredBevelBorder());
    jPanel1.setLayout(xYLayout2);
    jButton1.setBorder(titledBorder2);
    jButton1.setPreferredSize(new Dimension(47, 27));
    jButton1.setActionCommand("");
    jButton1.setText("New game");
    jButton1.addActionListener(new Frame1_jButton1_actionAdapter(this));
    jButton2.addActionListener(new Frame1_jButton2_actionAdapter(this));
    jButton2.setText("Exit");
    jButton2.addActionListener(new Frame1_jButton2_actionAdapter(this));
    jButton2.setActionCommand("Restart");
    jButton2.setBorder(titledBorder2);
    jButton2.setVerifyInputWhenFocusTarget(true);
    jlTime.setHorizontalAlignment(SwingConstants.CENTER);
    jlTime.setHorizontalTextPosition(SwingConstants.CENTER);
    jlTime.setText("00:00");
    lResult.setFont(new java.awt.Font("Dialog", 1, 15));
    lResult.setForeground(new Color(255, 20, 0));
    lResult.setDoubleBuffered(false);
    lResult.setText("");
    contentPane.add(jlTime,    new XYConstraints(327, 6, 74, 17));
    contentPane.add(jPanel1, new XYConstraints(3, 5, 322, 323));
    contentPane.add(jButton2, new XYConstraints(330, 314, 66, 22));
    contentPane.add(jButton1, new XYConstraints(330, 290, 66, 22));
    contentPane.add(lResult,    new XYConstraints(331, 99, 98, 16));
    for (byte i=0;i<15;i++){ //Make 15 buttons
      Matrix[i] = new JButton();
      Matrix[i].setText(String.valueOf(i+1));
      Matrix[i].setBorder(titledBorder2);
      Matrix[i].setPreferredSize(new Dimension(80,80));
      Matrix[i].setFont(new java.awt.Font("Dialog", 1, 17));
      Matrix[i].addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(ActionEvent e) {
          Frame1_ButtonMatrix_actionAdapter(e);
        }
      }
      );
      jPanel1.add(Matrix[i], new XYConstraints(1000,1000,80,80));
    }
    Hour=0; Min=0; Sek=0;
    bRepainting=true;
    timer = new javax.swing.Timer(1000, new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (!bCheckResult) {
          ++Sek;
          if (Sek == 60) {
            Sek = 0;
            ++Min;
          }
          if (Min == 60) {
            Min = 0;
            ++Hour;
          }
          if (Hour == 24) {
            Hour = 0;
          }
         //if (bRepainting)
         //  RepaintButtons();
         }
      }
      });
      StartGame();
      GameThread = new Thread(this);
      GameThread.start();

  }

  //Overridden so we can exit when window is closed
  protected void processWindowEvent(WindowEvent e) {
    super.processWindowEvent(e);
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
      System.exit(0);
    }
  }

  private void StartGame(){
    //repaint();
    lResult.setText("");
    jlTime.setForeground(Color.BLACK);
    jlTime.setText("00:00:00");
    Hour=0; Min=0; Sek=0;
    timer.start();

    bCheckResult=false;

    for (byte i=0;i<4;i++)
      for (byte j=0;j<4;j++) Game[i][j] = 0;

    int CountButton = 0;
    double Step = 126 /(15.);
    byte[] buf = new byte[1];
    while (CountButton<15) {
      random.nextBytes(buf);
      int randomValue = (int) (buf[0] * (buf[0] < 0 ? -1 : 1));
      byte ButtonNumber = (byte) (randomValue / Step);
      boolean bCheck = true;
      for (byte i = 0; i < 4; i++) {
        for (byte j = 0; j < 4; j++) {
          if (Game[i][j] == ButtonNumber) {
            bCheck = false;
            break;
          }
          else {
            if (Game[i][j] == 0) {
              Game[i][j] = ButtonNumber;
              int koef = Math.round(CountButton / 4);
              Matrix[ButtonNumber-1].setLocation((int)((CountButton-(koef*4))*80)+2,(int)(koef*80)+2);
              ++CountButton;
              bCheck = false;
              break;
            }
          }
        }
        if (!bCheck) break;
      }
    }
  }
  public void update(){
    RepaintButtons();
  }

  public void paint(Graphics g) {
    super.paint(g);
    //setVisible(true);
   RepaintButtons();
  }

  void jButton1_actionPerformed(ActionEvent e) { //start game
    StartGame();
  }

  void jButton2_actionPerformed(ActionEvent e) {
    System.exit(0);
  }

  private void MoveButton(byte x,byte y,int DirectionX,int DirectionY){
    bRepainting=false;
    for (byte i = 0; i < 80; i++) {
      Matrix[NumberOfClickedButton - 1].setLocation(Matrix[NumberOfClickedButton - 1].getX() + DirectionX,
                         Matrix[NumberOfClickedButton - 1].getY() +  DirectionY);
       try {
        GameThread.sleep(1);
      }
      catch (InterruptedException ie) {}
    }

    Game[x+DirectionX][y+DirectionY] = NumberOfClickedButton;
    Game[x][y] = 0;
    RepaintButtons();
    bRepainting=true;
  }

  public void run(){
    while (true){
      jlTime.setText( (Hour < 10 ? "0" : "") + String.valueOf(Hour) + ":" +
                (Min < 10 ? "0" : "") + String.valueOf(Min)
                + ":" + (Sek < 10 ? "0" : "") + String.valueOf(Sek));
      RepaintButtons();
      if (NumberOfClickedButton!=0){
        byte i = (byte) (Math.round(Matrix[NumberOfClickedButton -
                                    1].getLocation().x - 2) / 80);
        byte j = (byte) (Math.round(Matrix[NumberOfClickedButton -
                                    1].getLocation().y - 2) / 80);
        boolean bCheck = true;
        if (i > 0) {
          if (Game[i - 1][j] == 0) {
            MoveButton(i, j, -1, 0);
            bCheck = false;
          }
        }
        if (i < 3 & bCheck) {
          if (Game[i + 1][j] == 0) {
            MoveButton(i, j, 1, 0);
            bCheck = false;
          }
        }
        if (j > 0 & bCheck) {
          if (Game[i][j - 1] == 0) {
            MoveButton(i, j, 0, -1);
            bCheck = false;
          }
        }
        if (j < 3 & bCheck) {
          if (Game[i][j + 1] == 0) {
            MoveButton(i, j, 0, 1);
            bCheck = false;
          }
        }
        NumberOfClickedButton = 0;
        CheckResult();
        RepaintButtons();
      }
    }
  }

  private void RepaintButtons(){
 /*   for (byte i = 0; i < 15; i++) { //x
      Matrix[i].setLocation(Matrix[i].getLocation().x,Matrix[i].getLocation().y);
      Matrix[i].setVisible(true);
    }
*/
    for (byte i = 0; i < 4; i++) { //x
      for (byte j = 0; j < 4; j++) { //y
        //for (byte k=0;k<15;k++){
        //  if (Matrix[k].getText()==String.valueOf(Game[i][j])){
        byte k=(byte)(Game[i][j]-1);
        if (k>=0){
          Matrix[k].setLocation(2 + (i * 80), 2 + (j * 80));
          Matrix[k].setVisible(true);
          if (Matrix[k].getY()==102) {
            int s = 0;
          }
         // break;
        }
      }
       //  }}
    }

}

  private void CheckResult(){
    byte CountCheck = 0;
    boolean bCheck=true;
    for (byte i = 0; i < 4; i++) { //x
      for (byte j = 0; j < 4; j++) { //y
        if (Game[j][i]==0) continue;
        if ( (Game[j][i]-1) != CountCheck) {
          bCheck = false;
          break;
        }else
          CountCheck++;
      }
      if (!bCheck) break;
    }
    if (bCheck) { //Showing result
      lResult.setText("Perfect!");
      jlTime.setForeground(new Color(255, 20, 0));
      bCheckResult=true;
    }
  }

  void Frame1_ButtonMatrix_actionAdapter(ActionEvent e) {
    NumberOfClickedButton=Byte.parseByte(e.getActionCommand());
    int i=0;
  }
}

class Frame1_jButton1_actionAdapter implements java.awt.event.ActionListener {
  Frame1 adaptee;

  Frame1_jButton1_actionAdapter(Frame1 adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.jButton1_actionPerformed(e);
  }
}

class Frame1_jButton2_actionAdapter implements java.awt.event.ActionListener {
  Frame1 adaptee;

  Frame1_jButton2_actionAdapter(Frame1 adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.jButton2_actionPerformed(e);
  }
}
