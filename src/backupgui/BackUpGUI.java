/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backupgui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicArrowButton;

/**
 *
 * @author SARJIT
 */
public class BackUpGUI {

    /**
     * @param args the command line arguments
     */
    static JFrame frame;
    static JButton add,save,exit,changeSrc,changeDest,deleteSrc;
    static JComboBox<String> allSrc;
    static JTextField dest;
    public static Operations oper;
    Actions actions;
    static boolean flag;
    
    public static void main(String[] args) {
        // TODO code application logic here
        new BackUpGUI();
    }
    
    public static boolean getFlag()
    {
        return flag;
    }
    
    public static void setFlag(boolean f)
    {
        flag = f;
    }
    
    public BackUpGUI()
    {
        flag = true;
        oper = new Operations();
        actions = new Actions();
        frame = new JFrame("Backup GUI");
        
        frame.setSize(500, 250);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(6, 3));
        
        add = new JButton("Add");
        add.setActionCommand("Add");
        add.addActionListener(actions);
        
        save = new JButton("Save");
        save.setActionCommand("Save");
        save.addActionListener(actions);
        
        exit = new JButton("Exit");
        exit.setActionCommand("Exit");
        exit.addActionListener(actions);
        
        changeDest = new JButton("Change");
        changeDest.setActionCommand("ChangeDest");
        changeDest.addActionListener(actions);
        
        changeSrc = new JButton("Change");
        changeSrc.setActionCommand("ChangeSrc");
        changeSrc.addActionListener(actions);
        
        deleteSrc = new JButton("Delete");
        deleteSrc.setActionCommand("Delete");
        deleteSrc.addActionListener(actions);
        
        dest = new JTextField("");
        dest.setEditable(false);
        
        if(oper.isEmpty())
            allSrc = new JComboBox<>();
        else
            allSrc = new JComboBox<>(oper.getAllSrc());
        
        allSrc.setActionCommand("Src");
        allSrc.addActionListener(actions);
        
        frame.add(new JLabel(""));
        frame.add(new JLabel(""));
        frame.add(new JLabel(""));
        
        frame.add(new JLabel("Source ",SwingConstants.RIGHT));
        frame.add(allSrc);
        frame.add(new JLabel(""));
        
        frame.add(new JLabel(""));
        frame.add(changeSrc);
        frame.add(deleteSrc);
        
        frame.add(new JLabel("Destination ",SwingConstants.RIGHT));
        frame.add(dest);
        frame.add(new JLabel(""));
        
        frame.add(new JLabel(""));
        frame.add(changeDest);
        frame.add(new JLabel(""));
        
        frame.add(add);
        frame.add(save);
        frame.add(exit);
        
        frame.setVisible(true);
    }
    
    public static void updateSrc(String src)
    {
        allSrc.addItem(src);
    }
    
    public static void setDest(String des)
    {
        dest.setText(des);
    }
}

class Actions implements ActionListener
{

    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCmd = e.getActionCommand();
       
        switch (actionCmd) {
            case "Add":
            {
                String src, msg;
                while(true)
                {
                    src = JOptionPane.showInputDialog("Paste the path of Source Directory here");
                    msg = BackUpGUI.oper.checkSource(src);
                    if(msg == null)
                    {
                        break;
                    }
                    
                    if(msg.equals("True"))
                    {
                       break; 
                    }
                    
                    JOptionPane.showMessageDialog(null, msg);
                }
                if(msg == null) break;
                String dest = JOptionPane.showInputDialog("Paste the path of corresponding Destination Directory here.\nEnter upto the parent directory.\nFor Eg. source = D:\\BackUpthis and Destination = F:\\BackUpHere");
                if(dest == null)    break;
                BackUpGUI.oper.addFolder(src, dest);
                BackUpGUI.updateSrc(src);
                BackUpGUI.dest.setText("");
                BackUpGUI.setFlag(false);
                JOptionPane.showMessageDialog(null, "Successful");
                break;
            }
            case "Src":{
                if(BackUpGUI.allSrc.getItemCount() < 1)    break;
                        
                String src = (String) BackUpGUI.allSrc.getSelectedItem();
                String dest = BackUpGUI.oper.getDest(src);
                BackUpGUI.setDest(dest);
                break;
            }
            case "Exit":{
                
                if(BackUpGUI.getFlag())
                {
                    System.exit(0);
                }
                else
                {
                    int ans = JOptionPane.showConfirmDialog(null, "You have not saved the changes. Do you want to save changes?");
                    if(ans == 0)
                    {
                        BackUpGUI.oper.saveChanges();
                        System.exit(0);
                    }
                    else if(ans == 1)
                    {
                        System.exit(0);
                    }
                }
                break;
            }
            case "ChangeSrc":{
                if(BackUpGUI.allSrc.getItemCount() < 1) break;
                String oldSrc = (String) BackUpGUI.allSrc.getSelectedItem();
                String src , msg;
                while(true)
                {
                    src = JOptionPane.showInputDialog("Enter new Path of Source : ");
                    msg = BackUpGUI.oper.checkSource(src);
                    if(msg == null)
                    {
                        break;
                    }
                    
                    if(msg.equals("True"))
                    {
                       break; 
                    }
                    
                    JOptionPane.showMessageDialog(null, msg);
                }
                BackUpGUI.oper.changeSrc(oldSrc, src);
                BackUpGUI.dest.setText("");
                BackUpGUI.setFlag(false);
                JOptionPane.showMessageDialog(null, "Successful");
                break;
                
            }
            case "Save":{
                BackUpGUI.oper.saveChanges();
                BackUpGUI.dest.setText("");
                JOptionPane.showMessageDialog(null, "Successful");
                BackUpGUI.setFlag(true);
                break;
            }
            case "Delete":{
                if(BackUpGUI.allSrc.getItemCount() < 1) break;
                BackUpGUI.oper.removeSrc((String)BackUpGUI.allSrc.getSelectedItem());
                BackUpGUI.dest.setText("");
                BackUpGUI.allSrc.removeItem(BackUpGUI.allSrc.getSelectedItem());
                JOptionPane.showMessageDialog(null, "Successful");
                BackUpGUI.setFlag(false);
                break;
            }
            case "ChangeDest":{
                if(BackUpGUI.dest.getText().equals("")) break;
                String destStr = JOptionPane.showInputDialog("Enter new Destination for selected Source");
                BackUpGUI.oper.changeDest((String) BackUpGUI.allSrc.getSelectedItem(), destStr);
                JOptionPane.showMessageDialog(null, "Successful");
                break;
            }
        }
    }
    
}
