import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.sql.DriverManager;
import java.sql.SQLException;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.ResultSetMetaData;
import com.mysql.jdbc.Statement;
import com.mysql.jdbc.ResultSetMetaData;
import java.time.*;


public class Hospital {

	public static void displaySchedule(PriorityQueue<Patient> pq) {
		System.out.println("patients in the following ward are-");
		while (!pq.isEmpty()) {
			pq.poll().showLessDetails();
		}
		System.out.println("-----------END----------");
	}

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		
		
		LocalDate todayDate = LocalDate.now();
		// Identify and establish the driver.
		Class.forName("com.mysql.jdbc.Driver");

		// Create a connection to our required database. Here pfs is database.
		java.sql.Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hospital", "root", "gvarun99");

		System.out.println("Connected");
		
		Statement stmt1=(Statement) con.createStatement();
		ResultSet rs2=(ResultSet) stmt1.executeQuery("TRUNCATE table hospital");//For query execution
				
		

		Scanner sc = new Scanner(System.in);

		HashMap<String, Integer> Ortho1 = new HashMap<>();
		Ortho1.put("OP", 60); // Operation
		Ortho1.put("FR", 50); // Fracture
		Ortho1.put("LT", 40); // Ligament tear
		Ortho1.put("SP", 30); // Sprain
		Ortho1.put("SW", 20); // Swelling
		Ortho1.put("PA", 10); // Pain

		HashMap<String, Integer> Dental2 = new HashMap<>();
		Dental2.put("RC", 40); // Root Canal
		Dental2.put("CT", 30); // Cavity Treatment
		Dental2.put("BR", 20); // Braces
		Dental2.put("GC", 10); // General Checkup

		HashMap<String, Integer> COVID3 = new HashMap<>();
		COVID3.put("SARS", 20);
		COVID3.put("MERS", 10);

		PriorityQueue<Patient> Ortho = new PriorityQueue<Patient>(new PatientComparator());
		PriorityQueue<Patient> Dental = new PriorityQueue<Patient>(new PatientComparator());
		PriorityQueue<Patient> Idk = new PriorityQueue<Patient>();
		PriorityQueue<Patient> Covid = new PriorityQueue<Patient>(new CovidPatientComparator());

		Patient patientArr[] = new Patient[100];
		int patientCount = 0;

		String ans;
		int flag = 0;

		while (flag == 0) {

			System.out.println("-----------------Menu-------------------");
			System.out.println("1.Add Patient");
			System.out.println("2.Check Patient Queue");
			System.out.println("3.Exit");

			int menu = sc.nextInt();

			switch (menu) {

			case 1: {
				patientArr[patientCount] = new Patient();

				System.out.println("Enter Integer\n1. Ortho \n2. Dental \n3. Covid Test \n4. General Ward");
				int choice = sc.nextInt();
				sc.nextLine();
				switch (choice) {

				case 1:// Ortho
					patientArr[patientCount].getDetails();
					System.out.println(
							" Enter String \n1. Operation (OP) \n2. Fracture (FR) \n3. Ligament tear (LT) \n4. Sprain (SP) \n5. Swelling (SW) \n6. Pain (PA) ");
					patientArr[patientCount].disease = sc.nextLine();
					patientArr[patientCount].priority = Ortho1.get(patientArr[patientCount].disease);
					Ortho.add(patientArr[patientCount]);
					patientArr[patientCount].showDetails();
					
					PreparedStatement pstmt1 = (PreparedStatement) con.prepareStatement("insert into hospital values(?,?,?,?)");

					pstmt1.setInt(1,patientArr[patientCount].id);
					pstmt1.setString(2,patientArr[patientCount].name);
					pstmt1.setInt(3,patientArr[patientCount].age);
					pstmt1.setString(4,patientArr[patientCount].disease);
					pstmt1.executeUpdate();  
					break;

				case 2:// Dental
					patientArr[patientCount].getDetails();
					System.out.println(
							"Enter String\n1. Root Canal (RC) \n2. Cavitiy Treatment (CT) \n3. Braces (BR) \n4. General Checkup (GC)");
					patientArr[patientCount].disease = sc.nextLine();
					patientArr[patientCount].priority = Dental2.get(patientArr[patientCount].disease);
					Dental.add(patientArr[patientCount]);
					patientArr[patientCount].showDetails();
					
					PreparedStatement pstmt2 = (PreparedStatement) con.prepareStatement("insert into hospital values(?,?,?,?)");

					pstmt2.setInt(1,patientArr[patientCount].id);
					pstmt2.setString(2,patientArr[patientCount].name);
					pstmt2.setInt(3,patientArr[patientCount].age);
					pstmt2.setString(4,patientArr[patientCount].disease);
					pstmt2.executeUpdate();  
					break;

				case 3:// Covid Test
					patientArr[patientCount].getDetails();
					int s = 0, m = 0;
					System.out.println("Please answer following questions for determining the type of Covid");
					System.out.println("\n1.Do you face breathing problems:(Y/N)?");
					String ns1 = sc.nextLine();

					if (ns1.equalsIgnoreCase("Y"))
						m++; // Breath - MERS
					

					System.out.println("\n1.Do you have muscle pain:(Y/N)?");
					ns1 = sc.nextLine();
					if (ns1.equalsIgnoreCase("Y"))
						s++; // Muscle - SERS
					

					System.out.println("\n1.Do you have chest pain:(Y/N)?");
					ns1 = sc.nextLine();
					if (ns1.equalsIgnoreCase("Y"))
						m++; // Chest - MERS
					

					System.out.println("\n1.Do you have headache:(Y/N)?");
					ns1 = sc.nextLine();
					if (ns1.equalsIgnoreCase("Y"))
						s++; // Headache - SERS
					

					System.out.println("\n1.Are you suffering from Diarrhoea:(Y/N)?");
					ns1 = sc.nextLine();
					if (ns1.equalsIgnoreCase("Y"))
						m++; // Diarrhea - MERS
					
					
					if(s==0 && m==0)
					{
						System.out.println("Congratulations! You are a tested NEGATIVE for COVID.");
						patientCount--;
						break;
					}
					else if (s >= m) {
						patientArr[patientCount].disease = "SARS";
						patientArr[patientCount].priority = COVID3.get(patientArr[patientCount].disease);
					} else {
						patientArr[patientCount].disease = "MERS";
						patientArr[patientCount].priority = COVID3.get(patientArr[patientCount].disease);
					}

					Covid.add(patientArr[patientCount]);
					patientArr[patientCount].showDetails();
					
					PreparedStatement pstmt3 = (PreparedStatement) con.prepareStatement("insert into hospital values(?,?,?,?)");

					pstmt3.setInt(1,patientArr[patientCount].id);
					pstmt3.setString(2,patientArr[patientCount].name);
					pstmt3.setInt(3,patientArr[patientCount].age);
					pstmt3.setString(4,patientArr[patientCount].disease);
					pstmt3.executeUpdate();  
					break;

				case 4:
					patientArr[patientCount].getIdkDetails();
					Idk.add(patientArr[patientCount]);
					System.out.println("You are successfully enrolled in General Ward.");
					patientArr[patientCount].showIdkDetails();
					
					PreparedStatement pstmt4 = (PreparedStatement) con.prepareStatement("insert into hospital values(?,?,?,?)");

					pstmt4.setInt(1,patientArr[patientCount].id);
					pstmt4.setString(2,patientArr[patientCount].name);
					pstmt4.setInt(3,patientArr[patientCount].age);
					pstmt4.setString(4,patientArr[patientCount].disease);
					pstmt4.executeUpdate();  
					break;

				default:
					break;
				}
			}
				break;

			case 2: {
				System.out.println("1.Display Ortho \n2.Display Cardio \n3.Display COVID \n4.Display General Ward \n5.Display Global Patient List "+todayDate);
				int diseasechoice = sc.nextInt();
				sc.nextLine();

				switch (diseasechoice) {

				case 1:
					displaySchedule(Ortho);
					break;

				case 2:
					displaySchedule(Dental);
					break;

				case 3:
					displaySchedule(Covid);
					break;

				case 4:
					displaySchedule(Idk);
					break;
					
				case 5: 
					Statement stmt=(Statement) con.createStatement();
					ResultSet rs=(ResultSet) stmt.executeQuery("select * from hospital");
					ResultSetMetaData rs1=(ResultSetMetaData) rs.getMetaData();
					System.out.print(rs1.getColumnName(1)+"  ");
					System.out.print(rs1.getColumnName(2)+"  ");
					System.out.print(rs1.getColumnName(3)+" ");
					System.out.print(rs1.getColumnName(4)+" ");
					System.out.println();
					while(rs.next()){
					System.out.println(rs.getInt(1)+"  "+rs.getString(2)+"   "+rs.getInt(3)+"  "+rs.getString(4));
					}
					System.out.println();
					break;

				default:
					break;
				}
			}
				break;

			case 3:
				flag = 1;
				break;

			default:
				break;
			}

		}

	}

}

class PatientComparator implements Comparator<Patient> {
	public int compare(Patient s1, Patient s2) {
		if (s1.priority < s2.priority)
			return 1;
		else if (s1.priority > s2.priority)
			return -1;
		return 0;
	}
}

class CovidPatientComparator implements Comparator<Patient> {
	public int compare(Patient s1, Patient s2) {
		if (s1.age < s2.age) {
			if (s1.priority < s2.priority)
				return 2;
			else
				return 1;
		}

		else if (s1.age > s2.age) {
			if (s1.priority < s2.priority)
				return -1;
			else
				return -2;
		}

		return 0;
	}
}

class Patient {

	public String name;
	public int age;
	public int priority = 0;
	static int id = 100;
	String disease;

	public void getDetails() {
		Scanner sc = new Scanner(System.in);

		System.out.println("Enter Patient name");
		String pname = sc.nextLine();
		this.name = pname;

		System.out.println("Enter Patient age");
		int page = sc.nextInt();
		this.age = page;

	}

	public void showDetails() {

		System.out.println("--------------------");
		System.out.println(" PATIENT DETAILS ");
		System.out.println("--------------------");
		System.out.println("Patient id: " + id);
		System.out.println();
		System.out.println("Name: " + this.name);
		System.out.println();
		System.out.println("Age: " + this.age);
		System.out.println();
		System.out.println("Disease: " + this.disease);
		System.out.println("--------------------");
		System.out.println("--------------------");
		id++;
	}

	public void showLessDetails() {
		System.out.println("-----------------");
		System.out.println("Name: " + this.name);
		System.out.println("Disease: " + this.disease);
		System.out.println("-----------------");

	}

	public void getIdkDetails() {
		Scanner sc = new Scanner(System.in);

		System.out.println("Enter Patient name");
		String pname = sc.nextLine();
		this.name = pname;

		System.out.println("Enter Patient age");
		int page = sc.nextInt();
		this.age = page;

		this.priority = 0;
		this.disease = "Yet to be confirmed";

	}

	public void showIdkDetails() {
		System.out.println("-----------------");
		System.out.println("Name: " + this.name);
		System.out.println("Age: " + this.age);
		System.out.println("Disease: Yet to be confirmed!");
		System.out.println("-----------------");

	}

	public String getName() {
		return name;
	}
}