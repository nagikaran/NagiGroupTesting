package com.NagiGroup.utility;
import java.io.FileOutputStream;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

public class ExactInvoiceLayout {
    public static void main(String[] args) {
        String outputPath = "C:\\Users\\nagik\\Downloads\\Exact_ArriveInvoice.pdf";
        Document document = new Document(PageSize.LETTER, 36, 36, 36, 36);

        try {
            PdfWriter.getInstance(document, new FileOutputStream(outputPath));
            document.open();

            // Fonts
            BaseFont baseFont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
            Font headerFont = new Font(baseFont, 18, Font.BOLD);
            Font boldFont = new Font(baseFont, 11, Font.BOLD);
            Font normalFont = new Font(baseFont, 11, Font.NORMAL);
            Font smallBoldFont = new Font(baseFont, 9, Font.BOLD);

            // Header
            Paragraph title = new Paragraph("", headerFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(Chunk.NEWLINE);

            // Invoice Info Table
            PdfPTable invoiceTable = new PdfPTable(2);
            invoiceTable.setWidthPercentage(100);
            invoiceTable.setWidths(new float[]{1, 1});

            PdfPCell leftCell = new PdfPCell();
            leftCell.setBorder(PdfPCell.NO_BORDER);
            leftCell.addElement(new Paragraph("INVOICE  # 22695", boldFont));
            //leftCell.addElement(new Paragraph("LOAD # 6750866", normalFont));

            PdfPCell rightCell = new PdfPCell();
            rightCell.setBorder(PdfPCell.NO_BORDER);
            rightCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            rightCell.addElement(new Paragraph("INVOICE DATE", boldFont));
            rightCell.addElement(new Paragraph("03/30/2025", normalFont));

            invoiceTable.addCell(leftCell);
            invoiceTable.addCell(rightCell);
            document.add(invoiceTable);
            document.add(Chunk.NEWLINE);

            // Addresses
            PdfPTable addressTable = new PdfPTable(2);
            addressTable.setWidthPercentage(100);
            addressTable.setWidths(new float[]{1, 1});

            PdfPCell remitTo = new PdfPCell();
            remitTo.setBorder(PdfPCell.NO_BORDER);
            remitTo.addElement(new Paragraph("REMIT TO", smallBoldFont));
            remitTo.addElement(new Paragraph("NAGI GROUP INC", normalFont));
            remitTo.addElement(new Paragraph("5905 EDELLE DRIVE", normalFont));
            remitTo.addElement(new Paragraph("INDIANAPOLIS, IN 46237", normalFont));
            remitTo.addElement(new Paragraph("Nagigroup0076@gmail.com", normalFont));
           
            PdfPCell billTo = new PdfPCell();
            billTo.setBorder(PdfPCell.NO_BORDER);
            billTo.addElement(new Paragraph("BILL TO", smallBoldFont));
            billTo.addElement(new Paragraph("ARRIVE LOGISTICS, LLC", normalFont));
            billTo.addElement(new Paragraph("7701 METROPOLIS DR.", normalFont));
            billTo.addElement(new Paragraph("AUSTIN, TX 78744", normalFont));
            billTo.addElement(new Paragraph("invoices@arrivelogistics.com", normalFont));


            addressTable.addCell(remitTo);
            addressTable.addCell(billTo);
            document.add(addressTable);
            document.add(Chunk.NEWLINE);

            // Description Table
            PdfPTable detailTable = new PdfPTable(2);
            detailTable.setWidthPercentage(100);
            detailTable.setWidths(new float[]{4, 1});

            detailTable.addCell(createCell("DESCRIPTION", boldFont, Element.ALIGN_LEFT, BaseColor.LIGHT_GRAY));
            detailTable.addCell(createCell("BALANCE", boldFont, Element.ALIGN_RIGHT, BaseColor.LIGHT_GRAY));
            detailTable.addCell(createCell("LOAD # 6750866"	, normalFont, Element.ALIGN_LEFT, BaseColor.WHITE));
            detailTable.addCell(createCell("$500.00", normalFont, Element.ALIGN_RIGHT, BaseColor.WHITE));

            document.add(detailTable);
            document.add(Chunk.NEWLINE);

            // Balance Due
//            Paragraph balance = new Paragraph("BALANCE DUE $500.00", boldFont);
//            balance.setAlignment(Element.ALIGN_RIGHT);
//            document.add(balance);
            
         // Spacer
            document.add(Chunk.NEWLINE);

            // Soft Divider
            LineSeparator separator = new LineSeparator();
            separator.setLineWidth(0.5f);
            separator.setLineColor(new BaseColor(200, 200, 200));
            document.add(new Chunk(separator));
            document.add(Chunk.NEWLINE);

            // Balance Due - Clean, bold, right-aligned
            Font balanceFont = new Font(baseFont, 12, Font.BOLD);
            Paragraph balance = new Paragraph("BALANCE DUE   $500.00", balanceFont);
            balance.setAlignment(Element.ALIGN_RIGHT);
            document.add(balance);


            document.close();
            System.out.println("Exact invoice PDF generated: " + outputPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static PdfPCell createCell(String content, Font font, int align, BaseColor bg) {
        PdfPCell cell = new PdfPCell(new Phrase(content, font));
        cell.setHorizontalAlignment(align);
        cell.setBackgroundColor(bg);
        cell.setPadding(8f);
        return cell;
    }
}
