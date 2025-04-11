package com.NagiGroup.utility;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.NagiGroup.conroller.CommonController;
import com.NagiGroup.dto.companyDetails.CompanyDto;

public class CommonUtility {
	 private static final Logger logger = LoggerFactory.getLogger(CommonUtility.class);
	public static boolean saveDocument(MultipartFile documentPath, String loadNumber, String basePath,String folderName) {
		String fileName = "";
		String sourcePath = "";
		basePath=basePath+folderName+"/"+loadNumber;
		System.out.println("basePath: "+basePath);
		boolean isCreated = false;
		if (documentPath != null) {
			if (loadNumber != null) {
				isCreated = createDynamicFolder("", basePath);
				//fileName = documentPath.getOriginalFilename();
				fileName = CommonController.renameFileWithExtension(documentPath,loadNumber+"_roc");
			      
				if (isCreated) {
					sourcePath = basePath+"/" + fileName;
					System.out.println("sourcePath: "+sourcePath);
					try {
						documentPath.transferTo(new File(sourcePath));
					} catch (IllegalStateException | IOException e) {
						e.printStackTrace();
					}
				}
			} else {
				System.out.println("Folder Not Created");
				isCreated = false;
			}
		}
		return isCreated;
	}
	
	public static boolean createDynamicFolder(String folderName, String basePath) {
		boolean isCreated = false;
		try {
			System.out.println("basePath : " + basePath);
			String fullPath = basePath + folderName;
			System.out.println("fullPath : " + fullPath);
			File directory = new File(fullPath);
			System.out.println("Directory : " + directory.getPath());
			if (!directory.exists()) {
				if (directory.mkdirs()) {
					isCreated = true;
					System.out.println("Directory created successfully: " + fullPath);
				} else {
					isCreated = false;
					System.out.println("Failed to create directory: " + fullPath);
				}
			} else {
				isCreated = true;
				System.out.println("Directory already exists: " + fullPath);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error At : createDynamicFolder : " + e);
		}
		return isCreated;
	}
	
	public static LocalDateTime parseDateString(String dateString) {
	    if (dateString == null || dateString.isEmpty()) {
	        return null; // Handle null or empty case if needed
	    }
	    
	    dateString = dateString.replace("%20", " ").trim();
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	    
	    return LocalDateTime.parse(dateString, formatter);
	}
	 public static boolean deleteFile(String filePath) {
	        try {
	            Path path = Paths.get(filePath);
	            File file = path.toFile();

	            if (file.exists()) {
	                boolean deleted = Files.deleteIfExists(path);
	                if (deleted) {
	                    logger.info("File deleted successfully: " + filePath);
	                    return true;
	                } else {
	                    logger.warn("Failed to delete file: " + filePath);
	                    return false;
	                }
	            } else {
	                logger.warn("File not found: " + filePath);
	                return false;
	            }
	        } catch (Exception e) {
	            logger.warn("Error deleting file: " + e.getMessage());
	            return false;
	        }
	    }
	 
	 
	 public static void generateInvoicePdf(CompanyDto companyDto,long invoiceNumber,double amount,String loadNumber) {
		   logger.info("generateInvoicePdf start");
	        try (PDDocument document = new PDDocument()) {
	            PDPage page = new PDPage(PDRectangle.A4);
	            document.addPage(page);

	            PDPageContentStream content = new PDPageContentStream(document, page);
	            
	            // Current date in MM-dd-yyyy format
	            String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("MM-dd-yyyy"));

	            // Invoice Number (Left side)
	            content.beginText();
	            content.setFont(PDType1Font.HELVETICA_BOLD, 14);
	            content.newLineAtOffset(50, 800);
	            content.showText("INVOICE # "+invoiceNumber);
	            content.endText();

	            // Invoice Date Label (Right side)
	            content.beginText();
	            content.setFont(PDType1Font.HELVETICA_BOLD, 12);
	            content.newLineAtOffset(400, 800);
	            content.showText("INVOICE DATE");
	            content.endText();

	            // Invoice Date (Value)
	            content.beginText();
	            content.setFont(PDType1Font.HELVETICA, 12);
	            content.newLineAtOffset(400, 785);
	            content.showText(currentDate);
	            content.endText();

	            // Draw Big Rectangle
	            content.setStrokingColor(0, 0, 0); // Black border
	            content.setLineWidth(1);
	            content.addRect(50, 70, 450, 700); // X=50, Y=70, Width=450, Height=700
	            content.stroke();
	            
	            content.close();
	         // "Remit To" on the left
	            PDPageContentStream content2 = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true);

	            content2.beginText();
	            content2.setFont(PDType1Font.HELVETICA_BOLD, 12);
	            content2.newLineAtOffset(125, 750);
	            content2.showText("Remit To:");
	            content2.endText();
	            
	         // Bill To (centered in right half)
	            content2.beginText();
	            content2.setFont(PDType1Font.HELVETICA_BOLD, 12);
	            content2.newLineAtOffset(360, 750);
	            content2.showText("Bill To:");
	            content2.endText();
	            
	            content2.setStrokingColor(0, 0, 0);
	            content2.setLineWidth(1);
	            content2.moveTo(275, 70);   // Bottom center of the box
	            content2.lineTo(275, 770);  // Top center of the box
	            content2.stroke();
	            
	            content2.close();
	            
	            PDPageContentStream content3 = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true);

	         // Draw horizontal line under "Remit To" and "Bill To"
	         content3.setStrokingColor(0, 0, 0); // Black line
	         content3.setLineWidth(1);
	         content3.moveTo(50, 735);   // Left edge of the box
	         content3.lineTo(500, 735);  // Right edge of the box
	         content3.stroke();

	         content3.close();
	         
	         PDPageContentStream content4 = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true);

	      // Remit To details
	      content4.beginText();
	      content4.setFont(PDType1Font.HELVETICA, 11);
	      content4.newLineAtOffset(60, 720); // Start just below the horizontal line

	      content4.showText("NAGI GROUP INC");
	      content4.newLineAtOffset(0, -15);
	      content4.showText("5905 EDELLE DRIVE");
	      content4.newLineAtOffset(0, -15);
	      content4.showText("INDIANAPOLIS, IN 46237");
	      content4.newLineAtOffset(0, -15);
	      content4.showText("Nagigroup0076@gmail.com");
	      content4.endText();

	      content4.close();
	      
	      PDPageContentStream content5 = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true);

	   // Bill To details
	   content5.beginText();
	   content5.setFont(PDType1Font.HELVETICA, 11);
	   content5.newLineAtOffset(285, 720); // Right half, aligned similar to Remit To

	   content5.showText(companyDto.getCompany_name());
	   content5.newLineAtOffset(0, -15);
	   content5.showText(companyDto.getAddress_line1());
	   content5.newLineAtOffset(0, -15);
	   content5.showText(companyDto.getAddress_line2());
	   content5.newLineAtOffset(0, -15);
	   content5.showText(companyDto.getFax());
	   content5.newLineAtOffset(0, -15);
	   content5.showText(companyDto.getEmail());
	   content5.endText();

	   content5.close();
	   PDPageContentStream content6 = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true);

	// Draw horizontal line below address section
	    content6.setStrokingColor(0, 0, 0); // Black
	    content6.setLineWidth(1);
	    content6.moveTo(50, 650);  // Start (left)
	    content6.lineTo(500, 650); // End (right)
	    content6.stroke();
	    content6.close();
	    
	    PDPageContentStream content7 = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true);

	 // Centered DESCRIPTION
	 content7.beginText();
	 content7.setFont(PDType1Font.HELVETICA_BOLD, 12);
	 content7.newLineAtOffset(125, 630);
	 content7.showText("DESCRIPTION");
	 content7.endText();

	 // Centered BALANCE
	 content7.beginText();
	 content7.setFont(PDType1Font.HELVETICA_BOLD, 12);
	 content7.newLineAtOffset(350, 630);
	 content7.showText("BALANCE");
	 content7.endText();
	 content7.close();
	 
	 PDPageContentStream content8 = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true);

	//Line under DESCRIPTION and BALANCE
	content8.setStrokingColor(0, 0, 0); // Black
	content8.setLineWidth(1);
	content8.moveTo(50, 625);  // Start point
	content8.lineTo(500, 625); // End point
	content8.stroke();

	content8.close();

	PDPageContentStream content9 = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true);

	//Centered Description Row
	content9.beginText();
	content9.setFont(PDType1Font.HELVETICA, 12);
	content9.newLineAtOffset(125, 450);
	content9.showText("LOAD # "+loadNumber);
	content9.endText();
	content9.close();

	PDPageContentStream content10 = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true);

	//Balance Amount
	content10.beginText();
	content10.setFont(PDType1Font.HELVETICA, 12);
	content10.newLineAtOffset(350, 450);
	content10.showText("$"+amount);
	content10.endText();
	content10.close();


	PDPageContentStream content11 = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true);

	//Balance Due Label
	content11.beginText();
	content11.setFont(PDType1Font.HELVETICA_BOLD, 12);
	content11.newLineAtOffset(125, 75); // Bottom-left inside the box
	content11.showText("BALANCE DUE");
	content11.endText();

	//Balance Due Amount
	content11.beginText();
	content11.setFont(PDType1Font.HELVETICA_BOLD, 12);
	content11.newLineAtOffset(350, 75); // Bottom-right inside the box
	content11.showText("$"+amount);
	content11.endText();
	content11.close();
	PDPageContentStream content12 = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true);

	//Horizontal line above Balance Due
	content12.moveTo(50, 90);  // Start (x=100, y=90)
	content12.lineTo(500, 90);  // End (x=500, y=90)
	content12.stroke();
	content12.close();

	            // Save PDF
	            document.save("D:\\NAGI_GROUP\\invoice-sample\\invoice_step2.pdf");
	            System.out.println("Step 2 PDF created successfully!");
	            
	            logger.info("generateInvoicePdf end");
	        } catch (IOException e) {
	        	logger.error("generateInvoicePdf "+e.getMessage());
	            e.printStackTrace();
	        }
	    
	 }
	 
	 public static boolean mergeFinalInvoicePdf(String invoicePath, String rocPath, String bolPath, String outputPath) {
		  logger.info("mergeFinalInvoicePdf start"); 
		 PDFMergerUtility merger = new PDFMergerUtility();
		    merger.setDestinationFileName(outputPath);

		    try {
		        merger.addSource(invoicePath);
		        merger.addSource(rocPath);
		        merger.addSource(bolPath);

		        merger.mergeDocuments(null);
		        System.out.println("Merged final invoice created at: " + outputPath);
		        logger.info("mergeFinalInvoicePdf end"); 
		        return true;
		    } catch (IOException e) {
		    	logger.error("mergeFinalInvoicePdf error "+e.getMessage());
		        e.printStackTrace();
		        return false;
		    }
		}
	 
	 
	
}
