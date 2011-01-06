package ClientRMI2;

// This program was condensed from Chapter 13: Graphics demonstration of the book
// Java, How to programme, fifth edition, by Deitel and Deitel. (Prentice Hall)
// ISBN 0-13-101621-0
//
import java.awt.*;
import java.awt.event.*;
import java.awt.GraphicsEnvironment.*;
import javax.swing.*;

class GUIclass extends JFrame{

   public static int win[]={0,0,0,0,0,0,0,0,0};
   JLabel lText,lIcon,lIconText;    // 0. labels
   JTextField f1,f2,f3,fFont;       // 1. textFields
   JPasswordField f4;               //    passwordField
   JButton b0,b1,b2,b3,b4,b5;       // 2. buttons
   JCheckBox cbBold,cbItalic;       // 3. checkboxes & radio buttons
   JComboBox coImage;               // 4. comboBoxes
   JList lColour,lCopy;             // 5. JList
   JRadioButton rb1,rb2,rb3;        //    Selection modes
   JButton bCopy;
   CheckBoxHandler cbh=new CheckBoxHandler();
   ButtonHandler bh=new ButtonHandler();
   JRadioButton rbSerif,rbHelvetica,rbCourier;
   ButtonGroup rbGroup=new ButtonGroup();
   String names[]={"f1.gif","f2.gif","f3.gif"};
   Icon icons[]={new ImageIcon(names[0]),new ImageIcon(names[1]),new ImageIcon(names[2])};
   String last="";

   public GUIclass (int nType, String sTitle, int l1, int l2, int s1, int s2){

      super(sTitle);
      // GUI Stuff
      Container c=getContentPane();
      c.setLayout(new FlowLayout());

      switch(nType){
         case 0:  // labels
            lText=new JLabel("Text Label");
            lText.setToolTipText("Label with text only");
            c.add(lText);

            Icon button=new ImageIcon("button.gif");
            // note: this cute button was borrowed from Internet temporary files
            //       source is unfortunately unknown
            lIcon=new JLabel("animated button",button,SwingConstants.LEFT);
            lIcon.setToolTipText("Label with text left and Icon");
            // c.add(lIcon);  // undelete this line to show label

            lIconText=new JLabel();  // add details later
            lIconText.setText("Icon+text bottom");
            lIconText.setIcon(button);
            lIconText.setHorizontalTextPosition(SwingConstants.CENTER);
            lIconText.setVerticalTextPosition(SwingConstants.BOTTOM);
            lIconText.setToolTipText("Label with Icon and text at bottom");
            c.add(lIconText);

            win[0]=1;
            break;

            case 1: // textfields
               f1=new JTextField("Uneditable");
               f1.setEditable(false);
               f2=new JTextField(10);  // field with 10 spaces
               f3=new JTextField("Enter Text"); // field with initial text
               f4=new JPasswordField("Hidden");
               c.add(f1);c.add(f2);c.add(f3);c.add(f4);
               // now register event handlers
               TextFieldHandler h=new TextFieldHandler();
               f2.addActionListener(h);
               f3.addActionListener(h);
               f4.addActionListener(h);
               //out.append("1. uneditable textField\n2. normal textField\n3. textField with default text\n4. password textField");
               win[1]=1;
               break;

            case 2:  // buttons are used to call up different elements
               //ButtonHandler bh=new ButtonHandler();
               b0=new JButton("JLabels"); c.add(b0);
               b1=new JButton("JTextFields"); c.add(b1);
               b3=new JButton("chkBoxes+radio btns"); c.add(b3);
               b4=new JButton("JcomboBoxs"); c.add(b4);
               b5=new JButton("JLists"); c.add(b5);
               b0.addActionListener(bh);
               b1.addActionListener(bh);
               b3.addActionListener(bh);
               b4.addActionListener(bh);
               b5.addActionListener(bh);

               win[2]=1;
               break;
            case 3:  // check-boxes and radio buttons
               f1=new JTextField(30);
               f1.setText("Select Font");
               f1.setFont(new Font("Serif",Font.PLAIN,14));
               f1.setEditable(false);
               c.add(f1);

               // checkboxes
               cbBold=new JCheckBox("bold"); c.add(cbBold);
               cbItalic=new JCheckBox("italic"); c.add(cbItalic);
               //CheckBoxHandler cbh=new CheckBoxHandler();
               cbBold.addItemListener(cbh);
               cbItalic.addItemListener(cbh);

               // radiobuttons
               rbSerif=new JRadioButton("Times",true); c.add(rbSerif);
               rbHelvetica=new JRadioButton("Helvetica",false); c.add(rbHelvetica);
               rbCourier=new JRadioButton("Courrier",false); c.add(rbCourier);
               //rbGroup=new ButtonGroup();
               rbGroup.add(rbSerif);
               rbGroup.add(rbHelvetica);
               rbGroup.add(rbCourier);
               rbSerif.addItemListener(new RadioButtonHandler());
               rbHelvetica.addItemListener(new RadioButtonHandler());
               rbCourier.addItemListener(new RadioButtonHandler());

               // Font size
               lText=new JLabel("Font Size="); c.add(lText);
               fFont=new JTextField(5); c.add(fFont);
               fFont.addActionListener(new TextFieldHandler());

               win[3]=1;
               break;

            case 4:     // JComboBox
               coImage=new JComboBox(names);
               coImage.setMaximumRowCount(3);
               coImage.addItemListener(
                  new ItemListener(){
                     public void itemStateChanged(ItemEvent e){
                        if(e.getStateChange()==ItemEvent.SELECTED)lIcon.setIcon(icons[coImage.getSelectedIndex()]);
                     }
                  }
               );
               c.add(coImage);
               lIcon=new JLabel(icons[0]);
               c.add(lIcon);
               break;

            case 5:     // JList
               String colours[]={"RED","GREEN","BLUE","BLACK","WHITE","PINK","ORANGE","CYAN","MAGENTA","YELLOW","GRAY"};
               lColour=new JList(colours);
               lColour.setVisibleRowCount(colours.length);
               c.add(new JScrollPane(lColour));
               rb1=new JRadioButton("Single",true); c.add(rb1);
               rb2=new JRadioButton("Sing-Interval",false); c.add(rb2);
               rb3=new JRadioButton("Mult-Interval",false); c.add(rb3);
               rbGroup.add(rb1); rbGroup.add(rb2); rbGroup.add(rb3);
               rb1.addItemListener(cbh);
               rb2.addItemListener(cbh);
               rb3.addItemListener(cbh);
               bCopy=new JButton("COPY"); c.add(bCopy);
               bCopy.addActionListener(bh);
               lCopy=new JList();
               lCopy.setVisibleRowCount(5);
               lCopy.setFixedCellWidth(100);
               lCopy.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
               c.add(new JScrollPane(lCopy));
               break;

      }

      setLocation(l1,l2);
      setSize(s1,s2);
      setVisible(true);
   }

   int getFontSize(){
      int nFont=0;
      try{
         nFont=Integer.parseInt(fFont.getText());
      }
      catch(Exception e){ nFont=14;}
      if(nFont<10)nFont=10; if(nFont>30)nFont=30;
      return nFont;
   }

   private class TextFieldHandler implements ActionListener{
      // innerclass TextField Handler
      public void actionPerformed(ActionEvent e){
         String msg="Text entered : ";
         if(e.getSource()==fFont){
            f1.setFont(new Font(f1.getFont().getFamily(),f1.getFont().getStyle(),getFontSize()));
            return;
         }
         if(e.getSource()==f2)msg+=e.getActionCommand();
         else if(e.getSource()==f3)msg+=e.getActionCommand();
         else if(e.getSource()==f4)msg+=e.getActionCommand();
         JOptionPane.showMessageDialog(null,msg);
      }
   }

   private class ButtonHandler implements ActionListener{
      // innerclass ButtonHandler
      public void actionPerformed(ActionEvent e){
         if(e.getSource()==b0&&win[0]==0){
            GUIclass g0=new GUIclass(0,"Labels",0,0,350,120);
         } else if(e.getSource()==b1&&win[1]==0){
            GUIclass g1=new GUIclass(1,"TextFields",50,50,350,100);
         } else if(e.getSource()==b3&&win[3]==0){
            GUIclass g3=new GUIclass(3,"CheckBoxes & Radio Buttons",150,150,350,150);
         }else if(e.getSource()==b4&&win[4]==0){
            GUIclass g4=new GUIclass(4,"ComboBoxes",200,200,350,180);
         }else if(e.getSource()==b5&&win[5]==0){
            GUIclass g5=new GUIclass(5,"JList",250,250,350,400);
         }else if(e.getSource()==bCopy){  // copy colours
            lCopy.setListData(lColour.getSelectedValues());
         }
      }

   }

   private class CheckBoxHandler implements ItemListener{
      // innerclass CheckBoxHandler
      private int iBold=Font.PLAIN;
      private int iItalic=Font.PLAIN;

      public void itemStateChanged(ItemEvent e){
         if(e.getSource()==cbBold||e.getSource()==cbItalic){
            // Radio Buttons and CheckBoxes
            iBold=(cbBold.isSelected())?Font.BOLD:Font.PLAIN;
            iItalic=(cbItalic.isSelected())?Font.ITALIC:Font.PLAIN;
            f1.setFont(new Font(f1.getFont().getFamily(),iBold+iItalic, getFontSize()));
         }
      }
   }

   private class RadioButtonHandler implements ItemListener{
      // innerclass RadioButtonHandler

      public void itemStateChanged(ItemEvent e){
         if(e.getSource()==rbSerif||e.getSource()==rbHelvetica||e.getSource()==rbCourier){
            String Fam="";
            if(e.getSource()==rbSerif)Fam="Serif";
            else if(e.getSource()==rbHelvetica)Fam="Helvetica";
            else if(e.getSource()==rbCourier)Fam="Courier";
            f1.setFont(new Font(Fam,f1.getFont().getStyle(),getFontSize()));
         } else if(e.getSource()==rb1||e.getSource()==rb2||e.getSource()==rb3){
            // JList
            int sMode=0;
            if(rb1.isSelected())sMode=ListSelectionModel.SINGLE_SELECTION;
            else if(rb2.isSelected())sMode=ListSelectionModel.SINGLE_INTERVAL_SELECTION;
            else sMode=ListSelectionModel.MULTIPLE_INTERVAL_SELECTION;
            lColour.setSelectionMode(sMode);
         }
      }
   }

}

public class GUIDemo{
   public static void main(String[] args){
      String sType[]={"Labels","TextFields","Buttons"};
      GUIclass g2=new GUIclass(2,sType[2],100,100,350,150);// buttons
   }
}
