package MyPackage1;
import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.nio.file.Path;
import java.nio.file.Files;
import java.io.IOException;
import java.io.File;

public class MyFirstTest {
	Path Dir = null; //temp directory adress
	File file = null; 
	boolean created = false; // true - if file was created 
	
	@BeforeClass ( groups = {"positive","negative"})
	public void BeforeClass() throws IOException {
		String tmp_dir_prefix = "Name_of_temp_directory";
		// set a prefix
		Dir = Files.createTempDirectory(tmp_dir_prefix);
		System.out.println(Dir.toString());  
	}

	@AfterClass  (groups = {"positive","negative"})
	public void AfterClass() throws IOException {		
		Files.deleteIfExists(Dir);		//remove temp directory
	}

	@AfterMethod  (groups = {"positive","negative"})
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
		Assert.assertEquals(created, true);
	}

	@Test ( groups = {"positive"})
	public void SecondTest() throws IOException {
		created = false;
		file = new File(Dir.toString() + '\\' + "1234567890.txt");
		created = file.createNewFile();
		AssertJUnit.assertEquals(true, created);
	}

	@Test ( groups = {"negative"},alwaysRun = true)
	public void ThirdTestNegative() throws IOException {	
		created = false;
		
		file = new File(Dir.toString() + '\\' + "FileName.txt");		
			created = file.createNewFile();		//file created (should be deleted after test)
		try {
			Assert.assertEquals(file.createNewFile(),false,"The dublicate of file was created"); // creating the same file (result should be false); ! - let avoid useless variable
		} catch (IOException e) {			
			e.printStackTrace();
		} 			
	}

	@Test ( groups = {"negative"}, alwaysRun = true)
	public void FourthTestNegative() throws IOException {	
		SoftAssert s = new SoftAssert();
		created = false;
		s.assertEquals(created, false, "Variable \"Created\" isn't false");
		try {
			file = new File(Dir.toString() + '\\' + "*******");
			created = file.createNewFile(); // creating the wrong named file
			s.assertEquals(created, false, "File with wrong name was created");

		} catch (IOException e) {
			//System.out.println("Exception !!!!");
			e.printStackTrace();
		}
		s.assertAll();
		
	}
	
}
