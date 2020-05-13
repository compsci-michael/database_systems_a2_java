import java.util.HashMap;

///////////////////////////////////////////////////////////////////////////////
// File Written by: Michael A (s3662507) (Last Edit: 13/05/2020)
// Database Systems - Assignment 02
// Purpose of this Class:
// This is the Driver Class which contains the Main method for loading the
// java heap file and creating a Hashfile
///////////////////////////////////////////////////////////////////////////////
public class hashload {
	
	// Must be able to execute the following: java dbload -p pagesize datafile
	public static void main(String[] args) {
		final long full_start_time = System.nanoTime();
		HMethods hm = new HMethods();
		boolean data_read_failed = false;
		HashMap<String, Record> data = new HashMap<String, Record>();
		int data_size = 0;
		HashMap<String, Page> page_data = new HashMap<String, Page>();

		// Step 1: Validate the Input
		boolean correct_input = hm.input_validation_hashload(args);
		if(correct_input) {
			int page_size = hm.page_size(args);
			System.out.println(page_size);
			
			final long full_end_time = System.nanoTime();
			
			// Required Outputs
			System.out.println("System - Time Taken to Execute Script: "+
			(float)(full_end_time-full_start_time)/1000000000+" seconds");
			/*
			System.out.println("System - Time Taken to Write to Heap: "+ 
			(float)(hash_write_end_time-hash_write_start_time)/1000000000+" seconds");
			*/
		}
	}
}
