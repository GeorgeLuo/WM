package TestSuite;

import java.io.IOException;

import WalmartHW.model.SeatGroup;
import WalmartHW.service.VenueService;

public class VenueTest extends junit.framework.TestCase {
    private VenueService v;

    public void setUp() {
    	
    }

    
    public void test_findSeats() throws IOException {
		v = new VenueService();
		assertEquals(v.findSeats(), 50);
    }
    
    public void test_basicHold() throws IOException {
		v = new VenueService();
		v.holdSeats(15);
		assertEquals(v.findSeats(), 35);
    }
    
    public void test_expiration() throws IOException, InterruptedException {
		v = new VenueService();
		v.holdSeats(15);
		Thread.sleep(5000);
		assertEquals(v.findSeats(), 50);
    }
    
    public void test_basicReserve() throws IOException, InterruptedException {
		v = new VenueService();
		String seatGroupID = v.holdSeats(15).getGroupID();
		long n = v.reserveSeats(seatGroupID).getStartTime();
		assertTrue(n < 0);
		Thread.sleep(5000);
		assertEquals(v.findSeats(), 35);
    }
    
    public void test_multipleHolds() throws IOException {
		v = new VenueService();
		v.holdSeats(15).getGroupID();
		v.holdSeats(1).getGroupID();
		assertEquals(v.findSeats(), 34);
    }
    
    public void test_multipleHoldsWithExpiration() throws IOException, InterruptedException {
		v = new VenueService();
		v.holdSeats(15).getGroupID();
		Thread.sleep(5000);
		SeatGroup sG = v.holdSeats(10);
		assertTrue(sG.getTakenSeats().getFirst().getBlockStart() == 1);
		assertTrue(sG.getTakenSeats().getFirst().getBlockEnd() == 10);
		assertEquals(v.findSeats(), 40);
		SeatGroup sG2 = v.holdSeats(5);
		assertTrue(sG2.getTakenSeats().getFirst().getBlockStart() == 11);
		assertTrue(sG2.getTakenSeats().getFirst().getBlockEnd() == 15);
    }
    
    public void test_multipleSeatBlockGroup() throws IOException, InterruptedException {
		v = new VenueService();
		v.holdSeats(3);
		Thread.sleep(3000);
		v.holdSeats(10);
		Thread.sleep(2000);
		SeatGroup sG = v.holdSeats(10);
		assertTrue(sG.getTakenSeats().getFirst().getBlockStart() == 1);
		assertTrue(sG.getTakenSeats().getFirst().getBlockEnd() == 3);
		assertTrue(sG.getTakenSeats().get(1).getBlockStart() == 14);
		assertTrue(sG.getTakenSeats().get(1).getBlockEnd() == 20);
    }
    
    public void test_fullHouse() throws IOException {
		v = new VenueService();
		v.holdSeats(50);
		assertNull(v.holdSeats(1));
    }
    
    public void test_initializeVenue() throws IOException, InterruptedException {
		v = new VenueService(25, 3);
		assertEquals(25, v.findSeats());
		v.holdSeats(5);
		Thread.sleep(3000);
		assertEquals(25, v.findSeats());
    }
    
    public void test_multipleReserves() throws IOException, InterruptedException {
		v = new VenueService(30, 3);
		
		String seatGroupID = v.holdSeats(15).getGroupID();
		String seatGroupID2 = v.holdSeats(15).getGroupID();
		
		v.reserveSeats(seatGroupID);
		v.reserveSeats(seatGroupID2);
		
		assertEquals(0, v.findSeats());
		Thread.sleep(3000);
		assertEquals(0, v.findSeats());
    }
}