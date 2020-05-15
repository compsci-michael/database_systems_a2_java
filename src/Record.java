///////////////////////////////////////////////////////////////////////////////
// File Written by: Michael A (s3662507) (Last Edit: 15/05/2020)
// Database Systems - Assignment 02
// Purpose of this Class:
// This class was created to store all the Record related data
// This class is used in conjuction with the Slot and Page classes to create a 
// heap
///////////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////////////
// | 0: census_yr    (int)  | 1: block_id       (int)| 2: prop_id        (int)|
// | 3: base_prop_id (int)  | 4: building_name  (V63)| 5: street_address (V34)|
// | 6: suburb       (V28)  | 7: construct_yr   (int)| 8: refurbished_year (int)
// | 9: num_floors   (int)  | 10: space_usage   (V39)| 11: access_type   (V32)|
// | 12: access_desc (V81)  | 13: access_rating (int)| 14: bicycle_spaces (int)
// | 15: has_showers (int)  | 16: x_coor     (double)| 17: y_coor     (double)|
// | 18:location     (V27)  |                        |                        |
///////////////////////////////////////////////////////////////////////////////

public class Record {
	// ------------------------------ Members ------------------------------ //
		// ------------ Attributes (Fields in the Flat File) ----------- //
			// ------------------- Integer Fields ------------------ //	   
	private int census_yr, block_id, prop_id, base_prop_id; // 0-3
	private int construct_yr, refurbished_yr, num_floors; 	// 7-9 
	private int access_rating, bicycle_spaces, has_showers; // 13-15
	
			// ------------------- String Fields ------------------- //
	private String building_name, street_address, suburb;   // 4-6
	private String space_usage, access_type, access_desc; 	// 10-12
	private String location;								// 18 
	
			// ------------------- Double Fields ------------------- //
	private double x_coor, y_coor; 							// 16-17
	
	// ------------------------ Default Constructor ------------------------ //
	public Record() {}

	// ----------------------------- Mutators ------------------------------ //
	public void set_census_yr(int census_yr) {
		this.census_yr = census_yr;
	}
	public void set_block_id(int block_id) {
		this.block_id = block_id;
	}
	public void set_prop_id(int prop_id) {
		this.prop_id = prop_id;
	}
	public void set_base_prop_id(int base_prop_id) {
		this.base_prop_id = base_prop_id;
	}
	public void set_building_name(String building_name) {
		this.building_name = building_name;
	}
	public void set_street_address(String street_address) {
		this.street_address = street_address;
	}
	public void set_suburb(String suburb) {
		this.suburb = suburb;
	}
	public void set_construct_yr(int construct_yr) {
		this.construct_yr = construct_yr;
	}
	public void set_refurbished_yr(int refurbished_yr) {
		this.refurbished_yr = refurbished_yr;
	}
	public void set_num_floors(int num_floors) {
		this.num_floors = num_floors;
	}
	public void set_space_usage(String space_usage) {
		this.space_usage = space_usage;
	}
	public void set_access_type(String access_type) {
		this.access_type = access_type;
	}
	public void set_access_desc(String access_desc) {
		this.access_desc = access_desc;
	}
	public void set_access_rating(int access_rating) {
		this.access_rating = access_rating;
	}
	public void set_bicycle_spaces(int bicycle_spaces) {
		this.bicycle_spaces = bicycle_spaces;
	}
	public void set_has_showers(int has_showers) {
		this.has_showers = has_showers;
	}
	public void set_x_coor(double x_coor) {
		this.x_coor = x_coor;
	}
	public void set_y_coor(double y_coor) {
		this.y_coor = y_coor;
	}
	public void set_location(String location) {
		this.location = location;
	}
	
	// ------------------------ Additional Methods ------------------------- //

	// This Method displays all the details of the Record for Debugging Purposes
	public void record_display() {
		System.out.println("Census Year: "+this.census_yr);
		System.out.println("Block ID: "+this.block_id);
		System.out.println("Property ID: "+this.prop_id);
		System.out.println("Base Property ID: "+this.base_prop_id);
		System.out.println("Building Name: "+this.building_name);
		System.out.println("Street Address: "+this.street_address);
		System.out.println("Suburb: "+this.suburb);
		System.out.println("Construction Year: "+this.construct_yr);
		System.out.println("Refurbished Year: "+this.refurbished_yr);
		System.out.println("Number of Floors: "+this.num_floors);
		System.out.println("Space Usage: "+this.space_usage);
		System.out.println("Access Type: "+this.access_type);
		System.out.println("Access Description: "+this.access_desc);
		System.out.println("Access Rating: "+this.access_rating);
		System.out.println("Bicycle Spaces: "+this.bicycle_spaces);
		System.out.println("Has Showers: "+this.has_showers);
		System.out.println("X Coordinate: "+this.x_coor);
		System.out.println("Y Coordinate: "+this.y_coor);
		System.out.println("Location: "+this.location);	
		System.out.println();
	}
	
	// This Method displays unique information of the Record for Debugging
	public void record_display_simple() {
		System.out.print("Census Year: "+this.census_yr+", ");
		System.out.print("Block ID: "+this.block_id+", ");
		System.out.print("Property ID: "+this.prop_id+", ");
		System.out.print("Base Property ID: "+this.base_prop_id+", ");
		System.out.print("Building Name: "+this.building_name+", ");
		System.out.print("Street Address: "+this.street_address+", ");
		System.out.print("Suburb: "+this.suburb+", ");
		System.out.print("Construction Year: "+this.construct_yr+", ");
		System.out.println();
	}	
	
	// Method for Searching for a String in the Record
	public boolean contains(String sc) {
		if(sc.equals("#")) {
			sc = "###############################################################";
		}
		if(new String(this.building_name).toLowerCase().contains(sc.toLowerCase())) {
			return true;
		}
		return false;
	}
}
