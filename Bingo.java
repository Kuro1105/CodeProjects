package games;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Bingo
{
	// Track random number that has been displayed
    static int randNum;
    // Track player's card to make sure bingo called correctly
    static boolean displayed = false; 
    // Track player's inserted numbers to make sure non of them are equal
    static Set<Integer> insertedNum = new HashSet<>(); 

    public static void main(String[] args)
    {
    	// Create frame for the UI
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 900);
        frame.setLayout(new FlowLayout());

        // Create panel for the UI
        JPanel mainPanel = new JPanel();
        mainPanel.setPreferredSize(new Dimension(900, 900));

        // Create panel for the title
        JPanel title = new JPanel();
        title.setPreferredSize(new Dimension(800, 75));
        title.setBackground(Color.gray);
        title.setLayout(new FlowLayout());
        
        // Create label for the title
        JLabel label = new JLabel("BINGO");
        label.setFont(new Font("Times New Roman", Font.CENTER_BASELINE, 60));
        title.add(label);
        
        // Create panel for the random number
        JPanel sub1 = new JPanel();
        sub1.setPreferredSize(new Dimension(200, 600));
        sub1.setBackground(Color.white);
        sub1.setLayout(new FlowLayout());

        // Create label for the random number
        JLabel display = new JLabel("AA");
        display.setFont(new Font("Times New Roman", Font.BOLD, 40));
        sub1.add(display);

        // Create panel for the 5 by 5 cards
        JPanel sub2 = new JPanel();
        sub2.setPreferredSize(new Dimension(600, 600));
        // GridBagLayout arranged the "card" into grids
        sub2.setLayout(new GridBagLayout());

        // GridBagConstraints control the cards behavior 
        GridBagConstraints GBC = new GridBagConstraints();
        GBC.fill = GridBagConstraints.BOTH;
        // Each card fill 1 by 1 grid
        GBC.weightx = 1;
        GBC.weighty = 1;

        JButton[] buttons = new JButton[25];
        Set<Integer> existingNumbers = new HashSet<>();
        // Placing the cards using for loop
        for (int i = 0; i < 25; i++)
        {
            buttons[i] = new JButton();
            buttons[i].setPreferredSize(new Dimension(50, 50));
            buttons[i].setFont(new Font("Arial", Font.PLAIN, 20));
            // column position
            GBC.gridx = i % 5;
            // row position
            GBC.gridy = i / 5;
            sub2.add(buttons[i], GBC);
        }

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());

        // TextField for player's number
        TextField input = new TextField();
        input.setPreferredSize(new Dimension(100, 25));
        inputPanel.add(input);
        
        // Button to insert the number
        JButton insertButton = new JButton("INSERT");
        inputPanel.add(insertButton);

        // Button to iterate random number
        JButton nextButton = new JButton("NEXT");
        // Disable NEXT button initially
        nextButton.setEnabled(false); 
        inputPanel.add(nextButton);

        // Position on each card for each player's number
        GBC.gridx = 0;
        GBC.gridy = 5;
        GBC.gridwidth = 5;
        sub2.add(inputPanel, GBC);

        insertButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String text = input.getText();
                try {
                    int number = Integer.parseInt(text);
                    // Make sure the number are between 1 - 75
                    if (number < 1 || number > 75) {
                        JOptionPane.showMessageDialog(frame, "Number must be between 1 - 75");
                        return;
                    }
                    // Make sure the number didn't exist in the HastSet
                    if (existingNumbers.contains(number)) {
                        JOptionPane.showMessageDialog(frame, "Number already inserted. Pick another number.");
                        return;
                    }
                    // Adding the number to the cards using for each loop
                    for (JButton button : buttons)
                    {
                        if (button.getText().isEmpty())
                        {
                            button.setText(String.valueOf(number));
                            existingNumbers.add(number);
                            break;
                        }
                    }

                    if (CardsFilled(buttons))
                    {
                    	// Disable INSERT button after filling
                        insertButton.setEnabled(false);
                        // Enable NEXT button after filling
                        nextButton.setEnabled(true); 
                    }

                    input.setText("");
                    // using try-catch to make sure the number are properly inserted
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Please insert a valid number.");
                }
            }
        });

        nextButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {

                Random random = new Random();
                do
                {
                	// Generate a new random number
                    randNum = random.nextInt(75) + 1; 
                    // Check if the number is already called or doesn't exist in the HashSet
                } while (insertedNum.contains(randNum) || !existingNumbers.contains(randNum));
                // Add the number to the inserted numbers set
                insertedNum.add(randNum); 
                // Display the number
                display.setText(String.valueOf(randNum)); 

                // Highlight matching cards
                for (JButton button : buttons)
                {
                    if (button.getText().equals(String.valueOf(randNum)))
                    {
                        button.setBackground(Color.GREEN);
                        button.setBorder(BorderFactory.createEmptyBorder());
                    }
                }

                // Check for Bingo
                if (checkBingo(buttons))
                {
                    if (!displayed)
                    {
                    	displayed = true;
                        JOptionPane.showMessageDialog(frame, "Bingo!");
                    }
                }
                else
                {
                	displayed = false;
                }
            }
        });

        mainPanel.add(title);
        mainPanel.add(sub1);
        mainPanel.add(sub2);

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private static boolean CardsFilled(JButton[] buttons)
    {
        for (JButton button : buttons)
        {
            if (button.getText().isEmpty())
            {
            	// Return false if any button is empty
                return false; 
            }
        }
        // All buttons are filled
        return true; 
    }

    static boolean checkBingo(JButton[] buttons)
    {
        // Check rows
        for (int i = 0; i < 5; i++)
        {
            boolean row = true;
            for (int j = 0; j < 5; j++)
            {
            	// Check if the number on the card is in the called list
                if (!insertedNum.contains(Integer.parseInt(buttons[i * 5 + j].getText())))
                {
                	row = false;
                    break;
                }
            }
            if (row) return true;
        }

        // Check columns
        for (int i = 0; i < 5; i++)
        {
            boolean column = true;
            for (int j = 0; j < 5; j++)
            {
            	// Check if the number on the card is in the called list
                if (!insertedNum.contains(Integer.parseInt(buttons[j * 5 + i].getText())))
                {
                	column = false;
                    break;
                }
            }
            if (column) return true;
        }

        // Check diagonals
        boolean diagonal1 = true;
        boolean diagonal2 = true;
        for (int i = 0; i < 5; i++)
        {
        	// Check if the number on the card is in the called list
            if (!insertedNum.contains(Integer.parseInt(buttons[i * 5 + i].getText())))
            {
            	diagonal1 = false;
            }
            // Check if the number on the card is in the called list
            if (!insertedNum.contains(Integer.parseInt(buttons[i * 5 + (4 - i)].getText())))
            {
            	diagonal2 = false;
            }
        }

        return diagonal1 || diagonal2;
    }
}
