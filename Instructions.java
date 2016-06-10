import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class Instructions extends JFrame
{
    public Instructions()
    {
        // Initialize components
        JButton okayBtn = new JButton("OK");
        okayBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                dispose();
            }
        });

        // Create content pane, set layout
        JPanel content = new JPanel();
        content.setLayout(new BorderLayout());
        
        JTextArea _words = new JTextArea(18,58);
        _words.setLineWrap(true);
        _words.setWrapStyleWord(true); 
        _words.setEditable(false);
        _words.setFont(new Font("Consolas", Font.BOLD, 12));
        _words.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));
        _words.setText(
            "Welcome to Texas Hold'em!\n\nHere are the rules of the game:\n"+
            "1. The objective is to get all the chips from your opponents\n"+
            "2. You must either check, raise, call or fold on each betting round\n"+
            "3. You have a limited amount of chips, spend them wisely\n"+
            "4. Have Fun!"
        );

        // Add components to content panes
        content.add(_words,"Center");
        content.add(okayBtn,"South");

        // Set window attributes
        setContentPane(content);
        pack();
        setTitle("Instructions");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}