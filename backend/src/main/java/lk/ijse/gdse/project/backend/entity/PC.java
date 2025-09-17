package lk.ijse.gdse.project.backend.entity;

import jakarta.persistence.*;

@Entity
public class PC {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private PCSeries series;
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
    private Double oldPrice;
    private String badge;
    private String bestFor;
    private String imageUrl;
    private String specImageUrl;
    private String extraImage1;
    private String extraImage2;
    private String extraImage3;
    private Integer totalSales;

    public PC() {
    }

    public PC(Long id, String name, PCSeries series, String cpu, String cpuCooler, String ram, String gpu, String motherboard, String storage, String psu, String rgbFans, String pcCase, String operatingSystem, Double price, Double oldPrice, String badge, String bestFor, String imageUrl, String specImageUrl, String extraImage1, String extraImage2, String extraImage3, Integer totalSales) {
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
        this.oldPrice = oldPrice;
        this.badge = badge;
        this.bestFor = bestFor;
        this.imageUrl = imageUrl;
        this.specImageUrl = specImageUrl;
        this.extraImage1 = extraImage1;
        this.extraImage2 = extraImage2;
        this.extraImage3 = extraImage3;
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

    public PCSeries getSeries() {
        return series;
    }

    public void setSeries(PCSeries series) {
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

    public Double getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(Double oldPrice) {
        this.oldPrice = oldPrice;
    }

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }

    public String getBestFor() {
        return bestFor;
    }

    public void setBestFor(String bestFor) {
        this.bestFor = bestFor;
    }

    public String getSpecImageUrl() {
        return specImageUrl;
    }

    public void setSpecImageUrl(String specImageUrl) {
        this.specImageUrl = specImageUrl;
    }

    public String getExtraImage1() {
        return extraImage1;
    }

    public void setExtraImage1(String extraImage1) {
        this.extraImage1 = extraImage1;
    }

    public String getExtraImage2() {
        return extraImage2;
    }

    public void setExtraImage2(String extraImage2) {
        this.extraImage2 = extraImage2;
    }

    public String getExtraImage3() {
        return extraImage3;
    }

    public void setExtraImage3(String extraImage3) {
        this.extraImage3 = extraImage3;
    }
}
