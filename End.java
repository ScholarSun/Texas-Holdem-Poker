import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class End extends JFrame
{
    public End()
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
            "Congratulations on finishing the game\n"+
            "I hope you had lots of fun. Don't worry\n"+
            "If you won or not, as long as you tried\n"+
            "your best. Thats all that matters. See you\n"+
            "next time !"
        );

        // Add components to content panes
        content.add(_words,"Center");
        content.add(okayBtn,"South");

        // Set window attributes
        setContentPane(content);
        pack();
        setTitle("End");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}