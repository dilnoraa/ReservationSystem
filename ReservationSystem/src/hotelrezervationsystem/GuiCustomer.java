
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

public class GuiCustomer extends JFrame {
    
        private CustomerAgent customerAgent;
	private JTextField CustomerName, HotelCity, HotelRankField, HotelPriceField;
	
        GuiCustomer(CustomerAgent a) {
		super(a.getLocalName());
		
		customerAgent = a;
		
		JPanel p = new JPanel();
                p.setBackground(Color.decode("#ffffcc"));
                JLabel title = new JLabel("  Customer's Information and Demand", JLabel.CENTER);
                title.setForeground(Color.decode("#800000"));
                title.setFont(title.getFont().deriveFont(20f));
                 p.add(title);
                  p.add(new JLabel(" "));
                
		p.setLayout(new GridLayout(5, 2));
                
                p.add(new JLabel(" Customer Name:"));
		CustomerName = new JTextField(15);
		p.add(CustomerName);
                
                p.add(new JLabel(" Desired Hotel City:"));
		HotelCity = new JTextField(15);
		p.add(HotelCity);
                
		p.add(new JLabel(" Desired Hotel Rank:"));
		HotelRankField = new JTextField(15);
		p.add(HotelRankField);
                
		p.add(new JLabel(" Desired Hotel Room Price:"));
		HotelPriceField = new JTextField(15);
		p.add(HotelPriceField);
		getContentPane().add(p, BorderLayout.CENTER);
		
		JButton saveButton = new JButton(" SAVE ");
                saveButton.setForeground(Color.decode("#ffffff"));
                saveButton.setBackground(Color.decode("#333399"));
		saveButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				try {
                                        String customerName = CustomerName.getText().trim();
					String hotelCity = HotelCity.getText().trim();
					String hotelRank = HotelRankField.getText().trim();
                                        String hotelPrice = HotelPriceField.getText().trim();
                                        
                                        CustomerDemand demand=new CustomerDemand(customerName, hotelCity, new Integer(hotelRank),new Integer(hotelPrice));
				  // kullanicidan alinan  bilgiler Customer agent'teki demandInfo isimli objede saklanir 
                                        customerAgent.updateDemand(demand); 
                                      
                                                                        
				}
				catch (Exception e) {
                                       
					JOptionPane.showMessageDialog(GuiCustomer.this, "Invalid values. "+e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); 
				}
			}
		} );
		p = new JPanel();
		p.add(saveButton);
		getContentPane().add(p, BorderLayout.SOUTH);
		
		addWindowListener(new	WindowAdapter() {
			public void windowClosing(WindowEvent e) {
                              // eger pencere kullanici tarafindan kapatilirsa Agent sonlandirilir
				customerAgent.doDelete();
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

