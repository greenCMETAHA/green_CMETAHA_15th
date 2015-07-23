package untitled23;

import javax.swing.*;
import java.awt.*;

public class OurButton extends JButton {
  private Point XYPoint;

  public OurButton() {
    XYPoint = new Point(0,0);
  }

  public void setX(byte value){
    XYPoint.x=value;
  }

  public void setY(byte value){
    XYPoint.x=value;
  }


}
