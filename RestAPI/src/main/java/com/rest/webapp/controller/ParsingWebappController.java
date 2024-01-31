package com.rest.webapp.controller;

import java.awt.Graphics2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.strabag.app.controller.ParseFileController;
import com.strabag.app.controller.response.ResultResponse;
import com.strabag.app.entity.SectionAndClass;
import com.strabag.app.repository.SectionAndClassRepository;

import java.io.FileOutputStream;
import java.io.OutputStream;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import com.itextpdf.awt.DefaultFontMapper;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;

@Component
@RestController
@RequestMapping(value = "/parse")
public class ParsingWebappController {

	private static final Logger logger = LogManager.getLogger(ParsingWebappController.class);

	@Autowired
	ParseFileController parseFileController;

	ResultResponse resultResponse = new ResultResponse();

	@Autowired
	SectionAndClassRepository sectionAndClassRepository;

	@GetMapping("/register-job")
	public ResultResponse parseFile() {

		try {
			logger.info("STARTED REGISTERING CSV AND EXCEL FILE FROM Location: ");
			resultResponse = parseFileController.parseFile("F:\\Java\\JavaSpringSuite\\RestAPI");
			logger.info("Completed Parsing all csv and excel files");

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultResponse;

	}

	@GetMapping("/findbyNameAndCode")
	public List<SectionAndClass> getjobID(@RequestParam String className, @RequestParam String classCode) {
		List<SectionAndClass> sectionAndClass = new ArrayList<SectionAndClass>();

		sectionAndClass = sectionAndClassRepository.findAll();

		return sectionAndClass;

	}

	@GetMapping("/findbyJobId")
	public List<SectionAndClass> getByJobID(@RequestParam int jobID) {
		List<SectionAndClass> sectionAndClass = new ArrayList<SectionAndClass>();

		sectionAndClass = sectionAndClassRepository.findbyJobId(jobID);

		return sectionAndClass;

	}

	@GetMapping("/createChart")
	public String createChart() throws FileNotFoundException, DocumentException {
		List<SectionAndClass> sectionAndClasses = new ArrayList<SectionAndClass>();

		sectionAndClasses = sectionAndClassRepository.findAll();
		int totalSection1Count = 0;
		int totalSection2Count = 0;
		int totalSection3Count = 0;
		int totalSection4Count = 0;

		for (SectionAndClass sectionAndClass : sectionAndClasses) {

			switch (sectionAndClass.getSectionName()) {

			case "Section 1":
				totalSection1Count++;
				break;
			case "Section 2":
				totalSection2Count++;
				break;
			case "Section 3":
				totalSection3Count++;
				break;
			case "Section 4":
				totalSection4Count++;
				break;
			}
		}
		DefaultPieDataset dataset = new DefaultPieDataset();
		dataset.setValue("Section 1", totalSection1Count);
		dataset.setValue("Section 2", totalSection2Count);
		dataset.setValue("Section 3", totalSection3Count);
		dataset.setValue("Section 4", totalSection4Count);

		JFreeChart chart = ChartFactory.createPieChart("Section Count Chart", dataset, true, true, false);

		String pdfFilePath = "F:\\Java\\JavaSpringSuite\\RestAPI\\PIE_chart.pdf";
		OutputStream fos = new FileOutputStream(new File(pdfFilePath));
		Document document = new Document();
		PdfWriter writer = PdfWriter.getInstance(document, fos);

		document.open();

		PdfContentByte pdfContentByte = writer.getDirectContent();
		int width = 400; // width of PieChart
		int height = 300; // height of pieChart
		PdfTemplate pdfTemplate = pdfContentByte.createTemplate(width, height);

		// create graphics
		Graphics2D graphics2d = pdfTemplate.createGraphics(width, height, new DefaultFontMapper());

		// create rectangle
		java.awt.geom.Rectangle2D rectangle2d = new java.awt.geom.Rectangle2D.Double(0, 0, width, height);

		chart.draw(graphics2d, rectangle2d);

		graphics2d.dispose();
		pdfContentByte.addTemplate(pdfTemplate, 40, 500); // 0, 0 will draw PIE chart on bottom left of page

		document.close();
		System.out.println("PDF created in >> " + pdfFilePath);

		return "Chart downloaded";

	}

}
