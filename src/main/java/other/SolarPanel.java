package other;

/** Class to represent the parameters of a solar array.
 */
public class SolarPanel {
    private double area;
    private double efficiency;

    public SolarPanel(double area, double efficiency){
        this.area = area;
        this.efficiency = efficiency;
    }

    /** Returns the area of the solar array.
     * @return Area (m^2).
     */
    public double Area(){return  area;}

    /** Returns the efficiency of the solar array.
     * @return Efficiency (0 - 1).
     */
    public double Efficiency(){return efficiency;}
}
