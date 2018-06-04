
package hotelrezervationsystem;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.ContractNetInitiator;
import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CustomerAgent extends Agent {

     private static final long serialVersionUID = 1L;
     private AID[] HotelAgents;  
     private GuiCustomer CustomerGui;
     public CustomerDemand demandInfo; // musterinin istedigi otel bilgileri burada saklanir.
     private int bestProposalPrice = -1;
     private AID bestProposerHotel = null;
     
     @Override
     protected void setup() {
		
		 System.out.println("Hello! CustomerAgent "+getAID().getName()+" is ready.");
                 CustomerGui = new GuiCustomer(this);
                 CustomerGui.showGui();
                 //CustomerAgent yeni eklenen otelleri bulmak icin her dakika YellowPage'i kontrol eder 
                 addBehaviour(new TickerBehaviour(this, 60000) {
                    @Override
                     protected void onTick() {
                        DFAgentDescription template = new DFAgentDescription();
			ServiceDescription sd = new ServiceDescription();
			sd.setType("Hotel-Reserving");
			template.addServices(sd);
			try {
			 DFAgentDescription[] result = DFService.search(myAgent, template); 
		         System.out.println("This CustomerAgent-" + getAID().getLocalName()+ " found the following Hotel Agents:");
			 HotelAgents = new AID[result.length];
                         
			 for (int i = 0; i < result.length; ++i) { //YellowPage'te bulunan tum Hoteller listelenmektedir
			    HotelAgents[i] = result[i].getName();
			    System.out.println(HotelAgents[i].getName());
			}
                               System.out.println();                
			 } catch (FIPAException fe) {
					fe.printStackTrace();
				}         
                                      // Sorgu yollanmadan once musterinin talep bilgisi dolu olmali
                                      if(demandInfo!=null)
                                       myAgent.addBehaviour(new QueryBehaviour());
                                      else {  
                                             block();
                                              System.out.println(" Customer information must be entered from Gui ! ");
                                              System.out.println();
                                      }
                                              
                                  }
                });       
     }
     
     
     @Override
      protected void takeDown() {
		// doDelete metodu calistiktan sonra bu metot calisir ve Agenti sonlandirir
                System.out.println();
		System.out.println("CustomerAgent "+getAID().getName()+" terminating.");
	}
      
             
    
        public class QueryBehaviour extends OneShotBehaviour {
         private static final long serialVersionUID = 1L;
         private int numberOfHotels;
         
        @Override
        public void action() {
          	numberOfHotels=HotelAgents.length;
  		System.out.println( demandInfo.getCustomerName() + "-CustomerAgent sends query to  " +numberOfHotels+" hotels.");
  		ACLMessage msg = new ACLMessage(ACLMessage.CFP);
  		for (int i = 0; i < numberOfHotels; ++i) { // tum otellere CFP  yollanir
  			msg.addReceiver( HotelAgents[i]);
  		}
			msg.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
                        // customer, otellerden gelecek cevaplari en fazla 30 saniye bekler
			msg.setReplyByDate(new Date(System.currentTimeMillis() + 30000));
			msg.setContent("request-reserving");
            
             try {
                 // mesajin icinde  musterinin talep bilgisi de gonderilir
                 msg.setContentObject(demandInfo);
             } catch (IOException ex) {
                 Logger.getLogger(CustomerAgent.class.getName()).log(Level.SEVERE, null, ex);
             }
            
                                //FIPA-Contract Net Protocol Initiator
                             addBehaviour(new ContractNetInitiator(myAgent, msg) {			
                                @Override
				protected void handlePropose(ACLMessage propose, Vector v) {
					System.out.println("HotelAgent-"+propose.getSender().getLocalName()+" PROPOSED "+propose.getContent()+ " to CustomerAgent-" + myAgent.getLocalName());
				}
				
                                @Override
				protected void handleRefuse(ACLMessage refuse) {
					System.out.println("HotelAgent-"+refuse.getSender().getLocalName()+" REFUSED "+ " to CustomerAgent-" + myAgent.getName() );
				}
				
                                @Override
				protected void handleFailure(ACLMessage failure) {
					if (failure.getSender().equals(myAgent.getAMS())) {
						
						System.out.println("Responder (HotelAgent) does not exist");
					}
					else {
						System.out.println("Agent "+failure.getSender().getName()+" failed");
					}
			
					numberOfHotels--;
				}
			// burada otellerden  gelen tum cevaplar tek tek kontrol edilir ve en uygun fiyati teklif eden otele ACCEPTPROPOSAL gonderilir
                                @Override
				protected void handleAllResponses(Vector responses, Vector acceptances) {
                                       
					ACLMessage accept = null;
                                        System.out.println();
					if (responses.size() < numberOfHotels) {
					//cevap veren otel sayisi baslangicta Yellow Page'ten bulunan otel 
                                        // sayisindan daha azsa demek ki bazi oteller 30 sn icinde response gondermediler	
						System.out.println("30 seconds passed. Deadline was over and "+(numberOfHotels - responses.size())+" HotelAgent(s) did not respond");
					        System.out.println();
                                        }
                                        
                                         // eger sadece 1 otel cevap verdiyse ona ACCEPT gonderilir ve rezervasyon yapilir
                                      if(responses.size()==1) {                    
                                           ACLMessage msg=(ACLMessage)responses.firstElement();                                      
						if (msg.getPerformative() == ACLMessage.PROPOSE) {
                                                    ACLMessage reply = msg.createReply();
							reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                                                        acceptances.addElement(reply); // send komutu yerine acceptances kullanilir
							bestProposalPrice = Integer.parseInt(msg.getContent());
                                                        bestProposerHotel = msg.getSender();
                                                        accept=reply;     
                                                        return ; 
                                                }
                                       } 
                                       
                                       
                                // eger 1 den fazla otel teklif gonderdiyse aralarinda en dusuk ucretli olan secilir
					Enumeration e = responses.elements();                              
					while (e.hasMoreElements()) {
						ACLMessage msg = (ACLMessage) e.nextElement();
						if (msg.getPerformative() == ACLMessage.PROPOSE) {                                    
							ACLMessage reply = msg.createReply();
                                                        // once tum otellere REJECT gonderilecegi varsayilir
                                                        //  en dusuk fiyatli olan otel tespit edildikten sonra REJECT ACCEPTe donuşturulur
							reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
							acceptances.addElement(reply); // yanıtlar acceptances ile HotelAgente yollanacaktir 
							int proposal = Integer.parseInt(msg.getContent());
							if (bestProposalPrice == -1 || proposal < bestProposalPrice) {
								bestProposalPrice = proposal;
								bestProposerHotel = msg.getSender();
								accept = reply;                                          
							}
						}
					}				
					if (accept != null) {                                          
                                                //en dusuk fiyatli olan otel bulunduktan sonra REJECT ACCEPT, ACCEPT PROPOSAL'a donuşturulur
                                                 accept.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                                                 System.out.println();
				                 System.out.println("Accepting proposal "+bestProposalPrice+" from " +bestProposerHotel.getName()+ "-HotelAgent responder.");					                                           
					}
                                       
				}
				
                                @Override
				protected void handleInform(ACLMessage inform) {
                                         
                                        System.out.println( demandInfo.getCustomerName() +"-Customer reserved room in " + inform.getSender().getLocalName() + "-HotelAgent" );
                                      // musteri istedigi oteli bulduktan sonra musteriyi temsil eden Agent silinir        
                                       
                                      doDelete();
				}
			} ); 	        
        }        
      }
      
   
    // kullanicidan alinan bilgiler bu davranis ile objede guncellenir 
     public void updateDemand(CustomerDemand oneDemand) {
		addBehaviour(new OneShotBehaviour() {
			
			private static final long serialVersionUID = 1L;

                        @Override
			public void action() {                           
				demandInfo=oneDemand;
                                CustomerGui.dispose();
				System.out.println(" ------ Customer Agent's Demand Information: ------" );
                                System.out.println(" Customer Name: " + demandInfo.getCustomerName());
                                System.out.println(" Customer requested city: " + demandInfo.getHotelCity());
                                System.out.println(" Customer requested Hotel rank: " + demandInfo.getRank());
                                System.out.println(" Customer requested Hotel price: " + demandInfo.getPrice());
                                System.out.println();
                        }
		} );
	}
}