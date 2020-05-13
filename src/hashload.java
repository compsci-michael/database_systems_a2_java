import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;

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
				int page_number = 1;
				// Recording the Start Time
				final long hash_calc_start_time = System.nanoTime();

				// Go through the File and grab the page_size amount of Bytes
				while (heap_file_reader.read(read_page) != -1) {
					while((char)read_page[byte_count] != '#') {
						// Make a New Record which is being Read in
						Record new_record = new Record();
						
						// Go Through the r_page, block chunks at a time and set the Record members
						new_record.set_census_yr(ByteBuffer.wrap(read_page,byte_count,INT_SIZE).getInt());
						byte_count+=INT_SIZE;
						new_record.set_block_id(ByteBuffer.wrap(read_page,byte_count,INT_SIZE).getInt());
						byte_count+=INT_SIZE;
						new_record.set_prop_id(ByteBuffer.wrap(read_page,byte_count,INT_SIZE).getInt());
						byte_count+=INT_SIZE;
						new_record.set_base_prop_id(ByteBuffer.wrap(read_page,byte_count,INT_SIZE).getInt());
						byte_count+=INT_SIZE;
						new_record.set_building_name(hm.byte_buffer_to_string(read_page, byte_count, BUILDING_NAME_SIZE));
						byte_count+=BUILDING_NAME_SIZE;
						new_record.set_street_address(hm.byte_buffer_to_string(read_page, byte_count, STREET_ADDRESS_SIZE));
						byte_count+=STREET_ADDRESS_SIZE;
						new_record.set_suburb(hm.byte_buffer_to_string(read_page, byte_count, SUBURB_SIZE));
						byte_count+=SUBURB_SIZE;
						new_record.set_construct_yr(ByteBuffer.wrap(read_page,byte_count,INT_SIZE).getInt());
						byte_count+=INT_SIZE;
						new_record.set_refurbished_yr(ByteBuffer.wrap(read_page,byte_count,INT_SIZE).getInt());
						byte_count+=INT_SIZE;
						new_record.set_num_floors(ByteBuffer.wrap(read_page,byte_count,INT_SIZE).getInt());
						byte_count+=INT_SIZE;
						new_record.set_space_usage(hm.byte_buffer_to_string(read_page, byte_count, SPACE_USAGE_SIZE));
						byte_count+=SPACE_USAGE_SIZE;
						new_record.set_access_type(hm.byte_buffer_to_string(read_page, byte_count, ACCESS_TYPE_SIZE));
						byte_count+=ACCESS_TYPE_SIZE;
						new_record.set_access_desc(hm.byte_buffer_to_string(read_page, byte_count, ACCESS_DESC_SIZE));
						byte_count+=ACCESS_DESC_SIZE;
						new_record.set_access_rating(ByteBuffer.wrap(read_page,byte_count,INT_SIZE).getInt());
						byte_count+=INT_SIZE;
						new_record.set_bicycle_spaces(ByteBuffer.wrap(read_page,byte_count,INT_SIZE).getInt());
						byte_count+=INT_SIZE;
						new_record.set_has_showers(ByteBuffer.wrap(read_page,byte_count,INT_SIZE).getInt());
						byte_count+=INT_SIZE;
						new_record.set_x_coor(ByteBuffer.wrap(read_page,byte_count,DOUBLE_SIZE).getDouble());
						byte_count+=DOUBLE_SIZE;
						new_record.set_y_coor(ByteBuffer.wrap(read_page,byte_count,DOUBLE_SIZE).getDouble());
						byte_count+=DOUBLE_SIZE;
						new_record.set_location(hm.byte_buffer_to_string(read_page, byte_count, LOCATION_SIZE));
						byte_count+=LOCATION_SIZE;
						
						// Test Record Read
						new_record.record_display_simple();
					}
					
					// Reset the Buffer
					read_page = new byte[page_size];
					// Reset the Buffer Counter ready for the next Page
					byte_count = 0;
					// Increment Page Counter
					page_number++;
					if(page_number == 10) break;
					
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
