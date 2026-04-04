/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

/**
 *
 * @author user
 */
public class DoctorPerformanceKey implements Comparable<DoctorPerformanceKey> {

    private int count;
    private String doctorID;

    public DoctorPerformanceKey(int count, String doctorID) {
        this.count = count;
        this.doctorID = doctorID;
    }

    @Override
    public int compareTo(DoctorPerformanceKey o) {

        // DESC order (higher count first)
        int cmp = Integer.compare(o.count, this.count);
        if (cmp != 0) return cmp;

        return this.doctorID.compareTo(o.doctorID);
    }
}
