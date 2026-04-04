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
public class WalkInVisit extends Visit{
    
    private int triageScore;
    private String triageNote;
    
    
    public WalkInVisit(String visitID, Patient patient, Service service,
                       LocalDateTime time, int triageScore, String triageNote) {
        super(visitID, patient, service, time);
        this.triageScore = triageScore;
        this.triageNote = triageNote;
    }
    
    public int getTriageScore() { return triageScore; }
    
    public String getTriageNote() { return triageNote; }

    @Override
    public String getVisitType() {
        return "Walk-in";
    }
}
