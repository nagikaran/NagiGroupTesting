package com.NagiGroup.utility;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.multipdf.PDFMergerUtility;

public class MergePDFs {

	public static void main(String[] args) {
        // File paths
        String invoice = "C:\\Users\\nagik\\Downloads\\LOADSMART (5).pdf"; 
        String roc = "D:\\NAGI_GROUP\\documents_of_2025\\January\\atwal\\dispatch record\\854019_roc.pdf"; 
        String bol = "D:\\NAGI_GROUP\\documents_of_2025\\January\\atwal\\p.o.d + lumper receipt + scale\\2025-01-02 19-12.pdf";
        String mergeDoc = "D:\\NAGI_GROUP\\mergeFolder\\merge.pdf";

        // Ensure mergeFolder exists
        File outputDir = new File("D:\\NAGI_GROUP\\mergeFolder");
        if (!outputDir.exists()) {
            if (outputDir.mkdirs()) {
                System.out.println("Created merge folder: " + outputDir.getAbsolutePath());
            } else {
                System.err.println("Failed to create merge folder: " + outputDir.getAbsolutePath());
                return;
            }
        }

        // Set up the PDF merger
        PDFMergerUtility pdfMerger = new PDFMergerUtility();
        pdfMerger.setDestinationFileName(mergeDoc);

        try {
            // Add source PDFs
            pdfMerger.addSource(invoice);
            pdfMerger.addSource(roc);
            pdfMerger.addSource(bol);

            // Merge
            pdfMerger.mergeDocuments(null);
            System.out.println("PDFs merged successfully into: " + mergeDoc);
        } catch (IOException e) {
            System.err.println("Failed to merge PDFs: " + e.getMessage());
            e.printStackTrace();
        }
        
	}

}
