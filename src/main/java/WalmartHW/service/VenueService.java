package WalmartHW.service;

import java.util.LinkedList;
import java.util.TreeSet;

import org.springframework.stereotype.Service;

import WalmartHW.model.SeatBlock;
import WalmartHW.model.SeatGroup;

@Service
public class VenueService {
	
	
	private int numEmptySeats;
	private LinkedList<SeatGroup> releaseHoldList;
	private LinkedList<SeatGroup> reservedList;
	private TreeSet<SeatBlock> freeSeatBlocks;
	final int HOLD_EXPIRATION_TIME;
	
	public VenueService() {
		releaseHoldList = new LinkedList<SeatGroup>();
		reservedList = new LinkedList<SeatGroup>();
		freeSeatBlocks = new TreeSet<SeatBlock>();
		freeSeatBlocks.add(new SeatBlock(1, 50));
		numEmptySeats = 50;
		HOLD_EXPIRATION_TIME = 5;
	}
	
	//initiate empty venue with hold expiration time expT and number of seats numSeats
	public VenueService(int numSeats, int expT) {
		releaseHoldList = new LinkedList<SeatGroup>();
		reservedList = new LinkedList<SeatGroup>();
		freeSeatBlocks = new TreeSet<SeatBlock>();
		freeSeatBlocks.add(new SeatBlock(1, numSeats));
		numEmptySeats = numSeats;
		HOLD_EXPIRATION_TIME = expT;
	}
	
	//releases expired holds, then returns the number of available seats.
	public int findSeats() {
		releaseHold();
		return numEmptySeats;
	}
	
	//hold seats by best seat available first, release expired holds first.
	public SeatGroup holdSeats(int groupSize) {
		releaseHold();
		if(this.findSeats() < groupSize) {
			return null;
		}
		else {
			LinkedList<SeatBlock> holdBlocks = holdSeatsHelper(groupSize);
			SeatGroup sG = new SeatGroup(groupSize, holdBlocks);
			releaseHoldList.addLast(sG);
			numEmptySeats -= groupSize;
			return sG;
		}
	}

	//clean free seat groupings and set held seats.
	private LinkedList<SeatBlock> holdSeatsHelper(int groupSize) {
		LinkedList<SeatBlock> ret = new LinkedList<SeatBlock>();
		int numSeatsToHold = groupSize;
		for(SeatBlock fSB : freeSeatBlocks) {
			if(fSB.getBlockSize() > numSeatsToHold) {
				ret.add(new SeatBlock(fSB.getBlockStart(), fSB.getBlockStart() + numSeatsToHold - 1));
				fSB.setBlockStart(fSB.getBlockStart() + numSeatsToHold);
				break;
			}
			else if(fSB.getBlockSize() == numSeatsToHold) {
				ret.add(new SeatBlock(fSB.getBlockStart(), fSB.getBlockStart() + numSeatsToHold - 1));
				freeSeatBlocks.remove(fSB);
				break;
			}
			else {
				numSeatsToHold -= (fSB.getBlockSize());
				ret.add(new SeatBlock(fSB.getBlockStart(), fSB.getBlockEnd()));
			}
		}
		
		LinkedList<SeatBlock> holds = new LinkedList<SeatBlock>();
		for(int i = 0; i < ret.size(); i++) {
			freeSeatBlocks.remove(ret.get(i));
		}

		for(int i = 0; i < ret.size(); i++) {
			holds.add(ret.get(i));
			if(i < ret.size() - 1) {
				if(ret.get(i).getBlockEnd() == ret.get(i + 1).getBlockStart() - 1)
				{
					ret.set(i, new SeatBlock(ret.get(i).getBlockStart(), ret.get(i + 1).getBlockEnd()));
					ret.remove(i + 1);
					i--;
				}
			}
			else
				holds.add(ret.get(i));
		}
		return ret;
	}

	//move held seats to reserved seats by group ID reserveID.
	public SeatGroup reserveSeats(String reserveID) {
		releaseHold();
		for(SeatGroup sG : releaseHoldList) {
			if(sG.getGroupID().equals(reserveID)) {
				sG.setStartTime(sG.getStartTime() * -1);
				reservedList.add(sG);
				return sG;
			}
		}
		return null;
	}
	
	//called before changes to venue, release expired holds.
	public void releaseHold() {
		long currentTime = System.currentTimeMillis();
		for (int i = 0; i < releaseHoldList.size(); i++) {
			long check = (releaseHoldList.peekFirst().getStartTime() + HOLD_EXPIRATION_TIME * 1000);
			if(currentTime >= check && check > 0) {
				freeGroup(releaseHoldList.peekFirst());
				numEmptySeats = numEmptySeats + releaseHoldList.remove().getGroupSize();
			}
			else if(check < 0) {
				releaseHoldList.remove();
			}
			else {
				break;
			}
		}
	}
	
	  //allocate expired seats to empty seats.
	  private void freeGroup(SeatGroup sg) {
		for(SeatBlock sb : sg.getTakenSeats()) {
			freeSeatBlocks.add(sb);
		}
	}
}
