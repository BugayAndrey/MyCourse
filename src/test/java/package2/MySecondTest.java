package package2;
import org.testng.annotations.*;
import java.nio.file.Path;
import java.nio.file.Files;
import java.io.IOException;
import java.io.File;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;

public class MySecondTest {
	Path Dir = null; //temp directory adress
	File file = null; 
	boolean created = false; // true - if file was created 
	
	@BeforeClass ( groups = {"positive"})
	public void BeforeClass() throws IOException {
		String tmp_dir_prefix = "Name_of_temp_directory";
		// set a prefix
		Dir = Files.createTempDirectory(tmp_dir_prefix);
		System.out.println(Dir.toString());  
	}

	@AfterClass ( groups = {"positive"})
	public void AfterClass() throws IOException {		
		Files.deleteIfExists(Dir);		//remove temp directory
	}

	@AfterMethod ( groups = {"positive"})
	public void RemoveFile() {
		if (created) {		// if file was created
			file.delete();  // than delete it
			}
		} 	

	@Test ( groups = {"positive"})
	public void FirstTest() throws IOException {
		created = false;
		file = new File(Dir.toString() + '\\' + "test.txt");
		created = file.createNewFile();
		assertThat("File wasn't created",created,is(true));
	}

	@Test ( groups = {"positive"})
	public void SecondTest() throws IOException {
		created =  false;

		file = new File(Dir.toString() + '\\' + "123123.123");
		created = file.createNewFile();
		assertThat("File wasn't created",created, is(true));
	}

	@Test ( groups = {"negative"},alwaysRun = true) // alwaysRun 2 run test whatever passed positive or not
	public void ThirdTestNegative() throws IOException {
		SoftHamcrestAssert h = new SoftHamcrestAssert();
		created = false;
		
		file = new File(Dir.toString() + '\\' + "FileName.txt");		
			created = file.createNewFile();		//file created (should be deleted after test)
			h.assertThat("First file wasn't created", created, is(true));
		try {
			h.assertThat("The same file was created",file.createNewFile(), is (false)); // creating the same file (result should be false); ! - let avoid useless variable
		} catch (IOException e) {			
			e.printStackTrace();
		}
		h.assertAll();

	}

	@Test ( groups = {"negative"},alwaysRun = true) // alwaysRun 2 run test whatever passed positive or not
	public void FourthTestNegative() throws IOException {	
		created = false;
		try {
			file = new File(Dir.toString() + '\\' + "******");
			created = file.createNewFile(); // creating the wrong named file
			assertThat("File with wrong name was created",created,is (false));
		} catch (IOException e) {
			//System.out.println("Exception !!!!");
			e.printStackTrace();
		}

		
		
	}
	
}
