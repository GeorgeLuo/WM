package WalmartHW.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.transaction.annotation.Transactional;

@Entity
@Table(name = "SEAT_BLOCK")
public class SeatBlock implements Comparable<SeatBlock> {
	@Column(name = "BLOCKSTART")
	private int blockStart;
	 @Column(name = "BLOCKEND")
	private int blockEnd;
	
	public SeatBlock(int start, int end) {
		setBlockStart(start);
		setBlockEnd(end);
	}
	
    @GeneratedValue
    @Column(name = "BLOCK_SIZE")
	public int getBlockSize () {
		return getBlockEnd() - getBlockStart() + 1;
	}

    @Transactional
	public int compareTo(SeatBlock s) {
		if(getBlockStart() < s.getBlockStart())
			return -1;
		if(getBlockStart() > s.getBlockStart())
			return 1;
		return 0;
	}

    @Transactional
	public void setBlockStart(int bS) {
		blockStart = bS;
	}
	
    @Override
    @Transactional
	public String toString() {
		String ret = getBlockStart() + " to " + getBlockEnd();
		return ret;
	}

    @GeneratedValue
    @Column(name = "BLOCKEND")
	public int getBlockEnd() {
		return blockEnd;
	}

    @Transactional
	public void setBlockEnd(int blockEnd) {
		this.blockEnd = blockEnd;
	}

    @GeneratedValue
    @Column(name = "BLOCKSTART")
	public int getBlockStart() {
		return blockStart;
	}
}
