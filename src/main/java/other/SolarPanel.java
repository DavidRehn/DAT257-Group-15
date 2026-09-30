package other;

public class SolarPanel {
    private double area;
    private double efficiency;

    public SolarPanel(double area, double efficiency){
        this.area = area;
        this.efficiency = efficiency;
    }

    public double Area(){return  area;}

    public double Efficiency(){return efficiency;}
}
