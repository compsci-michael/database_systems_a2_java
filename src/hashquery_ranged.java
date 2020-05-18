///////////////////////////////////////////////////////////////////////////////
// File Written by: Michael A (s3662507) (Last Edit: 18/05/2020)
// Database Systems - Assignment 02
// Purpose of this Class:
// This is the Driver Class which contains the Main method for loading the
// java heap file and hash index file to extract the queried records in a range
// of census year
///////////////////////////////////////////////////////////////////////////////
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class hashquery_ranged {
	private static final int HASH_VALUE_INDEX = 0;
	private static final int FILE_OFFSET_INDEX = 1;
	// Must be able to execute the following: java hashload <census year> <street address> <pagesize>
	public static void main(String[] args) {
		final long full_start_time = System.nanoTime();
		// Set up Helper Methods
		HMethods hm = new HMethods();
		// Store the Page Size
		int page_size;
		// Stores the Census Year
		int census_year_from;
		int census_year_to;
		// Stores the Street Address
		String street_address;
		// Output Times if Error Didn't Occur
		boolean error_occured = false;
		
		// Step 1: Validate the Input
		boolean correct_input = hm.input_validation_hashquery_ranged(args);
		if(correct_input) {
			// Stores the Page Size
			page_size = Integer.parseInt(args[HMethods.PAGE_SIZE_ARGUEMENT_RANGED_QUERY].trim());
			// Stores the Census Year
			census_year_from = Integer.parseInt(args[HMethods.CENSUS_YEAR_FROM_ARGUEMENT_RANGED_QUERY].trim());
			census_year_to = Integer.parseInt(args[HMethods.CENSUS_YEAR_TO_ARGUEMENT_RANGED_QUERY].trim());
			// Stores the Street Address
			street_address = args[HMethods.STREET_ADDRESS_ARGUEMENT_RANGED_QUERY];
			// Load from the filename "hash.<page_size>"
			String hash_file_name = "hash."+page_size;
			// Load from the filename "heap.<page_size>"
			String heap_file_name = "heap."+page_size;
			// Sorted Set to Keep File Offsets in Ascending Order
	        SortedSet<Integer> file_offset_pointers = new TreeSet<>(); 
			
			// Create the Anticipated Hash Value
			int hash_value_from = hm.record_to_hash(street_address, census_year_from);
			int hash_value_to = hm.record_to_hash(street_address, census_year_to);
	        
	        // Special Case where Hash Function returns negative
	        // Must Swap Values
	        if(hash_value_to < hash_value_from) {
	        	int old_hash_from = hash_value_from;
	        	hash_value_from = hash_value_to;
	        	hash_value_to = old_hash_from;
	        }
	        
			//System.out.println(hash_value_from+","+hash_value_to);
			// Ensure Appropriate Shifting of Search
			int line_number_from = hash_value_from*HMethods.BUCKET_SIZE_USED;
			int line_number_to_expected = hash_value_to*HMethods.BUCKET_SIZE_USED; 
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
														
					// Go through the File and Locate the Hash Value
					while ((line = hash_file_reader.readLine()) != null) {
						// Will Seek if First Time Reading File
						if(!skip_move_ptr) {
							if(move_ptr++ < line_number_from) { 
								continue;
							}
						}
						// if we Reach an Empty String (Empty Line)
						if(!line.equals("")) {
							// Get all Strings that contain the hash_values until "" found
							String[] string_delimited = line.split(",");
							int hash_value = Integer.parseInt(string_delimited[HASH_VALUE_INDEX]);
							// if the hash value is in the range, save it's file pointer
							if(hash_value >= hash_value_from && hash_value <= hash_value_to) {
								file_offset_pointers.add(Integer.parseInt(string_delimited[FILE_OFFSET_INDEX]));
							}
						} else {
							// Ensure the Line Number Expected is Met
							if(move_ptr < line_number_to_expected) {
								continue;
							}
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
				heap_file_buffer = new BufferedInputStream(heap_file, page_size);
				heap_file_reader = new DataInputStream(heap_file_buffer);
				// This will store each Record being Read in
				byte[] read_record = new byte[HMethods.RECORD_SIZE];
				// An Offset for the Initial and Next for Moving the skipBytes
				int file_offset_initial=0, file_offset_next;
				 // Creating an Iterator for the Ordered File Offset Pointers
		        Iterator<Integer> iterator = file_offset_pointers.iterator(); 
		  
				// Go through the File and Grab Specific Locations
		        while(iterator.hasNext()) { 
		        	file_offset_next = iterator.next(); 
					// This may create issues if Threading is Used but should be fine for this application
					heap_file_reader.skipBytes(file_offset_next-file_offset_initial);
					// Read in the Record
					heap_file_reader.read(read_record);
					// Stores the Record that is Parsed
					Record queried_record = hm.hash_query_read_record(read_record); 

					// Check for the Queries Conditions
					int census_year_inbetween = census_year_from;
					while(census_year_inbetween <= census_year_to) {
						if(queried_record.matches_query(census_year_inbetween, street_address)) {
							returned_records.add(queried_record);
							break;
						}
						census_year_inbetween++;
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
				System.err.println("Error - No Record Found for the Range "+
						census_year_from+" to "+census_year_to+" and "+street_address+"!");
				error_occured = true;
			}
			final long full_end_time = System.nanoTime();
			
			// Ensure Print Statements Happen if Error Didn't Occur
			if(!error_occured) {
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
}
