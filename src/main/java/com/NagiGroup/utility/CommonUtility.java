package com.NagiGroup.utility;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

import javax.imageio.ImageIO;

import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.NagiGroup.config.GoogleDriveService;
import com.NagiGroup.conroller.CommonController;
import com.NagiGroup.dto.companyDetails.CompanyDetailsDto;
import com.NagiGroup.model.load.LoadAdditionalCharges;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;


public class CommonUtility {
	 private static final Logger logger = LoggerFactory.getLogger(CommonUtility.class);
	
	 public static boolean saveDocument(MultipartFile documentPath, String loadNumber, String basePath,String folderName) {
		String fileName = "";
		String sourcePath = "";
		basePath=basePath+folderName+""+loadNumber;
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
	 
	 
	 public static String generateInvoicePdf(CompanyDetailsDto companyDto,long invoiceNumber,double amount,String loadNumber) {
		   logger.info("generateInvoicePdf start");
		   String innerFileId = "";
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

	   content5.showText(companyDto.getCompany_name() != null && !companyDto.getCompany_name().equals("") ? companyDto.getCompany_name() : "");
	   content5.newLineAtOffset(0, -15);
	   content5.showText(companyDto.getAddress_line1() != null && !companyDto.getAddress_line1().equals("") ? companyDto.getAddress_line1() : "");
	   content5.newLineAtOffset(0, -15);
	   content5.showText(companyDto.getAddress_line2() != null && !companyDto.getAddress_line2().equals("") ? companyDto.getAddress_line2() : "");
	   content5.newLineAtOffset(0, -15);
	   content5.showText(companyDto.getFax() != null && !companyDto.getFax().equals("") ? companyDto.getFax() : "");
	   content5.newLineAtOffset(0, -15);
	   content5.showText(companyDto.getEmail() != null && !companyDto.getEmail().equals("") ? companyDto.getEmail() : "");

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

	//PDPageContentStream content9 = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true);

	//Centered Description Row
	//Load Number
//	content9.beginText();
//	content9.setFont(PDType1Font.HELVETICA, 12);
//	content9.newLineAtOffset(125, 450);
//	content9.showText("LOAD # "+loadNumber);
//	content9.endText();
//	content9.close();
	String loadText = "LOAD # " + loadNumber;
	PDType1Font helvetica = PDType1Font.HELVETICA;
	float fontSize = 12;

	try (PDPageContentStream content9 = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true)) {
	    content9.beginText();
	    content9.setFont(helvetica, fontSize);
	    content9.newLineAtOffset(60, 450);  // ✅ Extreme left inside box
	    content9.showText(loadText);
	    content9.endText();
	}


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
	String googleDriveRootFolderId = "1fmaG8oHZgel79ol0EuqYEfIqBYU--zzJ";
	String yearFolderId  = "";
	String monthFolderId = "";
	LocalDate currentDates = LocalDate.now();
	String month = currentDates.getMonth().toString();
	try {
		yearFolderId  = GoogleDriveService.getOrCreateFolder("year_" + Year.now().getValue(), googleDriveRootFolderId);
		monthFolderId = GoogleDriveService.getOrCreateFolder(month, yearFolderId);
		String driverFolderId = GoogleDriveService.getOrCreateFolder(companyDto.getDriver_name().trim(), monthFolderId);
		 String subFolderId = GoogleDriveService.getOrCreateFolder(
		            PropertiesReader.getProperty("constant", "BASEURL_FOR_INVOICE").trim(),
		            driverFolderId);
		   ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        document.save(baos);
	        byte[] pdfBytes = baos.toByteArray();
	        baos.close();
	        MultipartFile multipartFile = CommonUtility.convertByteArrayToMultipartFile(pdfBytes,loadNumber + "_invoice.pdf");
	        
	         innerFileId = invoiceFileId(multipartFile,subFolderId);
	         
		
	
       
	} catch (GeneralSecurityException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	String folderPath = "C:\\NAGI_GROUP\\invoice\\";
	File directory = new File(folderPath);
	if (!directory.exists()) {
	    directory.mkdirs(); // creates the folder if it doesn't exist
	}

	String filePath = folderPath + loadNumber + "_invoice.pdf";
	document.save(filePath);

	            logger.info("Step 2 PDF created successfully!");
	            
	            logger.info("generateInvoicePdf end");
	        } catch (IOException e) {
	        	logger.error("generateInvoicePdf "+e.getMessage());
	            e.printStackTrace();
	        }
			return innerFileId;
	    
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
	 
	 
	 public static void mergePDFDocuments(List<String> sourcePaths, String destinationPath) {
	        File outputFile = new File(destinationPath);
	        File parentDir = outputFile.getParentFile();

	        // Create directory if it doesn't exist
	        if (!parentDir.exists()) {
	            boolean created = parentDir.mkdirs();
	            if (created) {
	            	logger.info("Created folder: " + parentDir.getAbsolutePath());
	            } else {
	            	logger.info("Failed to create folder: " + parentDir.getAbsolutePath());
	                return;
	            }
	        }

	        PDFMergerUtility merger = new PDFMergerUtility();
	        merger.setDestinationFileName(destinationPath);

	        try {
	            for (String path : sourcePaths) {
	                merger.addSource(path);
	            }

	            merger.mergeDocuments(null);
	            logger.info("PDFs merged successfully into: " + destinationPath);
	        } catch (IOException e) {
	        	logger.error("Failed to merge PDFs: " + e.getMessage());
	            e.printStackTrace();
	        }
	    }
	 public static double getFinalRate(LoadAdditionalCharges loadAdditionalCharges) {
		    double finalAmount = loadAdditionalCharges.getAmount();
		    
		    // Creating an ArrayList of values to add
		    ArrayList<Double> values = new ArrayList<>();
		    
		    // Handle 0.0 as "no value"
		    values.add(loadAdditionalCharges.getDetention_value() == 0.0 ? 0.0 : loadAdditionalCharges.getDetention_value());
		    values.add(loadAdditionalCharges.getExtra_stop_charge() == 0.0 ? 0.0 : loadAdditionalCharges.getExtra_stop_charge());
		    values.add(loadAdditionalCharges.getLayover() == 0.0 ? 0.0 : loadAdditionalCharges.getLayover());
		    values.add(loadAdditionalCharges.getLumper_value() == 0.0 ? 0.0 : loadAdditionalCharges.getLumper_value());
		    values.add(loadAdditionalCharges.getScale_value() == 0.0 ? 0.0 : loadAdditionalCharges.getScale_value());
		    values.add(loadAdditionalCharges.getTrailer_wash() == 0.0 ? 0.0 : loadAdditionalCharges.getTrailer_wash());

		    // Adding each value to finalAmount
		    for (double value : values) {
		        finalAmount += value;
		    }

		    logger.info("Final Value: " + finalAmount);  // Output: Final Value: calculated value
		    return finalAmount;
		}
	 
	 
	 public Boolean deleteFileFromLocalAndDrive() {
		 return null;
	 }
	 public static MultipartFile convertImageToPdfUsingIText(MultipartFile imageFile) throws Exception {
		    Document document = new Document();
		    ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
		    PdfWriter.getInstance(document, pdfOutputStream);
		    document.open();

		    Image image = Image.getInstance(imageFile.getBytes());
		    image.setAlignment(Image.ALIGN_CENTER);
		    image.scaleToFit(600, 790); // Resize to fit page if needed
		    document.add(image);
//		    Image image = Image.getInstance(imageFile.getBytes());
//		    document.setPageSize(new Rectangle(image.getScaledWidth(), image.getScaledHeight()));
//		    document.newPage(); // Add a new page after setting the custom size
//		    document.add(image);

		    document.close();

		    byte[] pdfBytes = pdfOutputStream.toByteArray();
		    return new MockMultipartFile(
		            "file",
		            imageFile.getOriginalFilename().replaceAll("\\.(jpg|jpeg|png|bmp|gif)$", ".pdf"),
		            "application/pdf",
		            pdfBytes
		    );
		}
	 public static boolean isImage(MultipartFile file) {
		    if (file == null || file.isEmpty()) {
		        return false;
		    }

		    String contentType = file.getContentType();
		    if (contentType != null && contentType.startsWith("image/")) {
		        try {
		            BufferedImage image = ImageIO.read(file.getInputStream());
		            if (image == null) {
		                System.out.println("Rejected file: " + file.getOriginalFilename() + " - Not a valid image content");
		                return false;
		            }
		            return true;
		        } catch (IOException e) {
		            System.out.println("IOException while validating image: " + file.getOriginalFilename());
		            return false;
		        }
		    }

		    return false;
		}
	
	 public static MultipartFile convertByteArrayToMultipartFile(byte[] bytes, String fileName) {
	     try {
	         return new MockMultipartFile(
	             "file",                  // parameter name
	             fileName,                // original file name
	             "application/pdf",       // content type
	             new ByteArrayInputStream(bytes)  // input stream
	         );
	     } catch (IOException e) {
	         e.printStackTrace();
	         return null;  // or throw new RuntimeException(e);
	     }
	 }

	 public static String invoiceFileId(MultipartFile multipartFile, String subFolderId) {
		    try {
		        // Run the upload in a CompletableFuture and return the result
		        return CompletableFuture.supplyAsync(() -> {
		            try {
		                return GoogleDriveService.uploadFileToDrive(multipartFile, subFolderId);
		            } catch (IOException e) {
		                LoggerFactory.getLogger("invoiceFileId").error("IO error while uploading file: " + e.getMessage(), e);
		            } catch (Exception e) {
		                LoggerFactory.getLogger("invoiceFileId").error("Unexpected error while uploading file: " + e.getMessage(), e);
		            }
		            return null;
		        }).join(); // block and return the result (String)
		    } catch (Exception e) {
		        LoggerFactory.getLogger("invoiceFileId").error("Error during future execution: " + e.getMessage(), e);
		        return null;
		    }
		}

	 public static ByteArrayOutputStream mergePDFsToMemory(InputStream... pdfStreams) {
	        ByteArrayOutputStream mergedOutputStream = new ByteArrayOutputStream();
	        PDFMergerUtility pdfMerger = new PDFMergerUtility();

	        try {
	            pdfMerger.setDestinationStream(mergedOutputStream);

	            for (InputStream stream : pdfStreams) {
	                if (stream != null) {
	                    pdfMerger.addSource(stream);
	                } else {
	                	logger.warn("One of the input streams is null and will be skipped.");
	                }
	            }

	            pdfMerger.mergeDocuments(null); // Uses default memory settings
	            logger.info("PDFs merged successfully in memory.");
	        } catch (IOException e) {
	        	logger.error("Failed to merge PDFs: {}", e.getMessage(), e);

	        }

	        return mergedOutputStream;
	    }
}
