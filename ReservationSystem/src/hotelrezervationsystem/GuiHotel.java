
package hotelrezervationsystem;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GuiHotel extends JFrame {
    
        private HotelAgent hotelAgent;
	private JTextField cityField, priceField, rankField, roomCountField;
	
        GuiHotel(HotelAgent a) {
		super(a.getLocalName());
		
		hotelAgent = a;		
		JPanel p = new JPanel();
                 p.setBackground(Color.decode("#ffffcc"));
                JLabel title = new JLabel("  Hotel Information ", JLabel.CENTER);
                title.setForeground(Color.decode("#800000"));
                title.setFont(title.getFont().deriveFont(20f));
                p.add(title);
                p.add(new JLabel(" "));
		p.setLayout(new GridLayout(5, 2));
		p.add(new JLabel(" Hotel City:"));
		cityField = new JTextField(15);
		p.add(cityField);
                
                p.add(new JLabel(" Hotel Rank:"));
                rankField= new JTextField(15);
                p.add(rankField);
                
		p.add(new JLabel(" Hotel Room Price:"));
		priceField = new JTextField(15);
		p.add(priceField);
                
                p.add(new JLabel(" Hotel Room Count:"));
		roomCountField = new JTextField(15);
		p.add(roomCountField);
                
		getContentPane().add(p, BorderLayout.CENTER);
		
		JButton saveButton = new JButton(" SAVE ");
                saveButton.setForeground(Color.decode("#ffffff"));
                saveButton.setBackground(Color.decode("#333399"));
		saveButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				try {
					String city = cityField.getText().trim();
					String rank = rankField.getText().trim();
                                        String price = priceField.getText().trim();
                                        String roomC = roomCountField.getText().trim();
                                        
                                        HotelClass birOtel=new HotelClass(hotelAgent.getLocalName(), city,new Integer(rank), new Integer(price),new Integer(roomC));
					 // kullanicidan alinan  bilgiler Hotel Agent'teki hotelInformation isimli objede saklanir 
                                        hotelAgent.updateHotelInf(birOtel);					
				}
				catch (Exception e) {
					JOptionPane.showMessageDialog(GuiHotel.this, "Invalid values. "+e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); 
				}
			}
		} );
		p = new JPanel();
		p.add(saveButton);
		getContentPane().add(p, BorderLayout.SOUTH);
		
		addWindowListener(new	WindowAdapter() {
			public void windowClosing(WindowEvent e) {
	            // eger pencere kullanici tarafindan kapatilirsa Agent sonlandirilir
                            hotelAgent.doDelete();
			}
		} );
		
		setResizable(false);
	}
                
        public void showGui() {
		pack();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int centerX = (int)screenSize.getWidth() / 2;
		int centerY = (int)screenSize.getHeight() / 2;
		setLocation(centerX - getWidth() / 2, centerY - getHeight() / 2);
		super.setVisible(true);
	}		
}
