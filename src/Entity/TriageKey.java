/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;


/**
 *
 * @author user
 */
public class TriageKey implements Comparable<TriageKey> {  //another composite key for walkin visit

    private WalkInVisit visit;

    public TriageKey(WalkInVisit visit) {
        this.visit = visit;
    }

    @Override
    public int compareTo(TriageKey o) {

        // Higher score first
        int cmp = Integer.compare(o.visit.getTriageScore(),
                                  this.visit.getTriageScore());
        if (cmp != 0) return cmp;

        // Earlier time first
        cmp = this.visit.getTime().compareTo(o.visit.getTime());
        if (cmp != 0) return cmp;

        return this.visit.getVisitID().compareTo(o.visit.getVisitID());
    }
}
