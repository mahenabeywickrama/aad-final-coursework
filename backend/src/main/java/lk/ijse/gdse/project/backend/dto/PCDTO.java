package lk.ijse.gdse.project.backend.dto;

public class PCDTO {
    private Long id;
    private String name;
    private String series;
    private String cpu;
    private String cpuCooler;
    private String ram;
    private String gpu;
    private String motherboard;
    private String storage;
    private String psu;
    private String rgbFans;
    private String pcCase;
    private String operatingSystem;
    private Double price;
    private String imageUrl;
    private Integer totalSales;

    public PCDTO() {
    }

    public PCDTO(Long id, String name, String series, String cpu, String cpuCooler, String ram, String gpu, String motherboard, String storage, String psu, String rgbFans, String pcCase, String operatingSystem, Double price, String imageUrl, Integer totalSales) {
        this.id = id;
        this.name = name;
        this.series = series;
        this.cpu = cpu;
        this.cpuCooler = cpuCooler;
        this.ram = ram;
        this.gpu = gpu;
        this.motherboard = motherboard;
        this.storage = storage;
        this.psu = psu;
        this.rgbFans = rgbFans;
        this.pcCase = pcCase;
        this.operatingSystem = operatingSystem;
        this.price = price;
        this.imageUrl = imageUrl;
        this.totalSales = totalSales;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public String getCpuCooler() {
        return cpuCooler;
    }

    public void setCpuCooler(String cpuCooler) {
        this.cpuCooler = cpuCooler;
    }

    public String getRam() {
        return ram;
    }

    public void setRam(String ram) {
        this.ram = ram;
    }

    public String getGpu() {
        return gpu;
    }

    public void setGpu(String gpu) {
        this.gpu = gpu;
    }

    public String getMotherboard() {
        return motherboard;
    }

    public void setMotherboard(String motherboard) {
        this.motherboard = motherboard;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public String getPsu() {
        return psu;
    }

    public void setPsu(String psu) {
        this.psu = psu;
    }

    public String getRgbFans() {
        return rgbFans;
    }

    public void setRgbFans(String rgbFans) {
        this.rgbFans = rgbFans;
    }

    public String getPcCase() {
        return pcCase;
    }

    public void setPcCase(String pcCase) {
        this.pcCase = pcCase;
    }

    public String getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(String operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(Integer totalSales) {
        this.totalSales = totalSales;
    }
}
