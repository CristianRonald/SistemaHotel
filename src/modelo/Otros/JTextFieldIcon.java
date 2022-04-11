package modelo.Otros;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.Border;


@SuppressWarnings("unused")
public class JTextFieldIcon extends JTextField implements FocusListener{
/**
	 * 
	 */
private static final long serialVersionUID = 1L;
private JTextField jtf;
private Icon icon;
private String hint;
private Insets dummyInsets;
private String nom;

public JTextFieldIcon(JTextField jtf, String icon, String hint , String nom){
    this.jtf = jtf;
    setIcon(new  ImageIcon(getClass().getResource("/modelo/Images/"+nom)));
    
    this.hint = hint;

    Border border = UIManager.getBorder("TextField.border");
    JTextField dummy = new JTextField();
    this.dummyInsets = border.getBorderInsets(dummy);

    addFocusListener(this);
}

public void setIcon(Icon newIcon){
    this.icon = newIcon;
}

@Override
protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int textX = 2;

        if(this.icon!=null){
            int iconWidth = icon.getIconWidth();
            int iconHeight = icon.getIconHeight();
            int x = dummyInsets.left + 5;
            textX = x+iconWidth+2;
            int y = (this.getHeight() - iconHeight)/2;
            icon.paintIcon(this, g, x, y);
        }

        setMargin(new Insets(2, textX, 2, 2));

        if ( this.getText().equals("")) {
            int width = this.getWidth();
            int height = this.getHeight();
            Font prev = g.getFont();
            Font italic = prev.deriveFont(Font.ITALIC);
            Color prevColor = g.getColor();
            g.setFont(italic);
            g.setColor(UIManager.getColor("textInactiveText"));
            int h = g.getFontMetrics().getHeight();
            int textBottom = (height - h) / 2 + h - 4;
            int x = this.getInsets().left;
            Graphics2D g2d = (Graphics2D) g;
            RenderingHints hints = g2d.getRenderingHints();
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2d.drawString(hint, x, textBottom);
            g2d.setRenderingHints(hints);
            g.setFont(prev);
            g.setColor(prevColor);
        }

}

protected ImageIcon createImageIcon(String path, String description) {
    java.net.URL imgURL = getClass().getResource(path);
    if (imgURL != null) {
        return new ImageIcon(imgURL, description);
    } else {
        System.err.println("Couldn't find file: " + path);
        return null;
    }
}

@Override
public void focusGained(FocusEvent e) {
	// TODO Auto-generated method stub
	
}

@Override
public void focusLost(FocusEvent e) {
	// TODO Auto-generated method stub
	
}



}