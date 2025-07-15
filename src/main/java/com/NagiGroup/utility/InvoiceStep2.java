package com.NagiGroup.utility;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class InvoiceStep2 {
    public static void main(String[] args) {
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
            content.showText("INVOICE # 226695");
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

   content5.showText("ARRIVE LOGISTICS, LLC");
   content5.newLineAtOffset(0, -15);
   content5.showText("7701 METROPOLIS DR.");
   content5.newLineAtOffset(0, -15);
   content5.showText("AUSTIN, TX 78744");
   content5.newLineAtOffset(0, -15);
   content5.showText("invoices@arrivelogistics.com");
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
//content9.beginText();
//content9.setFont(PDType1Font.HELVETICA, 12);
//content9.newLineAtOffset(125, 450);
//content9.showText("LOAD # LOAD-TESTING-DEMO-00110");
//content9.endText();
//content9.close();
String loadText = "LOAD # " + "LOAD-TESTING";
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
content10.showText("$500.00");
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
content11.showText("$500.00");
content11.endText();
content11.close();
PDPageContentStream content12 = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true);

//Horizontal line above Balance Due
content12.moveTo(50, 90);  // Start (x=100, y=90)
content12.lineTo(500, 90);  // End (x=500, y=90)
content12.stroke();
content12.close();

            // Save PDF
            document.save("C:\\NAGI_GROUP\\invoice-sample\\invoice_step2.pdf");
            System.out.println("Step 2 PDF created successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
