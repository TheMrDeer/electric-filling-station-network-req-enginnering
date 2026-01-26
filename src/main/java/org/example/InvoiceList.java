package org.example;

import java.util.ArrayList;
import java.util.List;

public class InvoiceList {

    public static List<Invoice> invoices = new ArrayList<>();

    public void addInvoice(Invoice i){
        invoices.add(i);
    }

}
