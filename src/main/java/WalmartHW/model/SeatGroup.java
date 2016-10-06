package WalmartHW.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.UUID;

@Entity
@Table(name = "SEAT_GROUP")
public class SeatGroup {
	@Column(name = "STARTTIME")
	private long startTime;
	@Column(name = "TAKENSEATS")
	private LinkedList<SeatBlock> takenSeats;
	@Column(name = "GROUPSIZE")
	private int groupSize;
	@Id
	@Column(name = "GROUP_ID")
	private String groupID;
	
	public SeatGroup(int size, LinkedList<SeatBlock> holdBlocks) {
		setStartTime(System.currentTimeMillis());
		groupSize = size;
		setTakenSeats(holdBlocks);
		setGroupID(UUID.randomUUID().toString());
	}

    @GeneratedValue
    @Column(name = "START_TIME")
	public long getStartTime() {
		return startTime;
	}

    @Transactional
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

    @GeneratedValue
    @Column(name = "GROUP_SIZE")
	public int getGroupSize() {
		return groupSize;
	}
	
    @Override
    @Transactional
	public String toString() {
		String ret = "";
		for(SeatBlock sB : getTakenSeats()) {
			ret += sB.getBlockStart() + " to " + sB.getBlockEnd() + ", ";
		}
		ret += "at " +  startTime;
		return ret;
	}

    @GeneratedValue
    @Column(name = "TAKEN_SEATS")
	public LinkedList<SeatBlock> getTakenSeats() {
		return takenSeats;
	}

    @Transactional
	public void setTakenSeats(LinkedList<SeatBlock> holdBlocks) {
		this.takenSeats = holdBlocks;
	}

    @Id
    @GeneratedValue
    @Column(name = "GROUP_ID")
	public String getGroupID() {
		return groupID;
	}

    @Transactional
	public void setGroupID(String groupID) {
		this.groupID = groupID;
	}
}
