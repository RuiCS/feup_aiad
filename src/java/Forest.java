// Environment code for project forestFires

import jason.asSyntax.*;
import jason.environment.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.logging.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

public class Forest extends Environment {

    private Logger logger = Logger.getLogger("forestFires."+Forest.class.getName());
    private ForestGUI gui = new ForestGUI();
    
    /** Called before the MAS execution with the args informed in .mas2j */
    @Override
    public void init(String[] args) {	 
    }

    @Override
    public boolean executeAction(String agName, Structure action) {
      if (action.getFunctor().equals("burn")) {
        addPercept(Literal.parseLiteral("fire"));
        return true;
      } else {
        logger.info("executing: "+action+", but not implemented!");
        return false;
      }
    }

    /** Called before the end of MAS execution */
    @Override
    public void stop() {
      super.stop();
    }
    
}
