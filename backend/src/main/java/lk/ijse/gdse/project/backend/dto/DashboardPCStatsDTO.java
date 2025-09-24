package lk.ijse.gdse.project.backend.dto;

public class DashboardPCStatsDTO {
    private long totalPCs;
    private long totalSales;
    private double totalRevenue;
    private String topSeries;

    public DashboardPCStatsDTO() {
    }

    public DashboardPCStatsDTO(long totalPCs, long totalSales, double totalRevenue, String topSeries) {
        this.totalPCs = totalPCs;
        this.totalSales = totalSales;
        this.totalRevenue = totalRevenue;
        this.topSeries = topSeries;
    }

    public long getTotalPCs() {
        return totalPCs;
    }

    public void setTotalPCs(long totalPCs) {
        this.totalPCs = totalPCs;
    }

    public long getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(long totalSales) {
        this.totalSales = totalSales;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public String getTopSeries() {
        return topSeries;
    }

    public void setTopSeries(String topSeries) {
        this.topSeries = topSeries;
    }
}
