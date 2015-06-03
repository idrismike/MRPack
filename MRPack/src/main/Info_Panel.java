package main;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.event.*;


public class Info_Panel extends JPanel {
	public JButton start=new JButton("Start");
	public JButton stop=new JButton("Stop");
	public static SpinnerModel snm=new SpinnerNumberModel(1000,100,3000,100);
	public static JSpinner spinner=new JSpinner(snm);
	public JLabel fish=new JLabel("Fishes:" );
	public JLabel shark=new JLabel("Sharks:" );
	public JLabel prey=new JLabel("Prey:" );
	public static JTextField numFishes=new JTextField("fishes");
	public static JTextField numSharks=new JTextField("sharks");
	public static JTextField numPrey=new JTextField("prey::");
    public Info_Panel() {
    	setLayout(new FlowLayout());
    	setBorder(new TitledBorder("infopanel"));
    	fish.setForeground(Color.BLUE);
    	shark.setForeground(Color.RED);
    	add(start);
    	add(stop);
    	add(fish);
    	add(numFishes);
    	add(shark);
    	add(numSharks);
    	add(prey);
    	add(numPrey);
    	add(spinner);
    	start.addActionListener(Main_Class.simPanel);
    	stop.addActionListener(Main_Class.simPanel);
    	spinner.addChangeListener(Main_Class.simPanel);

   }



}