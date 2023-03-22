package utils;

import exeptions.ListWithoutHeaderExeption;

import java.util.ArrayList;
import java.util.List;

public class CsvFilter {

    private final String HEADER_LINE = "Num_factura, Fecha, Bruto, Neto, IVA, IGIC, Concepto, CIF_cliente, NIF_cliente";

    public List<String> apply(List<String> lines) throws ListWithoutHeaderExeption {

        if (lines.isEmpty()){
            return lines;
        }

        boolean listContainHeader = !lines.get(0).equals(HEADER_LINE);

        if ( listContainHeader ){
            throw new ListWithoutHeaderExeption("ERROR: don't exist header in the list");
        }

        return checkFieldsOfInvoice(lines);
    }

    private List<String> checkFieldsOfInvoice(List<String> lines){

        List<String> responseList = new ArrayList<>();

        for ( int index = 0 ; index < lines.size() ; index++ ) {

            List<String> invoiceLineContentsList = List.of(lines.get(index).split(","));

            if (index == 0) {
                responseList.add(lines.get(index));
            } else {

                boolean lineContentIva = !invoiceLineContentsList.get(4).isBlank();
                boolean lineContentIgic = !invoiceLineContentsList.get(5).isBlank();
                boolean checkContentOfIvaAndIgicInLine = !lineContentIva && lineContentIgic || lineContentIva && !lineContentIgic;

                if (!checkContentOfIvaAndIgicInLine){
                    return responseList;
                }

                boolean lineContentCif = !invoiceLineContentsList.get(6).isBlank();
                boolean lineContentNif = invoiceLineContentsList.size() == 8;

                if (lineContentNif){ lineContentNif = !invoiceLineContentsList.get(7).isBlank(); }

                boolean checkContentOfCifAndNifInLine = !lineContentCif && lineContentNif || lineContentCif && !lineContentNif;

                if (!checkContentOfCifAndNifInLine){
                    return responseList;
                }

                int invoiceTax = lineContentIva
                        ? Integer.parseInt(invoiceLineContentsList.get(4))      // invoiceTax -> IVA
                        : Integer.parseInt(invoiceLineContentsList.get(5));     // invoiceTax -> IGIC

                int grossValue = Integer.parseInt(invoiceLineContentsList.get(2));
                int netValue = Integer.parseInt(invoiceLineContentsList.get(3));

                boolean checkNetValue =  netValue == getNetValue(grossValue, invoiceTax);

                if ( checkNetValue ){
                    responseList.add(lines.get(index));
                }

            }
        }

        return responseList;
    }

    private int getNetValue(int grossValue, int tax){
        int grossMinusTaxes = (grossValue * tax) / 100;
        return (grossValue - grossMinusTaxes);
    }


}
