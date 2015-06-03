package main;



import javax.swing.*;
import java.awt.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.util.*;
public class Simulation_Panel extends JPanel implements ActionListener,ChangeListener {
	public static int matrix[][]=new int[40][50];		//array matrix of pixels
	public Random r_ind1=new Random();
	public Random r_ind2=new Random();
	public static Color c1=Color.WHITE;
	public static int num_of_fishes=10;				//no of fishes
	public static int num_of_sharks=700;				// no of sharks
	public int count_fish=0;							//counts no of fishes in drawimage()
	public int count_sharks=0;
	public static int oval_x=0;							//sets x of oval
	public static int oval_y=0;
	private int fish_neib=0;							//counts fish neighbour
   	private int shark_neib=0;
   	public static int prey=0;							//counts number of prey by sharks
   	private static boolean is_shark_neib[]=new boolean[8];//checks shark neighbour whether it is shark or not after checking neighbour
   	private static boolean neib_log[][]=new boolean[40][50];//has the record of checked shark neighbours
   	private int gen=0;



    public Simulation_Panel() {
    	setLayout(new GridLayout());
    	setBackground(Color.WHITE);
    	//zero();
    	//drawImage();


    }
    public   void paintComponent(Graphics g)
    {
    	super.paintComponent(g);
    for(int i=0;i<40;i++)
    {
    	for(int j=0;j<50;j++)
    	{
    		if(matrix[i][j]==1){
    			g.setColor(Color.BLUE);
    			g.fillOval(j*10,i*10,10,10);
    		}
    		if(matrix[i][j]==2)
    		{
    			g.setColor(Color.RED);
    			g.fillOval(j*10,i*10,10,10);
    		}


    	}
    }

    }
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource()==Main_Class.infoPanel.start)
		{
			//simThread.running=true;
		}
		if(e.getSource()==Main_Class.infoPanel.stop)
		{
			//simThread.running=false;
		}
	}
	public void stateChanged(ChangeEvent ee){
  		if(ee.getSource()==Info_Panel.spinner)
  			{
  		//simThread.time=(Integer)Info_Panel.spinner.getValue();
  		}
	}


   /*public void drawImage()
   {
   	for(int i=0;i<40;i++)
   	{
   		for(int j=0;j<50;j++)
   		{

   			int x=r_ind1.nextInt(40);
   			int y=r_ind2.nextInt(50);
   			if(matrix[x][y]==0)
   			{
   				if(count_fish<num_of_fishes)
   				{
   					matrix[x][y]=1;
   					count_fish++;
   				}
   				else if(count_sharks<num_of_sharks)
   				{

   					matrix[x][y]=2;
   					count_sharks++;
   				 }
   			}else
   				j--;

   		}

   	}
   }
   public void drawObjects()
   {
   	repaint();
   }


   public void zero()
   {
   	count_fish=0;
   	count_sharks=0;
   	for(int i=0;i<40;i++)
   	{
   		for(int j=0;j<50;j++)
   		{
   			matrix[i][j]=0;
   		}
   	}
   }


   public void fate()
   {
   	gen++;

   for(int i=0;i<40;i++)
   	for(int j=0;j<50;j++)
   		neib_log[i][j]=false;


   	for(int i=0;i<40;i++)
   	{
   		for(int j=0;j<50;j++)
   		{

   			if(i==0 && j==0)
   			{
				leftTop(i,j);

			}


   			else if(i==0 && j==49)
   			{
				rightTop(i,j);
   			}
   			else if(i==39 && j==0)
   			{
				leftBottom(i,j);
   			}
   			else if(i==39 && j==49)
   			{
				rightBottom(i,j);
   			}
   			else if(i==0 && j>0 && j<49)
   			{
				topRow(i,j);
   			}
   			else if(i>0 && i<39 && j==0)
   			{
				leftColumn(i,j);
   			}
   			else if(i==39 && j>0 && j<49)
   			{
				bottomRow(i,j);
   			}
   			else if(i>0 && i<39 && j==49)
   			{
				rightColumn(i,j);
   			}
   			else
   			{
				center(i,j);
   			}
   		}
   	}


   	num_of_fishes*=4;
   	if((gen%2)==0)
   	{
   		num_of_sharks*=3;
   	}


   if(num_of_fishes<=0 || num_of_sharks>=2000)
   {
   	num_of_sharks=0;
   	simThread.Stop=true;
   }
   else if(num_of_sharks<=0 || num_of_fishes>=2000)
   {
   	num_of_sharks=0;
   	num_of_fishes=2000;
   	simThread.Stop=true;
   }
Info_Panel.numFishes.setText(""+num_of_fishes);
Info_Panel.numSharks.setText(""+num_of_sharks);
Info_Panel.numPrey.setText(""+prey);

   	}



   public void leftTop(int x,int y)
   {
   	fish_neib=0;
   	shark_neib=0;

   	for(int i=0;i<3;i++)
   		is_shark_neib[i]=false;

   		if(matrix[x][y]==1)
   		{
   			if(matrix[x+1][y]==2)
   			{
   				matrix[x][y]=0;
   				num_of_fishes--;
   				prey++;

   			}
   			else
   			if(matrix[x][y+1]==2)
   			{
   				matrix[x][y]=0;
   				num_of_fishes--;
   				prey++;

   			}
   			else
   			if(matrix[x+1][y+1]==2)
   			{
     			matrix[x][y]=0;
     			num_of_fishes--;
   				prey++;

   			}

   		}
   		else if(matrix[x][y]==2)
   		{
   			if(matrix[x+1][y]==1)
   			{
   				matrix[x+1][y]=0;
   				num_of_fishes--;
   				prey++;

   			}
   			if(matrix[x+1][y]==2)
   			{
   				shark_neib++;
   				is_shark_neib[0]=true;
   			}
   			if(matrix[x][y+1]==1)
   			{
   				matrix[x][y+1]=0;
   				num_of_fishes--;
   				prey++;

   			}
   			if(matrix[x][y+1]==2)
   			{
   				shark_neib++;
   				is_shark_neib[1]=true;
   			}
   			if(matrix[x+1][y+1]==1)
   			{
   				matrix[x+1][y+1]=0;
   				num_of_fishes--;
   				prey++;

   			}
   			if(matrix[x+1][y+1]==2)
   			{
   				shark_neib++;
   				is_shark_neib[2]=true;
   			}
		if(shark_neib!=3)
		{

   			if(is_shark_neib[0]==true)
   				matrix[x+1][y]=0;
   			if(is_shark_neib[1]==true)
   				matrix[x][y+1]=0;
   			if(is_shark_neib[2]==true)
   				matrix[x+1][y+1]=0;
   			matrix[x][y]=0;
   			num_of_sharks-=(shark_neib+1);
		}}

   }
	public void rightTop(int x,int y)
   {
	fish_neib=0;
   	shark_neib=0;

   	   	for(int i=0;i<3;i++)
   		is_shark_neib[i]=false;


   		if(matrix[x][y]==1)
   		{
   			if(matrix[x][y-1]==2)
   			{
   				matrix[x][y]=0;
   				num_of_fishes--;
   				prey++;
   			}
   			else
   			if(matrix[x+1][y-1]==2)
   			{
   				matrix[x][y]=0;
   				num_of_fishes--;
   				prey++;
   			}
   			else
   			if(matrix[x+1][y]==2)
   			{
   				matrix[x][y]=0;
   				num_of_fishes--;
   				prey++;
   			}
   		}
   		else if(matrix[x][y]==2)
   		{
   			if(matrix[x][y-1]==1)
   			{
   				matrix[x][y-1]=0;
   				num_of_fishes--;
   				prey++;
   			}
   			if(matrix[x][y-1]==2)
   			{
   				shark_neib++;
   				is_shark_neib[0]=true;
   			}
   			if(matrix[x+1][y-1]==1)
   			{
				matrix[x+1][y-1]=0;
				num_of_fishes--;
   				prey++;
   			}
   			if(matrix[x+1][y-1]==2)
   			{
   				shark_neib++;
   				is_shark_neib[1]=true;
   			}
   			if(matrix[x+1][y]==1)
   			{
   				matrix[x+1][y]=0;
   				num_of_fishes--;
   				prey++;
   			}
   			if(matrix[x+1][y]==2)
   			{
   				shark_neib++;
   				is_shark_neib[2]=true;
   			}

   		 if(shark_neib!=3)
   		 {
   		 	if(is_shark_neib[0]==true)
   				matrix[x][y-1]=0;
   			if(is_shark_neib[1]==true)
   				matrix[x+1][y-1]=0;
   			if(is_shark_neib[2]==true)
   				matrix[x+1][y]=0;
   			matrix[x][y]=0;
   			num_of_sharks-=(shark_neib+1);
   		 }}
   }
   public void leftBottom(int x,int y)
   {
	fish_neib=0;
   	shark_neib=0;

   	for(int i=0;i<3;i++)
   		is_shark_neib[i]=false;

   		if(matrix[x][y]==1)
   		{
   			if(matrix[x-1][y]==2)
   			{
   				matrix[x][y]=0;
   				num_of_fishes--;
   				prey++;
   			}
   			else
   			if(matrix[x][y+1]==2)
   			{
   				matrix[x][y]=0;
   				num_of_fishes--;
   				prey++;
   			}
   			else
   			if(matrix[x-1][y+1]==2)
   			{
   				matrix[x][y]=0;
   				num_of_fishes--;
   				prey++;
   			}
   		}
   		else if(matrix[x][y]==2)
   		{
   			if(matrix[x-1][y]==1)
   			{
   				matrix[x-1][y]=0;
   				num_of_fishes--;
   				prey++;
   			}
   			if(matrix[x-1][y]==2)
   			{
   				shark_neib++;
   				is_shark_neib[0]=true;
   			}
   			if(matrix[x][y+1]==1)
   			{
   				matrix[x][y+1]=0;
   				num_of_fishes--;
   				prey++;
   			}
   			if(matrix[x][y+1]==2)
   			{
   				shark_neib++;
   				is_shark_neib[1]=true;
   			}
   			if(matrix[x-1][y+1]==1)
   			{
   				matrix[x-1][y+1]=0;
   				num_of_fishes--;
   				prey++;
   			}
   			if(matrix[x-1][y+1]==2)
   			{
   				shark_neib++;
   				is_shark_neib[2]=true;
   			}

   		if(shark_neib!=3)
   		{

   		   	if(is_shark_neib[0]==true)
   				matrix[x-1][y]=0;
   			if(is_shark_neib[1]==true)
   				matrix[x][y+1]=0;
   			if(is_shark_neib[2]==true)
   				matrix[x-1][y+1]=0;
   			matrix[x][y]=0;
   			num_of_sharks-=(shark_neib+1);
   		}}
   }
   public void rightBottom(int x,int y)
   {
	fish_neib=0;
   	shark_neib=0;

   	for(int i=0;i<3;i++)
   		is_shark_neib[i]=false;

   		if(matrix[x][y]==1)
   		{
   			if(matrix[x][y-1]==2)
   			{
   				matrix[x][y]=0;
   				num_of_fishes--;
   				prey++;
   			}
   			else
   			if(matrix[x-1][y]==2)
   			{
   				matrix[x][y]=0;
   				num_of_fishes--;
   				prey++;
   			}
   			else
   			if(matrix[x-1][y-1]==2)
   			{
   				matrix[x][y]=0;
   				num_of_fishes--;
   				prey++;
   			}

   		}
   		else if(matrix[x][y]==2)
   		{
   			if(matrix[x][y-1]==1)
   			{
   				matrix[x][y-1]=0;
   				num_of_fishes--;
   				prey++;
   			}
   			if(matrix[x][y-1]==2)
   			{
   				shark_neib++;
   				is_shark_neib[0]=true;
   			}
   			if(matrix[x-1][y]==1)
   			{
   				matrix[x-1][y]=0;
   				num_of_fishes--;
   				prey++;
   			}
   			if(matrix[x-1][y]==2)
   			{
   				shark_neib++;
   				is_shark_neib[1]=true;
   			}
   			if(matrix[x-1][y-1]==1)
   			{
   				matrix[x-1][y-1]=0;
   				num_of_fishes--;
   				prey++;
   			}
   			if(matrix[x-1][y-1]==2)
   			{
   				shark_neib++;
   				is_shark_neib[2]=true;
   			}
   		if(shark_neib!=3)
   		{

   		   	if(is_shark_neib[0]==true)
   				matrix[x][y-1]=0;
   			if(is_shark_neib[1]==true)
   				matrix[x-1][y]=0;
   			if(is_shark_neib[2]==true)
   				matrix[x-1][y-1]=0;
   			matrix[x][y]=0;
   			num_of_sharks-=(shark_neib+1);
   		}}
   }
   public void topRow(int x,int y)
   {
   	fish_neib=0;
   	shark_neib=0;
   	for(int i=0;i<5;i++)
   		is_shark_neib[i]=false;
		if(matrix[x][y]==1)
		{
			if(matrix[x][y-1]==2)
			{
				matrix[x][y]=0;
				num_of_fishes--;
				prey++;
			}
			else
			if(matrix[x+1][y-1]==2)
			{
				matrix[x][y]=0;
				num_of_fishes--;
				prey++;
			}
			else
			if(matrix[x+1][y]==2)
			{
				matrix[x][y]=0;
				num_of_fishes--;
				prey++;
			}
			else
			if(matrix[x+1][y+1]==2)
			{
				matrix[x][y]=0;
				num_of_fishes--;
				prey++;
			}
			else
			if(matrix[x][y+1]==2)
			{
				matrix[x][y]=0;
				num_of_fishes--;
				prey++;
			}



		}
		else if(matrix[x][y]==2)
		{
			if(matrix[x][y-1]==1)
			{
				matrix[x][y-1]=0;
				num_of_fishes--;
				prey++;
			}
			if(matrix[x][y-1]==2)
			{
				shark_neib++;
				is_shark_neib[0]=true;
			}
			if(matrix[x+1][y-1]==1)
			{
				matrix[x+1][y-1]=0;
				num_of_fishes--;
				prey++;
			}
			if(matrix[x+1][y-1]==2)
			{
				shark_neib++;
				is_shark_neib[1]=true;
			}
			if(matrix[x+1][y]==1)
			{
				matrix[x+1][y]=0;
				num_of_fishes--;
				prey++;
			}
			if(matrix[x+1][y]==2)
			{
				shark_neib++;
				is_shark_neib[2]=true;
			}
			if(matrix[x+1][y+1]==1)
			{
				matrix[x+1][y+1]=0;
				num_of_fishes--;
				prey++;
			}
			if(matrix[x+1][y+1]==2)
			{
				shark_neib++;
				is_shark_neib[3]=true;
			}
			if(matrix[x][y+1]==1)
			{
				matrix[x][y+1]=0;
				num_of_fishes--;
				prey++;
			}
			if(matrix[x][y+1]==2)
			{
				shark_neib++;
				is_shark_neib[4]=true;
			}


		if(shark_neib!=3)
		{

			if(is_shark_neib[0]==true)
   				matrix[x][y-1]=0;
   			if(is_shark_neib[1]==true)
   				matrix[x+1][y-1]=0;
   			if(is_shark_neib[2]==true)
   				matrix[x+1][y]=0;
   			if(is_shark_neib[3]==true)
   				matrix[x+1][y+1]=0;
   			if(is_shark_neib[4]==true)
   				matrix[x][y+1]=0;
   			matrix[x][y]=0;
   			num_of_sharks-=(shark_neib+1);
		}}
   }
   public void leftColumn(int x,int y)
   {
   	fish_neib=0;
   	shark_neib=0;
   	for(int i=0;i<5;i++)
   		is_shark_neib[i]=false;

   		if(matrix[x][y]==1)
		{
			if(matrix[x-1][y]==2)
			{
				matrix[x][y]=0;
				num_of_fishes--;
				prey++;
			}
			else
			if(matrix[x-1][y+1]==2)
			{
				matrix[x][y]=0;
				num_of_fishes--;
				prey++;
			}
			else
			if(matrix[x][y+1]==2)
			{
				matrix[x][y]=0;
				num_of_fishes--;
				prey++;
			}
			else
			if(matrix[x+1][y+1]==2)
			{
				matrix[x][y]=0;
				num_of_fishes--;
				prey++;
			}
			else
			if(matrix[x+1][y]==2)
			{
				matrix[x][y]=0;
				num_of_fishes--;
				prey++;
			}



		}
		else if(matrix[x][y]==2)
		{
			if(matrix[x-1][y]==1)
			{
				matrix[x-1][y]=0;
				num_of_fishes--;
				prey++;
			}
			if(matrix[x-1][y]==2)
			{
				shark_neib++;
				is_shark_neib[0]=true;
			}
			if(matrix[x-1][y+1]==1)
			{
				matrix[x-1][y+1]=0;
				num_of_fishes--;
				prey++;
			}
			if(matrix[x-1][y+1]==2)
			{
				shark_neib++;
				is_shark_neib[1]=true;
			}
			if(matrix[x][y+1]==1)
			{
				matrix[x][y+1]=0;
				num_of_fishes--;
				prey++;
			}
			if(matrix[x][y+1]==2)
			{
				shark_neib++;
				is_shark_neib[2]=true;
			}
			if(matrix[x+1][y+1]==1)
			{
				matrix[x+1][y+1]=0;
				num_of_fishes--;
				prey++;
			}
			if(matrix[x+1][y+1]==2)
			{
				shark_neib++;
				is_shark_neib[3]=true;
			}
			if(matrix[x+1][y]==1)
			{
				matrix[x+1][y]=0;
				num_of_fishes--;
				prey++;
			}
			if(matrix[x+1][y]==2)
			{
				shark_neib++;
				is_shark_neib[4]=true;
			}


		if(shark_neib!=3)
		{

			if(is_shark_neib[0]==true)
   				matrix[x-1][y]=0;
   			if(is_shark_neib[1]==true)
   				matrix[x-1][y+1]=0;
   			if(is_shark_neib[2]==true)
   				matrix[x][y+1]=0;
   			if(is_shark_neib[3]==true)
   				matrix[x+1][y+1]=0;
   			if(is_shark_neib[4]==true)
   				matrix[x+1][y]=0;
   			matrix[x][y]=0;
   			num_of_sharks-=(shark_neib+1);
   }}

   }
   public void bottomRow(int x,int y)
   {
   	fish_neib=0;
   	shark_neib=0;
   	for(int i=0;i<5;i++)
   		is_shark_neib[i]=false;

   	   	if(matrix[x][y]==1)
		{
			if(matrix[x][y-1]==2)
			{
				matrix[x][y]=0;
				num_of_fishes--;
				prey++;
			}
			else
			if(matrix[x-1][y-1]==2)
			{
				matrix[x][y]=0;
				num_of_fishes--;
				prey++;
			}
			else
			if(matrix[x-1][y]==2)
			{
				matrix[x][y]=0;
				num_of_fishes--;
				prey++;
			}
			else
			if(matrix[x-1][y+1]==2)
			{
				matrix[x][y]=0;
				num_of_fishes--;
				prey++;
			}
			else
			if(matrix[x][y+1]==2)
			{
				matrix[x][y]=0;
				num_of_fishes--;
				prey++;
			}



		}
		else if(matrix[x][y]==2)
		{
			if(matrix[x][y-1]==1)
			{
				matrix[x][y-1]=0;
				num_of_fishes--;
				prey++;
			}
			if(matrix[x][y-1]==2)
			{
				shark_neib++;
				is_shark_neib[0]=true;
			}
			if(matrix[x-1][y-1]==1)
			{
				matrix[x-1][y-1]=0;
				num_of_fishes--;
				prey++;
			}
			if(matrix[x-1][y-1]==2)
			{
				shark_neib++;
				is_shark_neib[1]=true;
			}
			if(matrix[x-1][y]==1)
			{
				matrix[x-1][y]=0;
				num_of_fishes--;
				prey++;
			}
			if(matrix[x-1][y]==2)
			{
				shark_neib++;
				is_shark_neib[2]=true;
			}
			if(matrix[x-1][y+1]==1)
			{
				matrix[x-1][y+1]=0;
				num_of_fishes--;
				prey++;
			}
			if(matrix[x-1][y+1]==2)
			{
				shark_neib++;
				is_shark_neib[3]=true;
			}
			if(matrix[x][y+1]==1)
			{
				matrix[x][y+1]=0;
				num_of_fishes--;
				prey++;
			}
			if(matrix[x][y+1]==2)
			{
				shark_neib++;
				is_shark_neib[4]=true;
			}


		if(shark_neib!=3)
		{

			if(is_shark_neib[0]==true)
   				matrix[x][y-1]=0;
   			if(is_shark_neib[1]==true)
   				matrix[x-1][y-1]=0;
   			if(is_shark_neib[2]==true)
   				matrix[x-1][y]=0;
   			if(is_shark_neib[3]==true)
   				matrix[x-1][y+1]=0;
   			if(is_shark_neib[4]==true)
   				matrix[x][y+1]=0;
   			matrix[x][y]=0;
   			num_of_sharks-=(shark_neib+1);
   }}

   }
   public void rightColumn(int x,int y)
   {
   	fish_neib=0;
   	shark_neib=0;
   	for(int i=0;i<5;i++)
   		is_shark_neib[i]=false;

   	   	if(matrix[x][y]==1)
		{
			if(matrix[x-1][y]==2)
			{
				matrix[x][y]=0;
				num_of_fishes--;
				prey++;
			}
			else
			if(matrix[x-1][y-1]==2)
			{
				matrix[x][y]=0;
				num_of_fishes--;
				prey++;
			}
			else if(matrix[x][y-1]==2)
			{
				matrix[x][y]=0;
				num_of_fishes--;
				prey++;
			}
			else if(matrix[x+1][y-1]==2)
			{
				matrix[x][y]=0;
				num_of_fishes--;
				prey++;
			}
			else if(matrix[x+1][y]==2)
			{
				matrix[x][y]=0;
				num_of_fishes--;
				prey++;
			}



		}
		else if(matrix[x][y]==2)
		{
			if(matrix[x-1][y]==1)
			{
				matrix[x-1][y]=0;
				num_of_fishes--;
				prey++;
			}
			if(matrix[x-1][y]==2)
			{
				shark_neib++;
				is_shark_neib[0]=true;
			}
			if(matrix[x-1][y-1]==1)
			{
				matrix[x-1][y-1]=0;
				num_of_fishes--;
				prey++;
			}
			if(matrix[x-1][y-1]==2)
			{
				shark_neib++;
				is_shark_neib[1]=true;
			}
			if(matrix[x][y-1]==1)
			{
				matrix[x][y-1]=0;
				num_of_fishes--;
				prey++;
			}
			if(matrix[x][y-1]==2)
			{
				shark_neib++;
				is_shark_neib[2]=true;
			}
			if(matrix[x+1][y-1]==1)
			{
				matrix[x+1][y-1]=0;
				num_of_fishes--;
				prey++;
			}
			if(matrix[x+1][y-1]==2)
			{
				shark_neib++;
				is_shark_neib[3]=true;
			}
			if(matrix[x+1][y]==1)
			{
				matrix[x+1][y]=0;
				num_of_fishes--;
				prey++;
			}
			if(matrix[x+1][y]==2)
			{
				shark_neib++;
				is_shark_neib[4]=true;
			}


		if(shark_neib!=3)
		{

			if(is_shark_neib[0]==true)
   				matrix[x-1][y]=0;
   			if(is_shark_neib[1]==true)
   				matrix[x-1][y-1]=0;
   			if(is_shark_neib[2]==true)
   				matrix[x][y-1]=0;
   			if(is_shark_neib[3]==true)
   				matrix[x+1][y-1]=0;
   			if(is_shark_neib[4]==true)
   				matrix[x+1][y]=0;
   			matrix[x][y]=0;
   			num_of_sharks-=(shark_neib+1);
   }}

   }
   public void center(int x,int y)
   {
   	fish_neib=0;
   	shark_neib=0;
   	for(int i=0;i<8;i++)
   		is_shark_neib[i]=false;

   	if(matrix[x][y]==1)
   	{
   		 if(matrix[x-1][y]==2)
   		{
   			matrix[x][y]=0;
   			num_of_fishes--;
   			prey++;
   		}
   		else
   		if(matrix[x-1][y+1]==2)
   		{
   			matrix[x][y]=0;
   			num_of_fishes--;
   			prey++;
   		}
   		else
   		if(matrix[x][y+1]==2)
   		{
   			matrix[x][y]=0;
   			num_of_fishes--;
   			prey++;
   		}
   		else
   		if(matrix[x+1][y+1]==2)
   		{
   			matrix[x][y]=0;
   			num_of_fishes--;
   			prey++;
   		}
   		else
   		if(matrix[x+1][y]==2)
   		{
   			matrix[x][y]=0;
   			num_of_fishes--;
   			prey++;
   		}
   		else
   		if(matrix[x-1][y-1]==2)
   		{
   			matrix[x][y]=0;
   			num_of_fishes--;
   			prey++;
   		}
   		else
   		if(matrix[x+1][y-1]==2)
   		{
   			matrix[x][y]=0;
   			num_of_fishes--;
   			prey++;
   		}
   		else
   		if(matrix[x][y-1]==2)
   		{
   			matrix[x][y]=0;
   			num_of_fishes--;
   			prey++;
   		}

   	}
   	else if(matrix[x][y]==2)
   	{
   		if(matrix[x-1][y]==1)
   		{
   			matrix[x-1][y]=0;
   			num_of_fishes--;
   			prey++;
   		}
   		 if(matrix[x-1][y]==2 && neib_log[x-1][y]==false)
   		{
   			shark_neib++;
   			is_shark_neib[0]=true;
   		}
   		if(matrix[x-1][y+1]==1)
   		{
   			matrix[x-1][y+1]=0;
   			num_of_fishes--;
   			prey++;
   		}
   		if(matrix[x-1][y+1]==2 && neib_log[x-1][y+1]==false)
   		{
   			shark_neib++;
   			is_shark_neib[1]=true;
   		}
   		 if(matrix[x][y+1]==1)
   		{
   			matrix[x][y+1]=0;
   			num_of_fishes--;
   			prey++;
   		}
   		if(matrix[x][y+1]==2 && neib_log[x][y+1]==false)
   		{
   			shark_neib++;
   			is_shark_neib[2]=true;
   		}
   		if(matrix[x+1][y+1]==1)
   		{
   			matrix[x+1][y+1]=0;
   			num_of_fishes--;
   			prey++;
   		}
   		if(matrix[x+1][y+1]==2 && neib_log[x+1][y+1]==false)
   		{
   			shark_neib++;
   			is_shark_neib[3]=true;
   		}
   		 if(matrix[x+1][y]==1)
   		{
   			matrix[x+1][y]=0;
   			num_of_fishes--;
   			prey++;
   		}
   		if(matrix[x+1][y]==2 && neib_log[x+1][y]==false)
   		{
   			shark_neib++;
   			is_shark_neib[4]=true;
   		}
   		if(matrix[x-1][y-1]==1)
   		{
   			matrix[x-1][y-1]=0;
   			num_of_fishes--;
   			prey++;
   		}
   		if(matrix[x-1][y-1]==2 && neib_log[x-1][y-1]==false)
   		{
   			shark_neib++;
   			is_shark_neib[5]=true;
   		}
   		if(matrix[x+1][y-1]==1)
   		{
   			matrix[x+1][y-1]=0;
   			num_of_fishes--;
   			prey++;
   		}
   		if(matrix[x+1][y-1]==2 && neib_log[x+1][y-1]==false)
   		{
   			shark_neib++;
   			is_shark_neib[6]=true;
   		}
   		if(matrix[x][y-1]==1)
   		{
   			matrix[x][y-1]=0;
   			num_of_fishes--;
   			prey++;
   		}
   		if(matrix[x][y-1]==2 && neib_log[x][y-1]==false)
   		{
   			shark_neib++;
   			is_shark_neib[7]=true;

   		}

		if(shark_neib!=3)
		{

  			 if(is_shark_neib[0]==true)
   				matrix[x-1][y]=0;
   			if(is_shark_neib[1]==true)
   				matrix[x-1][y+1]=0;
   			if(is_shark_neib[2]==true)
   				matrix[x][y+1]=0;
   			if(is_shark_neib[3]==true)
   				matrix[x+1][y+1]=0;
   			if(is_shark_neib[4]==true)
   				matrix[x+1][y]=0;
   			if(is_shark_neib[5]==true)
   				matrix[x-1][y-1]=0;
   			if(is_shark_neib[6]==true)
   				matrix[x+1][y-1]=0;
   			if(is_shark_neib[7]==true)
   				matrix[x][y-1]=0;
   			matrix[x][y]=0;
   			num_of_sharks-=shark_neib;
   }
   		if(shark_neib==3)
		{

  			if(is_shark_neib[0]==true)
   				neib_log[x-1][y]=true;
   			if(is_shark_neib[1]==true)
   				neib_log[x-1][y+1]=true;
   			if(is_shark_neib[2]==true)
   				neib_log[x][y+1]=true;
   			if(is_shark_neib[3]==true)
   				neib_log[x+1][y+1]=true;
   			if(is_shark_neib[4]==true)
   				neib_log[x+1][y]=true;
   			if(is_shark_neib[5]==true)
   				neib_log[x-1][y-1]=true;
   			if(is_shark_neib[6]==true)
   				neib_log[x+1][y-1]=true;
   			if(is_shark_neib[7]==true)
   				neib_log[x][y-1]=true;
   			neib_log[x][y]=true;
  		 }
  	 }
  }
*/
}
