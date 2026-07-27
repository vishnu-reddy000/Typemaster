package com.typemaster.dto;

public class SystemStatsDTO {
    private long totalTests;
    private long totalUsers;
    private long totalCountries;
    private double averageWpm;

    public SystemStatsDTO() {}

    public SystemStatsDTO(long totalTests, long totalUsers, long totalCountries, double averageWpm) {
        this.totalTests = totalTests;
        this.totalUsers = totalUsers;
        this.totalCountries = totalCountries;
        this.averageWpm = averageWpm;
    }

    public long getTotalTests() {
        return totalTests;
    }

    public void setTotalTests(long totalTests) {
        this.totalTests = totalTests;
    }

    public long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public long getTotalCountries() {
        return totalCountries;
    }

    public void setTotalCountries(long totalCountries) {
        this.totalCountries = totalCountries;
    }

    public double getAverageWpm() {
        return averageWpm;
    }

    public void setAverageWpm(double averageWpm) {
        this.averageWpm = averageWpm;
    }
}
