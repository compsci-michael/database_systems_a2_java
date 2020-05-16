///////////////////////////////////////////////////////////////////////////////
// File Written by: Michael A (s3662507) (Last Edit: 16/05/2020)
// Database Systems - Assignment 02
// Purpose of this Class:
// This is the Driver Class which contains the Main method for loading the
// java heap file and creating a Hashfile
///////////////////////////////////////////////////////////////////////////////
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class hashquery {
	private static final int FILE_OFFSET_INDEX = 1;
	// Must be able to execute the following: java dbload -p pagesize datafile
	public static void main(String[] args) {
		final long full_start_time = System.nanoTime();
		// Set up Helper Methods
		HMethods hm = new HMethods();
		// Store the Page Size
		int page_size;
		// Stores the Census Year
		int census_year;
		// Stores the Street Address
		String street_address;
		
		// Step 1: Validate the Input
		boolean correct_input = hm.input_validation_hashquery(args);
		if(correct_input) {
			// Stores the Page Size
			page_size = hm.page_size_query(args);
			// Stores the Census Year
			census_year = hm.census_year_query(args);
			// Stores the Street Address
			street_address = hm.street_address_query(args);
			// Load from the filename "hash.<page_size>"
			String hash_file_name = "hash."+page_size;
			// Load from the filename "heap.<page_size>"
			String heap_file_name = "heap."+page_size;
			
			List<String> file_offset_pointers = new ArrayList<String>();
			
			// Create the Anticipated Hash Value
			int hash_value = hm.record_to_hash(street_address, census_year);
			// Ensure Appropriate Shifting of Search
			System.out.println(hash_value); //hash_value = 115502;
			int line_number = hash_value*HMethods.BUCKET_SIZE_USED;
			
			// Stores Read Input from File
			String line;
			// if the Pointer was near or at the end of the file and we need to 
			// Linear Search, we need to Reset the File Reader to the Start
			boolean null_reached_without_empty = true; 
			boolean skip_move_ptr = false;
			
			// Step 2: Reading in the Input from the Hash File
			FileReader hash_file = null;
			BufferedReader hash_file_reader = null;
			
			FileInputStream heap_file = null;
			BufferedInputStream heap_file_buffer = null;
			DataInputStream heap_file_reader = null;
			
			final long hash_read_start_time = System.nanoTime();

			try {
				do {
					// Setting up File Operations
					System.out.println("System - Loading Hash File "+hash_file_name);
					hash_file = new FileReader(hash_file_name);
					hash_file_reader = new BufferedReader(hash_file);
					// Stores Number of Line to Seek
					int move_ptr = 0;
									
			//System.out.println(hash_value);
					
					// Go through the File and Locate the Hash Value
					while ((line = hash_file_reader.readLine()) != null) {
						// Will Seek if First Time Reading File
						if(!skip_move_ptr) {
							if(move_ptr++ < line_number) { 
								continue;
							}
						}
						// Get all Strings that contain the hash_value until "" found
						if(line.startsWith(hash_value+",")) {
							String[] string_delimited = line.split(",");
							file_offset_pointers.add(string_delimited[FILE_OFFSET_INDEX]);
						}
						
			//System.out.println(line);
						
						// if we Reach an Empty String (Empty Line)
						if(line.equals("")) {
							// We Finished
							null_reached_without_empty = false;
							break;
						}
					}
					// Reset the Reader to the Start and Start Reading
					if(null_reached_without_empty) {
						hash_file_reader.close();
						skip_move_ptr = true;
					}
				} while(null_reached_without_empty);
			} catch(IOException e) {
				System.err.println("Error - Data Couldn't Be Extracted!");				
			}
			
			final long hash_read_end_time = System.nanoTime();
			final long heap_read_start_time = System.nanoTime();
			
			// Holds all Successfully Queried Items
			List<Record> returned_records = new ArrayList<Record>();
			// Step 3: Read from the Heap File and Locate Lines
			try {
				// Setting up File Operations
				System.out.println("System - Loading Heap File "+heap_file_name);
				heap_file = new FileInputStream(new File(heap_file_name));
				heap_file_buffer = new BufferedInputStream(heap_file,page_size);
				heap_file_reader = new DataInputStream(heap_file_buffer);
				// This will store each Record being Read in
				byte[] read_record = new byte[HMethods.RECORD_SIZE];
				// An Offset for the Initial and Next for Moving the skipBytes
				int file_offset_initial=0, file_offset_next;
				
				// Go through the File and Grab Specific Locations
				for(int i=0; i<file_offset_pointers.size(); i++) {
					file_offset_next = Integer.parseInt(file_offset_pointers.get(i));
					// This may create issues if Threading is Used but should be fine for this application
					heap_file_reader.skipBytes(file_offset_next-file_offset_initial);
					// Read in the Record
					heap_file_reader.read(read_record);
					// Stores the Record that is Parsed
					Record queried_record = hm.hash_query_read_record(read_record); 

					// Check for the Queries Conditions
					if(queried_record.matches_query(census_year, street_address)) {
						returned_records.add(queried_record);
					}
						
					// Ensure skipBytes skips Accurately to the Next Record
					file_offset_initial = file_offset_next+HMethods.RECORD_SIZE;
				}
			} 
			// Catching IOException error
			catch (IOException e) {
				System.err.println("Error - Data Couldn't Be Extracted!");				
			}
			
			final long heap_read_end_time = System.nanoTime();
		
			// Step 4: Output Queried Records, if None was Found, Display Error Message
			if(returned_records.size() > 0) {
				System.out.println("\n|----------------------------- Records -----------------------------|");
				for(int i=0; i<returned_records.size(); i++) {
					returned_records.get(i).record_display();
				}
				System.out.println("|-------------------------- End of Records -------------------------|\n\n");
			} else {
				System.err.println("Error - No Record Found for "+census_year+" and "+street_address+"!");
			}
			final long full_end_time = System.nanoTime();
			
			// Required Outputs				
			System.out.println("System - Time Taken to Read and Locate from Hash File: "+
					(float)(hash_read_end_time-hash_read_start_time)/1000000000+" seconds");
			System.out.println("System - Time Taken to Read and Extract from Heap File: "+
					(float)(heap_read_end_time-heap_read_start_time)/1000000000+" seconds");
			System.out.println("System - Time Taken to Execute Script: "+
					(float)(full_end_time-full_start_time)/1000000000+" seconds");
		}
	}
}
