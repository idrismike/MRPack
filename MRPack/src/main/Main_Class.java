package main;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class Main_Class extends JFrame {
	public static Simulation_Panel simPanel=new Simulation_Panel();
	public static Info_Panel infoPanel=new Info_Panel();

    public Main_Class() {
    	setLayout(new BorderLayout());
    	add(simPanel,BorderLayout.CENTER);
    	add(infoPanel,BorderLayout.SOUTH);
    	this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    public static void main(String arg[])
    {

    	Main_Class mainFrame=new Main_Class();
    	mainFrame.setTitle("Simulation");
    	mainFrame.setSize(510,503);
    	mainFrame.setLocationRelativeTo(null);
    	mainFrame.setVisible(true);
    	//simThread t=new simThread();
    	//t.start();

    	mainFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);

    }


}