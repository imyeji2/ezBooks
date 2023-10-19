/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author eun15
 */
public class PublisherDTO {
    
    private String puId;
    private String puName;
    private String puTel;
    private String puManager;
    private String puHp;
    private int state;

    
    public PublisherDTO(String puId, String puName, String puTel, String puManager, String puHp) {
        this.puId = puId;
        this.puName = puName;
        this.puTel = puTel;
        this.puManager = puManager;
        this.puHp = puHp;
    }
    
   
    public PublisherDTO(String puId, String puName, String puTel, String puManager, String puHp, int state) {
        this.puId = puId;
        this.puName = puName;
        this.puTel = puTel;
        this.puManager = puManager;
        this.puHp = puHp;
        this.state = state;
    }

    public String getPuId() {
        return puId;
    }

    public void setPuId(String puId) {
        this.puId = puId;
    }

    public String getPuName() {
        return puName;
    }

    public void setPuName(String puName) {
        this.puName = puName;
    }

    public String getPuTel() {
        return puTel;
    }

    public void setPuTel(String puTel) {
        this.puTel = puTel;
    }

    public String getPuManager() {
        return puManager;
    }

    public void setPuManager(String puManager) {
        this.puManager = puManager;
    }

    public String getPuHp() {
        return puHp;
    }

    public void setPuHp(String puHp) {
        this.puHp = puHp;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "PublisherDTO{" + "puId=" + puId + ", puName=" + puName + ", puTel=" + puTel + ", puManager=" + puManager + ", puHp=" + puHp + ", state=" + state + '}';
    }
    
    
    
    
    
}
