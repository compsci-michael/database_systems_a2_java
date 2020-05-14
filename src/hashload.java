import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

///////////////////////////////////////////////////////////////////////////////
// File Written by: Michael A (s3662507) (Last Edit: 13/05/2020)
// Database Systems - Assignment 02
// Purpose of this Class:
// This is the Driver Class which contains the Main method for loading the
// java heap file and creating a Hashfile
///////////////////////////////////////////////////////////////////////////////
public class hashload {
	// -------------------------- Final Constants -------------------------- //
	private static final int INT_SIZE = 4;
	private static final int DOUBLE_SIZE = 8;
	private static final int BUILDING_NAME_SIZE = 63;
	private static final int STREET_ADDRESS_SIZE = 34;
	private static final int SUBURB_SIZE = 28;
	private static final int SPACE_USAGE_SIZE = 39;
	private static final int ACCESS_TYPE_SIZE = 32;
	private static final int ACCESS_DESC_SIZE = 81;
	private static final int LOCATION_SIZE = 27;
	
	private static final int RECORD_SIZE = 
			10*INT_SIZE+2*DOUBLE_SIZE
			+BUILDING_NAME_SIZE
			+STREET_ADDRESS_SIZE
			+SUBURB_SIZE
			+SPACE_USAGE_SIZE
			+ACCESS_TYPE_SIZE
			+ACCESS_DESC_SIZE
			+LOCATION_SIZE;
	
	// Must be able to execute the following: java dbload -p pagesize datafile
	public static void main(String[] args) {
		final long full_start_time = System.nanoTime();
		// Set up Helper Methods
		HMethods hm = new HMethods();
		// Store the Page Size
		int page_size;

		// Step 1: Validate the Input
		boolean correct_input = hm.input_validation_hashload(args);
		if(correct_input) {
			page_size = hm.page_size(args);
			String heap_file_name = hm.heap_file(args);
			
			// Step 2: Reading in the Input from the File
			FileInputStream heap_file = null;
			BufferedInputStream heap_file_buffer = null;
			DataInputStream heap_file_reader = null;
			
			try {
				// Setting up File Operations
				System.out.println("System - Loading Heap File "+heap_file_name);
				heap_file = new FileInputStream(new File(heap_file_name));
				heap_file_buffer = new BufferedInputStream(heap_file,page_size);
				heap_file_reader = new DataInputStream(heap_file_buffer);
				// This will store each Page being Read in
				byte[] read_page = new byte[page_size];
				// A Counter for the Buffer of r_page
				int byte_count = 0;
				// A Page Counter
				int page_number = 0;
				// A Total File Offset Counter
				int total_file_offset = 0;
				
				// Recording the Start Time
				final long hash_calc_start_time = System.nanoTime();

				// Go through the File and grab the page_size amount of Bytes
				while (heap_file_reader.read(read_page) != -1) {
					int ptr_move_to_prop_id = (2*INT_SIZE);
					int ptr_move_to_street_address = (2*INT_SIZE)+BUILDING_NAME_SIZE;
					while((char)read_page[byte_count] != '#') {
						// Get the Census Year
						int census_year = ByteBuffer.wrap(read_page, byte_count, INT_SIZE).getInt();
						
						// Move the Pointer to the Property ID
						byte_count+=ptr_move_to_prop_id;
						
						// Get the Property ID
						int prop_id = ByteBuffer.wrap(read_page, byte_count, INT_SIZE).getInt();
						
						// Move the Pointer to the Street Address
						byte_count+=(2*INT_SIZE)+BUILDING_NAME_SIZE;
						
						// Read the Street Address into a Variable
						String street_address = hm.byte_buffer_to_string(read_page, byte_count, STREET_ADDRESS_SIZE);
						
						// Move the Pointer to the End of the Record
						byte_count+=(RECORD_SIZE-ptr_move_to_street_address);
						
						//System.out.println(street_address.toLowerCase().hashCode());
						System.out.println(census_year + " " + prop_id + " " + street_address + " ");
								
						// Keeps Track of Page-Record File Offset
						total_file_offset+=RECORD_SIZE;
					}
					
					// Reset the Buffer
					read_page = new byte[page_size];
					// Reset the Buffer Counter ready for the next Page
					byte_count = 0;
					// Increment Page Counter
					page_number++;
					// Keeps Track of Page File Offset
					total_file_offset = page_number*page_size;
					//if(page_number == 1) break;
					
				}
				final long hash_calc_end_time = System.nanoTime();

				final long full_end_time = System.nanoTime();
				
				// Required Outputs
				System.out.println("System - Time Taken to Execute Script: "+
				(float)(full_end_time-full_start_time)/1000000000+" seconds");
				
				System.out.println("System - Time Taken to Read the Heap File and Calculate For Hash File: "+ 
				(float)(hash_calc_end_time-hash_calc_start_time)/1000000000+" seconds");
				
				/*
				System.out.println("System - Time Taken to Write a Hash File: "+ 
				(float)(hash_write_end_time-hash_write_start_time)/1000000000+" seconds");
				*/
			} 
			// Catching IOException error
			catch (IOException e) {
				System.err.println("Error - Data Couldn't Be Extracted!");				
			} 	
		}
	}
}
