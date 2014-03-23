/*
 * Author: Jerry To 
 * Password checker and generator program 
 * */

import java.awt.*;
import java.awt.event.*; 

import javax.swing.*; 
import javax.swing.event.*; 

import java.io.*; 
import java.util.*;
public class PasswordManager extends JFrame implements ActionListener {
	
	private JPanel pnlCheck, pnlGenerate; 
	private JTabbedPane tabPane; 
	private JTextField txtCheckPassword, txtNumChar; 
	private JButton btnCheck, btnGenerate, btnViewPasswords;
	private JCheckBox[] checkBoxes; 
	private JLabel lblCheck, lblGenerate;  
	
	private Dimension d;
	
	private char[] characters = {'0','1','2','3','4','5','6','7','8','9',
								'a','b','c','d','e','f','g','h','i','j','k',
								'l','m','n','o','p','q','r','s','t','u','v',
								'w','x','y','z','A','B','C','D','E','F','G',
								'H','I','J','K','L','M','N','O','P','Q','R',
								'S','T','U','V','W','X','Y','Z','~','`','!',
								'@','#','$','%','^','&','*','(',')','-','+',
								'_','=','{','}','[',']','\\','|',':',';','<',
								'>',',','.','?','/'}; 
	
	
	public PasswordManager()
	{
		createComponents();  
		
		pnlCheck.add(lblCheck, -1); 
		pnlCheck.add(txtCheckPassword, -1); 
		pnlCheck.add(btnCheck, -1); 
		
		for(int i = 0; i < 4; i++)
		{
			pnlGenerate.add(checkBoxes[i], -1); 
		}
		pnlGenerate.add(lblGenerate, -1); 
		pnlGenerate.add(txtNumChar, -1);
		pnlGenerate.add(btnGenerate, -1);
		pnlGenerate.add(btnViewPasswords, -1); 
		
		d = new Dimension(500, 500); 
		pnlCheck.setPreferredSize(d);
		pnlGenerate.setPreferredSize(d); 
		
		tabPane = new JTabbedPane(); 
		tabPane.addTab("Check Password Strength", pnlCheck);
		tabPane.addTab("Generate Password", pnlGenerate);
		
		
		add(tabPane); 
		pack(); 
		setVisible(true); 
		setDefaultCloseOperation(EXIT_ON_CLOSE); 
		setTitle("Password Security"); 
	}
	
	private void createComponents()
	{
		pnlCheck = new JPanel(); 
		pnlGenerate = new JPanel(); 
		pnlCheck.setLayout(new GridLayout(0, 1));
		pnlGenerate.setLayout(new GridLayout(0, 1)); 
		lblCheck = new JLabel("Check the strength of your password");
		lblGenerate = new JLabel("Enter the number of characters for your password");
		txtCheckPassword = new JTextField(); 
		txtNumChar = new JTextField(); 
		btnCheck = new JButton("Check password"); 
		btnGenerate = new JButton("Generate Password");
		btnViewPasswords = new JButton("View Saved Passwords"); 
		
		
		btnGenerate.addActionListener(this);
		btnCheck.addActionListener(this);
		btnViewPasswords.addActionListener(this);
		
		checkBoxes = new JCheckBox[4]; 
		checkBoxes[0] = new JCheckBox("Numbers"); 
		checkBoxes[1] = new JCheckBox("Lowercase letters"); 
		checkBoxes[2] = new JCheckBox("Uppercase letters"); 
		checkBoxes[3] = new JCheckBox("Special characters");
		
	}
	/*Generates random string based on checkboxes selected*/  
	public String generatePassword(char[] characters, int length)
	{
		/*Random number generators to determine if next character
		*is number, lowercase, uppercase or special, and to select
		*random characters
		*/ 
		Random rand1 = new Random(), rand2 = new Random(); 
		int i = 0, j = 0; 
		String generatedString = ""; 
		
		
		while(generatedString.length() < length)
		{
			i = rand1.nextInt(4); 
			/*
			 * If numbers checkbox selected and random num
			 * determines next character to be a number
			 * choose random number from characters array
			 * */
			if(checkBoxes[0].isSelected() && i == 0)
			{
				j = rand2.nextInt(10);
				generatedString += characters[j]; 				
				
			}
			/*
			 * If lowercase checkbox selected and random num
			 * determines next character to be a lowercase char
			 * choose random lowercase character from characters array
			 * */
			else if(checkBoxes[1].isSelected() && i == 1)
			{
				j = rand2.nextInt(36-10) + 10;
				generatedString += characters[j]; 
			}
			/*
			 * If uppercase checkbox selected and random num
			 * determines next character to be a uppercase char
			 * choose random uppercase character from characters array
			 * */
			else if(checkBoxes[2].isSelected() && i == 2)
			{
				j = rand2.nextInt(62-36) + 36;
				generatedString += characters[j]; 
			}
			/*
			 * If special checkbox selected and random num
			 * determines next character to be a special char
			 * choose random special character from characters array
			 * */
			else if(checkBoxes[3].isSelected() && i == 3)
			{
				j = rand2.nextInt(92-62) + 62;
				generatedString += characters[j]; 
			}
		}
		return generatedString; 
	}
	
	public String checkPassword(String password)
	{
		/*Shows strength of the password */
		int strength = 0; 
		String status = null; 
		/*Regular expressions for lowercase, uppercase, numbers, and special characters*/
		String[] regex = {".*[a-z]+.*", ".*[A-Z]+.*", ".*[\\d]+.*", ".*[^a-zA-Z0-9]+.*"};
		
		/*Add 1 to strength one or more of each type of character in password*/
		if(password.matches(regex[0])) strength += 1; 
		if(password.matches(regex[1])) strength += 1;
		if(password.matches(regex[2])) strength += 1;
		if(password.matches(regex[3])) strength += 1; 
		
		
		if(strength <= 1 || password.length() < 5) status = "Weak"; 
		else if(strength == 2 || strength == 3) status = "Medium"; 
		else if(strength == 4) status = "Strong"; 
		
		return status;  
	}
	
	public void writeToFile(String password)
	{
		try
		{
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("password.txt", true)));
		out.println(password);
		out.close(); 
		}
		
		catch(IOException ex)
		{
			JOptionPane.showMessageDialog(null, "Could not write to file"); 
		}
	}
	
	public String readFromFile() throws FileNotFoundException
	{
		String contents = ""; 
		Scanner sc = new Scanner(new File("password.txt")); 
		String lineSeparator = System.getProperty("line.separator"); 
		
		try
		{
			while(sc.hasNextLine())
			{
				contents += (sc.nextLine() + lineSeparator); 
			}
		}
		
		finally
		{
			sc.close();
		}
		
		return contents; 
	}
	
	
	
	public void actionPerformed(ActionEvent e)
	{
		/*
		 * If generate button clicked get desired length of password,
		 * generate password and display in dialog box 
		 */
		if(e.getSource() == this.btnGenerate)
		{
			String password = null; 
			int len = Integer.parseInt(txtNumChar.getText()); 
			password = generatePassword(characters, len); 
			JOptionPane.showMessageDialog(null, "Your password is: " + password);
			int save = JOptionPane.showConfirmDialog(null, "Save password?", "Save", JOptionPane.YES_NO_OPTION); 
			if(save == JOptionPane.YES_OPTION) 
			{
				String purpose = JOptionPane.showInputDialog("What is this password for?");
				writeToFile(password + "-" + purpose); 
			}
			
		}
		
		/*
		 * If check button clicked get password from textbox
		 * and pass into check function and show result in
		 * dialog box 
		 */
		else if(e.getSource() == this.btnCheck)
		{
			String enteredPassword = txtCheckPassword.getText(); 
			String strength = checkPassword(enteredPassword); 
			JOptionPane.showMessageDialog(null, "Your password strength is: " + strength); 
		}
		
		else if(e.getSource() == btnViewPasswords)
		{
			String savedPasswords = "";
			try {
				savedPasswords = readFromFile();
				JOptionPane.showMessageDialog(null, savedPasswords, "Saved Passwords", 
						JOptionPane.INFORMATION_MESSAGE);
			} catch (FileNotFoundException ex) {
				JOptionPane.showMessageDialog(null, "Could not read file");
			}
			
			
		}
		
		
	}
	

}
