package com.example.library.service.impl;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.UUID;

@Service
public class ReportGenerator {
    public String generateProductStatsPdf(List<Object[]> productStats, String from, String to){
        String fileName = UUID.randomUUID().toString();

        // Define the directory to save the PDF file
        String rootPath = System.getProperty("user.dir");
        String uploadDir = rootPath + "/src/main/resources/static/reports/";
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // Generate the file path
        String filePath = uploadDir + fileName + ".pdf";

        Document document = new Document(PageSize.A1);
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // Create the title
            Paragraph title = new Paragraph("Product Stats Report");
            title.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(title);

            Paragraph period = new Paragraph("From "+from+" to "+to);
            period.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(period);

            document.add(new Paragraph("\n"));

            // Create the table
            PdfPTable table = new PdfPTable(5); // Adjust the number of columns as per your requirement

            // Add table headers
            table.addCell("Product ID");
            table.addCell("Product Name");
            table.addCell("Category");
            table.addCell("Total Quantity Sold");
            table.addCell("Total Revenue");

            // Add table rows
            for (Object [] productStat : productStats) {
                table.addCell(productStat[0].toString());
                table.addCell(productStat[1].toString());
                table.addCell(String.valueOf(productStat[2]));
                table.addCell(String.valueOf(productStat[3]));
                table.addCell(String.valueOf(productStat[4]));

                System.out.println(productStat[0] + "" + productStat[1]);
            }

            document.add(table);

            int totalQuantitySold = 0;
            double totalRevenue = 0.0;

            for (Object[] productStat : productStats) {
                totalQuantitySold += ((Long) productStat[3]).intValue();
                totalRevenue += (double) productStat[4];
            }

            // Add total quantity sold and total revenue as additional lines in the PDF
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("Total Quantity Sold: " + totalQuantitySold));
            document.add(new Paragraph("Total Revenue: " + totalRevenue));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }

        return filePath;
    }



    public String generateProductStatsCsv(List<Object[]> productStats) {
        // Generate a unique file name for the CSV file
        String fileName = UUID.randomUUID().toString();



        // Define the directory to save the CSV file
        String rootPath = System.getProperty("user.dir");
        String uploadDir = rootPath + "/src/main/resources/static/reports/";
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // Generate the full file path
        String filePath = uploadDir + fileName + ".csv";

        try (PrintWriter writer = new PrintWriter(filePath)) {
            // Write the CSV header
            writer.println("Product ID,Product Name,Category,Total Quantity Sold,Total Revenue");

            // Iterate over the list of OrderHistory objects and write each record to the CSV file
            for (Object [] productStat : productStats) {
                writer.printf("%s,%s,%s,%s,%s%n",
                        productStat[0].toString(),
                        productStat[1].toString(),
                        productStat[2].toString(),
                        productStat[3].toString(),
                        productStat[4].toString());

                System.out.println(productStat[0] + "" + productStat[1]);
            }

            writer.flush();

            // Calculate totals
            int totalQuantitySold = 0;
            double totalRevenue = 0.0;

            for (Object[] productStat : productStats) {
                totalQuantitySold += (int) productStat[3];
                totalRevenue += (double) productStat[4];
            }

            // Add total quantity sold and total revenue as additional lines in the CSV
            writer.printf("Total Quantity Sold: %s%n", totalQuantitySold);
            writer.printf("Total Revenue: %s%n", totalRevenue);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filePath;
    }
}
