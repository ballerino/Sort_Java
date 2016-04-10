package sort;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import sort.RelayPlayer.SORTMETHOD;

/**
 * Megan Tran
 * CMSC350 
 * Project #3 Sorting Algorithms
 * February 16th, 2015
 * 
 * New Additions: two new buttons for QuickSort and InsertionSort algorithms
 * When button is pressed, the QuickSort Button will print a list of RelayPlayers
 * by their last names in ascending order. The InsertionSort button will print a list
 * of RelayPlayers by their successful relays in ascending order.
 */

public class RelayPlayerDatabase extends JFrame {
	private static final long serialVersionUID = 1L;
	private JLabel newplayer, info1, info2;
	private JButton add, print, random, quickSort, insertSort;
	private JTextField playerTF;
	private JTextArea output;
	private JScrollPane scroll;
	static int playercounter;
	RelayPlayer[] x;
	
	//inner class that helps open up the files needed.
	//Uses JFileChooser to search for files.
	public class OpenFile{
		StringBuilder db = new StringBuilder();
		
		public void PickFile() throws Exception{
			JFileChooser fileChosen = new JFileChooser();
			
			int result = fileChosen.showOpenDialog(new JFrame());
			if (result == JFileChooser.APPROVE_OPTION) {
				File selectedFile = fileChosen.getSelectedFile();
				Scanner input = new Scanner(selectedFile);
				while(input.hasNext()){
					db.append(input.nextLine());
					db.append("\n");
					playercounter++;
				}
				input.close();
			}
			else{
				db.append("No file was chosen.");
			}

		}
	}

	//constructor for the mainframe of the GUI.
	public RelayPlayerDatabase(){
		setTitle("Relay Player Database");
		setLayout(new BorderLayout());

		//This panel will hold all the components of the GUIbesides the text area
		JPanel top = new JPanel();
		top.setLayout(new GridLayout(3, 1, 4, 4));
		top.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		//Info panel - tells you how to add players
		JPanel rules = new JPanel();
		rules.setLayout(new GridLayout(2, 1, 2, 2));
		rules.setBorder(new EmptyBorder(10, 10, 10, 10));
		info1 = new JLabel("Add players to the database by typing in their first name, last name,\n" +
				" school, height (whole integers in cm), and successful relays (%)."
				);
		info2 = new JLabel ("Separate each field by a space.");
		rules.add(info1);
		rules.add(info2);
		
		//New player add panel
		JPanel addplayer = new JPanel();
		addplayer.setLayout(new GridLayout(1, 3, 4, 4));
		addplayer.setBorder(new EmptyBorder(10, 10, 10, 10));
		newplayer = new JLabel("New Player Info: ");
		addplayer.add(newplayer);
		playerTF = new JTextField(20);
		addplayer.add(playerTF);
		add = new JButton("Add");
		add.addActionListener(new ButtonClicked());
		addplayer.add(add);
		
		//Button panel
		JPanel buttons = new JPanel();
		buttons.setLayout(new GridLayout(2, 2, 5, 5));
		print = new JButton("Print File");
		random = new JButton("Make Random Players");
		print.addActionListener(new ButtonClicked());
		random.addActionListener(new ButtonClicked());
		buttons.add(print);
		buttons.add(random);
		buttons.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		/*newly added sort buttons*/
		quickSort = new JButton("QuickSort Players by Last Name");
		insertSort = new JButton("InsertionSort Players by Successful Relays");
		quickSort.addActionListener(new ButtonClicked());
		insertSort.addActionListener(new ButtonClicked());
		buttons.add(quickSort);
		buttons.add(insertSort);
		
		top.add(rules);
		top.add(addplayer);
		top.add(buttons);
		
		add(top, BorderLayout.NORTH);
		
		//Text area output.
		output = new JTextArea();
		output.setMargin( new Insets(10,10,10,10) );
		Font font = new Font("Courier", Font.PLAIN, 14);
		output.setFont(font);
		scroll = new JScrollPane(output);
		scroll.setBounds(3, 3, 300, 200);
		add(scroll, BorderLayout.CENTER);
		

	}
	
	//inner class that handles all the actions for button clicks
	public class ButtonClicked implements ActionListener {

		public void actionPerformed(ActionEvent a) {
			//Add button, for adding new players
			if(a.getSource() == add){
				output.setText("");
				//If the text field is empty, there is an error
				if (playerTF.getText().isEmpty()){
					JOptionPane.showMessageDialog(new Frame("Error"), 
							"You must type in correct player information to add to the database!", 
							"Adding Error",
							JOptionPane.ERROR_MESSAGE);
				}
				//Try adding String information from the text field onto the file.
				else {
					try {
						String newplayer = playerTF.getText();
						BufferedWriter out = new BufferedWriter
								(new FileWriter("strideplayers.txt", true));
						out.write("\n" + newplayer);
						out.close();
						JOptionPane.showMessageDialog(new Frame("Update"), 
								"You have successfully updated the relay player database!", 
								"Add Success", JOptionPane.INFORMATION_MESSAGE);
						playerTF.setText("");
					}catch(IOException e) {
						JOptionPane.showMessageDialog(new Frame("Error"), 
								"Can't write to file!", 
								"IOException",
								JOptionPane.ERROR_MESSAGE);
					}
					
				}
			}
			//Print file button action
			//Will use the OpenFile innerclass
			else if(a.getSource()==print){
				output.setText("");
				OpenFile newfile = new OpenFile();
				try{
					newfile.PickFile();
				}
				catch(Exception e){
					JOptionPane.showMessageDialog(new Frame("Error"), 
							"Oh no something happened!", 
							"Error",
							JOptionPane.ERROR_MESSAGE);
				}
				if(newfile.db.toString().equals("No file was chosen.")){
					output.setText(newfile.db.toString());
				}
				else{
					output.setText(playercounter + " entries.\n" + newfile.db.toString());
					//reset counter
					playercounter = 0;
				}
			}
			//Make Random Players button actions.
			else if(a.getSource()==random){
				output.setText("");
				int num;
				StringBuilder s = new StringBuilder();
				String input = new String();
				input = JOptionPane.showInputDialog("Enter a positive whole number of random players: ");
				try{
					num = Integer.parseInt(input);
					if (num <= 0) {
						JOptionPane.showMessageDialog(new Frame("Error"), 
								"You need to type in a positive integer!", 
								"Error",
								JOptionPane.ERROR_MESSAGE);
					}
					else {
						x = RelayPlayer.makeRandom (num);
						for (RelayPlayer p : x){
							s.append(p.toString());
							s.append("\n");
						}
					}
				}catch(NumberFormatException e){
					JOptionPane.showInputDialog("Please enter a positive integer!");
				}
				output.setText(s.toString());
			}
			
			//QuickSort
			else if(a.getSource()==quickSort){
				output.setText("");
				if(x == null){
					JOptionPane.showMessageDialog(new Frame("Error"), 
							"Make a list of Random players first!", 
							"Error",
							JOptionPane.ERROR_MESSAGE);
				}
				else {
					StringBuilder s = new StringBuilder();
					s.append("*Sorting Players with QuickSort method by last name*\n");
					RelayPlayer.sort = SORTMETHOD.LAST;
					s.append(RelayPlayer.printArray(RelayPlayer.quickSort(x, 0, x.length-1)));
					output.setText(s.toString());
				}
			}
			
			//InsertSort
			else {
				output.setText("");
				if(x == null){
					JOptionPane.showMessageDialog(new Frame("Error"), 
							"Make a list of Random players first!", 
							"Error",
							JOptionPane.ERROR_MESSAGE);
				}
				else {
					StringBuilder s = new StringBuilder();
					s.append("*Sorting Players with InsertionSort method by successful relays*\n");
					RelayPlayer.sort = SORTMETHOD.RELAY;
					s.append(RelayPlayer.printArray(RelayPlayer.insertionSort(x)));
					output.setText(s.toString());
				}
			}
			
		}
		
	}
	
	//method to help display the frame
	public void display() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 400);
        setMinimumSize(new Dimension(700, 300));
        setVisible(true);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int upperLeftCornerX = (screenSize.width - this.getWidth()) / 2;
		int upperLeftCornerY = (screenSize.height - this.getHeight()) / 2;
		setLocation(upperLeftCornerX, upperLeftCornerY);
        setResizable(true);
	   }
	
	//main method will use display to make the GUI visible
	public static void main(String[] args) {
		RelayPlayerDatabase db = new RelayPlayerDatabase();
		db.display();
	}

}
