package MyPackage1;

import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.nio.file.Path;
import java.nio.file.Files;
import java.io.IOException;
import java.io.File;
import java.util.Iterator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MyFirstTest {
    Path Dir = null; //temp directory adress
    File file = null;
    boolean created = false; // true - if file was created

    @BeforeClass(groups = {"positive", "negative"})
    public void BeforeClass() throws IOException {
        String tmp_dir_prefix = "Name_of_temp_directory";
        // set a prefix
        Dir = Files.createTempDirectory(tmp_dir_prefix);
        System.out.println(Dir.toString());
    }

    @AfterClass(groups = {"positive", "negative"})
    public void AfterClass() throws IOException {
        Files.deleteIfExists(Dir);        //remove temp directory
    }

    @AfterMethod(groups = {"positive", "negative"})
    public void RemoveFile() {
        if (created) {        // if file was created
            file.delete();  // than delete it
        }
    }

    @Test(groups = {"positive"}, dataProviderClass = DataProviders.class, dataProvider = "loadNameFromFile")
    public void FirstTest(String name) throws IOException {
        created = false;
        file = new File(Dir.toString() + '\\' + name);
        System.out.println(name);
        created = file.createNewFile();
        Assert.assertEquals(created, true);
    }

    @Test(groups = {"positive"}, dataProvider = "name")
    public void SecondTest(String name) throws IOException {
        created = false;
        file = new File(Dir.toString() + '\\' + name +".txt");
        System.out.println(name +".txt");
        created = file.createNewFile();
        AssertJUnit.assertEquals(true, created);
    }

    @DataProvider
    public Iterator<Object[]> name(){
       List<Object[]> data = new ArrayList<Object[]>();
        for (int i=0; i<10; i++){
            data.add(new Object[]{
                  generateRandomName()});
        }
        return data.iterator();
     }

    private Object generateRandomName(){
        return "name" + new Random().nextInt();
    }


    @Test(groups = {"negative"}, alwaysRun = true)
    public void ThirdTestNegative() throws IOException {
        created = false;

        file = new File(Dir.toString() + '\\' + "FileName.txt");
        created = file.createNewFile();        //file created (should be deleted after test)
        try {
            Assert.assertEquals(file.createNewFile(), false, "The dublicate of file was created"); // creating the same file (result should be false); ! - let avoid useless variable
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test(groups = {"negative"}, alwaysRun = true)
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
