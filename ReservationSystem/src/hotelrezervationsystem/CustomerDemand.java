
package hotelrezervationsystem;

public class CustomerDemand implements java.io.Serializable {
    
    private String customerName;
    private String hotelCity;
    private int hotelRank;
    private int HotelPrice;

    public CustomerDemand(String customerName, String hotelCity, int hotelRank, int HotelPrice) {
        this.customerName = customerName;
        this.hotelCity = hotelCity;
        this.hotelRank = hotelRank;
        this.HotelPrice = HotelPrice;
    }


    public String getCustomerName() {
        return customerName;
    }

    public String getHotelCity() {
        return hotelCity;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setHotelCity(String hotelCity) {
        this.hotelCity = hotelCity;
    }
    
    
    public int getRank() {
        return hotelRank;
    }

    public int getPrice() {
        return HotelPrice;
    }

    public void setRank(int hotelRank) {
        this.hotelRank = hotelRank;
    }

    public void setPrice(int HotelPrice) {
        this.HotelPrice = HotelPrice;
    }
    
    
}
