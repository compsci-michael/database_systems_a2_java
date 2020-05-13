///////////////////////////////////////////////////////////////////////////////
// File Written by: Michael A (s3662507) (Last Edit: 13/05/2020)
// Database Systems - Assignment 02
// Purpose of this Class:
// This class is Holds the Slots containing the Records from the Data Source
// This class is used to hold the data that is to be written to file
///////////////////////////////////////////////////////////////////////////////
public class Page {
	// ------------------------------ Members ------------------------------ //
	public static int FIXED_RECORD_LENGTH = 360;
	int number_of_records_in_page;
	int num_of_slots;
	boolean page_is_full;
	int page_size;
	Slot[] page_slots;
	
	// ------------------------ Default Constructor ------------------------ //
	public Page(int page_size) {
		this.number_of_records_in_page = 0;
		this.page_is_full = false;
		this.page_size = page_size;
		// Allow for Space at the End of the Page
		this.num_of_slots = (int) Math.floor(page_size/FIXED_RECORD_LENGTH)-1;
		this.page_slots = new Slot[num_of_slots];
	}

	// ----------------------------- Accessors ----------------------------- //
	public int get_number_of_records_in_page() {
		return number_of_records_in_page;
	}
	public boolean is_page_is_full() {
		return page_is_full;
	}
	public int get_page_size() {
		return page_size;
	}
	public Record get_page_slot_record(int slot_number) {
		return page_slots[slot_number].get_record();
	}
	public int get_number_of_available_bytes() {
		return page_size-(number_of_records_in_page*FIXED_RECORD_LENGTH);
	}
	
	// ----------------------------- Mutators ------------------------------ //
	public void set_page_size(int page_size) {
		this.page_size = page_size;
	}
	public void set_a_page_slot(Record record) {
		page_slots[number_of_records_in_page] = new Slot(record);
		number_of_records_in_page++;
		// Check if we are at the Limit
		if(number_of_records_in_page == num_of_slots) {
			page_is_full = true;
		}
	}
	
	// -------------------------- Override Equals -------------------------- //
	@Override
	public boolean equals(Object o) {
	    if (this == o) return true;
	    if (o == null || getClass() != o.getClass()) return false;
	    
	    Page page = (Page) o;
	    return page_slots == page.page_slots;
	}
}
