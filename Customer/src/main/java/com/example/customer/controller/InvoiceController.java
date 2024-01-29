package com.example.customer.controller;

import com.example.library.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource; // Correct import for Resource
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/downloadInvoice")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @GetMapping("/{orderId}")
    public ResponseEntity<Resource> downloadInvoice(@PathVariable Long orderId) {
        // Generate the PDF content (you need to implement this)
        byte[] pdfContent = invoiceService.generateInvoice(orderId);

        // Create a ByteArrayResource from the PDF content
        ByteArrayResource resource = new ByteArrayResource(pdfContent);

        // Set the headers for the response
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice.pdf");

        // Return the ResponseEntity with the PDF content and headers
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(pdfContent.length)
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }
}
