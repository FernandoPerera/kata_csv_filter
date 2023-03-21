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

        return checkLineOfIvaAndIgicFields(lines);
    }

    private List<String> checkLineOfIvaAndIgicFields(List<String> lines){
        List<String> responseList = new ArrayList<>();

        responseList.add(lines.get(0));

        List<String> invoiceLine = List.of(lines.get(1).split(","));

        boolean lineContentIva = !invoiceLine.get(4).isBlank();
        boolean lineContentIgic = !invoiceLine.get(5).isBlank();
        boolean checkContentOfIvaAndIgicInLine = !lineContentIva && lineContentIgic || lineContentIva && !lineContentIgic;

        if ( checkContentOfIvaAndIgicInLine ){
            responseList.add(lines.get(1));
        }

        return responseList;
    }

}
