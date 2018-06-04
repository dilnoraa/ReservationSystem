
package hotelrezervationsystem;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;


public class Results extends JFrame {
    
        private HotelAgent myAgent;
	private JLabel agentNameLabel, citylabel, priceLabel, rankLabel, roomCountLabel;
	
        Results (HotelClass hotel) {
			
		JPanel p = new JPanel();
                 p.setBackground(Color.decode("#ffffcc"));
                JLabel title = new JLabel("  Results ", JLabel.CENTER);
                title.setForeground(Color.decode("#800000"));
                title.setFont(title.getFont().deriveFont(25f));
                p.add(title);
                p.add(new JLabel(" "));
                
		p.setLayout(new GridLayout(7, 2));
                
                p.add(new JLabel(" Hotel Agent Name:"));              
		agentNameLabel = new JLabel(hotel.getHotelAgentName());
		p.add(agentNameLabel);
                
		p.add(new JLabel(" Hotel City:"));              
		citylabel = new JLabel(hotel.getCity());
		p.add(citylabel);
                
                p.add(new JLabel(" Hotel Rank: "));                
                rankLabel=new JLabel(Integer.toString(hotel.getRank()));              
                p.add(rankLabel);
                
                p.add(new JLabel(" Hotel Room Price: "));
		priceLabel = new JLabel(Integer.toString(hotel.getPrice())); 
		p.add(priceLabel);
                
                 p.add(new JLabel(" Hotel Room Count: "));
		roomCountLabel = new JLabel(Integer.toString(hotel.getRoomCnt()));
		p.add(roomCountLabel);
                
                 getContentPane().add(p, BorderLayout.CENTER);
                 
                 p.add(new JLabel(" Hotel Customers: "));
                 ArrayList <String> customers=hotel.getCustomerNames();
                JList<String> displayList = new JList<>(customers.toArray(new String[0]));
                JScrollPane scrollPane = new JScrollPane(displayList);

                p = new JPanel();
                p.setBackground(Color.decode("#ffffcc"));
		p.add(scrollPane);
		getContentPane().add(p, BorderLayout.SOUTH);
                 
               
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
