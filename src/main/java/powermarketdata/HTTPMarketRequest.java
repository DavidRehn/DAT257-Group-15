package powermarketdata;


public class HTTPMarketRequest{
    private final int year;
    private final int month;
    private final int day;
    private final String region;
    
    public HTTPMarketRequest(int year, int month, int day, String region){
    this.year = year;
    this.month = month;
    this.day = day;
    this.region = region;
    }

    public int getDay() {return day;}
    public int getMonth() {return month;}
    public int getYear() {return year;}
    public String getRegion() {return region;}
}