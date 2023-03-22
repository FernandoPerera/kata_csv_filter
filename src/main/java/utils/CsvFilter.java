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
        responseList.add(lines.get(0));

        List<String> invoiceLine = List.of(lines.get(1).split(","));

        boolean lineContentIva = !invoiceLine.get(4).isBlank();
        boolean lineContentIgic = !invoiceLine.get(5).isBlank();
        boolean checkContentOfIvaAndIgicInLine = !lineContentIva && lineContentIgic || lineContentIva && !lineContentIgic;

        if (!checkContentOfIvaAndIgicInLine){
            return responseList;
        }

        boolean lineContentCif = !invoiceLine.get(6).isBlank();
        boolean lineContentNif = invoiceLine.size() == 8;

        if (lineContentNif){ lineContentNif = !invoiceLine.get(7).isBlank(); }

        boolean checkContentOfCifAndNifInLine = !lineContentCif && lineContentNif || lineContentCif && !lineContentNif;

        if (!checkContentOfCifAndNifInLine){
            return responseList;
        }

        int invoiceTax = lineContentIva
                ? Integer.parseInt(invoiceLine.get(4))      // invoiceTax -> IVA
                : Integer.parseInt(invoiceLine.get(5));     // invoiceTax -> IGIC

        int grossValue = Integer.parseInt(invoiceLine.get(2));
        int netValue = Integer.parseInt(invoiceLine.get(3));

        boolean checkNetValue =  netValue == getNetValue(grossValue, invoiceTax);

        if ( checkNetValue ){
            responseList.add(lines.get(1));
        }
        return responseList;
    }

    private int getNetValue(int grossValue, int tax){
        int grossMinusTaxes = (grossValue * tax) / 100;
        return (grossValue - grossMinusTaxes);
    }


}
