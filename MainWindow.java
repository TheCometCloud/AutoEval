import javax.swing.*;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Toolkit;
import java.awt.datatransfer.*;
import java.io.File;
import java.io.FileNotFoundException;

import java.io.FileNotFoundException;

public class MainWindow extends JFrame
{
    // Window Controls
    public ArrayList<JCheckBox> options;
    public ArrayList<String> optionStrings;
    public JButton copyButton;
    public JButton generateButton;
    public JTextField displayField;

    // Other fields
    public String inputFileName = "options.txt";
    public static final String title = "AutoEval";

    // No-arg Constructor
    public MainWindow()
    {
        super(title);
    }
    
    // Loads from file
    public void loadOptionsFromFile() throws FileNotFoundException
    {
        options = new ArrayList<JCheckBox>();
        optionStrings = new ArrayList<String>();
        Scanner scanner = new Scanner(new File(inputFileName));
        while(scanner.hasNext())
        {
            String item = scanner.nextLine();
            options.add(new JCheckBox(item));
            optionStrings.add(item);
        }
    }

    // Generate GUI
    public void createGUI()
    {
        // Load up our checkboxes
        try 
        {
            loadOptionsFromFile();
        } 
        catch (FileNotFoundException e) 
        {
            JOptionPane.showMessageDialog(null, "output.txt file not found.", "ERROR", 1);
            System.exit(0);
        }

        // Create checkbox container
        JPanel panel = new JPanel();
        BoxLayout layout = new BoxLayout(panel, BoxLayout.PAGE_AXIS);
        panel.setLayout(layout);
        for(JCheckBox box : options)
        {
            panel.add(box);
        }

        // Create out button container
        JPanel buttonPanel = new JPanel();
        FlowLayout buttonLayout = new FlowLayout();

        // Add our buttons
        generateButton = new JButton("GENERATE");
        copyButton = new JButton("COPY");
        buttonPanel.add(generateButton);
        buttonPanel.add(copyButton);
        copyButton.addActionListener(new Listener());
        generateButton.addActionListener(new Listener());

        // Add the textField
        displayField = new JTextField(30);
        displayField.setEditable(false);
        buttonPanel.add(displayField);

        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        getContentPane().add(panel);
        getContentPane().add(buttonPanel);
        pack();
        setVisible(true);
    }

    public void generate()
    {
        ArrayList<String> items = new ArrayList<String>();

        for (JCheckBox option : options)
        {
            if (option.isSelected())
            {
                items.add(option.getText().toString());
            }
        }

        // Randomize the list
        Collections.shuffle(items);
        String temp = "";

        if (items.size() > 2)
        {
            int i = 0;
            for(; i < items.size() - 2; i++)
            {
                temp += items.get(i) + ", ";
            }
    
            temp += items.get(i) + ", and ";
            temp += items.get(++i) + ".";
        }

        else if (items.size() == 2)
        {
            int i = 0;
            temp += items.get(i++) + " and " + items.get(i) + ".";
        }

        else if (items.size() == 1)
        {
            temp += items.get(0);
        }
        
        displayField.setText(temp);
    }

    public void copy()
    {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(new StringSelection(displayField.getText()), null);
    }

    private class Listener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            if (e.getSource() == generateButton)
            {
                generate();
            }
            else if (e.getSource() == copyButton)
            {
                copy();
            }
        }
    }
}