import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

///////////////////////////////////////////////////////////////////////////////
// File Written by: Michael A (s3662507) (Last Edit: 15/05/2020)
// Database Systems - Assignment 02
// Purpose of this Class:
// This is the Driver Class which contains the Main method for loading the
// java heap file and creating a Hashfile
///////////////////////////////////////////////////////////////////////////////
public class hashload {
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
			// Stores the Page Size
			page_size = hm.page_size_load(args);
			// Stores the filename "heap.<page_size>"
			String heap_file_name = "heap."+page_size;
			// if the User Requires Statistics to be Exported
			boolean requires_statistics = hm.does_require_statistics(args);
			// --------------------- Statistics Variables --------------------- //
			int number_of_pages_read = 0;
			int number_of_records_read = 0;
			int num_of_duplicate_keys = 0;
			int count_of_duplicate_keys = 0;
			int num_of_initial_unique_keys = 0;
			int counted_num_of_collisions = 0;
			// Table and Bucket Size Retrievable from HMethods.java
			// Used for Duplicate Keys and Number of Initial Collisions
			Map<Integer, Integer> duplicate_keys = new TreeMap<Integer, Integer>();
			// Used for Initial Unique and Initial Available Keys
	        SortedSet<Integer> initial_unique_keys = new TreeSet<>(); 
			// Used to hold the Actual Hash File Data
	        Map<Integer, List<String>> hash_file_data = new TreeMap<Integer, List<String>>();
			
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
				// A Total File Offset Counter
				int total_file_offset = 0;
				
				// Recording the Start Time
				final long hash_calc_start_time = System.nanoTime();

				// Go through the File and grab the page_size amount of Bytes
				while (heap_file_reader.read(read_page) != -1) {
					while((char)read_page[byte_count] != '#') {
						int vacant_spot_ptr = 0;

						// Get the Census Year
						int census_year = ByteBuffer.wrap(read_page, byte_count, HMethods.INT_SIZE).getInt();

						// Move the Pointer to the Street Address
						byte_count+=HMethods.PTR_TO_STREET_ADDRESS;
						
						// Read the Street Address into a Variable
						String street_address = hm.byte_buffer_to_string(read_page, byte_count, HMethods.STREET_ADDRESS_SIZE);
						
						// Move the Pointer to the End of the Record
						byte_count+=(HMethods.RECORD_SIZE-HMethods.PTR_TO_STREET_ADDRESS);
						
						// Get the Hash Value of the Record
						int hash_value = hm.record_to_hash(street_address, census_year);
						
						// ------------- Statistics Gathering ------------- // 
						if(requires_statistics) {
							if(duplicate_keys.containsKey(hash_value)) {
								// if the Value already Exists, Increment its Count
								int new_value = duplicate_keys.get(hash_value)+1;
								duplicate_keys.replace(hash_value, new_value);
							} else {
								// if the Key is Not Unique, Add it to Duplicates
								if(initial_unique_keys.contains(hash_value)) {
									duplicate_keys.put(hash_value, 2);
								} else {
									// Else Add it to the Unique Keys
									initial_unique_keys.add(hash_value);
								}
							}		
						}
						
						// Store the Heap File Data
						// Step 2.1: Check if Key is being Used
						List<String> hash_and_file_offset_pointers = new ArrayList<String>();

				        if(hash_file_data.containsKey(hash_value)) {
							// Step 2.2: Check if the Number of Stored Pointers is equal to BUCKET_SIZE_USED
							if(hash_file_data.get(hash_value).size() == HMethods.BUCKET_SIZE_USED) {
								counted_num_of_collisions++;
								// Find Vacant Spot using Linear Probing
								int hash_value_ptr = hash_value+1;
								boolean spot_found = false;
								
								while(!spot_found) {
									//System.out.println(hash_file_data.size());
									// Step 2.2.1: Constrain New hash_value_ptr % HASH_TABLE_SIZE;
									hash_value_ptr = (hash_value_ptr == HMethods.HASH_TABLE_SIZE-1) ? 0 : hash_value_ptr;
									// Step 2.2.2: Check if hash_value_ptr is equal to hash_value
									if(hash_value_ptr == hash_value) {
										System.err.println("Error - No More Space in Hash File");
										System.err.println("Number of Probing Done: "+counted_num_of_collisions);
										System.exit(0);
									}
									// Step 2.3: Check Next Adjacent Hash Key
									if(hash_file_data.containsKey(hash_value_ptr)) {
										if(hash_file_data.get(hash_value_ptr).size() < HMethods.BUCKET_SIZE_USED) {
											vacant_spot_ptr = hash_value_ptr;
											break;
										} else {
											// Check Next
											hash_value_ptr++;
											counted_num_of_collisions++;
										}
									} else {
										vacant_spot_ptr = hash_value_ptr;
										break;
									}
								}
							} else {
								// if it's not equal to BUCKET_SIZE_USED its less than it
								vacant_spot_ptr = hash_value;
							}
						} else {
							vacant_spot_ptr = hash_value;
						}
						
						// if the Bucket contains data, save and add
						if(hash_file_data.containsKey(vacant_spot_ptr)) {
							hash_and_file_offset_pointers = hash_file_data.get(vacant_spot_ptr);
						}
						// Add the new File Offset with Hash Value
						hash_and_file_offset_pointers.add(hash_value+","+total_file_offset);
						
						// Save Value and Move
				        hash_file_data.put(vacant_spot_ptr, hash_and_file_offset_pointers);
				        
						// Keeps Track of Page-Record File Offset
						total_file_offset+=HMethods.RECORD_SIZE;
						number_of_records_read++;
					}
					
					// Reset the Buffer
					read_page = new byte[page_size];
					// Reset the Buffer Counter ready for the next Page
					byte_count = 0;
					// Increment Page Counter
					number_of_pages_read++;
					// Keeps Track of Page File Offset
					total_file_offset = number_of_pages_read*page_size;		
				}
				
				// ------------------ Statistics Cleanup ------------------ // 
				if(requires_statistics) {
					// Writes the Unique and Available Initial Keys
					num_of_initial_unique_keys = hm.write_initial_unique_and_available_set(initial_unique_keys);
					// Writes to Duplicate Key File and Gets Number of Duplicate Keys
					num_of_duplicate_keys = hm.write_duplicate_map(duplicate_keys); 
					count_of_duplicate_keys = hm.count_total_duplicate_keys(duplicate_keys);
					
					// Clear Memory
					initial_unique_keys.clear();
					duplicate_keys.clear();
				}
				
				final long hash_calc_end_time = System.nanoTime();
				final long full_end_time = System.nanoTime();
				
				// Required Outputs				
				System.out.println("System - Time Taken to Read the Heap File and Calculate For Hash File: "+ 
				(float)(hash_calc_end_time-hash_calc_start_time)/1000000000+" seconds");
				
				// Write the Hash File
				hm.write_hash_file(hash_file_data, page_size);

				System.out.println("System - Time Taken to Execute Script: "+
				(float)(full_end_time-full_start_time)/1000000000+" seconds");

				// ----------------- Statistics Reporting ------------------ // 
				if(requires_statistics) {
					int num_of_initial_available_keys = HMethods.HASH_TABLE_SIZE-num_of_initial_unique_keys;
					float occupancy = (float) (((float)hash_file_data.size()/(float)HMethods.HASH_TABLE_SIZE)*100.00);
					System.out.println("|----------------------------- Read ------------------------------|");
					System.out.printf("|%-35s%-30s|\n", "Number of Pages Read:", number_of_pages_read);
					System.out.printf("|%-35s%-30s|\n", "Number of Records Read:", number_of_records_read);
					System.out.println("|--------------------- Hash Table Properties ---------------------|");
					System.out.printf("|%-35s%-30s|\n", "Table Size Used:", HMethods.HASH_TABLE_SIZE);
					System.out.printf("|%-35s%-30s|\n", "Bucket Size Used:", HMethods.BUCKET_SIZE_USED);
					System.out.println("|----------------- Initial Hash Table Attributes -----------------|");
					System.out.printf("|%-35s%-30s|\n", "Count of Duplicate Keys (-Bucket):", num_of_duplicate_keys);
					System.out.printf("|%-35s%-30s|\n", "Total Count of Duplicates:", count_of_duplicate_keys);
					System.out.printf("|%-35s%-30s|\n", "Count of Initial Unique Keys:", num_of_initial_unique_keys);
					System.out.printf("|%-35s%-30s|\n", "Count of Initial Available Keys:", num_of_initial_available_keys);
					System.out.println("|--------------------------- Hash Table --------------------------|");	
					System.out.printf("|%-35s%-30s|\n", "Number of Keys Used:", hash_file_data.size());
					System.out.printf("|%-35s%-30s|\n", "Count of Keys Linear Probing Used:", hash_file_data.size()-num_of_initial_unique_keys);
					
					System.out.printf("|%-35s%-30s|\n", "Number of Available Keys:", HMethods.HASH_TABLE_SIZE-hash_file_data.size());
					System.out.printf("|%-35s%-30s|\n", "Number of Linear Probing Done:", counted_num_of_collisions);
					System.out.printf("|%-35s%05.2f%%                        |\n", "Occupancy:", occupancy);
					System.out.println("|-----------------------------------------------------------------|");	

				}
			} 
			// Catching IOException error
			catch (IOException e) {
				System.err.println("Error - Data Couldn't Be Extracted!");				
			} 	
		}
	}
}
