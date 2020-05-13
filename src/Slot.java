///////////////////////////////////////////////////////////////////////////////
// File Written by: Michael A (s3662507) (Last Edit: 13/05/2020)
// Database Systems - Assignment 02
// Purpose of this Class:
// This class is just a notion class to store the Records of the Page
///////////////////////////////////////////////////////////////////////////////
public class Slot {
	// ------------------------------ Members ------------------------------ //
	Record record;
	
	// ------------------------ Default Constructor ------------------------ //
	public Slot(Record record) {
		this.record = record;
	}
	
	// ----------------------------- Accessors ----------------------------- //
	public Record get_record() {
		return record;
	}
	
	// ----------------------------- Mutators ------------------------------ //
	public void set_record(Record record) {
		this.record = record;
	}
}
