///////////////////////////////////////////////////////////////////////////////
// File Written by: Michael A (s3662507) (Last Edit: 17/05/2020)
// Database Systems - Assignment 02
// Purpose of this Class:
// This is the Driver Class which contains the Main method for loading the
// java heap file and conduct a Linear Search to extract the queried records.
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
import java.util.Arrays;

public class hashquery_without {
	// Must be able to execute the following: java hashload <census year> <street address> <pagesize>
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
			// Load from the filename "heap.<page_size>"
			String heap_file_name = "heap."+page_size;
								
			// Step 2: Reading in the Input from the Heap File
			FileInputStream heap_file = null;
			BufferedInputStream heap_file_buffer = null;
			DataInputStream heap_file_reader = null;
			
			final long heap_read_start_time = System.nanoTime();
			
			// Holds all Successfully Queried Items
			List<Record> returned_records = new ArrayList<Record>();
			// Step 3: Read from the Heap File and Locate Lines
			try {
				// Setting up File Operations
				heap_file = new FileInputStream(new File(heap_file_name));
				heap_file_buffer = new BufferedInputStream(heap_file, page_size);
				heap_file_reader = new DataInputStream(heap_file_buffer);
				// This will store each Page being Read in
				byte[] read_page = new byte[page_size];
				// This will store each Record being Read in
				byte[] read_record = new byte[HMethods.RECORD_SIZE];
				// A Counter for the Buffer of read_page
				int from_c = 0;
				
				// Go through the File and grab the page_size amount of Bytes
				while (heap_file_reader.read(read_page) != -1) {
					//Page new_page = new Page(page_size);
					while((char)read_page[from_c] != '#') {
						int to_c = from_c + HMethods.RECORD_SIZE;
						// Get the Record Bytes
						read_record = Arrays.copyOfRange(read_page, from_c, to_c);
						// Create the Record
						Record queried_record = hm.hash_query_read_record(read_record);
						// Check for the Queries Conditions
						if(queried_record.matches_query(census_year, street_address)) {
							returned_records.add(queried_record);
						}
						from_c+=HMethods.RECORD_SIZE;
					}
					// Resetting the Buffer Counter ready for the next Page
					from_c = 0;
					// Reset the Buffer
					read_page = new byte[page_size];
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
			// Heap File Search, Total Execution Time
			float heap_read = (float)(heap_read_end_time-heap_read_start_time)/1000000000;
			float exe_time = (float)(full_end_time-full_start_time)/1000000000;
			System.out.println(heap_read+","+exe_time);
		}
	}
}
