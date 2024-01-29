package com.example.library.service.impl;

import com.example.library.model.Customer;
import com.example.library.model.Order;
import com.example.library.model.OrderDetail;
import com.example.library.service.AddressService;
import com.example.library.service.CustomerService;
import com.example.library.service.InvoiceService;
import com.example.library.service.OrderService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class InvoiceServiceImpl implements InvoiceService {
    @Autowired
    private OrderService orderService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private CustomerService customerService;
    @Override
    public byte[] generateInvoice(Long orderId) {
        try (PDDocument document = new PDDocument()) {
            Order order = orderService.findOrderById(orderId);
            if (order != null) {
                PDPage page = new PDPage();
                document.addPage(page);
                try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                    contentStream.beginText();
                    contentStream.newLineAtOffset(100, 700);
                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 15);
                    contentStream.showText("Invoice for Order ID: " + orderId);
                    contentStream.newLine();
                    contentStream.newLine();
                    contentStream.newLineAtOffset(0, -15);
                    contentStream.showText("CUSTOMER : MR." + order.getCustomer().getFirstName());
                    contentStream.newLine();
                    contentStream.newLineAtOffset(0, -15);
                    contentStream.showText("PRODUCT DETAIL:");
                    contentStream.newLine();
                    for (OrderDetail orderDetail : order.getOrderDetails()) {
                        contentStream.newLineAtOffset(0, -15);
                        contentStream.newLineAtOffset(0, -15);
                        contentStream.showText("PRODUCT: " + orderDetail.getProduct().getName());
                        contentStream.newLineAtOffset(0, -15);
                        contentStream.newLineAtOffset(0, -15);
                        contentStream.newLineAtOffset(0, -15);
                        contentStream.showText("SHIPPING ADDRESS :");
                        contentStream.newLineAtOffset(0, -15);
                        contentStream.showText("Address 1 : " + order.getShippingAddress().getAddress_line_1());
                        contentStream.newLineAtOffset(0, -15);
                        contentStream.showText("Address 2 : " + order.getShippingAddress().getAddress_line_2());
                        contentStream.newLineAtOffset(0, -15);
                        contentStream.showText("City : " + order.getShippingAddress().getCity());
                        contentStream.newLineAtOffset(0, -15);
                        contentStream.newLineAtOffset(0, -15);
                        contentStream.newLineAtOffset(0, -15);
                        contentStream.newLineAtOffset(0, -15);
                        contentStream.newLine();
                    }
                    contentStream.showText("Payment Status: Paid");
                    contentStream.newLineAtOffset(0, -15);                     contentStream.showText("Total Price: " + order.getTotalPrice());
                    contentStream.newLineAtOffset(0, -15);
                    contentStream.newLineAtOffset(0, -15);
                    contentStream.newLineAtOffset(0, -15);
                    contentStream.newLineAtOffset(0, -15);
                    contentStream.showText("Thank You, For using Cosmic-Craft");
                    contentStream.newLineAtOffset(0, -15);
                    contentStream.endText();
                }
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                document.save(outputStream);
                return outputStream.toByteArray();
            } else {
                return new byte[0];
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }
}
