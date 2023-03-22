package utils;

import exeptions.ListWithoutHeaderExeption;

import java.util.ArrayList;
import java.util.List;

public class CsvFilter {

    public List<String> apply(List<String> lines) throws ListWithoutHeaderExeption {

        if (lines.isEmpty()){
            return lines;
        }

        String HEADER_LINE = "Num_factura, Fecha, Bruto, Neto, IVA, IGIC, Concepto, CIF_cliente, NIF_cliente";

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

                String invoiceNumber = invoiceLineContentsList.get(0);

                if (!checkLinesWhenHaveSamesInvoiceNumbers(invoiceNumber, lines)){

                    if (checkIvaAndIgicFields(invoiceLineContentsList)){

                        if (checkCifAndNifFields(invoiceLineContentsList)){

                            if (checkNetValue(invoiceLineContentsList) ){
                                responseList.add(lines.get(index));
                            }
                        }

                    }

                }

            }
        }

        return responseList;
    }

    private boolean checkNetValue(List<String> invoiceLineContentsList){

        boolean lineContentIva = !invoiceLineContentsList.get(4).isBlank();

        int invoiceTax = lineContentIva
                ? Integer.parseInt(invoiceLineContentsList.get(4))
                : Integer.parseInt(invoiceLineContentsList.get(5));

        int grossValue = Integer.parseInt(invoiceLineContentsList.get(2));
        int netValue = Integer.parseInt(invoiceLineContentsList.get(3));

        return netValue == getNetValue(grossValue, invoiceTax);
    }

    private boolean checkCifAndNifFields(List<String> invoiceLineContentsList){

        boolean lineContentCif = !invoiceLineContentsList.get(6).isBlank();
        boolean lineContentNif = invoiceLineContentsList.size() == 8;

        if (lineContentNif){ lineContentNif = !invoiceLineContentsList.get(7).isBlank(); }

        return  !lineContentCif && lineContentNif || lineContentCif && !lineContentNif;
    }

    private boolean checkIvaAndIgicFields(List<String> invoiceLineContentsList){

        boolean lineContentIva = !invoiceLineContentsList.get(4).isBlank();
        boolean lineContentIgic = !invoiceLineContentsList.get(5).isBlank();

        return !lineContentIva && lineContentIgic || lineContentIva && !lineContentIgic;
    }

    private boolean checkLinesWhenHaveSamesInvoiceNumbers(String invoiceNumber, List<String> lines){

        int countValue = 0;

        for (String line : lines) {
            if (invoiceNumber.equals(line.substring(0,1))) {
                countValue++;
            }
        }
        return countValue > 1;
    }

    private int getNetValue(int grossValue, int tax){
        int grossMinusTaxes = (grossValue * tax) / 100;
        return (grossValue - grossMinusTaxes);
    }


}
