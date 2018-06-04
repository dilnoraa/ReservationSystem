
package hotelrezervationsystem;

import java.util.ArrayList;

public class HotelClass implements java.io.Serializable {
    
    private String hotelAgentName;
    private String city;
    private int rank;
    private int price;
    private int roomCnt;
    ArrayList<String> customerNames = new ArrayList<String>(); // bu otelde rezervasyon yapan musterilerin isimleri tutulur
    
   
    public HotelClass(String hotelAgentName, String city, int rank, int price, int roomCnt) {
        this.hotelAgentName = hotelAgentName;
        this.city = city;
        this.rank = rank;
        this.price = price;
        this.roomCnt = roomCnt;
    }

    public String getHotelAgentName() {
        return hotelAgentName;
    }

    public void setHotelAgentName(String hotelAgentName) {
        this.hotelAgentName = hotelAgentName;
    }

    
    public ArrayList<String> getCustomerNames() {
        return customerNames;
    }
    
     
    public int getRank() {
        return rank;
    }

    public int getRoomCnt() {
        return roomCnt;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public void setRoomCnt(int roomCnt) {
        this.roomCnt = roomCnt;
    }
       

    public String getCity() {
        return city;
    }

    public int getPrice() {
        return price;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setPrice(int price) {
        this.price = price;
    }
    
}
