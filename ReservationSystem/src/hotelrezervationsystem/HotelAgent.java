package hotelrezervationsystem;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.proto.ContractNetResponder;
import java.util.Random;

public class HotelAgent extends Agent {
    private static final long serialVersionUID = 1 L;
    private HotelClass hotelInformation = new HotelClass(" ", " ", 0, -1, -1);
    private GuiHotel hotelGui;
    protected void setup() {
        System.out.println();
        System.out.println("Hello! HotelAgent " + getAID().getName() + " is ready.");
        hotelGui = new GuiHotel(this);
        hotelGui.showGui();
       
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("Hotel-Reserving");
        sd.setName("Hotel Customer Communication");
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
            System.out.println(" DF Registration of " + getLocalName());
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }

        System.out.println("HotelAgent-" + getLocalName() + " waiting CFP from CustomerAgents...");
        MessageTemplate template = MessageTemplate.and(
            MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET),
            MessageTemplate.MatchPerformative(ACLMessage.CFP));

        //FIPA-Contract Net Protocol             
        addBehaviour(new ContractNetResponder(this, template) {
            CustomerDemand objectCustomer = null;

            @Override
            protected ACLMessage handleCfp(ACLMessage cfp) throws NotUnderstoodException, RefuseException {

                try { 
                    objectCustomer = (CustomerDemand) cfp.getContentObject();

                } catch (UnreadableException ex) {
                    System.out.println(ex);
                }
              
                int price = execQuery(objectCustomer);
                if (price == -1) {
                    System.out.println("Hotel Information of " + getLocalName() + " still didn't entered from gui!");
                    block();
                    return null;
                }

                if (price == 0) {
                    // eger bilgiler uyusmadiysa REFUSE iletilir				
                    System.out.println("HotelAgent-" + getLocalName() + ", sends REFUSE to " + objectCustomer.getCustomerName() + " ,( no appropriate hotel ) ");
                    ACLMessage propose = cfp.createReply();
                    propose.setPerformative(ACLMessage.REFUSE);
                    return propose; // myAgent.send(propose) yerine return kullianilir

                } else { 

                    if (getRandomBoolean()) {

                        ACLMessage propose = cfp.createReply();
                        propose.setPerformative(ACLMessage.PROPOSE);
                        propose.setContent(String.valueOf(price));
                        propose.setConversationId("reservation");
                        return propose;
                    }
                }
                return null;

            }

            @Override
            protected ACLMessage handleAcceptProposal(ACLMessage cfp, ACLMessage propose, ACLMessage accept) throws FailureException {
                System.out.println("CustomerAgent accepts Proposal from " + getLocalName());
                ACLMessage inform = accept.createReply();             
                inform.setPerformative(ACLMessage.INFORM);           
                int room_count = hotelInformation.getRoomCnt();
                room_count--;
                hotelInformation.setRoomCnt(room_count);
                hotelInformation.customerNames.add(objectCustomer.getCustomerName());

                Results result = new Results((hotelInformation));
                result.showGui();
                System.out.println("HotelAgent-" + getLocalName() + ", sends INFORM");
                System.out.println("Hotel Reservation is completed. Action successfully performed");
                return inform;


            }

            @Override
            protected void handleRejectProposal(ACLMessage cfp, ACLMessage propose, ACLMessage reject) {
                System.out.println(" HotelAgent-" + getLocalName() + " Proposal rejected");
            }
        });

        if (hotelInformation.getRoomCnt() == 0)
            doDelete();
    }


    public boolean getRandomBoolean() {

        Random random = new Random();
        return random.nextBoolean();
    }


    private int execQuery(CustomerDemand demand) {

        if (hotelInformation.getPrice() == -1)
            return -1;
    
        if (hotelInformation.getRoomCnt() > 0 && demand.getHotelCity().equals(hotelInformation.getCity()) && demand.getRank() == hotelInformation.getRank())

            return hotelInformation.getPrice();

        else
            return 0;
    }

    @Override
    protected void takeDown() {

        try {
            DFService.deregister(this);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }

        hotelGui.dispose();
        System.out.println("HotelAgent " + getAID().getName() + " is terminating.");
    }


    // kullanicidan alinan bilgiler bu davranisla guncellenir 
    public void updateHotelInf(HotelClass oneHotel) {
        addBehaviour(new OneShotBehaviour() {
            private static final long serialVersionUID = 1 L;
            @Override
            public void action() {
                hotelInformation = oneHotel;
                hotelGui.dispose();
                System.out.println(" ------ Hotel Agent's Information: ------");
                System.out.println(" Hotel Agent Name: " + hotelInformation.getHotelAgentName());
                System.out.println(" Hotel City: " + hotelInformation.getCity());
                System.out.println(" Hotel Rank: " + hotelInformation.getRank());
                System.out.println(" Hotel Room Price: " + hotelInformation.getPrice());
                System.out.println(" Hotel Room Count: " + hotelInformation.getRoomCnt());
                System.out.println();

            }
        });
    }

}
