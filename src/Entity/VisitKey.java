/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

import java.time.LocalDateTime;

/**
 *
 * @author user
 */


//special class to implement composite key (instead of using only visitid)
public class VisitKey implements Comparable<VisitKey> {
    private LocalDateTime time;
    private String visitID;
    
    public VisitKey(LocalDateTime time, String visitID) {
        this.time = time;
        this.visitID = visitID;
    }
    
    @Override
    public int compareTo(VisitKey other) {
        // Primary: time
        int timeCompare = this.time.compareTo(other.time);
        if (timeCompare != 0) return timeCompare;
        
        // Secondary: visitID (ensures uniqueness)
        return this.visitID.compareTo(other.visitID);
    }
}
