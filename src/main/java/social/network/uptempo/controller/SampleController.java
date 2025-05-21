package social.network.uptempo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spire.doc.FileFormat;
import com.spire.doc.Document;

@RestController
public class SampleController {

	
	
	
	@RequestMapping("/sample/word")
	public String sampleWord(HttpServletRequest req, HttpServletResponse res) throws Exception{
		Document doc = new Document();
        doc.loadFromFile("/temp/test2.docx");

        
        //XML 형식으로 변환
        doc.saveToFile("/temp/toXml2.xml.",FileFormat.Word_Xml);
        
        
		
        doc = new Document();
        doc.loadFromFile("/temp/toXml2.xml");
        
        doc.replace("Test", "Lucas", false, true);
        
        //워드 문서 형식으로 저장
        doc.saveToFile("/temp/test3.docx",FileFormat.Docx);
		
		return "";
	}
	
	
	public static void main(String[] args) throws Exception {
		Document doc = new Document();
        doc.loadFromFile("/temp/sample_test.doc");

        doc.replace("{{company}}", "Uptempo", false, true);
//        doc.replace("{{phone}}", "010-2015-8230", false, true);
        doc.replace("{{name}}", "심진보", false, true);
//        doc.replace("{{eng_name}}", "Lucas", false, true);
        
        //워드 문서 형식으로 저장
        doc.saveToFile("/temp/sample_test_after.doc",FileFormat.Doc);
	}
//	public static void main(String[] args) throws Exception {
//		Document doc = new Document();
//		doc.loadFromFile("/temp/test2.docx");
//		
//		doc.replace("{{company}}", "Uptempo", false, true);
//		doc.replace("{{phone}}", "010-2015-8230", false, true);
//		doc.replace("{{name}}", "심진보", false, true);
//		doc.replace("{{eng_name}}", "Lucas", false, true);
//		
//		//워드 문서 형식으로 저장
//		doc.saveToFile("/temp/test4.docx",FileFormat.Docx);
//	}
}
