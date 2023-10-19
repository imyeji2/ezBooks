/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author eun15
 */
public class PublisherTableDTO {
    private int Count;
    private String firstConDate;


    public PublisherTableDTO(int Count, String firstConDate) {
        this.Count = Count;
        this.firstConDate = firstConDate;
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int Count) {
        this.Count = Count;
    }

    public String getFirstConDate() {
        return firstConDate;
    }

    public void setFirstConDate(String firstConDate) {
        this.firstConDate = firstConDate;
    }

    @Override
    public String toString() {
        return "PublisherTableDTO{" + "Count=" + Count + ", firstConDate=" + firstConDate + '}';
    }
    
    
}
